package ru.alexpshkov.reaxessentials.commands.implementation;

import org.bukkit.command.CommandSender;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Collections;


public class ReaxEssentialsCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public ReaxEssentialsCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "reaxessentials");
        super.setAliases(Collections.singletonList("essentials"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender sender, String alias, String[] args) {
        if (!hasPermission(sender, "*")) {
            printMessage(sender, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length < 1) {
            printMessage(sender, ReaxMessage.INVALID_SYNTAX, alias + " <reload>");
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            try {
                printMessage(sender, ReaxMessage.RELOAD_START);
                reaxEssentials.registerAll();
                printMessage(sender, ReaxMessage.RELOAD_COMPLETE);
            } catch (Exception e) {
                printMessage(sender, ReaxMessage.RELOAD_FAILED);
                e.printStackTrace();
            }
            return true;
        }
        printMessage(sender, ReaxMessage.INVALID_SYNTAX, alias + " <reload>");
        return true;
    }


}
