package ru.alexpshkov.reaxessentials.listeners;

import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IReaxListener;

import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
public class ChatListener implements IReaxListener {
    private final ReaxEssentials reaxEssentials;


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        Set<Player> recipients = event.getRecipients();
        event.setCancelled(true);

        reaxEssentials.getDataBase().getUserEntity(player.getName()).thenAcceptAsync(userEntity -> {
            if (userEntity.getMuteExpired() > new Date().getTime()) {
                long timeLeft = userEntity.getMuteExpired() - new Date().getTime();
                String muteMessage = reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.MUTE_MESSAGE, Utils.convertSecondsToDate(timeLeft / 1000));
                player.sendMessage(muteMessage);
                return;
            }
            boolean isGlobal = message.startsWith("!");
            Chat chatService = reaxEssentials.getServiceManager().getChatService();
            String playerDisplayName = chatService.getPlayerPrefix(player) + " " + player.getName() + " " + chatService.getPlayerSuffix(player);
            String formattedMessage = reaxEssentials.getMessagesConfig().getMessage(isGlobal ? ReaxMessage.CHAT_GLOBAL : ReaxMessage.CHAT_LOCAL, playerDisplayName, message);
            if (isGlobal) formattedMessage = formattedMessage.replaceFirst("!", "");

            TextComponent textComponent = new TextComponent(formattedMessage);
            if (isGlobal) sendGlobalMessage(textComponent, recipients);
            else sendLocalMessage(textComponent, player.getLocation(), recipients);
        }, reaxEssentials.getBukkitAsyncExecutor());

    }

    public void sendGlobalMessage(TextComponent message, Set<Player> recipients) {
        for (Player recipient : recipients) {
            if (!recipient.isOnline()) continue;
            recipient.spigot().sendMessage(message);
        }
    }

    public void sendLocalMessage(TextComponent message, Location sendPoint, Set<Player> recipients) {
        int maxDistance = reaxEssentials.getMainConfig().getLocalChatRadius();

        for (Player recipient : recipients) {
            if (hasPermission(recipient, "chat.long")) {
                recipient.spigot().sendMessage(message);
                continue;
            }
            if (!recipient.getWorld().getName().equalsIgnoreCase(sendPoint.getWorld().getName())) continue;
            if (!recipient.isOnline()) continue;
            if (recipient.getLocation().distance(sendPoint) > maxDistance) continue;
            recipient.spigot().sendMessage(message);
        }
    }

    /**
     * Check if user have some permission
     *
     * @param sender     Sender
     * @param permission Relative permission
     */
    public boolean hasPermission(@NotNull CommandSender sender, String permission) {
        if (sender.isOp()) return true;
        if (sender.hasPermission("*")) return true;
        if (sender.hasPermission("reaxessentials.*")) return true;
        if (sender.hasPermission("reaxessentials.admin")) return true;
        return sender.hasPermission("reaxessentials." + permission) || sender.hasPermission("essentials." + permission);
    }

}
