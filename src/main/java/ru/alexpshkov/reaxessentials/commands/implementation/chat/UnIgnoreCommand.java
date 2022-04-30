package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.IgnoredUser;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class UnIgnoreCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public UnIgnoreCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "unignore");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();


        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            IgnoredUser ignoredUser = new IgnoredUser();
            ignoredUser.setIgnoredUserName(args[0]);
            ignoredUser.setUserEntity(userEntity);
            dataBase.getIgnoredPlayer(ignoredUser).thenAccept(realIgnoredPlayer -> {
                if (realIgnoredPlayer == null) {
                    printMessage(player, ReaxMessage.PERSONALMESSAGE_IGNORE_REMOVE_ALREADY);
                    return;
                }
                dataBase.removeIgnoredPlayer(realIgnoredPlayer);
                printMessage(player, ReaxMessage.PERSONALMESSAGE_IGNORE_REMOVE, args[0]);
            });

        });

        return true;
    }




}
