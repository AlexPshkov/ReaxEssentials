package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.DeathPositionEntity;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;


public class BackCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public BackCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "back");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "back")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : player.getName());

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "back.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        reaxEssentials.getDataBase().getUserEntity(target.getName()).thenAccept(userEntity -> {
            DeathPositionEntity deathPositionEntity = userEntity.getLastDeathPosition();
            if (deathPositionEntity == null || deathPositionEntity.getLocation() == null) {
                printMessage(player, ReaxMessage.BACK_NOT_EXISTS);
                return;
            }
            if (args.length >= 1 && !target.getName().equals(player.getName())) {
                printMessage(player, ReaxMessage.BACK_TELEPORTED, target.getName());
                printMessage(target, ReaxMessage.BACK_TELEPORTED_YOU, target.getName());
            } else printMessage(player, ReaxMessage.BACK_TELEPORTED_YOU, target.getName());

            target.teleport(deathPositionEntity.getLocation());
            playSound(target, ReaxSound.BACK_TELEPORTED_YOU);
        });

        return true;
    }




}
