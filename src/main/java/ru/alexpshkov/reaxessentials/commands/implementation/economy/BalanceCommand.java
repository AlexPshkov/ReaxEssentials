package ru.alexpshkov.reaxessentials.commands.implementation.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class BalanceCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public BalanceCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "balance");
        super.setAliases(Arrays.asList("bal"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        String stringTarget = args.length >= 1 ? args[0] : player.getName();

        if (!player.getName().equalsIgnoreCase(stringTarget) && !hasPermission(player, "balance.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        reaxEssentials.getDataBase().getUserEntity(stringTarget).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
                return;
            }
            if (player.getName().equalsIgnoreCase(stringTarget)) printMessage(player, ReaxMessage.COINS_AMOUNT, userEntity.getCoins() + "");
            else printMessage(player, ReaxMessage.COINS_AMOUNT_OTHER, stringTarget, userEntity.getCoins() + "");

            Bukkit.getScheduler().runTask(reaxEssentials, () -> playSound(player, ReaxSound.COINS_RECEIVED));
        });


        return true;
    }
}

