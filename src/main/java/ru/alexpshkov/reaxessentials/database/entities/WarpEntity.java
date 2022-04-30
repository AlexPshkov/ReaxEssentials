package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.database.LocationBasedEntity;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;


@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_warps")
public class WarpEntity extends LocationBasedEntity implements IDataEntity {

    @DatabaseField(id = true) String warpName;
    @DatabaseField(canBeNull = false, foreign = true, columnName = "whoOwned") UserEntity whoOwned;

}
