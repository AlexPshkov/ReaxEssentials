package ru.alexpshkov.reaxessentials.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.database.entities.UserEntity;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IReaxListener;

@RequiredArgsConstructor
public class JoinQuitListener implements IReaxListener {
    private final ReaxEssentials reaxEssentials;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.QUIT_MESSAGE, event.getPlayer().getName()));
    }


    @EventHandler
    public void onPreLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.KICK_BANNED) return;
        BanEntry banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(event.getPlayer().getName());
        String message = reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.BANUSER_LOGIN, banEntry.getSource(), Utils.convertMillisToDate(banEntry.getExpiration().getTime()), banEntry.getReason());
        event.setKickMessage(message);
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(reaxEssentials.getMessagesConfig().getMessage(ReaxMessage.JOIN_MESSAGE, event.getPlayer().getName()));

        IDataBase dataBase = reaxEssentials.getDataBase();

        dataBase.getUserEntity(player.getName()).thenAccept(userEntity -> {
            if (userEntity != null) {
                Bukkit.getScheduler().runTask(reaxEssentials, () -> {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        player.setAllowFlight(userEntity.getIsFlight());
                        player.setFlying(userEntity.getIsFlight());
                    }
                });
                return;
            }
            UserEntity userTable = new UserEntity();
            userTable.setUserName(player.getName());
            dataBase.saveUserEntity(userTable).thenAccept(savedUserEntity -> {
                //Kit give
                dataBase.getKitEntity(reaxEssentials.getMainConfig().getStartKitName()).thenAccept(kitEntity -> {
                    if (kitEntity != null) reaxEssentials.getKitsManager().addKitToPlayer(player, kitEntity, savedUserEntity);
                });

            });
        });
    }
}
