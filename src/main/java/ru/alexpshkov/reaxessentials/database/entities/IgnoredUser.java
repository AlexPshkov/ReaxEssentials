package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;

@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_ignored_users")
public class IgnoredUser implements IDataEntity {

    @DatabaseField(generatedId = true) int id;
    @DatabaseField String ignoredUserName;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "userEntity") UserEntity userEntity;

}
