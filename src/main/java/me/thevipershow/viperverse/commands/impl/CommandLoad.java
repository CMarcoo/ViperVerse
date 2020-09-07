package me.thevipershow.viperverse.commands.impl;

import me.thevipershow.viperverse.commands.CommandNode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandLoad extends CommandNode {
    private final Plugin plugin;

    protected CommandLoad(String[] args, CommandSender commandSender, Plugin plugin) {
        super(args, commandSender, 2, ConsoleCommandSender.class, Player.class);
        this.plugin = plugin;
    }

    @Override
    protected void logic() {

    }
}
