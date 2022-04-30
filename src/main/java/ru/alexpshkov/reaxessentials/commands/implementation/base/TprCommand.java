package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.configs.implementation.MainConfig;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.teleportation.TeleportationManager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


public class TprCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public TprCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "tpr");
        super.setAliases(Arrays.asList("rtp", "randomtp", "tprandom", "randomteleport"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : player.getName());

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "tpr.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        //Check time
        TeleportationManager teleportationManager = reaxEssentials.getTeleportationManager();
        int timeForNextTpr = teleportationManager.getTimeForNextRandomTeleport(player.getName());
        if (!hasPermission(player, "tpr.delay.bypass") && timeForNextTpr > 0) {
            printMessage(player, ReaxMessage.TPR_DELAY, Utils.convertSecondsToDate(timeForNextTpr));
            return false;
        }

        teleportationManager.setLastRandomTeleport(player.getName(), new Date().getTime());

        printMessage(player, ReaxMessage.TPR_SEARCH_START);
        teleportationManager.findRandomLocation(player.getWorld()).thenAccept(randomLocation -> {
            if (args.length >= 1 && !target.getName().equals(player.getName())) {
                printMessage(player, ReaxMessage.TPR_TELEPORTED_OTHER, target.getName());
                printMessage(target, ReaxMessage.TPR_TELEPORTED_YOU);
            } else printMessage(player, ReaxMessage.TPR_TELEPORTED_YOU);

            target.teleport(randomLocation);
            playSound(target, ReaxSound.TPR_TELEPORTED);
        });

        return true;
    }




}
