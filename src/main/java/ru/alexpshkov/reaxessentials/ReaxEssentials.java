package ru.alexpshkov.reaxessentials;

import lombok.Getter;
import org.atteo.classindex.ClassIndex;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.alexpshkov.reaxessentials.commands.CommandManager;
import ru.alexpshkov.reaxessentials.configs.implementation.MainConfig;
import ru.alexpshkov.reaxessentials.configs.implementation.MessagesConfig;
import ru.alexpshkov.reaxessentials.configs.implementation.SoundsConfig;
import ru.alexpshkov.reaxessentials.database.implementation.SQLDataBase;
import ru.alexpshkov.reaxessentials.kits.KitsManager;
import ru.alexpshkov.reaxessentials.service.BukkitAsyncExecutor;
import ru.alexpshkov.reaxessentials.players.PlayersManager;
import ru.alexpshkov.reaxessentials.service.BukkitSyncExecutor;
import ru.alexpshkov.reaxessentials.service.ServiceManager;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IReaxListener;
import ru.alexpshkov.reaxessentials.teleportation.TeleportationManager;

import java.io.IOException;

@Getter
public final class ReaxEssentials extends JavaPlugin {
    private BukkitAsyncExecutor bukkitAsyncExecutor;
    private BukkitSyncExecutor bukkitSyncExecutor;

    private MainConfig mainConfig;
    private MessagesConfig messagesConfig;
    private SoundsConfig soundsConfig;

    private CommandManager commandManager;
    private PlayersManager playersManager;
    private TeleportationManager teleportationManager;
    private KitsManager kitsManager;

    private IDataBase dataBase;
    private ServiceManager serviceManager;

    @Override
    public void onEnable() {
        this.bukkitAsyncExecutor = new BukkitAsyncExecutor(this);
        this.bukkitSyncExecutor = new BukkitSyncExecutor(this);
        this.dataBase = new SQLDataBase(this);
        this.serviceManager = new ServiceManager(this);
        try {
            serviceManager.init();
            registerAll();
            registerListeners();
            dataBase.init();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.shutdown();
        }
    }

    @Override
    public void onDisable() {
        kitsManager.forceSaveAllKits();
    }


    /**
     * Regiser all services
     */
    public void registerAll() throws IOException, NoSuchFieldException, IllegalAccessException {
        Bukkit.getScheduler().cancelTasks(this);

        this.mainConfig = new MainConfig(this);
        this.messagesConfig = new MessagesConfig(this);
        this.soundsConfig = new SoundsConfig(this);
        this.commandManager = new CommandManager(this);
        this.kitsManager = new KitsManager(this);
        this.teleportationManager = new TeleportationManager(this);
        this.playersManager = new PlayersManager(this);

        getLogger().info("Initializing all managers...");
        this.mainConfig.init();
        this.messagesConfig.init();
        this.soundsConfig.init();
        this.commandManager.init();
        this.kitsManager.init();
        this.teleportationManager.init();
        this.playersManager.init();
        getLogger().info("Managers successfully initialized");

        getLogger().info("Loading configs");
        this.mainConfig.loadConfiguration();
        this.messagesConfig.loadConfiguration();
        this.soundsConfig.loadConfiguration();
        getLogger().info("Configs successfully loaded");
    }

    /**
     * Regisering listeners
     */
    private void registerListeners() {
        getLogger().info("Loading listeners...");
        ClassIndex.getSubclasses(IReaxListener.class, this.getClassLoader()).forEach(aClass -> {
            try {
                Listener listener = aClass.getDeclaredConstructor(ReaxEssentials.class).newInstance(this);
                Bukkit.getPluginManager().registerEvents(listener, this);
                getLogger().info("Loaded listener " + aClass.getSimpleName());
            } catch (Exception ignored) {}
        });
    }

}
