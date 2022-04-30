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

public class PersonalMessageCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public PersonalMessageCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "msg");
        super.setAliases(Arrays.asList("pm", "personalmessage", "m"));
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(CommandSender sender, String alias, String[] args) {
        if (args.length < 2) {
            printMessage(sender, ReaxMessage.INVALID_SYNTAX, alias + " <player> <message>");
            return false;
        }
        Player target = Utils.getOnlinePlayer(args[0]);

        //Check if target not online
        if (target == null) {
            printMessage(sender, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        //Check if player sends message to him
        if (sender.getName().equalsIgnoreCase(target.getName())) {
            printMessage(sender, ReaxMessage.PERSONALMESSAGE_ONLY_OTHERS);
            return false;
        }

        //Combine args into string
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        String message = stringBuilder.toString();

        ReplyMessageCommand.lastCompanion.put(sender.getName(), target.getName());
        ReplyMessageCommand.lastCompanion.put(target.getName(), sender.getName());
        printMessage(sender, ReaxMessage.PERSONALMESSAGE_FORMAT_YOU, target.getName(), message);

        reaxEssentials.getDataBase().getUserEntity(target.getName()).thenAccept(userEntity -> {
            //Check if player ignores you
            IgnoredUser ignoredUser = new IgnoredUser();
            ignoredUser.setIgnoredUserName(sender.getName());
            ignoredUser.setUserEntity(userEntity);

            reaxEssentials.getDataBase().getIgnoredPlayer(ignoredUser).thenAccept(realIgnoredUser -> {
                if (realIgnoredUser != null) return;
                printMessage(target, ReaxMessage.PERSONALMESSAGE_FORMAT, sender.getName(), message);
                reaxEssentials.getSoundsConfig().playSound(target, ReaxSound.PERSONALMESSAGE_RECEIVED);
            });
        });
        return true;
    }


}
