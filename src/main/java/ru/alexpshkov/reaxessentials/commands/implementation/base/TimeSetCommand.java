package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class TimeSetCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public TimeSetCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "timeset");
        super.setAliases(Arrays.asList("settime", "day", "night"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        String reqTime = "";

        if (!hasPermission(player, "timeset")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        switch (alias) {
            case "/day": {
                reqTime = "0";
                break;
            }
            case "/night": {
                reqTime = "18000";
                break;
            }
            default: {
                if (args.length == 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <time>");
                    return false;
                }
                reqTime = args[0];
            }
        }

        try {
            long ticks = Long.parseLong(reqTime);
            player.getWorld().setTime(ticks);
            printMessage(player, ReaxMessage.TIMESET, reqTime);
            playSound(player, ReaxSound.TIME_SET);
        } catch (NumberFormatException exception) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <time>");
        }

        return true;
    }


}
