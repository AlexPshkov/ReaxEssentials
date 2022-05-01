package ru.alexpshkov.reaxessentials.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReaxSound {

    GUI_OPEN("gui_open"),

    PERSONALMESSAGE_RECEIVED("personalmessage_received"),
    ADMINCHAT_RECEIVED("adminchat_received"),

    COINS_RECEIVED("coins_received"),

    BROADCAST_RECEIVED("broadcast_received"),
    SAY_RECEIVED("say_received"),

    HELPOP_TASK("helpop_task"),
    HELPOP_ANSWER("helpop_answer"),

    USER_MUTED("user_muted"),
    USER_BANNED("user_banned"),
    USER_WARNED("user_warned"),
    USER_KICKED("user_kicked"),

    BACK_TELEPORTED_YOU("back_teleported_you"),

    TPR_TELEPORTED("tpr_teleported"),

    GAMEMODE_CHANGE("gameMode_change"),

    GOD_ON_YOU("god_on_you"),

    TIME_SET("time_set"),

    WEATHER_SET("weather_set"),

    FLY_ON_YOU("fly_on_you"),

    HEAL_YOU("heal_you"),
    FEED_YOU("feed_you"),

    KIT_RECEIVED("kit_received"),

    SPAWN_TELEPORT("spawn_teleport"),
    SPAWN_CREATE("spawn_create"),

    WARP_CREATED("warp_created"),
    WARP_REMOVED("warp_removed"),
    WARP_TELEPORTED("warp_teleported"),

    HOME_TELEPORTATION("home_teleportation"),
    HOME_NEW("home_new"),
    HOME_SHARED_WITH_YOU("home_shared_with_you"),
    HOME_SHARED("home_shared"),

    TELEPORTATION_TELEPORTED("teleportation_teleported"),

    TELEPORTATION_ACCEPT("teleportation_accept"),
    TELEPORTATION_IGNORE_OFF("teleportation_ignore_off"),
    TELEPORTATION_IGNORE_ON("teleportation_ignore_on"),
    TELEPORTATION_DECLINE("teleportation_decline"),
    TELEPORTATION_SENT("teleportation_sent");

    private final String configField;
}
