package ru.alexpshkov.reaxessentials.kits;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.alexpshkov.reaxessentials.ReaxEssentials;
import ru.alexpshkov.reaxessentials.database.entities.KitEntity;
import ru.alexpshkov.reaxessentials.database.entities.ReceivedKitEntity;
import ru.alexpshkov.reaxessentials.database.entities.UserEntity;
import ru.alexpshkov.reaxessentials.service.interfaces.IInitRequired;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KitsManager implements IInitRequired {
    private final ReaxEssentials reaxEssentials;
    private final HashMap<String, KitEntity> kitsInProcessing = new HashMap<>();

    @Override
    public void init() {}


    /**
     * Add async itemStacks to player
     */
    public void addKitToPlayer(Player player, KitEntity kitEntity, UserEntity userEntity) {
        ReceivedKitEntity receivedKit = new ReceivedKitEntity();
        receivedKit.setKitName(kitEntity.getKitName());
        receivedKit.setWhoReceived(userEntity);
        receivedKit.setReceivedTime(new Date().getTime());

        reaxEssentials.getDataBase().addReceivedKit(userEntity, receivedKit).thenAccept(flag -> {
            addSyncItemStacks(player, kitEntity.getContents());
        });
    }

    /**
     * Open GUI for editing kit
     * @param player Editor
     * @param kitTable Kit
     */
    public void openKitEditGUI(Player player, KitEntity kitTable) {
        Inventory inventory = Bukkit.createInventory(null, 54, kitTable.getKitName());
        for (ItemStack content : kitTable.getContents()) {
            inventory.addItem(content);
        }
        player.openInventory(inventory);
    }

    /**
     * Add async itemStacks to player
     */
    public void addSyncItemStacks(Player player, ItemStack... itemStacks) {
        Bukkit.getScheduler().runTask(reaxEssentials, () -> {
            List<ItemStack> itemsToDrop = new ArrayList<>();
            for (ItemStack itemStack : itemStacks) {
                if(itemStack == null) continue;
                if (Arrays.stream(player.getInventory().getStorageContents()).anyMatch(Objects::isNull)) //Any empty slot
                    player.getInventory().addItem(itemStack);
                else itemsToDrop.add(itemStack); //Else drop item near the player
            }
            player.updateInventory();
            dropItems(player.getLocation(), itemsToDrop);
        });
    }

    /**
     * Drop items
     */
    public void dropItems(Location location, List<ItemStack> itemStacks) {
        Bukkit.getScheduler().runTask(reaxEssentials, () ->
                itemStacks.forEach(item -> location.getWorld().dropItem(location, item)));
    }

    /**
     * Save kit to dataBase
     * @param kitTable Kit
     * @return Success or not
     */
    public CompletableFuture<Boolean> saveKit(KitEntity kitTable) {
        return reaxEssentials.getDataBase().saveKitEntity(kitTable);
    }

    /**
     * Save kit to dataBase
     * @param kitName Kit name
     * @param contents Kit contents
     * @return Success or not
     */
    public CompletableFuture<Boolean> saveAsKit(String kitName, ItemStack[] contents) {
        KitEntity kitTable = new KitEntity();
        kitTable.setKitName(kitName);
        kitTable.setContents(contents);
        return saveKit(kitTable);
    }

    /**
     * Save kit to dataBase
     * @param kitName Kit name
     * @param inventory Some inventory
     * @return Success or not
     */
    public CompletableFuture<Boolean> saveAsKit(String kitName, Inventory inventory) {
        List<ItemStack> itemStack = Arrays.stream(inventory.getContents()).filter(Objects::nonNull).collect(Collectors.toList()); //Gets only not null items
        ItemStack[] kitContents = new ItemStack[itemStack.size()];
        itemStack.toArray(kitContents);
        return saveAsKit(kitName, kitContents);
    }


    //Force save all kits.
    public void forceSaveAllKits() {
        kitsInProcessing.forEach((k, v) -> saveKit(v));
    }

    //Add kit to hashmap
    public void addKitToProcessing(String handler, KitEntity kitTable) {
        kitsInProcessing.put(handler, kitTable);
    }

    //Check if is such kit in hashmap
    public boolean isSuchHandlerOfKit(String handler) {
        return kitsInProcessing.containsKey(handler);
    }

    //Gets kit from hashMap
    public KitEntity getKitTableOfHandler(String handler) {
        return kitsInProcessing.getOrDefault(handler, null);
    }

    //Remove kits from hashMap
    public void removeInventoryOfHandler(String handler) {
        kitsInProcessing.remove(handler);
    }
}
