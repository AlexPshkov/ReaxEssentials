package ru.alexpshkov.reaxessentials.commands.implementation.economy;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class PayCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public PayCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "pay");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length < 2) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> <amount>");
            return false;
        }

        Player target = Utils.getOnlinePlayer(args[0]);

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        double amount = convertStringToDouble(args[1]);

        if (amount <= 0) {
            String coinsExample = getMessageFromConfig(ReaxMessage.COINS_FORMAT_EXAMPLE);
            printMessage(player, ReaxMessage.INVALID_SYNTAX, "amount", coinsExample);
            return false;
        }

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            if (userEntity == null) return;

            if (userEntity.getCoins() < amount) {
                printMessage(player, ReaxMessage.COINS_LACK, userEntity.getCoins() + "");
                return;
            }

            dataBase.getUserEntity(target.getName()).thenAccept(targetEntity -> {
                targetEntity.setCoins(targetEntity.getCoins() + amount);
                dataBase.saveUserEntity(targetEntity).thenAccept(savedTargetEntity -> {
                    if (savedTargetEntity == null) return;
                    userEntity.setCoins(userEntity.getCoins() - amount);
                    dataBase.saveUserEntity(userEntity).thenAccept(savedUserEntity -> {
                        printMessage(player, ReaxMessage.COINS_PAY, amount + "", target.getName());
                        printMessage(target, ReaxMessage.COINS_RECEIVE, player.getName(), amount + "");
                        playSound(target, ReaxSound.COINS_RECEIVED);
                    });

                });
            });
        });


        return true;
    }
}

