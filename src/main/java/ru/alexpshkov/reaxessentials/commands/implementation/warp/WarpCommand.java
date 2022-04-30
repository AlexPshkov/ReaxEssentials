package ru.alexpshkov.reaxessentials.commands.implementation.warp;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class WarpCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public WarpCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "warp");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <warpName>");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args[0].equalsIgnoreCase("list")) {
            dataBase.getAllWarpEntities().thenAccept(warpEntities -> {
                if (warpEntities.isEmpty()) {
                    printMessage(player, ReaxMessage.WARP_LIST_EMPTY);
                    return;
                }
                TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.WARP_LIST);

                warpEntities.forEach(warpEntity -> {
                    TextComponent warpName = getTextComponentFromConfig(ReaxMessage.WARP_LIST_FIELD, warpEntity.getWarpName());
                    warpName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getTextComponentFromConfig(ReaxMessage.TELEPORTATION_BUTTON_HOVER)).create()));
                    warpName.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, alias + " " + warpEntity.getWarpName()));
                    textComponent.addExtra(" ");
                    textComponent.addExtra(warpName);
                });

                printTextComponent(player, textComponent);
            });
            return true;
        }

        dataBase.getWarpEntity(args[0]).thenAccept(warpEntity -> {
            if (warpEntity == null) {
                printMessage(player, ReaxMessage.WARP_NOT_EXISTS, args[0]);
                return;
            }
            player.teleport(warpEntity.getLocation());
            printMessage(player, ReaxMessage.WARP_TELEPORTED, warpEntity.getWarpName());
            reaxEssentials.getSoundsConfig().playSound(player, ReaxSound.WARP_TELEPORTED);
        });
        return true;
    }
}
