package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class TeleportCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public TeleportCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "tp");
        super.setAliases(Arrays.asList("teleport", "tppos"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission( commandSender, "teleport")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length == 0) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        if (args.length < 2 && !(commandSender instanceof Player)) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <who> <where>");
            return false;
        }

        if (args.length >= 3) {
            if (!(commandSender instanceof Player)) {
                printMessage(commandSender, ReaxMessage.ONLY_PLAYERS);
                return false;
            }

            String stringTarget = args.length >= 4 ? args[3] : commandSender.getName();
            Player target = Utils.getOnlinePlayer(stringTarget);
            if (target == null) {
                printMessage(commandSender, ReaxMessage.USER_NOTFOUND, stringTarget);
                return false;
            }
            Player player = (Player) commandSender;

            try {
                int x = args[0].equals("~") ? ((Player) commandSender).getLocation().getBlockX() : Integer.parseInt(args[0]);
                int y = args[1].equals("~") ? ((Player) commandSender).getLocation().getBlockY() : Integer.parseInt(args[1]);
                int z = args[2].equals("~") ? ((Player) commandSender).getLocation().getBlockZ() : Integer.parseInt(args[2]);

                String stringPosition = "x:" + x + " y:" + y + " z:" + z;
                if (!stringTarget.equals(commandSender.getName())) printMessage(commandSender, ReaxMessage.TELEPORTATION_POSITION_OTHER, stringTarget, stringPosition);
                else if (!hasPermission( commandSender, "teleport.others")) {
                    printMessage(commandSender, ReaxMessage.NO_PERM);
                    return false;
                }

                target.teleport(new Location(player.getWorld(), x, y, z));
                printMessage(target, ReaxMessage.TELEPORTATION_POSITION_YOU, stringPosition);
                playSound(target, ReaxSound.TELEPORTATION_TELEPORTED);
            } catch (NumberFormatException e) {
                printMessage(commandSender, ReaxMessage.INVALID_FORMAT, "x, y, z", getMessageFromConfig(ReaxMessage.POSITION_FORMAT_EXAMPLE));
                return false;
            }
            return true;
        }

        String stringTarget = args.length == 2 ? args[1] : args[0];
        String stringPlayer = args.length == 2 ? args[0] : commandSender.getName();
        Player target = Utils.getOnlinePlayer(stringTarget);
        Player player = Utils.getOnlinePlayer(stringPlayer);
        if (target == null) {
            printMessage(commandSender, ReaxMessage.USER_NOTFOUND, stringTarget);
            return false;
        }
        if (player == null) {
            printMessage(commandSender, ReaxMessage.USER_NOTFOUND, stringTarget);
            return false;
        }

        player.teleport(target);

        if (!stringPlayer.equals(commandSender.getName())) printMessage(commandSender, ReaxMessage.TELEPORTATION_PLAYER_OTHER, stringPlayer, stringTarget);
        else if (!hasPermission( commandSender, "teleport.others")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }

        printMessage(player, ReaxMessage.TELEPORTATION_PLAYER_YOU, stringTarget);
        playSound(player, ReaxSound.TELEPORTATION_TELEPORTED);
        return true;
    }




}
