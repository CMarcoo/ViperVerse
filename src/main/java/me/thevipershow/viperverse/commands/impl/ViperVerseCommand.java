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
import static me.thevipershow.viperverse.commands.Utils.colour;

public final class ViperVerseCommand implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    private final static List<String> immutableFirstArgs = Collections.unmodifiableList(Lists.newArrayList(
            "list","tp", "unload", "delete", "move-all", "load"));

    public ViperVerseCommand(final Plugin plugin) {
        this.plugin = plugin;
        final Server srv = plugin.getServer();
        final PluginCommand pluginCommand = srv.getPluginCommand("vv");
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    private static void sendHelp(final CommandSender sender) {
        sender.sendMessage(colour("§aViperVerse's Help Page§7:")); // │ ├
        sender.sendMessage(colour("§7  │"));
        sender.sendMessage(colour("§7  ├─ §8[§avv tp §7<§2w§7>§8]"));
        sender.sendMessage(colour("§7  │  §o§fUsed to teleport yourself to a world."));
        sender.sendMessage(colour("§7  │"));
        sender.sendMessage(colour("§7  ├─ §8[§avv unload §7<§2w§7>§8]"));
        sender.sendMessage(colour("§7  │  §o§fUsed to unload a loaded world."));
        sender.sendMessage(colour("§7  │"));
        sender.sendMessage(colour("§7  ├─ §8[§avv delete §7<§2w§7>§8]"));
        sender.sendMessage(colour("§7  │  §o§fUsed to delete a world §r§7(§4§lIRREVERSIBLE!§r§7)"));
        sender.sendMessage(colour("§7  │"));
        sender.sendMessage(colour("§7  ├─ §8[§avv move-all §7<§2w§7>§8]"));
        sender.sendMessage(colour("§7  │  §o§fUsed to teleport everyone to a world."));
        sender.sendMessage(colour("§7  │"));
        sender.sendMessage(colour("§7  ├─ §8[§avv load §7<§2w§7>§8]"));
        sender.sendMessage(colour("§7  │  §o§fUsed to load a world in the server folder."));
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
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
                case "load":
                    new CommandLoad(args, sender, this.plugin).perform();
                    break;
                default:
                    break;
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return immutableFirstArgs;
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "tp":
                case "unload":
                case "delete":
                case "load":
                case "move-all":
                case "list":
                    return WorldUtils.getPossibleWorldDirectories(plugin.getServer());
                default:
                    return null;
            }
        }

        return null;
    }
}
