package ru.alexpshkov.reaxessentials.players;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;
import ru.alexpshkov.reaxessentials.service.interfaces.IReaxUpdatable;

@RequiredArgsConstructor
public class PlayersManager implements IInitRequired, IReaxUpdatable {
    private final ReaxEssentials reaxEssentials;

    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(reaxEssentials, this::update, 20L, 20L);
    }

    @Override
    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            reaxEssentials.getDataBase().getUserEntity(player.getName()).thenAccept(userEntity -> {
                userEntity.setSecondsPlayed(userEntity.getSecondsPlayed() + 1);
                reaxEssentials.getDataBase().saveUserEntity(userEntity);
            });
        });
    }
}
