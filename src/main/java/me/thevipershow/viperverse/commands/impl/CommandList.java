package me.thevipershow.viperverse.commands.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import me.thevipershow.viperverse.WorldUtils;
import me.thevipershow.viperverse.commands.CommandNode;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandList extends CommandNode {
    private final Plugin plugin;

    protected CommandList(String[] args, CommandSender commandSender, Plugin plugin) {
        super(args, commandSender, 1, Player.class);
        this.plugin = plugin;
    }

    public static File worldFolderFinder(final Plugin plugin, final String wName) {
        final File worldContainerFolder = plugin.getServer().getWorldContainer();
        for (final File file : worldContainerFolder.listFiles()) {
            if (file.getName().equals(wName)) return file;
        }
        return null;
    }

    public static long folderFileSize(final File folder) {
        if (folder == null) return -1;
        try {
            return Files.walk(folder.toPath()).mapToLong(p -> p.toFile().length()).sum();
        } catch (IOException e) {
        }
        return -1L;
    }

    public static TextComponent buildWorldInfoHover(final Plugin plugin, final String worldName) {
        final TextComponent resultingComp = new TextComponent();

        final TextComponent wNameComp = new TextComponent();
        wNameComp.setText("§7-  " + worldName);

        final World targetWorld = Bukkit.getWorld(worldName);
        final File worldFolderFile = worldFolderFinder(plugin, worldName);
        final long worldFolderSize = folderFileSize(worldFolderFile);
        final TextComponent statusComp = new TextComponent(" §7[§eSTATUS§7] ");
        final TextComponent[] hoverStatusComp = {new TextComponent()};

        hoverStatusComp[0].setText("§aStatus§7: " + (targetWorld == null ? "§cUNLOADED" : "§2LOADED") +
                "\n§aFile name§a: " + (worldFolderFile == null ? "§cUNAVAILABLE" : "§f" + worldFolderFile.getAbsolutePath())
                + "\n\n§aFile size§a: " + (worldFolderSize == -1L ? "§cUNAVAILABLE" : "§f" + worldFolderSize / 1000L + " KB"));

        statusComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverStatusComp));

        wNameComp.addExtra(statusComp);

        if (targetWorld != null) {
            final TextComponent infoComp = new TextComponent("§7[§bINFO§7] ");
            final TextComponent[] hoverInfoComp = {new TextComponent()};
            hoverInfoComp[0].setText("§aPlayers§7: §f" + targetWorld.getPlayers().size() +
                    "\n§aLiving Entities§7: §f" + targetWorld.getLivingEntities().size() +
                    "\n§aActive Chunks§7: §f" + targetWorld.getLoadedChunks().length +
                    "\n§aDifficulty§7: §f" + targetWorld.getDifficulty().name() +
                    "\n§aWeather§7: §f" + (targetWorld.hasStorm() || targetWorld.isThundering() ? "Clear" : "Raining") +
                    "\n§aTime§7: §f" + targetWorld.getTime() +
                    "\n§aWorld's Age§7: §f" + targetWorld.getFullTime());
            infoComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverInfoComp));

            wNameComp.addExtra(infoComp);
        }

        return wNameComp;
    }

    @Override
    protected void logic() {
        commandSender.sendMessage(prefix + "§aLoaded worlds:");
        WorldUtils.getPossibleWorldDirectories(plugin.getServer())
                .forEach(wname -> ((Player) commandSender).spigot().sendMessage(buildWorldInfoHover(plugin, wname)));
    }
}
