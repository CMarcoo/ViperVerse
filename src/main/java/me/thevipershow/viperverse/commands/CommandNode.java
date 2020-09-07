package me.thevipershow.viperverse.commands;

import org.bukkit.command.CommandSender;

public abstract class CommandNode {
    public static final String prefix = "§7[§aViperVerse§7]: ";
    protected final String[] args;
    protected final CommandSender commandSender;
    protected final int desiredLength;
    protected final String wrongArgsMessage;
    protected final Class<? extends CommandSender>[] desiredSender;
    protected final String wrongCommandSenderMessage;

    private final String getAllowedSenderString() {
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
            Class<? extends CommandSender>... desiredSender) {
        this.args = args;
        this.commandSender = commandSender;
        this.desiredLength = desiredLength;
        this.wrongArgsMessage = prefix + "§athe number of arguments must be §2" + desiredLength;
        this.desiredSender = desiredSender;
        this.wrongCommandSenderMessage = prefix + "§aYou must be a §2" + getAllowedSenderString() + " §ato execute this command.";
    }

    protected abstract void logic();

    protected static long now() {
        return System.currentTimeMillis();
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

        if (!closed) {
           logic();
        }
    }
}
