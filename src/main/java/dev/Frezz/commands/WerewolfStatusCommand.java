package dev.Frezz.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.Frezz.manager.WerewolfManager;
import dev.Frezz.state.WerewolfState;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Commande pour afficher le statut loup-garou d'un joueur.
 * Usage: /werewolfstatus
 */
public class WerewolfStatusCommand extends AbstractPlayerCommand {

    public WerewolfStatusCommand() {
        super("werewolfstatus", "Affiche votre statut loup-garou");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        if (store.getComponent(ref, Player.getComponentType()) instanceof Player player) {
            WerewolfState state = WerewolfManager.getInstance().getState(player);

            String statusMessage = "§6=== Statut Loup-Garou ===\n" +
                    "§7Statut: §f" + state.getStatusDescription() + "\n" +
                    "§7Est un Loup-Garou: " + (state.isWerewolf() ? "§aOui" : "§cNon") + "\n" +
                    "§7Transformé: " + (state.isTransformed() ? "§aOui" : "§cNon");

            commandContext.sendMessage(Message.raw(statusMessage));
        } else {
            commandContext.sendMessage(Message.raw("§cCette commande ne peut être utilisée que par un joueur."));
        }
    }
}

