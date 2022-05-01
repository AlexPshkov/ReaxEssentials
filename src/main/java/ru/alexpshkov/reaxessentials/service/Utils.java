package ru.alexpshkov.reaxessentials.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");


    /**
     * Gets online player
     * @param playerName Player name
     * @return Player or null if player is not online
     */
    @Nullable
    public static Player getOnlinePlayer(String playerName) {
        return Bukkit.getOnlinePlayers().stream() //Search player in playerList
                .filter(somePlayer -> somePlayer.getName().equals(playerName))
                .findAny().orElse(null);
    }

    /**
     * Round double value
     * @param value unformatted value
     * @param places how many places keep
     * @return formatted double
     */
    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * Convert string to time in milliseconds
     *
     * @param string input string
     * @return seconds
     */
    public static long convertToSeconds(String string) throws NumberFormatException {
        Matcher matcher = Pattern.compile("\\d+\\w+|\\d").matcher(string);
        if (!matcher.matches()) throw new NumberFormatException();
        long number;
        try {
            number = Long.parseLong(string);
            return number * 1000;
        } catch (NumberFormatException ignored) {}

        String num = string.substring(0, string.length() - 1);
        number = Long.parseLong(num);
        switch (string.replace(num, "").toLowerCase(Locale.ROOT)) {
            case "w": case "week": case "weeks":
                number *= 7;
            case "d": case "day": case "days":
                number *= 24;
            case "h": case "hour": case "hours":
                number *= 60;
            case "m": case "min": case "mins": case "minute": case "minutes":
                number *= 60;
            case "s": case "sec": case "secs": case "seconds": case "second":
                number *= 1000;
        }
        return number;
    }


    /**
     * Convert milliseconds to normal date
     */
    public static String convertMillisToDate(long milliseconds) {
        return simpleDateFormat.format(new Date(milliseconds));
    }

    /**
     * Convert seconds to normal time
     */
    public static String convertSecondsToDate(long simpleSeconds) {
        long hours = simpleSeconds / 3600;
        long minutes = (simpleSeconds % 3600) / 60;
        long seconds = simpleSeconds % 60;
        return (hours > 0 ? (hours + " ч ") : "") + (minutes > 0 ? (minutes + " мин ") : "") + seconds + " сек ";
    }
}
