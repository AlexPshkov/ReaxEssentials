package ru.alexpshkov.reaxessentials.teleportation;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.configs.implementation.MainConfig;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;
import ru.alexpshkov.reaxessentials.service.interfaces.IReaxUpdatable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TeleportationManager implements IInitRequired, IReaxUpdatable {
    private final ReaxEssentials reaxEssentials;
    private final HashMap<Player, TeleportationRequest> teleportationRequests = new HashMap<>();
    private final HashMap<String, Long> lastRandomTeleports = new HashMap<>();

    @Override
    public void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(reaxEssentials, this::update, 60L, 60L);
    }


    /**
     * Gets time from last random teleport
     * @param playerName Player name
     */
    public int getTimeForNextRandomTeleport(String playerName) {
        Long lastTime = lastRandomTeleports.getOrDefault(playerName, null);
        if (lastTime == null) return 0;

        int timeDelay = reaxEssentials.getMainConfig().getTprDelay();
        long timeLeft = new Date().getTime() - lastTime;
        return (int) (timeDelay - timeLeft) / 1000;
    }

    /**
     * Sets last random teleport time
     * @param playerName Player name
     * @param time Teleport time
     */
    public void setLastRandomTeleport(String playerName, long time) {
        lastRandomTeleports.put(playerName, time);
    }

    /**
     * Find random location on some world
     * @param world Some world
     * @return CompletableFuture of Location
     */
    public CompletableFuture<Location> findRandomLocation(World world) {
        return CompletableFuture.supplyAsync(() -> {
            Random random = new Random();
            MainConfig mainConfig = reaxEssentials.getMainConfig();

            int diffX = mainConfig.getTprMaxX() - mainConfig.getTprMinX();
            int diffZ = mainConfig.getTprMaxZ() - mainConfig.getTprMinZ();

            int randomX = random.nextInt(diffX + 1);
            int randomZ = random.nextInt(diffZ + 1);
            Location randomLocation = new Location(world, randomX, 70, randomZ);


            randomLocation.setY(world.getHighestBlockYAt(randomLocation) + 1);
            return randomLocation;
        }, reaxEssentials.getBukkitSyncExecutor());
    }

    /**
     * Create teleportation request
     */
    public void createTeleportationRequest(Player player, Player target) {
        TeleportationRequest teleportationRequest = new TeleportationRequest(player, target, new Date().getTime());
        teleportationRequests.put(player, teleportationRequest);
    }

    /**
     * Gets teleportation request
     */
    public TeleportationRequest getTeleportationRequest(Player player) {
        return teleportationRequests.getOrDefault(player, null);
    }

    /**
     * Remove teleportation request
     */
    public void removeTeleportationRequest(Player player) {
        teleportationRequests.remove(player);
    }

    /**
     * Make update
     */
    @Override
    public void update() {
        List<Player> requestsForDelete = new ArrayList<>();
        int expireTime = reaxEssentials.getMainConfig().getTpaExpireTime();
        teleportationRequests.forEach((sender, tpRequest) -> {
            if (new Date().getTime() - tpRequest.getCreatedTime() >= expireTime)
                requestsForDelete.add(sender);
            if (!tpRequest.getPlayer().isOnline() || !tpRequest.getTarget().isOnline())
                requestsForDelete.add(sender);
        });
        requestsForDelete.forEach(sender -> {
            String message = reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.TELEPORTATION_REQUEST_DELETED);
            if (sender.isOnline()) {
                sender.sendMessage(message);
                reaxEssentials.getSoundsConfig().playSound(sender, ReaxSound.TELEPORTATION_DECLINE);
            }
            teleportationRequests.remove(sender);
        });

        for (Map.Entry<String, Long> stringLongEntry : lastRandomTeleports.entrySet()) {
            if (getTimeForNextRandomTeleport(stringLongEntry.getKey()) <= 0)
                lastRandomTeleports.remove(stringLongEntry.getKey());
        }
    }


}
