package me.thevipershow.viperverse.commands.impl;

import java.util.Optional;
import me.thevipershow.viperverse.WorldUtils;
import me.thevipershow.viperverse.commands.CommandNode;
import me.thevipershow.viperverse.commands.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandMoveAll extends CommandNode {
    protected CommandMoveAll(String[] args, CommandSender commandSender) {
        super(args, commandSender, 2, "viperverse.admin.move-all", Player.class, ConsoleCommandSender.class);
    }

    @Override
    protected void logic() {
        final String worldName = args[1];
        final Optional<World> searchedWorld = WorldUtils.findFromString(worldName);
        if (searchedWorld.isPresent()) {
            final World targetWorld = searchedWorld.get();
            Bukkit.getWorlds().stream().filter(w -> !w.equals(targetWorld)).flatMap(v -> v.getPlayers().stream()).forEach(p -> p.teleport(targetWorld.getSpawnLocation()));
            commandSender.sendMessage(prefix + Utils.colour(String.format("§aAll players have been moved to \"§2%s§a\"'s spawnpoint.", worldName)));
        } else {
            commandSender.sendMessage(prefix + Utils.colour(String.format("§aA world named \"§2%s§a\" does not exist.", worldName)));
        }
    }
}
