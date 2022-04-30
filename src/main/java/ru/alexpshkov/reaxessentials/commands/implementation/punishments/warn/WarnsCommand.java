package ru.alexpshkov.reaxessentials.commands.implementation.punishments.warn;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class WarnsCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public WarnsCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "warns");
        super.setAliases(Arrays.asList("warnsamount", "amountwarns"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();
        String stringTarget = args.length >= 1 ? args[0] : player.getName();

        if (!player.getName().equalsIgnoreCase(stringTarget) && !hasPermission(player, "warnsamount.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        dataBase.getUserEntity(stringTarget).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(player, ReaxMessage.USER_NOTFOUND, stringTarget);
                return;
            }
            if (stringTarget.equalsIgnoreCase(player.getName()))
                printMessage(player, ReaxMessage.WARNS_AMOUNT_YOU, userEntity.getWarnsAmount() + "");
            else
                printMessage(player, ReaxMessage.WARNS_AMOUNT_OTHER, userEntity.getUserName(), userEntity.getWarnsAmount() + "");
        });
        return true;
    }


}
