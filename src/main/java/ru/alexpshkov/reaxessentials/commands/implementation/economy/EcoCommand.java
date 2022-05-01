package ru.alexpshkov.reaxessentials.commands.implementation.economy;

import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;
import java.util.Locale;

public class EcoCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public EcoCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "eco");
        super.setAliases(Arrays.asList("economy"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "economy")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length < 3) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + "<give|take|set> <playerName> <amount>");
            return false;
        }


        double amount = convertStringToDouble(args[2]);
        if (amount <= 0) {
            String coinsExample = getMessageFromConfig(ReaxMessage.COINS_FORMAT_EXAMPLE);
            printMessage(commandSender, ReaxMessage.INVALID_FORMAT, "amount", coinsExample);
            return false;
        }

        dataBase.getUserEntity(args[1]).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }

            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "give": {
                    userEntity.setCoins(userEntity.getCoins() + amount);
                    printMessage(commandSender, ReaxMessage.COINS_GIVE, userEntity.getUserName(), amount + "");
                    break;
                }
                case "take": {
                    userEntity.setCoins(userEntity.getCoins() - amount);
                    printMessage(commandSender, ReaxMessage.COINS_TAKE, userEntity.getUserName(), amount + "");
                    break;
                }
                case "set": {
                    userEntity.setCoins(amount);
                    printMessage(commandSender, ReaxMessage.COINS_SET, userEntity.getUserName(), amount + "");
                    break;
                }
                default: {
                    printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + "<give|take|set> " + args[1] + " " + args[2]);
                    return;
                }
            }

           dataBase.saveUserEntity(userEntity);
        });


        return true;
    }
}

