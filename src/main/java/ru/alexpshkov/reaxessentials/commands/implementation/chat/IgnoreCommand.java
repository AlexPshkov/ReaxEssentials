package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.IgnoredUser;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class IgnoreCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public IgnoreCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "ignore");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        Player target = Utils.getOnlinePlayer(args[0]);
        IDataBase dataBase = reaxEssentials.getDataBase();

        //Check if username is invalid
        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        //Check if player sends message to him
        if (player.getName().equalsIgnoreCase(target.getName())) {
            printMessage(player, ReaxMessage.PERSONALMESSAGE_IGNORE_ONLY_OTHERS);
            return false;
        }

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            IgnoredUser ignoredUser = new IgnoredUser();
            ignoredUser.setIgnoredUserName(target.getName());
            ignoredUser.setUserEntity(userEntity);

            reaxEssentials.getDataBase().getIgnoredPlayer(ignoredUser).thenAccept(realIgnoredUser -> {
                if (realIgnoredUser != null) {
                    printMessage(player, ReaxMessage.PERSONALMESSAGE_IGNORE_ADD_ALREADY);
                    return;
                }
                dataBase.addIgnoredPlayer(ignoredUser).thenAccept(flag -> {
                    printMessage(player, ReaxMessage.PERSONALMESSAGE_IGNORE_ADD, target.getName());
                });
            });


        });

        return true;
    }


}
