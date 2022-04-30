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

public class FlyCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public FlyCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "fly");
        super.setAliases(Collections.singletonList("flight"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "fly")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();
        String stringTarget = args.length >= 1 ? args[0] : player.getName();

        if (!player.getName().equalsIgnoreCase(stringTarget) && !hasPermission(player, "fly.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        dataBase.getUserEntity(stringTarget).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(player, ReaxMessage.USER_NOTFOUND, stringTarget);
                return;
            }

            userEntity.setIsFlight(!userEntity.getIsFlight());
            dataBase.saveUserEntity(userEntity).thenAccept(flag -> {
                if (args.length >= 1 && !player.getName().equalsIgnoreCase(stringTarget)) {
                    printMessage(player, userEntity.getIsFlight() ? ReaxMessage.FLY_ON : ReaxMessage.FLY_OFF, stringTarget);
                }

                Player target = Utils.getOnlinePlayer(stringTarget);
                if (target == null) return;

                Bukkit.getScheduler().runTask(reaxEssentials, () -> {
                    if (userEntity.getIsFlight()) playSound(target, ReaxSound.FLY_ON_YOU);
                    printMessage(target, userEntity.getIsFlight() ? ReaxMessage.FLY_ON_YOU : ReaxMessage.FLY_OFF_YOU, stringTarget);
                    target.setAllowFlight(userEntity.getIsFlight());
                    target.setFlying(userEntity.getIsFlight());
                });

            });
        });
        return true;
    }




}
