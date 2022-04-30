package ru.alexpshkov.reaxessentials.teleportation;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;

@Data
public class TeleportationRequest {
    @NonNull private final Player player;
    @NonNull private final Player target;
    @NonNull private Long createdTime;
}
