package ru.alexpshkov.reaxessentials.commands.implementation.tp;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.configs.implementation.MessagesConfig;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;

public class CallCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public CallCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "call");
        super.setAliases(Arrays.asList("tpa", "tpTo"));
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

        //Check if username is invalid
        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        //Check if already have any request
        if (reaxEssentials.getTeleportationManager().getTeleportationRequest(player) != null) {
            printMessage(player, ReaxMessage.TELEPORTATION_REQUEST_ALREADY, args[0]);
            return false;
        }

        //Check if player sends request to him
        if (target.getName().equals(player.getName())) {
            printMessage(player, ReaxMessage.TELEPORTATION_REQUEST_ONLY_OTHERS, args[0]);
            return false;
        }

        reaxEssentials.getDataBase().getUserEntity(target.getName()).thenAccept(userEntity -> {
            //Check if player toggled off requests
            if (userEntity.getIsTpIgnore()) {
                printMessage(player, ReaxMessage.TELEPORTATION_IGNORED);
                return;
            }

            reaxEssentials.getTeleportationManager().createTeleportationRequest(player, target);
            printMessage(player, ReaxMessage.TELEPORTATION_REQUEST_SENT, target.getPlayerListName());


            printMessage(target, ReaxMessage.TELEPORTATION_REQUEST_RECEIVED, player.getPlayerListName(), reaxEssentials.getMainConfig().getTpaExpireTime() / 1000 + "");
            sendButtons(player, target);
            reaxEssentials.getSoundsConfig().playSound(target, ReaxSound.TELEPORTATION_SENT);
        });

        return true;
    }

    /**
     * Send buttons for confirm and decline
     */
    private void sendButtons(Player player, Player target) {
        MessagesConfig messagesConfig = reaxEssentials.getMessagesConfig();
        TextComponent message = new TextComponent();

        String acceptButtonText = messagesConfig.getMessage(ReaxMessage.ACCEPT_BUTTON);
        String acceptButtonHoverText = messagesConfig.getMessage(ReaxMessage.ACCEPT_BUTTON_HOVER);
        TextComponent acceptButton = new TextComponent(acceptButtonText);
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + player.getName()));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(acceptButtonHoverText).create()));

        String declineButtonText = messagesConfig.getMessage(ReaxMessage.DECLINE_BUTTON);
        String declineButtonHoverText = messagesConfig.getMessage(ReaxMessage.DECLINE_BUTTON_HOVER);
        TextComponent declineButton = new TextComponent(declineButtonText);
        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + player.getName()));
        declineButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(declineButtonHoverText).create()));

        message.addExtra("           ");
        message.addExtra(acceptButton);
        acceptButton.addExtra("           ");
        message.addExtra(declineButton);

        target.spigot().sendMessage(message);
    }


}
