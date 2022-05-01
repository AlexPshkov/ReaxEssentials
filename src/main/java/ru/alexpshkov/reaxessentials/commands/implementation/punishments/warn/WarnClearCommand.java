package ru.alexpshkov.reaxessentials.commands.implementation.punishments.warn;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class WarnClearCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public WarnClearCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "warnclear");
        super.setAliases(Arrays.asList("unwarn", "clearwarns"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "warn.clear")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length == 0) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> [amount]");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(args[0]).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }

            int amount = args.length >= 2 ? convertStringToInt(args[1]) : userEntity.getWarnsAmount();

            if (amount < 0) {
                String example = getMessageFromConfig(ReaxMessage.NUMBER_FORMAT_EXAMPLE);
                printMessage(commandSender, ReaxMessage.INVALID_FORMAT, "amount", example);
                return;
            }
            int newAmount = userEntity.getWarnsAmount() - amount;
            if (newAmount < 0) newAmount = 0;

            userEntity.setWarnsAmount(newAmount);

            dataBase.saveUserEntity(userEntity).thenAccept(savedUserEntity -> {
                printMessage(commandSender, ReaxMessage.WARN_CLEAR, amount + "", savedUserEntity.getUserName(), savedUserEntity.getWarnsAmount() + "");
                Player target = Utils.getOnlinePlayer(userEntity.getUserName());
                if (target == null) return;

                printMessage(target, ReaxMessage.WARN_CLEAR_FROM_YOU, commandSender.getName(), amount + "", savedUserEntity.getWarnsAmount() + "");
            });


        });
        return true;
    }


}
