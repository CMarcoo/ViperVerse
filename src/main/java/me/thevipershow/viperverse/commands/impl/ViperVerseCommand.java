package me.thevipershow.viperverse.commands.impl;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import me.thevipershow.viperverse.WorldUtils;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public final class ViperVerseCommand implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    private final static List<String> immutableFirstArgs = Collections.unmodifiableList(Lists.newArrayList(
            "list","tp", "unload", "delete", "move-all"));

    public ViperVerseCommand(Plugin plugin) {
        this.plugin = plugin;
        final Server srv = plugin.getServer();
        final PluginCommand pluginCommand = srv.getPluginCommand("vv");
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "tp":
                    new CommandTeleport(args, sender).perform();
                    break;
                case "unload":
                    new CommandUnload(args, sender, this.plugin).perform();
                    break;
                case "delete":
                    new CommandDelete(args, sender, this.plugin).perform();
                    break;
                case "move-all":
                    new CommandMoveAll(args, sender).perform();
                    break;
                case "list":
                    new CommandList(args, sender, this.plugin).perform();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return immutableFirstArgs;

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "tp":
                case "unload":
                case "delete":
                case "move-all":
                case "list":
                    return WorldUtils.getLoadedWorldNames();
                default:
                    return null;
            }
        }

        return null;
    }
}
