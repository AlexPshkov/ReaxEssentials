package ru.alexpshkov.reaxessentials.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IDataEntity;

@Getter
@Setter
@DatabaseTable(tableName = "ReaxEssentials_helptasks")
public class HelpEntity implements IDataEntity {

    @DatabaseField(generatedId = true) int id;
    @DatabaseField String askerName = "";
    @DatabaseField String answerName = "";
    @DatabaseField String askString = "";
    @DatabaseField String markComment = "";
    @DatabaseField String answer = "";
    @DatabaseField Boolean inHandle = false;
    @DatabaseField Boolean isComplete = false;
    @DatabaseField int mark;
    @DatabaseField long createdTime;
    @DatabaseField long answeredTime;

}
