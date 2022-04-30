package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class AdminChatCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public AdminChatCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "adminchat");
        super.setAliases(Arrays.asList("a", "achat", "ac"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender sender, String alias, String[] args) {
        if (!hasPermission(sender, "adminchat")) {
            printMessage(sender, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length < 1) {
            printMessage(sender, ReaxMessage.INVALID_SYNTAX, alias + " <message>");
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }

        String message = stringBuilder.toString();
        String messageToSend = getMessageFromConfig(ReaxMessage.ADMINCHAT_FORMAT, sender.getName(), message);

        Bukkit.getOnlinePlayers().stream().filter(pl -> hasPermission(pl, "adminchat")).forEach(player -> {
            player.sendMessage(messageToSend);
            playSound(player, ReaxSound.ADMINCHAT_RECEIVED);
        });

        if (!(sender instanceof Player)) printMessage(sender, ReaxMessage.ADMINCHAT_FORMAT, sender.getName(), message);
        return true;
    }


}
