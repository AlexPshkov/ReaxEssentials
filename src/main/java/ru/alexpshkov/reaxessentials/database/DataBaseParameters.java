package ru.alexpshkov.reaxessentials.database;

import lombok.Data;

@Data
public class DataBaseParameters {
    private final String dataBaseType;
    private final String fileRelativePath;
    private final String userName;
    private final String userPassword;
}
