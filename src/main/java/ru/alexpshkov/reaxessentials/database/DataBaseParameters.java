package ru.alexpshkov.reaxessentials.database;

import lombok.Data;

@Data
public class DataBaseParameters {
    private final String dataBaseUrl;
    private final String dataBaseDriver;
    private final String userName;
    private final String userPassword;
}
