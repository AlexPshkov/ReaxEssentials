package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;

@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_receivedKits")
public class ReceivedKitEntity implements IDataEntity {

    @DatabaseField(generatedId = true) int id;
    @DatabaseField() String kitName;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "whoReceived") UserEntity whoReceived;
    @DatabaseField() long receivedTime;

}
