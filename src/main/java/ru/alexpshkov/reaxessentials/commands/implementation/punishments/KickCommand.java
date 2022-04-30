package ru.alexpshkov.reaxessentials.commands.implementation.punishments;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

public class KickCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public KickCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "kick");
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "kick")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 2) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> <reason>");
            return false;
        }

        Player target = Utils.getOnlinePlayer(args[0]);
        if (target == null) {
            printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }
        String reason = combineArgsIntoString(1, args);

        String messageToSendPlayer = getMessageFromConfig(ReaxMessage.KICK_LOGIN, commandSender.getName(), reason);
        target.kickPlayer(messageToSendPlayer);
        String messageToSend = getMessageFromConfig(ReaxMessage.KICK_CHAT, commandSender.getName(), target.getName(), reason);
        Bukkit.getOnlinePlayers().forEach(player -> {
            printMessage(player, messageToSend);
            playSound(player, ReaxSound.USER_KICKED);
        });



        return true;
    }


}
