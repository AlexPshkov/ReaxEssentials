package ru.alexpshkov.reaxessentials.commands.implementation.tp;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.teleportation.TeleportationRequest;

import java.util.Arrays;

public class TpAcceptCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public TpAcceptCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "tpaccept");
        super.setAliases(Arrays.asList("tpaaccept", "tpyes"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        Player target = Utils.getOnlinePlayer(args[0]);

        //Check if username is invalid
        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        //Check if there is no requests from this user
        TeleportationRequest teleportationRequest = reaxEssentials.getTeleportationManager().getTeleportationRequest(target);
        if (teleportationRequest == null || !teleportationRequest.getTarget().getName().equals(player.getName())) {
            printMessage(player, ReaxMessage.TELEPORTATION_REQUEST_EMPTY);
            return false;
        }

        target.teleport(player);
        printMessage(player, ReaxMessage.TELEPORTATION_REQUEST_ACCEPT, target.getPlayerListName());
        printMessage(target, ReaxMessage.TELEPORTATION_REQUEST_ACCEPT_BY, player.getPlayerListName());
        reaxEssentials.getTeleportationManager().removeTeleportationRequest(target);
        reaxEssentials.getSoundsConfig().playSound(target, ReaxSound.TELEPORTATION_ACCEPT);
        return true;
    }


}
