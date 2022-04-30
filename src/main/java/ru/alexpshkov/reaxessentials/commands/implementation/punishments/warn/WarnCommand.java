package ru.alexpshkov.reaxessentials.commands.implementation.punishments.warn;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class WarnCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public WarnCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "warn");
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "warn")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 2) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> <reason>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(args[0]).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }

            String reason = combineArgsIntoString(1, args);
            userEntity.setWarnsAmount(userEntity.getWarnsAmount() + 1);

            dataBase.saveUserEntity(userEntity).thenAccept(savedUserEntity -> {
                String messageToSend = getMessageFromConfig(ReaxMessage.WARN_CHAT, commandSender.getName(), savedUserEntity.getWarnsAmount() + "", savedUserEntity.getUserName(), reason);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    printMessage(player, messageToSend);
                    playSound(player, ReaxSound.USER_WARNED);
                });
            });

        });
        return true;
    }


}
