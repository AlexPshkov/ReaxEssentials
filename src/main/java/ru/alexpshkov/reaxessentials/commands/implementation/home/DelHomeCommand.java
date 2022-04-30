package ru.alexpshkov.reaxessentials.commands.implementation.home;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class DelHomeCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public DelHomeCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "delhome");
        super.setAliases(Arrays.asList("homedel", "homeremove", "removehome"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();
        String homeName = args.length > 0 ? args[0] : player.getName();

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            dataBase.getHomeEntityWithOwner(homeName, player.getName()).thenAccept(homeEntity -> {
                if (homeEntity == null) {
                    printMessage(player, ReaxMessage.HOME_NOT_EXISTS);
                    return;
                }

                dataBase.removeHomeEntity(homeEntity).thenAccept(flag -> {
                    printMessage(player, ReaxMessage.HOME_REMOVE, homeEntity.getHomeName());
                    playSound(player, ReaxSound.HOME_TELEPORTATION);
                });

            });
        });
        return true;

    }


}
