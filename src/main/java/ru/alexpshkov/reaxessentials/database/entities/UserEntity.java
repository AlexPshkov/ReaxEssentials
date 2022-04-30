package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;

@Data
@DatabaseTable(tableName = "ReaxEssentials_users")
public class UserEntity implements IDataEntity {
    @DatabaseField(id = true) private String userName;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true) private DeathPositionEntity lastDeathPosition = new DeathPositionEntity();
    @ForeignCollectionField private ForeignCollection<ReceivedKitEntity> receivedKits;
    @ForeignCollectionField private ForeignCollection<IgnoredUser> ignoredUsers;
    @DatabaseField private Boolean isTpIgnore = false;
    @DatabaseField private Boolean isFlight = false;
    @DatabaseField private Boolean isGod = false;
    @DatabaseField private double coins;
    @DatabaseField private long secondsPlayed;
    @DatabaseField private long muteExpired;
    @DatabaseField private int warnsAmount;
}
