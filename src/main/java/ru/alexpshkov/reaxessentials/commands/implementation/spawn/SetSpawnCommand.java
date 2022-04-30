package ru.alexpshkov.reaxessentials.commands.implementation.spawn;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.WarpEntity;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

public class SetSpawnCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public SetSpawnCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "setspawn");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "setspawn")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            WarpEntity newWarpEntity = new WarpEntity();
            newWarpEntity.setWarpName("spawn");
            newWarpEntity.setWhoOwned(userEntity);
            newWarpEntity.setLocation(player.getLocation());
            dataBase.saveWarpEntity(newWarpEntity).thenAccept(flag -> {
                printMessage(player, ReaxMessage.SPAWN_CREATE);
                player.getWorld().setSpawnLocation(player.getLocation());
                reaxEssentials.getSoundsConfig().playSound(player, ReaxSound.SPAWN_CREATE);
            });
        });


        return true;
    }
}
