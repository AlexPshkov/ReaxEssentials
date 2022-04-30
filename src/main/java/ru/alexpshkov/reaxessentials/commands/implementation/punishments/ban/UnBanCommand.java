package ru.alexpshkov.reaxessentials.commands.implementation.punishments.ban;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Arrays;

public class UnBanCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public UnBanCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "unban");
        super.setAliases(Arrays.asList("pardon"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender commandSender, String alias, String[] args) {
        if (!hasPermission(commandSender, "unban")) {
            printMessage(commandSender, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length == 0) {
            printMessage(commandSender, ReaxMessage.INVALID_SYNTAX, alias + " <playerName>");
            return false;
        }

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        if (!banList.isBanned(args[0])) {
            printMessage(commandSender, ReaxMessage.BANUSER_UNBAN_ALREADY);
            return false;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reaxEssentials, () -> {
            banList.pardon(args[0]);
            String message = reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.BANUSER_UNBAN, commandSender.getName(), args[0]);
            Bukkit.getOnlinePlayers().forEach(player -> printMessage(player, message));
        });

        return true;
    }


}
