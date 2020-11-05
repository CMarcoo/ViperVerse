package me.thevipershow.viperverse.commands.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import me.thevipershow.viperverse.WorldUtils;
import me.thevipershow.viperverse.commands.CommandNode;
import me.thevipershow.viperverse.commands.Utils;
import me.thevipershow.viperverse.commands.api.AsyncWorldDeleteEvent;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandDelete extends CommandNode {
    private final Plugin plugin;

    public CommandDelete(String[] args, CommandSender commandSender, Plugin plugin) {
        super(args, commandSender, 2, "viperverse.admin.delete", Player.class, ConsoleCommandSender.class);
        this.plugin = plugin;
    }

    @Override
    protected void logic() {
        final String worldName = args[1];
        final Optional<World> searchedWorld = WorldUtils.findFromString(worldName);
        if (searchedWorld.isPresent()) {
            final World targetWorld = searchedWorld.get();
            final World lobbyWorld = plugin.getServer().getWorlds().get(0);

            commandSender.sendMessage(prefix + Utils.colour("§aStarting the unloading. . ."));

            if (lobbyWorld == null) {
                commandSender.sendMessage(prefix + Utils.colour("§cERROR: something went wrong when searching for a default server."));
                return;
            }

            targetWorld.getPlayers().forEach(p -> p.teleport(lobbyWorld.getSpawnLocation()));

            final long milliStart = now();

            plugin.getServer().unloadWorld(targetWorld, true);

            commandSender.sendMessage(prefix + Utils.colour(String.format("§aWorld \"§2%s§a\" has been unloaded in %d ms", worldName, now() - milliStart)));

            commandSender.sendMessage(prefix + Utils.colour("§aStarting deletion. . ."));

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                final long deleteStart = now();
                final File toDelete = targetWorld.getWorldFolder();

                final AsyncWorldDeleteEvent wde = new AsyncWorldDeleteEvent(toDelete);
                plugin.getServer().getPluginManager().callEvent(wde);

                if (wde.isCancelled()) return;
                try {
                    Files.walk(toDelete.toPath())
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                    commandSender.sendMessage(prefix + Utils.colour(String.format("§a\"§2%s§a\"'s world folder has been delete asynchronously in %d ms",
                            worldName,
                            now() - milliStart)));
                } catch (final IOException e) {
                    commandSender.sendMessage(prefix + Utils.colour("§cERROR: World deletion has failed, check logs."));
                    e.printStackTrace();
                }
            });
        } else {
            commandSender.sendMessage(prefix + Utils.colour(String.format("§aA world named \"§2%s§a\" does not exist.", worldName)));
        }
    }
}
