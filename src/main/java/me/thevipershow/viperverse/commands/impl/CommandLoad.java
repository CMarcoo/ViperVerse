package me.thevipershow.viperverse.commands.impl;

import java.util.Optional;
import me.thevipershow.viperverse.WorldUtils;
import me.thevipershow.viperverse.commands.CommandNode;
import me.thevipershow.viperverse.commands.Utils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandLoad extends CommandNode {
    private final Plugin plugin;

    protected CommandLoad(String[] args, CommandSender commandSender, Plugin plugin) {
        super(args, commandSender, 2, "viperverse.admin.load", ConsoleCommandSender.class, Player.class);
        this.plugin = plugin;
    }

    private World.Environment determineEnvironment(final String worldName) {
        if (worldName.toLowerCase().equals("world") || worldName.toLowerCase().contains("void")) {
            return World.Environment.NORMAL;
        } else if (worldName.toLowerCase().contains("nether")) {
            return World.Environment.NETHER;
        } else if (worldName.toLowerCase().contains("end")) {
            return World.Environment.THE_END;
        }
        return World.Environment.NORMAL;
    }

    private WorldType determineType(final String worldName) {
        final String hrCaseCopy = worldName.toUpperCase();
        for (final WorldType type : WorldType.values())
            if (hrCaseCopy.contains(type.name())) return type;
        return WorldType.NORMAL;
    }

    @Override
    protected void logic() {
        final String worldName = args[1];
        final Optional<String> opt = WorldUtils.getPossibleWorldDirectories(plugin.getServer())
                .stream()
                .filter(str -> str.equals(worldName))
                .findAny();
        if (!opt.isPresent()) {
            commandSender.sendMessage(prefix + Utils.colour("§cWe could not find a valid world to load with such name."));
        } else {
            commandSender.sendMessage(prefix + Utils.colour("§aStarting to load world . . ."));
            final long startAt = now();

            final WorldCreator god = WorldCreator.name(worldName)
                                                 .environment(determineEnvironment(worldName))
                                                 .type(determineType(worldName));

            final World world = god.createWorld();
            if (world != null) {
                commandSender.sendMessage(prefix + Utils.colour("§aWorld created correctly."));
                commandSender.sendMessage(prefix + Utils.colour(String.format("§aTime taken §2%.3f §as", (now() - startAt) / 1E3)));
            }
        }
    }
}
