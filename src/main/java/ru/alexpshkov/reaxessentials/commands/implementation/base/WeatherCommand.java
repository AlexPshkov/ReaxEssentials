package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class WeatherCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public WeatherCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "weather");
        super.setAliases(Arrays.asList("sun", "storm", "rain", "thundering"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        String reqWeather = "";

        if (!hasPermission(player, "weather")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        switch (alias) {
            case "/sun": {
                reqWeather = "clear";
                break;
            }
            case "/thundering": {
                reqWeather = "thundering";
                break;
            }
            case "/storm": {
                reqWeather = "storm";
                break;
            }
            case "/rain": {
                break;
            }
            default: {
                if (args.length == 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <weather>");
                    return false;
                }
                reqWeather = args[0];
            }
        }

        switch (reqWeather) {
            case "thundering": case "thunder":
                sayPlayer(player, ReaxMessage.WEATHER_THUNDERING);
                player.getWorld().setThundering(true);
                break;
            case "storm": case "rain":
                sayPlayer(player, ReaxMessage.WEATHER_STORM);
                player.getWorld().setStorm(true);
                break;
            case "sun": case "clear":
                sayPlayer(player, ReaxMessage.WEATHER_SUN);
                World world = player.getWorld();
                world.setThundering(false);
                world.setStorm(false);
                break;
            default:
                printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <weather>");
        }

        return true;
    }

    /**
     * Announce player of gameMode change
     */
    private void sayPlayer(Player player,  ReaxMessage reaxMessage) {
        printMessage(player, reaxMessage);
        playSound(player, ReaxSound.WEATHER_SET);
    }

}
