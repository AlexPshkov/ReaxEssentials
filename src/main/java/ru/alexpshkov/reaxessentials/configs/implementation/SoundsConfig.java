package ru.alexpshkov.reaxessentials.configs.implementation;

import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.configs.AbstractConfig;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Optional;

public class SoundsConfig extends AbstractConfig {
    private final String BAD_SOUND = ChatColor.RED + "ERROR: " + ChatColor.WHITE + "Invalid sound format found for " + ChatColor.RED;

    /**
     * Make messages configuration
     * @param reaxEssentials Plugin
     */
    public SoundsConfig(@NotNull ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "sounds.yaml");
    }

    /**
     * Play sound from config
     */
    public void playSound(Player player, ReaxSound reaxSound) {
        if (reaxSound == null) return;
        String textFromConfig = getStringFromConfig(reaxSound.getConfigField()).orElse(BAD_SOUND + reaxSound);
        String[] textArgs = textFromConfig.split(":");
        if(textArgs.length < 3) return;
        try {
            Sound sound = Sound.valueOf(textArgs[0]);
            float volume = Float.parseFloat(textArgs[1]);
            float pitch = Float.parseFloat(textArgs[2]);
            player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
        } catch (IllegalArgumentException exception) {
            super.getReaxEssentials().getLogger().info(textFromConfig);
        }
    }

    /**
     * Get simply from config
     * @param soundField field name
     * @return Optional value
     */
    private Optional<String> getStringFromConfig(String soundField) {
        return Optional.ofNullable(super.getFileConfiguration().getString(soundField));
    }
}
