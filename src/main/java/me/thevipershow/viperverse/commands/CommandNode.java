package me.thevipershow.viperverse.commands;

import org.bukkit.command.CommandSender;
import static me.thevipershow.viperverse.commands.Utils.colour;

public abstract class CommandNode {
    public static final String prefix = colour("§7[§aViperVerse§7]: ");
    protected final String[] args;
    protected final CommandSender commandSender;
    protected final int desiredLength;
    protected final String wrongArgsMessage;
    protected final Class<? extends CommandSender>[] desiredSender;
    protected final String wrongCommandSenderMessage;
    protected final String nodePerm;

    private String getAllowedSenderString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < desiredSender.length; i++) {
            stringBuilder.append(desiredSender[i].getName());
            if (i + 1 < desiredSender.length)
                stringBuilder.append(" or ");
        }
        return stringBuilder.toString();
    }

    @SafeVarargs
    protected CommandNode(
            String[] args,
            CommandSender commandSender,
            int desiredLength,
            String nodePerm, Class<? extends CommandSender>... desiredSender) {
        this.args = args;
        this.commandSender = commandSender;
        this.desiredLength = desiredLength;
        this.wrongArgsMessage = colour( prefix + "§athe number of arguments must be §2" + desiredLength);
        this.nodePerm = nodePerm;
        this.desiredSender = desiredSender;
        this.wrongCommandSenderMessage = colour( prefix + "§aYou must be a §2" + getAllowedSenderString() + " §ato execute this command.");
    }

    protected abstract void logic();

    protected static long now() {
        return System.currentTimeMillis();
    }

    private static boolean checkForPermission(final CommandSender commandSender, final String permission) {
        if (!commandSender.hasPermission(permission)) {
            commandSender.sendMessage(colour(prefix + "§cMissing permissions to do this."));
            return false;
        }
        return true;
    }

    public void perform() {
        boolean closed = false;
        if (desiredLength != -1 && args.length != desiredLength) {
            commandSender.sendMessage(wrongArgsMessage);
            closed = true;
        }

        boolean foundNone = true;
        for (final Class<? extends CommandSender> clazz : desiredSender) {
            if (clazz.isInstance(commandSender)) {
                foundNone = false;
                break;
            }
        }

        closed = closed && !foundNone;

        if (!closed && checkForPermission(this.commandSender, this.nodePerm)) logic();
    }
}
