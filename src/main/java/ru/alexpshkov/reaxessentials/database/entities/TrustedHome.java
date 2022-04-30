package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;


@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_trustedHomes")
public class TrustedHome implements IDataEntity {

    @DatabaseField(generatedId = true) int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "homeEntity") HomeEntity homeEntity;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "trustedUser") UserEntity trustedUser;

}
