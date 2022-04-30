package ru.alexpshkov.reaxessentials.configs.implementation;

import com.sun.istack.internal.NotNull;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.configs.AbstractConfig;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesConfig extends AbstractConfig {

    /**
     * Make messages configuration
     * @param reaxEssentials Plugin
     */
    public MessagesConfig(@NotNull ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "messages.yaml");
    }


    /**
     * Get formatted message
     */
    public String getMessage(ReaxMessage reaxMessage, String ... args) {
        String BAD_MESSAGE = "&4ERROR: &fNo message found for &4";
        String textFromConfig = getStringFromConfig(reaxMessage.getConfigField()).orElse(BAD_MESSAGE + reaxMessage);
        return makeReplacements(textFromConfig, args);
    }

    /**
     * Make replacements for message
     * @param textFromConfig from config
     * @param args content replacements
     * @return formatted message
     */
    private String makeReplacements(@NotNull String textFromConfig, String ... args) {
        Matcher matcher = Pattern.compile("%\\w+%", Pattern.CASE_INSENSITIVE).matcher(textFromConfig);
        String result = textFromConfig;
        int counter = 0;
        while (matcher.find()) {
            String placeHolder = textFromConfig.substring(matcher.start(), matcher.end());
            String replacement = placeHolder;
            switch (placeHolder.toLowerCase(Locale.ROOT)) {
                case "%error_prefix%": {
                    replacement = getStringFromConfig(ReaxMessage.ERROR_PREFIX.getConfigField()).orElse("");
                    break;
                }
                case "%prefix%": {
                    replacement = getStringFromConfig(ReaxMessage.PREFIX.getConfigField()).orElse("");
                    break;
                }
                case "%content%": {
                    replacement = counter > args.length - 1 ? "" : args[Math.min(args.length - 1, counter)];
                    counter++;
                    break;
                }
            }
            result = result.replaceFirst(placeHolder, replacement);
        }
        return result.replaceAll("&", "§");
    }

    /**
     * Get simply from config
     * @param messageField field name
     * @return Optional value
     */
    private Optional<String> getStringFromConfig(String messageField) {
        return Optional.ofNullable(super.getFileConfiguration().getString(messageField));
    }
}
