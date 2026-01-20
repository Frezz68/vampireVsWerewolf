package dev.Frezz.keybinds;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import dev.Frezz.manager.WerewolfManager;
import dev.Frezz.state.WerewolfState;

import java.util.function.Consumer;

/**
 * Gestionnaire du keybind pour la transformation en loup-garou.
 */
public class TransformKeybind implements Consumer<Player> {

    public static final String KEYBIND_ID = "werewolf_transform";
    public static final String KEYBIND_NAME = "Transformation Loup-Garou";

    @Override
    public void accept(Player player) {
        onKeyPressed(player);
    }

    public void onKeyPressed(Player player) {
        WerewolfManager manager = WerewolfManager.getInstance();
        WerewolfState state = manager.getState(player);

        if (!state.isWerewolf()) {
            player.sendMessage(Message.raw("§cVous n'êtes pas un loup-garou!"));
            return;
        }

        boolean success = state.toggleTransformation();

        if (success) {
            if (state.isTransformed()) {
                // Transformation en loup-garou
                player.sendMessage(Message.raw("§6Vous vous transformez en loup-garou!"));
                applyWerewolfModel(player);
            } else {
                // Retour à la forme humaine
                player.sendMessage(Message.raw("§aVous reprenez votre forme humaine."));
                applyHumanModel(player);
            }
            // Sauvegarder l'état après transformation
            manager.saveData();
        }
    }

    /**
     * Applique le modèle loup-garou au joueur.
     * TODO: Implémenter le changement de modèle une fois l'API disponible.
     */
    private void applyWerewolfModel(Player player) {
        // TODO: Changer le modèle du joueur vers le modèle loup-garou
        // player.setModel("werewolf_model");
        System.out.println("[VampireVsWerewolf] " + player.getDisplayName() + " s'est transformé en loup-garou");
    }

    /**
     * Remet le modèle humain au joueur.
     * TODO: Implémenter le changement de modèle une fois l'API disponible.
     */
    private void applyHumanModel(Player player) {
        // TODO: Remettre le modèle humain au joueur
        // player.setModel("human_model");
        System.out.println("[VampireVsWerewolf] " + player.getDisplayName() + " est redevenu humain");
    }
}

