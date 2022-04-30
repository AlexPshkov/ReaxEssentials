package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class GodCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public GodCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "god");
        super.setAliases(Arrays.asList("godmode", "godmod"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "god")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();
        String stringTarget = args.length >= 1 ? args[0] : player.getName();

        if (!player.getName().equalsIgnoreCase(stringTarget) && !hasPermission(player, "god.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        dataBase.getUserEntity(stringTarget).thenAccept(userEntity -> {
            if (userEntity == null) {
                printMessage(player, ReaxMessage.USER_NOTFOUND, stringTarget);
                return;
            }

            userEntity.setIsGod(!userEntity.getIsGod());
            dataBase.saveUserEntity(userEntity).thenAccept(flag -> {
                if (args.length >= 1 && !player.getName().equalsIgnoreCase(stringTarget)) {
                    printMessage(player, userEntity.getIsGod() ? ReaxMessage.GOD_ON : ReaxMessage.GOD_OFF, stringTarget);
                }

                Player target = Utils.getOnlinePlayer(stringTarget);
                if (target == null) return;


                Bukkit.getScheduler().runTask(reaxEssentials, () -> {
                    printMessage(target, userEntity.getIsGod() ? ReaxMessage.GOD_ON_YOU : ReaxMessage.GOD_OFF_YOU, stringTarget);
                    if (userEntity.getIsGod()) playSound(target, ReaxSound.GOD_ON_YOU);
                    target.setInvulnerable(userEntity.getIsGod());
                });

            });
        });
        return true;
    }


}
