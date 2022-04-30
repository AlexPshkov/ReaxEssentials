package ru.alexpshkov.reaxessentials.commands.implementation.home;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.HomeEntity;
import ru.alexpshkov.reaxessentials.database.entities.TrustedHome;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public HomeCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "home");
        super.setAliases(Arrays.asList("tphome", "gohome"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            dataBase.getHomeEntitiesOfOwner(player.getName()).thenAccept(homeEntities -> dataBase.getTrustedHomesByUserName(player.getName()).thenAccept(trustedHomeEntities -> {
                List<String> homesList = homeEntities.stream().map(HomeEntity::getHomeName).collect(Collectors.toList());
                homesList.addAll(trustedHomeEntities.stream().map(home -> home.getHomeEntity().getWhoOwned().getUserName() + " " + home.getHomeEntity().getHomeName()).collect(Collectors.toList()));
                if (homesList.isEmpty()) {
                    printMessage(player, ReaxMessage.HOME_LIST_EMPTY);
                    return;
                }
                TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HOME_LIST);
                homesList.forEach(homeName -> {
                    textComponent.addExtra("\n");
                    TextComponent cmdName = getTextComponentFromConfig(ReaxMessage.HOME_LIST_FIELD, alias + " " + homeName);
                    cmdName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getTextComponentFromConfig(ReaxMessage.TELEPORTATION_BUTTON_HOVER)).create()));
                    cmdName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, alias + " " + homeName));
                    textComponent.addExtra(cmdName);
                });
                printTextComponent(player, textComponent);
            }));
            return true;
        }

        String homeName = args.length >= 2 ? args[1] : args.length == 1 ? args[0] : player.getName();
        String homeOwner = args.length >= 2 ? args[0] : player.getName();

        dataBase.getHomeEntityWithOwner(homeName, homeOwner).thenAccept(homeEntity -> {
            if (homeEntity == null) {
                printMessage(player, ReaxMessage.HOME_NOT_EXISTS, homeName);
                return;
            }
            dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
                TrustedHome trustedHome = new TrustedHome();
                trustedHome.setHomeEntity(homeEntity);
                trustedHome.setTrustedUser(userEntity);
                dataBase.isSuchTrustedHome(trustedHome).thenAccept(isSuchTrusted -> {
                    if (isSuchTrusted || homeEntity.getWhoOwned().getUserName().equals(player.getName())) {
                        player.teleport(homeEntity.getLocation());
                        printMessage(player, args.length == 2 ? ReaxMessage.HOME_TELEPORTATION_ANOTHER : ReaxMessage.HOME_TELEPORTATION_OWN, homeOwner);
                        playSound(player, ReaxSound.HOME_TELEPORTATION);
                    } else printMessage(player, ReaxMessage.HOME_TELEPORTATION_ANOTHER_DENY);
                });
            });
        });

        return true;
    }
}

