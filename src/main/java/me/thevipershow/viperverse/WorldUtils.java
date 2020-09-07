package me.thevipershow.viperverse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;

public final class WorldUtils {
    private WorldUtils() {
    }

    public static Optional<World> findFromString(final String s) {
        return Bukkit.getWorlds()
                .stream()
                .filter(w -> w.getName().equals(s))
                .findAny();
    }

    public static List<String> getLoadedWorldNames() {
        return Bukkit.getWorlds()
                .stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    public static boolean isDefinetivelyABukkitWorldFolder(final File file) {
        if (!file.isDirectory()) return false;
        if (!file.exists()) return false;
        final String fileName = file.getName();
        if (fileName.equals("cache")) return false;
        if (file.equals("logs")) return false;
        if (file.equals("plugins")) return false;
        final File[] filez = file.listFiles();
        double certain = 1.0;
        for (final File f : filez) {
            final String fName = f.getName();
            if (f.isDirectory()) {
                switch (fName) {
                    case "data":
                        certain *= 2.0;
                        break;
                    case "playerdata":
                        certain *= 3.5;
                        break;
                    case "region":
                        certain *= 3.5;
                        break;
                    case "stats":
                        certain *= 2.5;
                        break;
                }
            } else {
                try {
                    final String ext = fName.split("\\.")[1];
                    switch (ext) {
                        case "dat":
                            certain += 50.0;
                            break;
                        case "dat_old":
                            certain += 55.0;
                            break;
                        case "lock":
                            certain += 35.0;
                            break;
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        return certain >= 100.00;
    }


    public static List<String> getPossibleWorldDirectories(final Server server) {
        final List<String> fList = new ArrayList<>();
        for (final File file : server.getWorldContainer().listFiles())
            if (isDefinetivelyABukkitWorldFolder(file))
                fList.add(file.getName());

        return Collections.unmodifiableList(fList);
    }
}
