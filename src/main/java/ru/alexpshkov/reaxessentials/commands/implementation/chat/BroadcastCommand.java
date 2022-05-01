package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;


public class BroadcastCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public BroadcastCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "broadcast");
        super.setAliases(Arrays.asList("bc", "announce"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "broadcast")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length == 0) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <message>");
            return false;
        }


        String message = combineArgsIntoString(0, args);
        TextComponent formattedMessage = getTextComponentFromConfig(ReaxMessage.BROADCAST_FORMAT, message);

        Bukkit.getOnlinePlayers().forEach(player -> {
            printTextComponent(player, formattedMessage);
            playSound(player, ReaxSound.BROADCAST_RECEIVED);
        });
        return true;
    }


}
