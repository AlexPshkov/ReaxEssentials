package ru.alexpshkov.reaxessentials.commands.implementation.home;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.HomeEntity;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.regex.Pattern;

public class SetHomeCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public SetHomeCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "sethome");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();
        String homeName = args.length > 0 ? args[0] : player.getName();
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(homeName).find()) {
            printMessage(player, ReaxMessage.HOME_INVALID_CHARS);
            return false;
        }
        int homeNameMaxLength = reaxEssentials.getMainConfig().getHomeNameMaxLength();
        if (homeName.length() >= homeNameMaxLength) {
            printMessage(player, ReaxMessage.HOME_INVALID_AMOUNT, homeNameMaxLength + "");
            return false;
        }
        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            dataBase.getHomeEntitiesOfOwner(player.getName()).thenAccept(userHomesList -> {
                getAmountFromPermission(player, "home.amount.").thenAccept(maxAmount -> {
                    if (userHomesList.size() >= maxAmount) {
                        printMessage(player, ReaxMessage.HOME_MAXAMOUNT_CREATE, maxAmount + "");
                        return;
                    }

                    HomeEntity newHomeEntity = new HomeEntity();
                    newHomeEntity.setLocation(player.getLocation());
                    newHomeEntity.setHomeName(homeName);
                    newHomeEntity.setWhoOwned(userEntity);
                    dataBase.saveHomeEntity(newHomeEntity).thenAccept(flag -> {
                        printMessage(player, ReaxMessage.HOME_NEW, homeName);
                        playSound(player, ReaxSound.HOME_NEW);
                    });
                });

            });

        });
        return true;
    }


}
