package me.thevipershow.viperverse.commands;

import org.bukkit.ChatColor;

public final class Utils {

    private Utils() {}

    public static String colour(final String s) {
        return ChatColor.translateAlternateColorCodes('§', s);
    }
}
