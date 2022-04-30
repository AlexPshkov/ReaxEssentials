package ru.alexpshkov.reaxessentials.commands.implementation.home;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.TrustedHome;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class HomeUnInviteCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public HomeUnInviteCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "homeuninvite");
        super.setAliases(Arrays.asList("uninvitehome", "unsharehome", "homeunshare", "homeuninv", "uninvhome"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length == 0) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <playerName> [homeName]");
            return false;
        }

        String homeName = args.length >= 2 ? args[1] : player.getName();

        dataBase.getHomeEntityWithOwner(homeName, player.getName()).thenAccept(homeEntity -> {
            if (homeEntity == null) {
                printMessage(player, ReaxMessage.HOME_NOT_EXISTS, homeName);
                return;
            }
            dataBase.getUserEntity(args[0]).thenAccept(targetEntity -> {
                if (targetEntity == null) {
                    printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
                    return;
                }

                if (targetEntity.getUserName().equalsIgnoreCase(player.getName())) {
                    printMessage(player, ReaxMessage.HOME_SHARED_REMOVE_ALREADY, args[0], homeEntity.getHomeName());
                    return;
                }

                TrustedHome trustedHome = new TrustedHome();
                trustedHome.setTrustedUser(targetEntity);
                trustedHome.setHomeEntity(homeEntity);

                dataBase.getTrustedHome(trustedHome).thenAccept(savedTrustedHome -> {
                    if (savedTrustedHome == null) {
                        printMessage(player, ReaxMessage.HOME_SHARED_REMOVE_ALREADY, args[0], homeEntity.getHomeName());
                        return;
                    }
                    dataBase.removeTrustedHome(savedTrustedHome).thenAccept(flag -> printMessage(player, ReaxMessage.HOME_SHARED_REMOVE, homeEntity.getHomeName(), args[0]));

                });
            });
        });
        return true;
    }
}


