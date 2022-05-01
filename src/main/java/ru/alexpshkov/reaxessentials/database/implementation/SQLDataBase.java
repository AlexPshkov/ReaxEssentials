package ru.alexpshkov.reaxessentials.database.implementation;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.atteo.classindex.ClassIndex;
import org.bukkit.Bukkit;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.database.DataBaseParameters;
import ru.alexpshkov.reaxessentials.database.entities.*;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SQLDataBase implements IDataBase {
    private final ReaxEssentials reaxEssentials;

    private Dao<KitEntity, String> kitsTableDao;
    private Dao<UserEntity, String> usersTableDao;
    private Dao<HomeEntity, Integer> homesTableDao;
    private Dao<WarpEntity, String> warpTableDao;
    private Dao<TrustedHome, Integer> trustedHomeDao;
    private Dao<DeathPositionEntity, Integer> deathPositionDao;
    private Dao<HelpEntity, Integer> helpsDao;

    private final HashMap<String, UserEntity> cachedUsers = new HashMap<>();
    private final HashMap<String, Long> lastAccessTime = new HashMap<>();

    @Override
    public void init() throws Exception {
        DataBaseParameters dataBaseParameters = reaxEssentials.getMainConfig().getDataBaseParameters();
        reaxEssentials.getLogger().info("Initializing data base. Using: " + dataBaseParameters.getDataBaseDriver());
        Class.forName(dataBaseParameters.getDataBaseDriver());
        try (ConnectionSource connectionSource = new JdbcConnectionSource(dataBaseParameters.getDataBaseUrl(), dataBaseParameters.getUserName(), dataBaseParameters.getUserPassword())) {
            //Creating DAOs
            kitsTableDao = DaoManager.createDao(connectionSource, KitEntity.class);
            usersTableDao = DaoManager.createDao(connectionSource, UserEntity.class);
            homesTableDao = DaoManager.createDao(connectionSource, HomeEntity.class);
            warpTableDao = DaoManager.createDao(connectionSource, WarpEntity.class);
            trustedHomeDao = DaoManager.createDao(connectionSource, TrustedHome.class);
            helpsDao = DaoManager.createDao(connectionSource, HelpEntity.class);
            deathPositionDao = DaoManager.createDao(connectionSource, DeathPositionEntity.class);

            ClassIndex.getSubclasses(IDataEntity.class, reaxEssentials.getClass().getClassLoader())
                    .forEach(aClass -> createTableIfNotExists(connectionSource, aClass));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(reaxEssentials, this::purgeCache, 60 * 20L, 60 * 20L);
        reaxEssentials.getLogger().info("Data base initialized successfully");

    }

    /**
     * Purge old data of users. Only from memory
     */
    public void purgeCache() {
        for (Map.Entry<String, Long> stringLongEntry : lastAccessTime.entrySet()) {
            if (Bukkit.getPlayer(stringLongEntry.getKey()) != null) continue;
            if (new Date().getTime() - stringLongEntry.getValue() >= 60000) {
                cachedUsers.remove(stringLongEntry.getKey());
                lastAccessTime.remove(stringLongEntry.getKey());
            }
        }
    }

    //Creating table
    public void createTableIfNotExists(ConnectionSource connectionSource, Class<?> aClass) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, aClass);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public CompletableFuture<HomeEntity> getHomeEntityWithOwner(@NonNull String homeName, @NonNull String whoOwned) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return homesTableDao.queryBuilder().where()
                        .eq("whoOwned", whoOwned)
                        .and()
                        .eq("homeName", homeName)
                        .queryForFirst();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<HomeEntity>> getHomeEntitiesOfOwner(@NonNull String whoOwned) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return homesTableDao.queryBuilder().where()
                        .eq("whoOwned", whoOwned)
                        .query();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> saveHomeEntity(@NonNull HomeEntity homeEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                homesTableDao.createOrUpdate(homeEntity);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> removeHomeEntity(@NonNull HomeEntity homeEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                homesTableDao.delete(homeEntity);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<ReceivedKitEntity> findReceivedKit(@NonNull UserEntity userEntity, @NonNull String kitName) {
        return CompletableFuture.supplyAsync(() -> userEntity.getReceivedKits().stream().filter(receivedKitEntity ->
                receivedKitEntity.getKitName().equalsIgnoreCase(kitName)).findAny().orElse(null), reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<ReceivedKitEntity>> getReceivedKits(@NonNull UserEntity userEntity) {
        return CompletableFuture.supplyAsync(() -> new ArrayList<>(userEntity.getReceivedKits()), reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> addReceivedKit(@NonNull UserEntity userEntity, @NonNull ReceivedKitEntity receivedKitEntity) {
        return CompletableFuture.supplyAsync(() -> {
            userEntity.getReceivedKits().add(receivedKitEntity);
            return true;
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> removeTrustedHome(@NonNull TrustedHome trustedHome) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                trustedHomeDao.delete(trustedHome);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());

    }

    @Override
    public CompletableFuture<Boolean> addTrustedHome(@NonNull TrustedHome trustedHome) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                trustedHomeDao.createOrUpdate(trustedHome);
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<TrustedHome>> getTrustedHomes(@NonNull UserEntity userEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return trustedHomeDao.queryBuilder().where().eq("trustedUser", userEntity).query();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<TrustedHome>> getTrustedHomes(@NonNull HomeEntity homeEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return trustedHomeDao.queryBuilder().where().eq("homeEntity", homeEntity).query();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<TrustedHome> getTrustedHome(@NonNull TrustedHome trustedHome) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<TrustedHome> trustedHomes = trustedHomeDao.queryForMatchingArgs(trustedHome);
                if (trustedHomes.isEmpty()) return null;
                return trustedHomes.get(0);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<TrustedHome>> getTrustedHomesByUserName(@NonNull String trustedUserName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return trustedHomeDao.queryBuilder().where().eq("trustedUser", trustedUserName).query();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> isSuchTrustedHome(@NonNull TrustedHome trustedHome) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return (!trustedHomeDao.queryForMatchingArgs(trustedHome).isEmpty());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<UserEntity> saveUserEntity(@NonNull UserEntity userEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Dao.CreateOrUpdateStatus result = SYNC_saveUserEntity(userEntity);
                if (result.isCreated()) {
                    usersTableDao.assignEmptyForeignCollection(userEntity, "receivedKits");
                    usersTableDao.assignEmptyForeignCollection(userEntity, "ignoredUsers");
                }
                return userEntity;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<UserEntity> getUserEntity(@NonNull String userName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return SYNC_getUserEntity(userName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> saveKitEntity(@NonNull KitEntity kitTable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                kitsTableDao.createOrUpdate(kitTable);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> isSuchKit(@NonNull String kitName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return kitsTableDao.idExists(kitName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> removeKit(@NonNull String kitName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return kitsTableDao.deleteById(kitName) > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<KitEntity> getKitEntity(@NonNull String kitName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return kitsTableDao.queryForId(kitName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<KitEntity>> getAllKitEntities() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return kitsTableDao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<WarpEntity>> getAllWarpEntities() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return warpTableDao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<WarpEntity>> getWarpEntitiesOfOwner(@NonNull UserEntity userEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return warpTableDao.queryForEq("whoOwned", userEntity);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<WarpEntity> getWarpEntity(@NonNull String warpName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return warpTableDao.queryForId(warpName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> saveWarpEntity(@NonNull WarpEntity warpEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                warpTableDao.createOrUpdate(warpEntity);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> removeWarpEntity(@NonNull WarpEntity warpEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                warpTableDao.delete(warpEntity);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> removeIgnoredPlayer(@NonNull IgnoredUser ignoredUser) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ignoredUser.getUserEntity().getIgnoredUsers().getDao().delete(ignoredUser);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> addIgnoredPlayer(@NonNull IgnoredUser ignoredUser) {
        return CompletableFuture.supplyAsync(() -> {
            ignoredUser.getUserEntity().getIgnoredUsers().add(ignoredUser);
            return true;
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<IgnoredUser> getIgnoredPlayer(@NonNull IgnoredUser ignoredUser) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<IgnoredUser> ignoredUserList = ignoredUser.getUserEntity().getIgnoredUsers().getDao().queryForMatchingArgs(ignoredUser);
                if (ignoredUserList.isEmpty()) return null;
                return ignoredUserList.get(0);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<Boolean> saveDeathPositionEntity(@NonNull DeathPositionEntity deathPositionEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                deathPositionDao.createOrUpdate(deathPositionEntity);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<HelpEntity> saveHelpEntity(@NonNull HelpEntity helpEntity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                helpsDao.createOrUpdate(helpEntity);
                return helpEntity;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<HelpEntity>> getHelpsForPlayer(@NonNull String userName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return helpsDao.queryForEq("askerName", userName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<List<HelpEntity>> getPendingHelps() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return helpsDao.queryForEq("isComplete", false);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    @Override
    public CompletableFuture<HelpEntity> getHelpById(int helpId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return helpsDao.queryForId(helpId);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }, reaxEssentials.getBukkitAsyncExecutor());
    }

    /**============================[SYNC METHODS]=======================================**/
    public boolean SYNC_isSuchUser(@NonNull String userName) throws SQLException {
        if (cachedUsers.containsKey(userName)) return true;
        return usersTableDao.idExists(userName);
    }
    public Dao.CreateOrUpdateStatus SYNC_saveUserEntity(@NonNull UserEntity userEntity) throws SQLException {
        cachedUsers.put(userEntity.getUserName(), userEntity);
        lastAccessTime.put(userEntity.getUserName(), new Date().getTime());
        return usersTableDao.createOrUpdate(userEntity);
    }
    public UserEntity SYNC_getUserEntity(@NonNull String userName) throws SQLException {
        if (cachedUsers.containsKey(userName)) {
            lastAccessTime.put(userName, new Date().getTime());
            return cachedUsers.get(userName);
        }
        UserEntity userEntity = usersTableDao.queryForId(userName);
        if (userEntity != null) {
            cachedUsers.put(userName, userEntity);
            lastAccessTime.put(userName, new Date().getTime());
        }
        return userEntity;
    }

}
