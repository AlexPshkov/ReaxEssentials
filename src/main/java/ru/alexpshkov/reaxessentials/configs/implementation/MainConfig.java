package ru.alexpshkov.reaxessentials.configs.implementation;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.configs.AbstractConfig;
import ru.alexpshkov.reaxessentials.database.DataBaseParameters;

import java.util.Optional;

@Getter
public class MainConfig extends AbstractConfig {
    private DataBaseParameters dataBaseParameters;

    private Integer tprDelay;
    private Integer tprMaxX;
    private Integer tprMaxZ;
    private Integer tprMinX;
    private Integer tprMinZ;

    private Integer tpaExpireTime;
    private Integer homeNameMaxLength;
    private Integer warpNameMaxLength;

    private String startKitName;

    private int helpopTimeForMark;
    private int localChatRadius;


    /**
     * Make main configuration
     * @param reaxEssentials Plugin
     */
    public MainConfig(@NotNull ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "config.yaml");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();
        tprDelay = getIntegerFromConfig("tpr.delay").orElse(10) * 1000;
        tprMaxX = getIntegerFromConfig("tpr.maxX").orElse(0);
        tprMaxZ = getIntegerFromConfig("tpr.maxZ").orElse(0);
        tprMinX = getIntegerFromConfig("tpr.minX").orElse(0);
        tprMinZ = getIntegerFromConfig("tpr.minZ").orElse(0);

        tpaExpireTime = getIntegerFromConfig("tpa.expire").orElse(0) * 1000;
        homeNameMaxLength = getIntegerFromConfig("home.maxLength").orElse(100);
        warpNameMaxLength = getIntegerFromConfig("warp.maxLength").orElse(100);

        startKitName = getStringFromConfig("kit.start").orElse("start");

        helpopTimeForMark = getIntegerFromConfig("helpop.timeForMark").orElse(3600) * 1000;

        localChatRadius = getIntegerFromConfig("chat.localChatRadius").orElse(100);

        dataBaseParameters = loadDataBaseParameters();
    }

    private DataBaseParameters loadDataBaseParameters() {
        String dbDriver = getStringFromConfig("dataBase.driver").orElse("org.sqlite.JDBC");
        String dbUrl = getStringFromConfig("dataBase.url").orElse("jdbc:sqlite:./plugins/ReaxEssentials/database.db");
        String dbUserName = getStringFromConfig("dataBase.userName").orElse("user");
        String dbUserPassword = getStringFromConfig("dataBase.userPassword").orElse("password");
        return new DataBaseParameters(dbUrl, dbDriver, dbUserName, dbUserPassword);
    }


    /**
     * Get simply string from config
     * @param configField field name
     * @return Optional value
     */
    private Optional<String> getStringFromConfig(String configField) {
        return Optional.ofNullable(super.getFileConfiguration().getString(configField));
    }

    /**
     * Get simply integer from config
     * @param configField field name
     * @return Optional value
     */
    private Optional<Integer> getIntegerFromConfig(String configField) {
        return Optional.of(super.getFileConfiguration().getInt(configField));
    }
}
