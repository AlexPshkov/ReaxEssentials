package ru.alexpshkov.reaxessentials.commands.implementation.kit;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Arrays;

public class KitCreateCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public KitCreateCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "kitcreate");
        super.setAliases(Arrays.asList("createkit", "newkit", "kitnew"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "kit.change")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <kitName>");
            return false;
        }

        reaxEssentials.getDataBase().isSuchKit(args[0]).thenAccept(flag -> {
            if (flag) {
                printMessage(player, ReaxMessage.KIT_ALREADY_EXISTS, args[0]);
                return;
            }
            reaxEssentials.getKitsManager().saveAsKit(args[0], player.getInventory());
            printMessage(player, ReaxMessage.KIT_CREATED, args[0]);
        });

        return true;
    }
}
