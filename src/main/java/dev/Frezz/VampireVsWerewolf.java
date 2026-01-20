package dev.Frezz;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.Frezz.commands.SetWerewolfCommand;
import dev.Frezz.commands.TransformCommand;
import dev.Frezz.commands.WerewolfStatusCommand;
import dev.Frezz.events.ExampleEvent;

import javax.annotation.Nonnull;

public class VampireVsWerewolf extends JavaPlugin {

    private static VampireVsWerewolf instance;

    public VampireVsWerewolf(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        // Enregistrement des commandes
        this.getCommandRegistry().registerCommand(new WerewolfStatusCommand());
        this.getCommandRegistry().registerCommand(new TransformCommand());
        this.getCommandRegistry().registerCommand(new SetWerewolfCommand());

        // Enregistrement des événements
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);

        System.out.println("[VampireVsWerewolf] Plugin chargé!");
    }

    public static VampireVsWerewolf getInstance() {
        return instance;
    }
}