package ru.alexpshkov.reaxessentials.commands;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.configs.implementation.MessagesConfig;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@IndexSubclasses
public abstract class AbstractCommand extends BukkitCommand {
    private final ReaxEssentials reaxEssentials;
    private final String commandName;
    private Boolean onlyForPlayers = false;

    protected AbstractCommand(ReaxEssentials reaxEssentials, String commandName) {
        super(commandName);
        this.reaxEssentials = reaxEssentials;
        this.commandName = commandName;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        String slash = sender instanceof Player ? "/" : "";
        if (onlyForPlayers) {
            if (!(sender instanceof Player)) {
                printMessage(sender, ReaxMessage.ONLY_PLAYERS);
                return false;
            }
            return handleCommand((Player) sender,  slash + alias, args);
        } else return handleCommand(sender, slash + alias, args);
    }

    /**
     * Execute command for players
     * @param sender Sender
     * @param alias Command tag
     * @param args Command args
     * @return flag
     */
    public boolean handleCommand(Player sender, String alias, String[] args) {
        return false;
    }

    /**
     * Execute command for any sender
     * @param sender Sender
     * @param alias Command tag
     * @param args Command args
     * @return flag
     */
    public boolean handleCommand(CommandSender sender, String alias, String[] args) {
        return false;
    }


    /**
     * Check if user have some permission
     * @param sender Sender
     * @param permission Relative permission
     */
    public boolean hasPermission(@NotNull CommandSender sender, String permission) {
        if (sender.isOp()) return true;
        if (sender.hasPermission("*")) return true;
        if (sender.hasPermission("reaxessentials.*")) return true;
        if (sender.hasPermission("reaxessentials.admin")) return true;
        return sender.hasPermission("reaxessentials." + permission) || sender.hasPermission("essentials." + permission);
    }

    /**
     * Try to get amount of smth from permission
     * @param sender Sender
     * @param permission Relative permission
     */
    public CompletableFuture<Integer> getAmountFromPermission(@NotNull CommandSender sender, String permission) {
        return CompletableFuture.supplyAsync(() -> {
            if (sender.isOp()) return 1000;
            if (sender.hasPermission("*")) return 1000;
            if (sender.hasPermission("reaxessentials.*")) return 1000;

            int resultValue = -1;
            for (PermissionAttachmentInfo effectivePermission : sender.getEffectivePermissions()) {
                if (!effectivePermission.getPermission().startsWith("reaxessentials." + permission)) continue;
                String value = effectivePermission.getPermission().replace("reaxessentials." + permission, "");
                try {
                    resultValue = Integer.parseInt(value);
                } catch (NumberFormatException exception) {
                    reaxEssentials.getLogger().info("Error permission " + effectivePermission.getPermission());
                }
                return resultValue;
            }
            return resultValue;
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    /**
     * Print message from config
     * @param sender Sender
     * @param reaxMessage ReaxMessage
     */
    public void printMessage(@NotNull CommandSender sender, @NotNull ReaxMessage reaxMessage, String ... args) {
        printMessage(sender, reaxEssentials.getMessagesConfig().getMessage(reaxMessage, args));
    }

    /**
     * Print message
     * @param sender Sender
     * @param message Text message
     */
    public void printMessage(@NotNull CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    /**
     * Combine args into one string
     */
    public String combineArgsIntoString(int argIndexStart, int argIndexEnd, String ... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = argIndexStart; i < argIndexEnd; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * Combine args into one string
     */
    public String combineArgsIntoString(int argIndexStart, String ... args) {
        return combineArgsIntoString(argIndexStart, args.length, args);
    }

    /**
     * Convert to integer from string
     */
    public int convertStringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Convert to double from string
     */
    public double convertStringToDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Get message as TextComponent
     */
    public TextComponent getTextComponentFromConfig(ReaxMessage reaxMessage, String ... args) {
        return new TextComponent(reaxEssentials.getMessagesConfig().getMessage(reaxMessage, args));
    }

    /**
     * Get message
     */
    public String getMessageFromConfig(ReaxMessage reaxMessage, String ... args) {
        return reaxEssentials.getMessagesConfig().getMessage(reaxMessage, args);
    }

    /**
     * Create button with default action as RUN_COMMAND
     */
    public TextComponent createButton(ReaxMessage buttonText, ReaxMessage buttonHoverText, String command) {
        return createButton(buttonText, buttonHoverText, command, ClickEvent.Action.RUN_COMMAND);
    }

    /**
     * Create button
     */
    public TextComponent createButton(ReaxMessage buttonText, ReaxMessage buttonHoverText, String command, ClickEvent.Action action) {
        MessagesConfig messagesConfig = reaxEssentials.getMessagesConfig();

        String stringButtonText = messagesConfig.getMessage(buttonText);
        String stringButtonHoverText = messagesConfig.getMessage(buttonHoverText);
        TextComponent button = new TextComponent(stringButtonText);
        button.setClickEvent(new ClickEvent(action, command));
        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(stringButtonHoverText).create()));
        return button;
    }

    /**
     * Sends textComponent to the player
     */
    public void printTextComponent(Player player, TextComponent textComponent) {
        player.spigot().sendMessage(textComponent);
    }

    /**
     * Plays sound to the player as MASTER
     */
    public void playSound(Player player, ReaxSound reaxSound) {
        reaxEssentials.getSoundsConfig().playSound(player, reaxSound);
    }
}
