package dev.Frezz.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.Frezz.manager.WerewolfManager;
import dev.Frezz.state.WerewolfState;

import javax.annotation.Nonnull;

/**
 * Commande admin pour définir un joueur comme loup-garou.
 * Usage: /setwerewolf <true/false>
 */
public class SetWerewolfCommand extends AbstractPlayerCommand {

    public SetWerewolfCommand() {
        super("setwerewolf", "Définir si vous êtes un loup-garou (admin)");
    }

    private final RequiredArg<String> statusArg = this.withRequiredArg("status", "true ou false", ArgTypes.STRING);

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        if (store.getComponent(ref, Player.getComponentType()) instanceof Player player) {
            WerewolfManager manager = WerewolfManager.getInstance();
            WerewolfState state = manager.getState(player);

            String arg = statusArg.get(commandContext).toLowerCase();
            boolean newValue;

            if (arg.equals("true") || arg.equals("on") || arg.equals("1")) {
                newValue = true;
            } else if (arg.equals("false") || arg.equals("off") || arg.equals("0")) {
                newValue = false;
            } else {
                commandContext.sendMessage(Message.raw("§cUsage: /setwerewolf <true/false>"));
                return;
            }

            state.setWerewolf(newValue);
            manager.saveData();

            if (newValue) {
                commandContext.sendMessage(Message.raw("§aVous êtes maintenant un loup-garou!"));
            } else {
                commandContext.sendMessage(Message.raw("§cVous n'êtes plus un loup-garou."));
            }
        } else {
            commandContext.sendMessage(Message.raw("§cCette commande ne peut être utilisée que par un joueur."));
        }
    }
}
