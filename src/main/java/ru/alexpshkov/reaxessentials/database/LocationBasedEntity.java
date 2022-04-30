package ru.alexpshkov.reaxessentials.database;

import com.j256.ormlite.field.DatabaseField;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationBasedEntity {
    @DatabaseField String worldName;
    @DatabaseField double posX;
    @DatabaseField double posY;
    @DatabaseField double posZ;
    @DatabaseField float yaw;
    @DatabaseField float pitch;

    /**
     * Sets location of homePoint
     * @param location Bukkit location
     */
    public void setLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.posX = location.getX();
        this.posY = location.getY();
        this.posZ = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    /**
     * Gets location of homePoint
     */
    public Location getLocation() {
        if (worldName == null) return null;
        return new Location( //Make location from homeEntity
                Bukkit.getWorld(this.worldName),
                this.posX,
                this.posY,
                this.posZ,
                this.yaw,
                this.pitch);
    }
}
