package de.snap20lp.offlineplayers.depends.integrations;

import com.ranull.graves.Graves;
import com.ranull.graves.event.GraveCreateEvent;
import com.ranull.graves.manager.DataManager;
import com.ranull.graves.manager.GraveManager;
import com.ranull.graves.type.Grave;
import de.snap20lp.offlineplayers.OfflinePlayers;
import de.snap20lp.offlineplayers.events.OfflinePlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

/**
 * The RanullGravesIntegration exists to let clones spawn graves when they die using Ranull's Graves plugin.
 *
 * @author Sugaku
 */
public class RanullGravesIntegration implements Listener {

    /**
     * The GraveManager instance we need to create new graves.
     */
    private final GraveManager graveManager;

    /**
     * Attempts to create the RanullGravesIntegration module.
     */
    public RanullGravesIntegration () {
        if (Bukkit.getPluginManager().isPluginEnabled("GravesX")) {
            graveManager = ((Graves) Bukkit.getPluginManager().getPlugin("GravesX")).getGraveManager();
            OfflinePlayers.getInstance().getLogger().log(Level.INFO, "We have located Ranull's Graves. OfflinePlayers will spawn graves on death.");
        } else {
            graveManager = null;
            throw new NoClassDefFoundError();
        }
    }

    /**
     * Called whenever a clone dies.
     *
     * @param event The clone death event.
     */
    @EventHandler
    public void onDeath (OfflinePlayerDeathEvent event) {
        if (graveManager == null) return;
        List<ItemStack> drops = new ArrayList<>();
        for (ItemStack i : event.getOfflinePlayer().getAddedItems())
            drops.add(i);
        for (ItemStack i : event.getOfflinePlayer().getSavedInventoryContents())
            drops.add(i);
        List<ItemStack> swap = new ArrayList<>();
        for (ItemStack i : drops)
            if (i != null)
                if (!graveManager.shouldIgnoreItemStack(i, event.getOfflinePlayer().getCloneEntity(), new ArrayList<>()))
                    swap.add(i);
        if (!swap.isEmpty()) {
            DataManager dataManager = ((Graves) Bukkit.getPluginManager().getPlugin("GravesX")).getDataManager();
            Grave grave = graveManager.createGrave(event.getOfflinePlayer().getCloneEntity(), swap);
            grave.setOwnerUUID(event.getOfflinePlayer().getOfflinePlayer().getUniqueId());
            grave.setOwnerName(event.getOfflinePlayer().getOfflinePlayer().getPlayerProfile().getName());
            grave.setOwnerNameDisplay(event.getOfflinePlayer().getOfflinePlayer().getPlayerProfile().getName());
            grave.setTimeCreation(System.currentTimeMillis());
            grave.setTimeAlive(OfflinePlayers.getInstance().getConfig().getLong("OfflinePlayer.graves.duration") * 1000);
            grave.setLocationDeath(event.getOfflinePlayer().getCloneEntity().getLocation());
            grave.setPermissionList(new ArrayList<>());
            Player killer = event.getOfflinePlayer().getCloneEntity().getKiller();
            if (killer != null) {
                grave.setKillerName(killer.getName());
                grave.setKillerType(EntityType.PLAYER);
                grave.setKillerUUID(killer.getUniqueId());
                grave.setKillerNameDisplay(killer.getDisplayName());
            }
            grave.setOwnerType(EntityType.PLAYER);
            grave.setProtection(true);
            grave.setTimeProtection(OfflinePlayers.getInstance().getConfig().getLong("OfflinePlayer.graves.protection-duration") * 1000);
            grave.setYaw(0.0f);
            grave.setPitch(grave.getLocationDeath().getPitch());
            Map<EquipmentSlot, ItemStack> equipmentMap = new HashMap<>();
            grave.setEquipmentMap(equipmentMap);
            GraveCreateEvent createGrave = new GraveCreateEvent(event.getOfflinePlayer().getCloneEntity(), grave);
            Bukkit.getPluginManager().callEvent(createGrave);
            graveManager.placeGrave(event.getOfflinePlayer().getCloneEntity().getLocation(), grave);
            dataManager.addGrave(grave);
        }
        event.getOfflinePlayer().getSavedInventoryContents().clear();
        event.getOfflinePlayer().getAddedItems().clear();
        event.getOfflinePlayer().getSavedArmorContents().clear();
    }
}
