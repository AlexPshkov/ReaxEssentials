package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class GameModeCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public GameModeCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "gamemode");
        super.setAliases(Arrays.asList("gmsp", "gmc", "gms", "gma", "gm"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        String reqMode = "";
        String target = "";

        if (!hasPermission(player, "gamemode")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        switch (alias) {
            case "/gma": {
                reqMode = "adventure";
                break;
            }
            case "/gmsp": {
                reqMode = "spectator";
                break;
            }
            case "/gmc": {
                reqMode = "creative";
                break;
            }
            case "/gms": {
                reqMode = "survival";
                break;
            }
            default: {
                if (args.length == 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <gameMode>");
                    return false;
                }
                if (args.length == 1) target = player.getName();
                if (args.length >= 2) target = args[1];
                reqMode = args[0];
            }
        }

        target = target.isEmpty() ? args.length >= 1 ? args[0] : player.getName() : target;
        Player playerTarget = Utils.getOnlinePlayer(target);

        if (playerTarget == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, target);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(playerTarget.getName()) && !hasPermission(player, "gamemode.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        switch (reqMode) {
            case "0": case "surv": case "survival":
                announce(player, playerTarget, ReaxMessage.GAMEMODE_SURVIVAL);
                playerTarget.setGameMode(GameMode.SURVIVAL);
                break;
            case "1": case "creat": case "creative":
                playerTarget.setGameMode(GameMode.CREATIVE);
                announce(player, playerTarget, ReaxMessage.GAMEMODE_CREATIVE);
                break;
            case "2": case "adv": case "adventure":
                playerTarget.setGameMode(GameMode.ADVENTURE);
                announce(player, playerTarget, ReaxMessage.GAMEMODE_ADVENTURE);
                break;
            case "3": case "spec": case "spectator":
                playerTarget.setGameMode(GameMode.SPECTATOR);
                announce(player, playerTarget, ReaxMessage.GAMEMODE_SPECTATOR);
                break;
            default:
                printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <gameMode>");
        }

        return true;
    }

    /**
     * Announce player of gameMode change
     */
    private void announce(Player player, Player target, ReaxMessage reaxMessage) {
        String gameMode = reaxEssentials.getMessagesConfig().getMessage(reaxMessage);

        printMessage(target, ReaxMessage.GAMEMODE_CHANGE_OWN, gameMode);
        if (!player.getName().equalsIgnoreCase(target.getName())) printMessage(player, ReaxMessage.GAMEMODE_CHANGE, target.getName(), gameMode);
        playSound(target, ReaxSound.GAMEMODE_CHANGE);
    }

}
