package ru.alexpshkov.reaxessentials.commands.implementation.kit;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Arrays;

public class KitDelCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public KitDelCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "kitdel");
        super.setAliases(Arrays.asList("kitremove", "removekit", "delkit"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "kit.change")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 1) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <kitName>");
            return false;
        }

        reaxEssentials.getDataBase().removeKit(args[0]).thenAccept(flag ->
                printMessage(player, flag ? ReaxMessage.KIT_REMOVED : ReaxMessage.KIT_NOT_EXISTS, args[0]));

        return true;
    }
}
