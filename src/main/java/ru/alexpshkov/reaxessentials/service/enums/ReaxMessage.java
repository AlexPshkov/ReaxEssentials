package ru.alexpshkov.reaxessentials.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReaxMessage {

    PREFIX("prefix"),
    YES("message_yes"),
    NO("message_no"),
    ERROR_PREFIX("error_prefix"),
    NO_ONE("message_no_one"),
    MESSAGE_EMPTY("message_empty"),
    TIME_FORMAT_EXAMPLE("time_format_example"),
    NUMBER_FORMAT_EXAMPLE("number_format_example"),
    COINS_FORMAT_EXAMPLE("coins_format_example"),

    CHAT_LOCAL("chat_local"),
    CHAT_GLOBAL("chat_global"),
    JOIN_MESSAGE("join_message"),
    QUIT_MESSAGE("quit_message"),

    COINS_PAY("coins_pay"),
    COINS_RECEIVE("coins_receive"),
    COINS_LACK("coins_lack"),
    COINS_AMOUNT("coins_amount"),
    COINS_AMOUNT_OTHER("coins_amount_other"),
    COINS_SET("coins_set"),
    COINS_GIVE("coins_give"),
    COINS_TAKE("coins_take"),

    RELOAD_START("reload_start"),
    RELOAD_COMPLETE("reload_complete"),
    RELOAD_FAILED("reload_failed"),

    MUTE_CHAT("mute_chat"),
    MUTE_UNMUTE_CHAT("mute_unmute_chat"),
    MUTE_UNMUTE_CHAT_ALREADY("mute_unmute_chat_already"),
    MUTE_MESSAGE("mute_message"),

    WARN_CHAT("warn_chat"),
    WARN_CLEAR("warn_clear"),
    WARN_CLEAR_FROM_YOU("warn_clear_from_you"),
    WARNS_AMOUNT_OTHER("warns_amount_other"),
    WARNS_AMOUNT_YOU("warns_amount_you"),

    KICK_CHAT("kick_chat"),
    KICK_LOGIN("kick_login"),

    BANUSER_CHAT("banuser_chat"),
    BANUSER_LOGIN("banuser_login"),
    BANUSER_UNBAN("banuser_unban"),
    BANUSER_UNBAN_ALREADY("banuser_unban_already"),

    ADMINCHAT_FORMAT("adminchat_format"),

    HELPOP_ALREADY_ANSWERED("helpop_already_answered"),
    HELPOP_ABOUT_TASK("helpop_about_task"),
    HELPOP_LIST_EMPTY("helpop_list_empty"),
    HELPOP_LIST_FIELD("helpop_list_field"),
    HELPOP_LIST_TAG("helpop_list_tag"),
    HELPOP_TASK_SENDED("helpop_task_sended"),
    HELPOP_INHANDLE_ALREADY_BY_YOU("helpop_inHandle_already_by_you"),
    HELPOP_ITS_FUCKING_YOURS("helpop_its_fucking_yours"),
    HELPOP_ALREADY_INHANDLE("helpop_already_inhandle"),
    HELPOP_INHANDLE_ADMINS("helpop_inHandle_admins"),
    HELPOP_MARKTIME_EXPIRED("helpop_marktime_expired"),
    HELPOP_NO_ANSWER("helpop_no_answer"),
    HELPOP_MARK("helpop_mark"),
    HELPOP_INHANDLE("helpop_inHandle"),
    HELPOP_ANSWER("helpop_answer"),
    HELPOP_TASK("helpop_task"),
    HELPOP_ANSWERED("helpop_answered"),
    HELPOP_NO_SUCH_ID("helpop_no_such_id"),

    PERSONALMESSAGE_ONLY_OTHERS("personalmessage_only_others"),
    PERSONALMESSAGE_IGNORE_ONLY_OTHERS("personalmessage_ignore_only_others"),
    PERSONALMESSAGE_FORMAT("personalmessage_format"),
    PERSONALMESSAGE_FORMAT_YOU("personalmessage_format_you"),
    PERSONALMESSAGE_IGNORE_ADD("personalmessage_ignore_add"),
    PERSONALMESSAGE_IGNORE_REMOVE("personalmessage_ignore_remove"),
    PERSONALMESSAGE_IGNORE_REMOVE_ALREADY("personalmessage_ignore_remove_already"),
    PERSONALMESSAGE_IGNORE_ADD_ALREADY("personalmessage_ignore_add_already"),

    REPLYMESSAGE_TARGET_NOT_FOUND("replymessage_target_not_found"),

    GAMEMODE_CHANGE("gameMode_change"),
    GAMEMODE_CHANGE_OWN("gameMode_change_own"),
    GAMEMODE_SURVIVAL("gameMode_survival"),
    GAMEMODE_CREATIVE("gameMode_creative"),
    GAMEMODE_SPECTATOR("gameMode_spectator"),
    GAMEMODE_ADVENTURE("gameMode_adventure"),

    BACK_NOT_EXISTS("back_not_exists"),
    BACK_TELEPORTED_YOU("back_teleported_you"),
    BACK_TELEPORTED("back_teleported"),

    HEAL_YOU("heal_you"),
    HEAL("heal"),

    FEED_YOU("feed_you"),
    FEED("feed"),

    WEATHER_STORM("weather_storm"),
    WEATHER_SUN("weather_sun"),
    WEATHER_THUNDERING("weather_thundering"),

    TIMESET("timeset"),

    FLY_ON("fly_on"),
    FLY_OFF("fly_off"),
    FLY_OFF_YOU("fly_off_you"),
    FLY_ON_YOU("fly_on_you"),

    GOD_ON("god_on"),
    GOD_OFF("god_off"),
    GOD_OFF_YOU("god_off_you"),
    GOD_ON_YOU("god_on_you"),

    PLAY_TIME_OWN("playtime_own"),
    PLAY_TIME_OTHER("playtime_other"),

    ACCEPT_BUTTON("accept_button"),
    ACCEPT_BUTTON_HOVER("accept_button_hover"),
    DECLINE_BUTTON("decline_button"),
    DECLINE_BUTTON_HOVER("decline_button_hover"),
    MARK_BUTTON("mark_button"),
    MARK_BUTTON_HOVER("mark_button_hover"),
    MARK_BUTTON_PLACEHOLDER("mark_button_placeholder"),
    HELPOP_BUTTON_RESERVE("helpop_button_reserve"),
    HELPOP_BUTTON_RESERVE_HOVER("helpop_button_reserve_hover"),
    HELPOP_BUTTON_REPLY("helpop_button_reply"),
    HELPOP_BUTTON_REPLY_HOVER("helpop_button_reply_hover"),
    HELPOP_BUTTON_INFO("helpop_button_info"),
    HELPOP_BUTTON_INFO_HOVER("helpop_button_info_hover"),
    TELEPORTATION_BUTTON_HOVER("teleportation_button_hover"),
    TELEPORTATION_BUTTON("teleportation_button"),

    SPAWN_TELEPORT_OTHER("spawn_teleport_other"),
    SPAWN_TELEPORT_YOU("spawn_teleport_you"),
    SPAWN_NOT_EXISTS("spawn_not_exists"),
    SPAWN_CREATE("spawn_create"),

    WARP_MAXAMOUNT_CREATE("warp_maxamount_create"),
    WARP_NOT_EXISTS("warp_not_exists"),
    WARP_ALREADY_EXISTS("warp_already_exists"),
    WARP_INVALID_AMOUNT("warp_invalid_amount"),
    WARP_INVALID_CHARS("warp_invalid_chars"),
    WARP_CREATED("warp_created"),
    WARP_REMOVED("warp_removed"),
    WARP_TELEPORTED("warp_teleported"),
    WARP_LIST("warp_list"),
    WARP_LIST_FIELD("warp_list_field"),
    WARP_LIST_EMPTY("warp_list_empty"),

    TPR_DELAY("tpr_delay"),
    TPR_SEARCH_START("tpr_search_start"),
    TPR_TELEPORTED_YOU("tpr_teleported_you"),
    TPR_TELEPORTED_OTHER("tpr_teleported_other"),

    TELEPORTATION_IGNORED("teleportation_ignored"),
    TELEPORTATION_IGNORE_ON_OWN("teleportation_ignore_on_own"),
    TELEPORTATION_IGNORE_OFF_OWN("teleportation_ignore_off_own"),
    TELEPORTATION_IGNORE_ON("teleportation_ignore_on"),
    TELEPORTATION_IGNORE_OFF("teleportation_ignore_off"),
    TELEPORTATION_REQUEST_RECEIVED("teleportation_request_received"),
    TELEPORTATION_REQUEST_SENT("teleportation_request_sent"),
    TELEPORTATION_REQUEST_EMPTY("teleportation_request_empty"),
    TELEPORTATION_REQUEST_DELETED("teleportation_request_deleted"),
    TELEPORTATION_REQUEST_ALREADY("teleportation_request_already"),
    TELEPORTATION_REQUEST_ONLY_OTHERS("teleportation_request_only_others"),
    TELEPORTATION_REQUEST_ACCEPT("teleportation_request_accept"),
    TELEPORTATION_REQUEST_DECLINE("teleportation_request_decline"),
    TELEPORTATION_REQUEST_DECLINE_BY("teleportation_request_decline_by"),
    TELEPORTATION_REQUEST_ACCEPT_BY("teleportation_request_accept_by"),


    KIT_LIST("kit_list"),
    KIT_LIST_EMPTY("kit_list_empty"),
    KIT_CREATED("kit_created"),
    KIT_ALREADY_EXISTS("kit_already_exists"),
    KIT_PARAMETER_CHANGED("kit_parameter_changed"),
    KIT_REMOVED("kit_removed"),
    KIT_EDITED("kit_edited"),
    KIT_RECEIVED("kit_received"),
    KIT_NOT_PERMIT("kit_not_permit"),
    KIT_NOT_EXISTS("kit_not_exists"),
    KIT_DELAY("kit_delay"),

    HOME_MAXAMOUNT_SHARE("home_maxamount_share"), //TODO Realize it (May be :D)
    HOME_MAXAMOUNT_CREATE("home_maxamount_create"),
    HOME_NOT_EXISTS("home_not_exists"),
    HOME_INVALID_CHARS("home_invalid_chars"),
    HOME_INVALID_AMOUNT("home_invalid_amount"),
    HOME_NEW("home_new"),
    HOME_LIST("home_list"),
    HOME_LIST_FIELD("home_list_field"),
    HOME_LIST_EMPTY("home_list_empty"),
    HOME_SHARED("home_shared"),
    HOME_SHARED_WITH_YOU("home_share_with_you"),
    HOME_SHARED_ALREADY("home_shared_already"),
    HOME_SHARED_REMOVE_ALREADY("home_shared_remove_already"),
    HOME_SHARED_REMOVE("home_shared_remove"),
    HOME_TELEPORTATION_OWN("home_teleportation_own"),
    HOME_TELEPORTATION_ANOTHER("home_teleportation_another"),
    HOME_TELEPORTATION_ANOTHER_DENY("home_teleportation_another_deny"),
    HOME_REMOVE("home_remove"),

    ONLY_PLAYERS("only_players"),
    USER_NOTFOUND("user_notFound"),
    NO_PERM("no_permissions"),
    SAVE_EXCEPTION("save_exception"),
    INVALID_SYNTAX("invalid_syntax"),
    INVALID_FORMAT("invalid_format");

    private final String configField;
}
