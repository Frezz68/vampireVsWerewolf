package dev.Frezz.commands;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.PlayerSkin;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.Frezz.manager.WerewolfManager;
import dev.Frezz.state.WerewolfState;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Commande pour se transformer en loup-garou.
 * Usage: /transform
 */
public class TransformCommand extends AbstractPlayerCommand {

    // Modèle loup-garou (à remplacer par le vrai modèle quand disponible)
    private static final String WEREWOLF_MODEL_NAME = "Werewolf";
    private static final String HUMAN_MODEL_NAME = "Player"; // Modèle humain par défaut

    public TransformCommand() {
        super("transform", "Se transformer en loup-garou ou reprendre forme humaine");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        if (store.getComponent(ref, Player.getComponentType()) instanceof Player player) {
            WerewolfManager manager = WerewolfManager.getInstance();
            WerewolfState state = manager.getState(player);

            if (!state.isWerewolf()) {
                commandContext.sendMessage(Message.raw("Vous n'êtes pas un loup-garou!").color(Color.RED));
                return;
            }

            boolean success = state.toggleTransformation();

            if (success) {

                if (state.isTransformed()) {

                    // Transformation en loup-garou
                    ModelAsset werewolfModelAsset = ModelAsset.getAssetMap().getAsset(WEREWOLF_MODEL_NAME);
                    Model werewolfModel = Model.createScaledModel(werewolfModelAsset, 1.0f);
                    PlayerSkinComponent playerSkinComp = store.getComponent(ref, PlayerSkinComponent.getComponentType());

                    state.saveOriginalSkin(playerSkinComp.getPlayerSkin());

                    store.replaceComponent(ref,ModelComponent.getComponentType(), new ModelComponent(werewolfModel));
                    store.replaceComponent(ref, BoundingBox.getComponentType(), new BoundingBox(werewolfModel.getBoundingBox()));

                    commandContext.sendMessage(Message.raw("Vous vous transformez en loup-garou!").color(Color.YELLOW));
                    System.out.println("[VampireVsWerewolf] " + player.getDisplayName() + " s'est transformé en loup-garou");
                } else {
                    // Retour à la forme humaine
                    ModelAsset humanModelAsset = ModelAsset.getAssetMap().getAsset(HUMAN_MODEL_NAME);
                    Model humanModel = Model.createScaledModel(humanModelAsset, 1.0f);
                    store.replaceComponent(ref,ModelComponent.getComponentType(), new ModelComponent(humanModel));
                    store.replaceComponent(ref,BoundingBox.getComponentType(), new BoundingBox(humanModel.getBoundingBox()));
                    store.replaceComponent(ref, PlayerSkinComponent.getComponentType(), new PlayerSkinComponent(state.getOriginalSkin()));

                    commandContext.sendMessage(Message.raw("Vous reprenez votre forme humaine.").color(Color.YELLOW));
                    System.out.println("[VampireVsWerewolf] " + player.getDisplayName() + " est redevenu humain");
                }
                manager.saveData();
            }
        } else {
            commandContext.sendMessage(Message.raw("Cette commande ne peut être utilisée que par un joueur."));
        }
    }
}

