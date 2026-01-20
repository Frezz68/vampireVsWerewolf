package dev.Frezz.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.server.core.entity.entities.Player;
import dev.Frezz.state.WerewolfState;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire des états loup-garou pour tous les joueurs.
 * Les états sont sauvegardés sur disque pour persister entre les sessions.
 */
public class WerewolfManager {

    private static WerewolfManager instance;
    private final Map<UUID, WerewolfState> playerStates;
    private final Path saveFile;
    private final Gson gson;

    private WerewolfManager() {
        this.playerStates = new ConcurrentHashMap<>();
        this.saveFile = Paths.get("plugins", "VampireVsWerewolf", "werewolf_data.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    /**
     * Obtient l'instance singleton du gestionnaire.
     */
    public static WerewolfManager getInstance() {
        if (instance == null) {
            instance = new WerewolfManager();
        }
        return instance;
    }

    /**
     * Obtient l'état loup-garou d'un joueur.
     * Crée un nouvel état si le joueur n'en a pas.
     */
    public WerewolfState getState(Player player) {
        return playerStates.computeIfAbsent(player.getUuid(), uuid -> new WerewolfState());
    }

    /**
     * Obtient l'état loup-garou par UUID.
     */
    public WerewolfState getState(UUID playerId) {
        return playerStates.computeIfAbsent(playerId, uuid -> new WerewolfState());
    }

    /**
     * Supprime l'état d'un joueur.
     */
    public void removePlayer(Player player) {
        playerStates.remove(player.getUuid());
        saveData();
    }

    /**
     * Sauvegarde les données sur le disque.
     */
    public void saveData() {
        try {
            Files.createDirectories(saveFile.getParent());
            // Convertir Map<UUID, WerewolfState> en Map<String, WerewolfState> pour JSON
            Map<String, WerewolfState> toSave = new HashMap<>();
            playerStates.forEach((uuid, state) -> toSave.put(uuid.toString(), state));

            try (Writer writer = new FileWriter(saveFile.toFile())) {
                gson.toJson(toSave, writer);
            }
        } catch (IOException e) {
            System.err.println("[VampireVsWerewolf] Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    /**
     * Charge les données depuis le disque.
     */
    private void loadData() {
        if (Files.exists(saveFile)) {
            try (Reader reader = new FileReader(saveFile.toFile())) {
                Type type = new TypeToken<HashMap<String, WerewolfState>>(){}.getType();
                Map<String, WerewolfState> loaded = gson.fromJson(reader, type);
                if (loaded != null) {
                    // Convertir Map<String, WerewolfState> en Map<UUID, WerewolfState>
                    loaded.forEach((uuidStr, state) -> {
                        try {
                            playerStates.put(UUID.fromString(uuidStr), state);
                        } catch (IllegalArgumentException e) {
                            System.err.println("[VampireVsWerewolf] UUID invalide ignoré: " + uuidStr);
                        }
                    });
                }
                System.out.println("[VampireVsWerewolf] Données chargées: " + playerStates.size() + " joueurs");
            } catch (IOException e) {
                System.err.println("[VampireVsWerewolf] Erreur lors du chargement: " + e.getMessage());
            }
        }
    }
}
