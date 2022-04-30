package ru.alexpshkov.reaxessentials.commands.implementation.base;

import org.bukkit.entity.Player;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.commands.AbstractCommand;
import ru.alexpshkov.reaxessentials.service.Utils;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.enums.ReaxSound;

import java.util.Collections;

public class HealCommand extends AbstractCommand {
    private final ReaxEssentials reaxEssentials;

    /**
     * Command configuration
     */
    public HealCommand(ReaxEssentials reaxEssentials) {
        super(reaxEssentials, "heal");
        super.setAliases(Collections.singletonList("healme"));
        super.setOnlyForPlayers(true);
        this.reaxEssentials = reaxEssentials;
    }

    @Override
    public boolean handleCommand(Player player, String alias, String[] args) {
        if (!hasPermission(player, "heal")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        Player target = Utils.getOnlinePlayer(args.length >= 1 ? args[0] : player.getName());

        if (target == null) {
            printMessage(player, ReaxMessage.USER_NOTFOUND, args[0]);
            return false;
        }

        if (!player.getName().equalsIgnoreCase(target.getName()) && !hasPermission(player, "heal.others")) {
            printMessage(player, ReaxMessage.NO_PERM);
            return false;
        }

        if (args.length >= 1 && !target.getName().equals(player.getName())) {
            printMessage(player, ReaxMessage.HEAL, target.getName());
            printMessage(target, ReaxMessage.HEAL_YOU, target.getName());
        } else printMessage(target, ReaxMessage.HEAL_YOU, target.getName());

        playSound(target, ReaxSound.HEAL_YOU);
        target.setHealth(20);
        target.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        return true;
    }


}
