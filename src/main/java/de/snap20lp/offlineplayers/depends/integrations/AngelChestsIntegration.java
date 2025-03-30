package de.snap20lp.offlineplayers.depends.integrations;

import de.jeff_media.angelchest.AngelChestBuilder;
import de.jeff_media.angelchest.AngelChestPlugin;
import de.jeff_media.angelchest.DeathReason;
import de.snap20lp.offlineplayers.OfflinePlayers;
import de.snap20lp.offlineplayers.events.OfflinePlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The AngelChest Integration spawns graves when OfflinePlayers die.
 *
 * @author Sugaku
 */
public class AngelChestsIntegration implements Listener {

    /**
     * The AngelChest plugin instance being used during runtime.
     */
    AngelChestPlugin angelChestPlugin;

    /**
     * Attempts to create the RanullGravesIntegration module.
     */
    public AngelChestsIntegration () {
        if (Bukkit.getPluginManager().isPluginEnabled("AngelChest")) {
            angelChestPlugin = (AngelChestPlugin) Bukkit.getServer().getPluginManager().getPlugin("AngelChest");
            OfflinePlayers.getInstance().getLogger().log(Level.INFO, "We have located mfnalex's AngelChest. OfflinePlayers will spawn graves on death.");
        } else {
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
        OfflinePlayer bukkitOffline = event.getOfflinePlayer().getOfflinePlayer();
        AngelChestBuilder builder = new AngelChestBuilder(bukkitOffline.getUniqueId(),
                bukkitOffline.getName(),
                event.getOfflinePlayer().getCloneEntity().getLocation().getBlock());

        List<ItemStack> storage = event.getOfflinePlayer().getSavedInventoryContents();
        storage.addAll(event.getOfflinePlayer().getAddedItems());

        if (storage.isEmpty()) {
            event.getOfflinePlayer().getSavedInventoryContents().clear();
            event.getOfflinePlayer().getSavedArmorContents().clear();
            event.getOfflinePlayer().getAddedItems().clear();
            return;
        }

        if (storage.size() > 36) {
            storage = new ArrayList<>();
            while (storage.size() < 36)
                storage.add(event.getOfflinePlayer().getSavedInventoryContents().get(storage.size()));
        }

        while (storage.size() < 36)
            storage.add(new ItemStack(Material.AIR));
        builder.storageInv(storage.toArray(new ItemStack[0]));
        List<ItemStack> armor = event.getOfflinePlayer().getSavedArmorContents();
        builder.armorInv(armor.toArray(new ItemStack[4]));
        builder.secondsLeft(OfflinePlayers.getInstance().getConfig().getInt("OfflinePlayer.graves.duration"));
        builder.unlockIn(OfflinePlayers.getInstance().getConfig().getInt("OfflinePlayer.graves.protection-duration"));
        builder.isProtected(true);
        Player killer = event.getOfflinePlayer().getCloneEntity().getKiller();
        String killerName = "Environment";
        if (killer != null)
            killerName = killer.getName();
        builder.deathReason(new DeathReason(EntityDamageEvent.DamageCause.ENTITY_ATTACK, killerName));
        builder.ownerName(bukkitOffline.getName());
        builder.experience(event.getOfflinePlayer().getPlayerExp());
        event.getOfflinePlayer().getSavedInventoryContents().clear();
        event.getOfflinePlayer().getSavedArmorContents().clear();
        event.getOfflinePlayer().getAddedItems().clear();
        angelChestPlugin.createAngelChest(builder);
    }
}
