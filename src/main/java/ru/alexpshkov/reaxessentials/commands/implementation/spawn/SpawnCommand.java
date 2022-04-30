package ru.alexpshkov.reaxessentials.commands.implementation.spawn;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class SpawnCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public SpawnCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "spawn");
        super.setAliases(Arrays.asList("spawnpoint", "tospawn"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();
        if (args.length == 0 && !(commandSender instanceof Player)) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : commandSender.getName());

        if (target == null) {
            printMessage(commandSender, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!commandSender.getName().equalsIgnoreCase(target.getName()) && !hasPermission(commandSender, "spawn.others")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }

        dataBase.getWarpEntity("spawn").thenAccept(warpEntity -> {
            if (warpEntity == null) {
                printMessage(commandSender, ReaxMessage.SPAWN_NOT_EXISTS);
                return;
            }
            if (args.length >= 1 && !target.getName().equals(commandSender.getName())) {
                printMessage(commandSender, ReaxMessage.SPAWN_TELEPORT_OTHER, target.getName());
                printMessage(target, ReaxMessage.SPAWN_TELEPORT_YOU);
            } else printMessage(commandSender, ReaxMessage.SPAWN_TELEPORT_YOU);

            Bukkit.getScheduler().runTask(reaxEssentials, () -> {
                target.teleport(warpEntity.getLocation());
                reaxEssentials.getSoundsConfig().playSound(target, ReaxSound.SPAWN_TELEPORT);
            });
        });
        return true;
    }




}
