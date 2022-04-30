package ru.alexpshkov.reaxessentials.commands.implementation.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.database.entities.IgnoredUser;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Arrays;
import java.util.HashMap;

public class ReplyMessageCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;
    public static HashMap<String, String> lastCompanion = new HashMap<>();

    /**
     * Command configuration
     */
    public ReplyMessageCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "reply");
        super.setAliases(Arrays.asList("r", "replymessage"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender sender, String alias, String[] args) {
        String stringTarget = lastCompanion.getOrDefault(sender.getName(), null);

        if (stringTarget == null) {
            printMessage(sender, ReaxMessage.REPLYMESSAGE_TARGET_NOT_FOUND);
            return false;
        }
        if (args.length < 1) {
            printMessage(sender, ReaxMessage.INVALID_SYNTAX, alias + " <message>");
            return false;
        }
        Player target = Utils.getOnlinePlayer(stringTarget);

        //Check if target not online
        if (target == null) {
            printMessage(sender, ReaxMessage.USER_NOTFOUND, stringTarget);
            return false;
        }

        //Combine args into string
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }
        String message = stringBuilder.toString();

        lastCompanion.put(target.getName(), sender.getName());
        printMessage(sender, ReaxMessage.PERSONALMESSAGE_FORMAT_YOU, target.getName(), message);

        reaxEssentials.getDataBase().getUserEntity(target.getName()).thenAccept(userEntity -> {
            //Check if player ignores you
            IgnoredUser ignoredUser = new IgnoredUser();
            ignoredUser.setIgnoredUserName(sender.getName());
            ignoredUser.setUserEntity(userEntity);

            reaxEssentials.getDataBase().getIgnoredPlayer(ignoredUser).thenAccept(realIgnoredUser -> {
                if (realIgnoredUser != null) return;
                printMessage(target, ReaxMessage.PERSONALMESSAGE_FORMAT, sender.getName(), message);
                playSound(target, ReaxSound.PERSONALMESSAGE_RECEIVED);
            });
        });
        return true;
    }


}
