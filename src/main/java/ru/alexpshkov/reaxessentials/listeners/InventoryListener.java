package ru.alexpshkov.reaxessentials.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.kits.KitsManager;
import ru.alexpshkov.reaxessentials.service.enums.ReaxMessage;
import ru.alexpshkov.reaxessentials.service.interfaces.index.IReaxListener;

@RequiredArgsConstructor
public class InventoryListener implements IReaxListener {
    private final ReaxEssentials reaxEssentials;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String handler = event.getPlayer().getName();
        KitsManager kitsManager = reaxEssentials.getKitsManager();
        Inventory kitInventory = event.getInventory();

        if (!kitsManager.isSuchHandlerOfKit(handler)) return; //Check if is such handler
        if (!kitsManager.getKitTableOfHandler(handler).getKitName().equalsIgnoreCase(kitInventory.getTitle())) return; //Check inventories titles

        kitsManager.saveAsKit(kitInventory.getTitle(), kitInventory).thenAccept(flag -> { //Send message
            ReaxMessage messageKey = flag ? ReaxMessage.KIT_EDITED : ReaxMessage.SAVE_EXCEPTION;
            String message = reaxEssentials.getMessagesConfig().getMessage(messageKey, kitInventory.getTitle());
            kitsManager.removeInventoryOfHandler(handler);
            event.getPlayer().sendMessage(message);
        });
    }
}
