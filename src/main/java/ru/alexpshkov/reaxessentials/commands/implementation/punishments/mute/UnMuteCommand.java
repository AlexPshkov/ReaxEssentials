package ru.alexpshkov.reaxessentials.commands.implementation.punishments.mute;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;
import java.util.Date;

public class UnMuteCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public UnMuteCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "unmute");
        super.setAliases(Arrays.asList("tempunmute"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "unmute")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length == 0) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(args[0]).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }

            if (userEntity.getMuteExpired() <= new Date().getTime()) {
                printMessage(commandSender, ReaxMessage.MUTE_UNMUTE_CHAT_ALREADY);
                return;
            }
            userEntity.setMuteExpired(new Date().getTime());
            dataBase.saveUserEntity(userEntity).thenAccept(savedUserEntity -> {
                String messageToSend = getMessageFromConfig(ReaxMessage.MUTE_UNMUTE_CHAT, commandSender.getName(), userEntity.getUserName());
                Bukkit.getOnlinePlayers().forEach(player -> printMessage(player, messageToSend));
            });

        });
        return true;
    }


}
