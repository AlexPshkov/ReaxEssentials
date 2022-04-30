package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.database.LocationBasedEntity;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;


@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_homes")
public class HomeEntity extends LocationBasedEntity implements IDataEntity {

    @DatabaseField(generatedId = true) int id;
    @DatabaseField String homeName;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "whoOwned") UserEntity whoOwned;

}
