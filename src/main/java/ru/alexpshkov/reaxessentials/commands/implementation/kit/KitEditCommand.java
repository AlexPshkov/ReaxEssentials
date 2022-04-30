package ru.alexpshkov.reaxessentials.commands.implementation.kit;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.kits.KitsManager;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Arrays;
import java.util.Locale;

public class KitEditCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public KitEditCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "kitedit");
        super.setAliases(Arrays.asList("editkit", "changekit", "kitchange"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "kit.change")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }
        if (args.length < 2) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <kitName> <content|delay|name> [newValue]");
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();
        KitsManager kitsManager = reaxEssentials.getKitsManager();

        dataBase.getKitEntity(args[0]).thenAccept(kitEntity -> {
            if (kitEntity == null) {
                printMessage(player, ReaxMessage.KIT_NOT_EXISTS);
                return;
            }
            switch (args[1].toLowerCase(Locale.ROOT)) {
                case "name": {
                    if (args.length < 3) {
                        printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + args[0] + args[1] + " <name>");
                        return;
                    }
                    dataBase.removeKit(kitEntity.getKitName()).thenAccept(flag -> {
                        kitEntity.setKitName(args[2]);
                        kitsManager.saveKit(kitEntity);
                        printMessage(player, ReaxMessage.KIT_PARAMETER_CHANGED, args[1], args[2]);
                    });
                    break;
                }
                case "content": {
                    kitsManager.addKitToProcessing(player.getName(), kitEntity);
                    kitsManager.openKitEditGUI(player, kitEntity);
                    break;
                }
                case "delay": {
                    if (args.length < 3) {
                        printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + args[0] + args[1] + " <time>");
                        return;
                    }
                    long time = -1;
                    try {
                        time = Utils.convertToSeconds(args[2]);
                    } catch (NumberFormatException exception) {
                        printMessage(player, ReaxMessage.INVALID_FORMAT, args[1], getMessageFromConfig(ReaxMessage.TIME_FORMAT_EXAMPLE));
                        return;
                    }
                    kitEntity.setDelayTime(time);
                    kitsManager.saveKit(kitEntity);
                    printMessage(player, ReaxMessage.KIT_PARAMETER_CHANGED, args[1], args[2]);
                    break;
                }
                default: {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <kitName> <content|delay|name> [newValue]");
                }
            }
        });

        return true;
    }
}
