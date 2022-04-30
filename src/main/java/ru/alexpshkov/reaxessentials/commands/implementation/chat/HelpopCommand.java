package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import lombok.NonNull;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.HelpEntity;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;
import ru.alexpshkov.reaxessentials.service.interfaces.IDataBase;

import java.util.Date;
import java.util.Locale;


public class HelpopCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public HelpopCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "helpop");
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        IDataBase dataBase = reaxEssentials.getDataBase();

        if (args.length == 0) {
            printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " <message>");
            return false;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reply": {
                if (!hasPermission(player, "helpop.answer")) {
                    printMessage(player, ReaxMessage.NO_PERM);
                    return false;
                }
                if (args.length <= 2) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId> <message>");
                    return false;
                }

                int taskId = convertStringToInt(args[1]);
                if (taskId <= 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId> <message>");
                    return false;
                }

                dataBase.getHelpById(taskId).thenAccept(helpEntity -> {
                    if (isAlreadyAnsweredOrReserved(player, helpEntity, taskId)) return;

                    String message = combineArgsIntoString(2, args);
                    helpEntity.setAnswer(message);
                    helpEntity.setAnsweredTime(new Date().getTime());
                    helpEntity.setInHandle(false);
                    helpEntity.setIsComplete(true);
                    dataBase.saveHelpEntity(helpEntity);

                    Player target = Utils.getOnlinePlayer(helpEntity.getAskerName());
                    if (target == null) return;

                    String placeholder = getMessageFromConfig(ReaxMessage.MARK_BUTTON_PLACEHOLDER);
                    TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HELPOP_ANSWER, message);
                    textComponent.addExtra("  ");
                    textComponent.addExtra(createButton(ReaxMessage.MARK_BUTTON, ReaxMessage.MARK_BUTTON_HOVER, alias + " mark " + taskId + " " + placeholder, ClickEvent.Action.SUGGEST_COMMAND));

                    printTextComponent(target, textComponent);
                    printMessage(player, ReaxMessage.HELPOP_ANSWERED, taskId + "");
                    playSound(player, ReaxSound.HELPOP_ANSWER);
                });
                break;
            }
            case "reserve": case "catch": case "book": case "take": {
                if (!hasPermission(player, "helpop.answer")) {
                    printMessage(player, ReaxMessage.NO_PERM);
                    return false;
                }
                if (args.length <= 1) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId>");
                    return false;
                }

                int taskId = convertStringToInt(args[1]);
                if (taskId <= 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId>");
                    return false;
                }

                dataBase.getHelpById(taskId).thenAccept(helpEntity -> {
                    if (isAlreadyAnsweredOrReserved(player, helpEntity, taskId)) return;
                    if (helpEntity.getInHandle()) {
                        printMessage(player, ReaxMessage.HELPOP_INHANDLE_ALREADY_BY_YOU);
                        return;
                    }
                    helpEntity.setAnswerName(player.getName());
                    helpEntity.setInHandle(true);
                    dataBase.saveHelpEntity(helpEntity).thenAccept(savedHelpEntity -> {
                        Player target = Utils.getOnlinePlayer(helpEntity.getAskerName());
                        if (target != null) printMessage(target, ReaxMessage.HELPOP_INHANDLE, helpEntity.getId() + "");

                        TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HELPOP_INHANDLE_ADMINS, player.getName(), helpEntity.getId() + "");
                        announceAdmins(textComponent, null);
                    });
                });
                break;
            }
            case "mark": {
                if (args.length <= 2) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId> <mark> [comment]");
                    return false;
                }

                int taskId = convertStringToInt(args[1]);
                if (taskId <= 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId> <mark> [comment]");
                    return false;
                }

                int mark = convertStringToInt(args[2]);
                if (mark < 0 || mark > 5) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId> <0-5> [comment]");
                    return false;
                }

                String comment = combineArgsIntoString(3, args);

                dataBase.getHelpById(taskId).thenAccept(helpEntity -> {
                    if (helpEntity == null) {
                        printMessage(player, ReaxMessage.NO_PERM);
                        return;
                    }
                    if (!helpEntity.getAskerName().equals(player.getName())) {
                        printMessage(player, ReaxMessage.NO_PERM);
                        return;
                    }
                    if (!helpEntity.getIsComplete()) {
                        printMessage(player, ReaxMessage.HELPOP_NO_ANSWER);
                        return;
                    }
                    if ((new Date().getTime() - helpEntity.getAnsweredTime()) > reaxEssentials.getMainConfig().getHelpopTimeForMark()) {
                        printMessage(player, ReaxMessage.HELPOP_MARKTIME_EXPIRED);
                        return;
                    }

                    helpEntity.setMark(mark);
                    helpEntity.setMarkComment(comment);
                    dataBase.saveHelpEntity(helpEntity);
                    printMessage(player, ReaxMessage.HELPOP_MARK, taskId + "", mark + "", comment);
                });
                break;
            }
            case "info": case "about": {
                if (!hasPermission(player, "helpop.answer")) {
                    printMessage(player, ReaxMessage.NO_PERM);
                    return false;
                }
                if (args.length <= 1) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId>");
                    return false;
                }

                int taskId = convertStringToInt(args[1]);
                if (taskId <= 0) {
                    printMessage(player, ReaxMessage.INVALID_SYNTAX, alias + " " + args[0] + " <taskId>");
                    return false;
                }

                dataBase.getHelpById(taskId).thenAccept(helpEntity -> {
                    if (helpEntity == null) {
                        printMessage(player, ReaxMessage.HELPOP_NO_SUCH_ID, taskId + "");
                        return;
                    }
                    String stringTaskId = helpEntity.getId() + "";
                    String stringInHandle = getMessageFromConfig(helpEntity.getInHandle() ? ReaxMessage.YES : ReaxMessage.NO);
                    String stringIsComplete = getMessageFromConfig(helpEntity.getIsComplete() ? ReaxMessage.YES : ReaxMessage.NO);
                    String stringAskerName = helpEntity.getAskerName();
                    String stringAskTime = Utils.convertMillisToDate(helpEntity.getCreatedTime());
                    String stringAnswer = helpEntity.getAnswer().isEmpty() ? getMessageFromConfig(ReaxMessage.MESSAGE_EMPTY) : helpEntity.getAnswer();
                    String stringAnswerName = helpEntity.getAnswerName().isEmpty() ? getMessageFromConfig(ReaxMessage.NO_ONE) : helpEntity.getAnswerName();
                    String stringAnswerTime = helpEntity.getAnsweredTime() > 0 ? Utils.convertMillisToDate(helpEntity.getAnsweredTime()) : getMessageFromConfig(ReaxMessage.MESSAGE_EMPTY);
                    String stringMark = helpEntity.getMark() + "";
                    String stringMarkComment = helpEntity.getMarkComment().isEmpty() ? getMessageFromConfig(ReaxMessage.MESSAGE_EMPTY) : helpEntity.getMarkComment();

                    TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HELPOP_ABOUT_TASK, stringTaskId, helpEntity.getAskString(), stringInHandle, stringIsComplete, stringAskerName, stringAskTime, stringAnswer, stringAnswerName, stringAnswerTime, stringMark, stringMarkComment);

                    textComponent.addExtra("\n");
                    textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_REPLY, ReaxMessage.HELPOP_BUTTON_REPLY_HOVER, alias + " reply " + stringTaskId + " ", ClickEvent.Action.SUGGEST_COMMAND));
                    textComponent.addExtra("  ");
                    textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_RESERVE, ReaxMessage.HELPOP_BUTTON_RESERVE_HOVER, alias + " reserve " + stringTaskId));

                    printTextComponent(player, textComponent);
                });
                break;
            }
            case "list": {

                dataBase.getPendingHelps().thenAccept(helpEntities -> {
                    if (helpEntities.isEmpty()) {
                        printMessage(player, ReaxMessage.HELPOP_LIST_EMPTY);
                        return;
                    }
                    TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HELPOP_LIST_TAG);
                    helpEntities.forEach(helpEntity -> {
                        String stringTaskId = helpEntity.getId() + "";
                        String stringAskerName = helpEntity.getAskerName();
                        String stringAskTime = Utils.convertMillisToDate(helpEntity.getCreatedTime());
                        textComponent.addExtra("\n");
                        textComponent.addExtra(getTextComponentFromConfig(ReaxMessage.HELPOP_LIST_FIELD, stringTaskId, stringAskerName, stringAskTime));
                        textComponent.addExtra("  ");
                        textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_INFO, ReaxMessage.HELPOP_BUTTON_INFO_HOVER, alias + " info " + stringTaskId));
                        textComponent.addExtra("  ");
                        textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_RESERVE, ReaxMessage.HELPOP_BUTTON_RESERVE_HOVER, alias + " reserve " + stringTaskId));


                    });
                    printTextComponent(player, textComponent);
                });
                break;
            }
            default: {
                String message = combineArgsIntoString(0, args).replaceAll("&", "");

                HelpEntity helpEntity = new HelpEntity();
                helpEntity.setAskerName(player.getName());
                helpEntity.setAskString(message);
                helpEntity.setCreatedTime(new Date().getTime());

                dataBase.saveHelpEntity(helpEntity).thenAccept(savedHelpEntity -> {
                    TextComponent textComponent = getTextComponentFromConfig(ReaxMessage.HELPOP_TASK, savedHelpEntity.getId() + "", player.getName(), message);

                    textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_REPLY, ReaxMessage.HELPOP_BUTTON_REPLY_HOVER, alias + " reply " + savedHelpEntity.getId() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                    textComponent.addExtra("  ");
                    textComponent.addExtra(createButton(ReaxMessage.HELPOP_BUTTON_RESERVE, ReaxMessage.HELPOP_BUTTON_RESERVE_HOVER, alias + " reserve " + savedHelpEntity.getId()));

                    printMessage(player, ReaxMessage.HELPOP_TASK_SENDED, savedHelpEntity.getId() + "");

                    announceAdmins(textComponent, ReaxSound.HELPOP_TASK);
                });
            }
        }

        return true;
    }

    private void announceAdmins(TextComponent textComponent, ReaxSound reaxSound) {
        Bukkit.getOnlinePlayers().stream().filter(pl -> hasPermission(pl, "helpop.answer")).forEach(somePlayer -> {
            printTextComponent(somePlayer, textComponent);
            playSound(somePlayer, reaxSound);
        });
    }

    private boolean isAlreadyAnsweredOrReserved(@NonNull Player player, HelpEntity helpEntity, int helpId) {
        if (helpEntity == null) {
            printMessage(player, ReaxMessage.HELPOP_NO_SUCH_ID, helpId + "");
            return true;
        }
        if (helpEntity.getIsComplete()) {
            printMessage(player, ReaxMessage.HELPOP_ALREADY_ANSWERED, helpEntity.getId() + "");
            return true;
        }
        if (helpEntity.getInHandle() && !helpEntity.getAnswerName().equalsIgnoreCase(player.getName())) {
            printMessage(player, ReaxMessage.HELPOP_ALREADY_INHANDLE, helpEntity.getId() + "", helpEntity.getAnswerName());
            return true;
        }
        if (helpEntity.getAskerName().equalsIgnoreCase(player.getName())) {
            printMessage(player, ReaxMessage.HELPOP_ITS_FUCKING_YOURS);
            return true;
        }
        return false;
    }



}
