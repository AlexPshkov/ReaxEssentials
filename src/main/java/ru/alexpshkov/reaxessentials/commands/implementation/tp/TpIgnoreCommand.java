package ru.alexpshkov.reaxessentials.commands.implementation.tp;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class TpIgnoreCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public TpIgnoreCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "tpignore");
        super.setAliases(Arrays.asList("ignoretp", "tptoggle"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : player.getName());
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "tpignore.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }


        dataBase.getUserEntity(target.getName()).thenAccept(userEntity -> {
            userEntity.setIsTpIgnore(!userEntity.getIsTpIgnore());

            dataBase.saveUserEntity(userEntity).thenAccept(flag -> {
                if (args.length >= 1 && !target.getName().equals(player.getName())) {
                    printMessage(player, userEntity.getIsTpIgnore() ? ReaxMessage.TELEPORTATION_IGNORE_ON : ReaxMessage.TELEPORTATION_IGNORE_OFF, target.getName());
                    printMessage(target, userEntity.getIsTpIgnore() ? ReaxMessage.TELEPORTATION_IGNORE_ON_OWN : ReaxMessage.TELEPORTATION_IGNORE_OFF_OWN);
                } else printMessage(player, userEntity.getIsTpIgnore() ? ReaxMessage.TELEPORTATION_IGNORE_ON_OWN : ReaxMessage.TELEPORTATION_IGNORE_OFF_OWN);


                reaxEssentials.getSoundsConfig().playSound(target, userEntity.getIsTpIgnore() ? ReaxSound.TELEPORTATION_IGNORE_ON : ReaxSound.TELEPORTATION_IGNORE_OFF);

            });

        });

        return true;
    }




}
