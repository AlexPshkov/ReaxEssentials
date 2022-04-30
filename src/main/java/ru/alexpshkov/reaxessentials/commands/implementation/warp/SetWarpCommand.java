package ru.alexpshkov.reaxessentials.commands.implementation.warp;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.WarpEntity;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.regex.Pattern;

public class SetWarpCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public SetWarpCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "setwarp");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "setwarp")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <warpName>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getWarpEntity(args[0]).thenAccept(warpEntity -> {
            if (warpEntity != null) {
                printMessage(player, ReaxMessage.WARP_ALREADY_EXISTS, args[0]);
                return;
            }
            if (Pattern.compile("[^a-zA-Z0-9]").matcher(args[0]).find()) {
                printMessage(player, ReaxMessage.WARP_INVALID_CHARS);
                return;
            }
            int warpNameMaxLength = reaxEssentials.getMainConfig().getWarpNameMaxLength();
            if (args[0].length() >= 100) {
                printMessage(player, ReaxMessage.WARP_INVALID_AMOUNT, warpNameMaxLength + "");
                return;
            }

            dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
                getAmountFromPermission(player, "warp.amount.").thenAccept(maxAmount -> {

                    dataBase.getWarpEntitiesOfOwner(userEntity).thenAccept(warpEntities -> {
                        if (warpEntities.size() >= maxAmount) {
                            printMessage(player, ReaxMessage.WARP_MAXAMOUNT_CREATE, maxAmount + "");
                            return;
                        }

                        WarpEntity newWarpEntity = new WarpEntity();
                        newWarpEntity.setWarpName(args[0]);
                        newWarpEntity.setWhoOwned(userEntity);
                        newWarpEntity.setLocation(player.getLocation());
                        dataBase.saveWarpEntity(newWarpEntity).thenAccept(flag -> {
                            printMessage(player, ReaxMessage.WARP_CREATED, newWarpEntity.getWarpName());
                            reaxEssentials.getSoundsConfig().playSound(player, ReaxSound.WARP_CREATED);
                        });
                    });

                });
            });


        });
        return true;
    }
}
