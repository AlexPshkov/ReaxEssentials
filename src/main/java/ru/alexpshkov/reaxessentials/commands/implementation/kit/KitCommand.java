package ru.alexpshkov.reaxessentials.commands.implementation.kit;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Date;

public class KitCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public KitCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "kit");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length < 1 || args[0].equalsIgnoreCase("list")) {
            dataBase.getAllKitEntities().thenAccept(kitEntities -> {
                StringBuilder stringBuilder = new StringBuilder();
                kitEntities.forEach(kitEntity -> {
                    if (!hasPermission(player, "kit.receive." + kitEntity.getKitName())) return;
                    stringBuilder.append(kitEntity.getKitName()).append(" ");
                });
                String kitsList = stringBuilder.toString();
                if (kitsList.isEmpty()) {
                    printMessage(player, ReaxMessage.KIT_LIST_EMPTY);
                    return;
                }
                printMessage(player, ReaxMessage.KIT_LIST, kitsList);
            });
            return true;
        }

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> dataBase.getKitEntity(args[0]).thenAccept(kitEntity -> {
            if (kitEntity == null) {
                printMessage(player, ReaxMessage.KIT_NOT_EXISTS);
                return;
            }
            dataBase.findReceivedKit(userEntity, args[0]).thenAccept(receivedKitEntity -> {
                if(receivedKitEntity != null) {
                    long minTimeForKitGet = receivedKitEntity.getReceivedTime() + kitEntity.getDelayTime();
                    if (new Date().getTime() < minTimeForKitGet) {
                        printMessage(player, ReaxMessage.KIT_DELAY, Utils.convertMillisToDate(minTimeForKitGet));
                        return;
                    }
                }
                if (!hasPermission(player, "kit.receive." + kitEntity.getKitName())) {
                    printMessage(player, ReaxMessage.KIT_NOT_PERMIT);
                    return;
                }
                reaxEssentials.getKitsManager().addKitToPlayer(player, kitEntity, userEntity);
                printMessage(player, ReaxMessage.KIT_RECEIVED, args[0]);
                playSound(player, ReaxSound.KIT_RECEIVED);
            });
        }));
        return true;
    }
}
