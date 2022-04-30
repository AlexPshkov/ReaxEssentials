package ru.alexpshkov.reaxessentials.service.interfaces;

import com.j256.ormlite.dao.Dao;
import lombok.NonNull;
import ru.alexpshkov.reaxessentials.database.entities.*;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDataBase extends IInitRequired {

    CompletableFuture<Boolean> saveDeathPositionEntity(@NonNull DeathPositionEntity deathPositionEntity);

    CompletableFuture<Boolean> addIgnoredPlayer(@NonNull IgnoredUser ignoredUser);
    CompletableFuture<Boolean> removeIgnoredPlayer(@NonNull IgnoredUser ignoredUser);
    CompletableFuture<IgnoredUser> getIgnoredPlayer(@NonNull IgnoredUser ignoredUser);

    CompletableFuture<List<WarpEntity>> getAllWarpEntities();
    CompletableFuture<List<WarpEntity>> getWarpEntitiesOfOwner(@NonNull UserEntity userEntity);
    CompletableFuture<WarpEntity> getWarpEntity(@NonNull String warpName);
    CompletableFuture<Boolean> saveWarpEntity(@NonNull WarpEntity warpEntity);
    CompletableFuture<Boolean> removeWarpEntity(@NonNull WarpEntity warpEntity);

    CompletableFuture<Boolean> removeTrustedHome(@NonNull TrustedHome trustedHome);
    CompletableFuture<Boolean> addTrustedHome(@NonNull TrustedHome trustedHome);
    CompletableFuture<List<TrustedHome>> getTrustedHomes(@NonNull UserEntity userEntity);
    CompletableFuture<TrustedHome> getTrustedHome(@NonNull TrustedHome trustedHome);
    CompletableFuture<List<TrustedHome>> getTrustedHomesByUserName(@NonNull String trustedUserName);
    CompletableFuture<Boolean> isSuchTrustedHome(@NonNull TrustedHome trustedHome);
    CompletableFuture<List<TrustedHome>> getTrustedHomes(@NonNull HomeEntity homeEntity);

    CompletableFuture<Boolean> removeHomeEntity(@NonNull HomeEntity homeEntity);
    CompletableFuture<List<HomeEntity>> getHomeEntitiesOfOwner(@NonNull String whoOwned);
    CompletableFuture<Boolean> saveHomeEntity( @NonNull HomeEntity homeEntity);
    CompletableFuture<HomeEntity> getHomeEntityWithOwner(@NonNull String homeName, @NonNull String whoOwned);;


    CompletableFuture<ReceivedKitEntity> findReceivedKit(@NonNull UserEntity userEntity, @NonNull String kitName);
    CompletableFuture<List<ReceivedKitEntity>> getReceivedKits(@NonNull UserEntity userEntity);
    CompletableFuture<Boolean> addReceivedKit(@NonNull UserEntity userEntity, @NonNull ReceivedKitEntity receivedKitEntity);

    CompletableFuture<UserEntity> saveUserEntity(@NonNull UserEntity userEntity);
    CompletableFuture<UserEntity> getUserEntity(@NonNull String userName);

    CompletableFuture<Boolean> saveKitEntity(@NonNull KitEntity kitTable);
    CompletableFuture<KitEntity> getKitEntity(@NonNull String kitName);
    CompletableFuture<List<KitEntity>> getAllKitEntities();
    CompletableFuture<Boolean> removeKit(@NonNull String kitName);
    CompletableFuture<Boolean> isSuchKit(@NonNull String kitName);

    CompletableFuture<List<HelpEntity>> getHelpsForPlayer(@NonNull String userName);
    CompletableFuture<List<HelpEntity>> getPendingHelps();
    CompletableFuture<HelpEntity> getHelpById(@NonNull int helpId);
    CompletableFuture<HelpEntity> saveHelpEntity(@NonNull HelpEntity helpEntity);

    UserEntity SYNC_getUserEntity(@NonNull String userName) throws SQLException;
    Dao.CreateOrUpdateStatus SYNC_saveUserEntity(@NonNull UserEntity userEntity) throws SQLException;
    boolean SYNC_isSuchUser(@NonNull String userName) throws SQLException;
}
