package me.thevipershow.viperverse.commands.impl;

import java.util.Optional;
import me.thevipershow.viperverse.WorldUtils;
import me.thevipershow.viperverse.commands.CommandNode;
import me.thevipershow.viperverse.commands.Utils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandUnload extends CommandNode {
    private final Plugin plugin;

    protected CommandUnload(String[] args, CommandSender commandSender, Plugin plugin) {
        super(args, commandSender, 2, "viperverse.admin.unload", Player.class, ConsoleCommandSender.class);
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
        } else {
            commandSender.sendMessage(prefix + Utils.colour(String.format("§aA world named \"§2%s§a\" does not exist.", worldName)));
        }
    }
}
