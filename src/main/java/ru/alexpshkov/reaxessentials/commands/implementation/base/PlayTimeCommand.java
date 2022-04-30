package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Arrays;

public class PlayTimeCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public PlayTimeCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "playtime");
        super.setAliases(Arrays.asList("playedtime", "timeplayed"));
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

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "playtime.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        reaxEssentials.getDataBase().getUserEntity(target.getName()).thenAccept(userEntity -> {
            if (args.length >= 1 && !target.getName().equals(player.getName())) printMessage(player, ReaxMessage.PLAY_TIME_OTHER, target.getPlayerListName(), Utils.convertSecondsToDate(userEntity.getSecondsPlayed()));
            else printMessage(player,ReaxMessage.PLAY_TIME_OWN, Utils.convertSecondsToDate(userEntity.getSecondsPlayed()));
        });

        return true;
    }


}
