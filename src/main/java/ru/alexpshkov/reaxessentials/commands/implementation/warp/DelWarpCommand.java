package ru.alexpshkov.reaxessentials.commands.implementation.warp;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class DelWarpCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public DelWarpCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "delwarp");
        super.setAliases(Arrays.asList("warpdel", "warpremove", "removewarp"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "delwarp")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <warpName>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getWarpEntity(args[0]).thenAccept(warpEntity -> {
            if (warpEntity == null) {
                printMessage(player, ReaxMessage.WARP_NOT_EXISTS, args[0]);
                return;
            }
            if (!warpEntity.getWhoOwned().getUserName().equalsIgnoreCase(player.getName()) && !hasPermission(player, "delwarp.others")) {
                printMessage(player, ReaxMessage.NO_PERM);
                return;
            }
            dataBase.removeWarpEntity(warpEntity).thenAccept(flag -> {
                printMessage(player, ReaxMessage.WARP_REMOVED, warpEntity.getWarpName());
                reaxEssentials.getSoundsConfig().playSound(player, ReaxSound.WARP_REMOVED);
            });
        });
        return true;
    }
}
