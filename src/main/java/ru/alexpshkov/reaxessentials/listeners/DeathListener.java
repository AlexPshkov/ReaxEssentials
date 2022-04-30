package ru.alexpshkov.reaxessentials.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.database.entities.DeathPositionEntity;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IReaxListener;

import java.util.Date;

@RequiredArgsConstructor
public class DeathListener implements IReaxListener {
    private final ReaxEssentials reaxEssentials;


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        reaxEssentials.getDataBase().getUserEntity(player.getName()).thenAccept(userEntity -> {
            DeathPositionEntity deathPositionEntity = userEntity.getLastDeathPosition();

            deathPositionEntity.setDeathTime(new Date().getTime());
            deathPositionEntity.setLocation(player.getLocation());

            reaxEssentials.getDataBase().saveDeathPositionEntity(deathPositionEntity);
        });

    }
}
