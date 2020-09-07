package me.thevipershow.viperverse;

import me.thevipershow.viperverse.commands.impl.ViperVerseCommand;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class ViperVerse extends JavaPlugin {

    private ViperVerseCommand viperVerseCommand;
    private Server server;

    @Override
    public void onEnable() { // Plugin startup logic
        server = getServer();
        viperVerseCommand = new ViperVerseCommand(this);
    }

    @Override
    public void onDisable() { // Plugin shutdown logic

    }
}
