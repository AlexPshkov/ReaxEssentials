package ru.alexpshkov.reaxessentials.commands.implementation.punishments.ban;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;
import java.util.Date;

public class BanCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public BanCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "ban");
        super.setAliases(Arrays.asList("tempban"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "ban")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 3) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> <time> <reason>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(args[0]).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }
            long time = -1;
            try {
                time = Utils.convertToSeconds(args[1]);
            } catch (NumberFormatException exception) {
                printMessage(commandSender, ReaxMessage.INVALID_FORMAT, "time", getMessageFromConfig(ReaxMessage.TIME_FORMAT_EXAMPLE));
                return;
            }

            long expireTime = new Date().getTime() + time;
            String reason = combineArgsIntoString(2, args);

            String messageToSend = getMessageFromConfig(ReaxMessage.BANUSER_CHAT, commandSender.getName(), userEntity.getUserName(), Utils.convertMillisToDate(expireTime), reason);
            Bukkit.getOnlinePlayers().forEach(player -> {
                printMessage(player, messageToSend);
                playSound(player, ReaxSound.USER_BANNED);
            });


            Bukkit.getScheduler().runTaskAsynchronously(reaxEssentials, () -> {
                String banLoginMessage = getMessageFromConfig(ReaxMessage.BANUSER_LOGIN, commandSender.getName(), Utils.convertMillisToDate(expireTime), reason);
                BanList banList = Bukkit.getBanList(BanList.Type.NAME);
                banList.addBan(userEntity.getUserName(), reason, new Date(expireTime), commandSender.getName());
                Player player = Utils.getOnlinePlayer(args[0]);
                if (player == null) return;
                Bukkit.getScheduler().runTask(reaxEssentials, () -> player.kickPlayer(banLoginMessage));
            });

        });
        return true;
    }


}
