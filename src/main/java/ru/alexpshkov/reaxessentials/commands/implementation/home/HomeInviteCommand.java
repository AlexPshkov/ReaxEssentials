package ru.alexpshkov.reaxessentials.commands.implementation.home;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.TrustedHome;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;

public class HomeInviteCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public HomeInviteCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "homeinvite");
        super.setAliases(Arrays.asList("invitehome", "sharehome", "homeshare", "homeinv", "invhome"));
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
                    printMessage(player, ReaxMessage.HOME_SHARED_ALREADY, homeEntity.getHomeName());
                    return;
                }

                TrustedHome trustedHome = new TrustedHome();
                trustedHome.setTrustedUser(targetEntity);
                trustedHome.setHomeEntity(homeEntity);


                dataBase.isSuchTrustedHome(trustedHome).thenAccept(isSuch -> {
                    if (isSuch) {
                        printMessage(player, ReaxMessage.HOME_SHARED_ALREADY, homeEntity.getHomeName());
                        return;
                    }
                    dataBase.addTrustedHome(trustedHome).thenAccept(flag -> {
                        printMessage(player, ReaxMessage.HOME_SHARED, args[0]);
                        playSound(player, ReaxSound.HOME_SHARED);

                        Player target = Utils.getOnlinePlayer(args[0]);
                        if (target != null) {
                            TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HOME_SHARED_WITH_YOU, player.getPlayerListName());

                            TextComponent homeCmd = new TextComponent("§c/home " + player.getName() + " " + homeEntity.getHomeName());
                            homeCmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getMessageFromConfig(ReaxMessage.TELEPORTATION_BUTTON_HOVER)).create()));
                            homeCmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + " " + player.getName() + " " + homeEntity.getHomeName()));
                            textComponent.addExtra(homeCmd);

                            printTextComponent(target, textComponent);
                            playSound(target, ReaxSound.HOME_SHARED_WITH_YOU);
                        }
                    });
                });
            });
        });
        return true;
    }
}


