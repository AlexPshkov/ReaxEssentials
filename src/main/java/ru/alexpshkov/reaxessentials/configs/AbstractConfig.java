package ru.alexpshkov.reaxessentials.configs;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@IndexSubclasses
public abstract class AbstractConfig implements IInitRequired {
    private final ReaxEssentials reaxEssentials;
    private FileConfiguration fileConfiguration;
    private final File absolutFile;
    private final String relativeFilePath;

    /**
     * Construct
     * @param reaxEssentials Plugin
     * @param relativeFilePath Config filepath
     */
    public AbstractConfig(@NotNull ReaxEssentials reaxEssentials, @NotNull String relativeFilePath) {
        this.reaxEssentials = reaxEssentials;
        this.relativeFilePath = relativeFilePath;
        this.absolutFile = new File(reaxEssentials.getDataFolder() + File.separator + relativeFilePath);
    }

    /**
     * Init configuration
     * @throws IOException
     */
    public void init() throws IOException {
        if (absolutFile.exists()) return;
        if (!reaxEssentials.getDataFolder().exists()) reaxEssentials.getDataFolder().mkdirs();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativeFilePath)) {
            if (inputStream == null) return;
            try (FileOutputStream out = new FileOutputStream(absolutFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        }
    }

    /**
     * Load YAML configuration
     */
    public void loadConfiguration() {
        fileConfiguration = YamlConfiguration.loadConfiguration(absolutFile);
    }

}
