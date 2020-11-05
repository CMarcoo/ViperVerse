package me.thevipershow.viperverse.commands.impl;

import java.util.Optional;
import me.thevipershow.viperverse.commands.CommandNode;
import me.thevipershow.viperverse.WorldUtils;
import static me.thevipershow.viperverse.commands.Utils.colour;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CommandTeleport extends CommandNode {

    protected CommandTeleport(String[] args, CommandSender commandSender) {
        super(args, commandSender, 2, "viperverse.admin.teleport", Player.class);
    }

    @Override
    public void logic() {
        final String worldName = args[1];
        final Optional<World> searchedWorld = WorldUtils.findFromString(worldName);
        if (searchedWorld.isPresent()) {
            final World w = searchedWorld.get();
            ((Player) commandSender).teleport(w.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            commandSender.sendMessage(prefix + String.format(colour("§aYou have been moved to \"§2%s§a\"'s spawnpoint."), worldName));
        } else {
            commandSender.sendMessage(prefix + String.format(colour("§aA world named \"§2%s§a\" does not exist."), worldName));
        }
    }
}
