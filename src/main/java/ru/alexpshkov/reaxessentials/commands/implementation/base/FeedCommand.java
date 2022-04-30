package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Collections;

public class FeedCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public FeedCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "feed");
        super.setAliases(Collections.singletonList("eat"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "feed")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : player.getName());

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "feed.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length >= 1 && !target.getName().equals(player.getName())) {
            printMessage(player, ReaxMessage.FEED, target.getName());
            printMessage(target, ReaxMessage.FEED_YOU, target.getName());
        } else printMessage(target, ReaxMessage.FEED_YOU, target.getName());

        playSound(target, ReaxSound.FEED_YOU);
        target.setFoodLevel(20);

        return true;
    }


}
