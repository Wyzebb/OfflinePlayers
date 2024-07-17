package de.snap20lp.offlineplayers.depends.integrations;

import de.jeff_media.angelchest.AngelChestBuilder;
import de.jeff_media.angelchest.AngelChestPlugin;
import de.snap20lp.offlineplayers.OfflinePlayers;
import de.snap20lp.offlineplayers.events.OfflinePlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

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
        builder.storageInv(storage.toArray(new ItemStack[0]));
        List<ItemStack> armor = event.getOfflinePlayer().getSavedArmorContents();
        builder.armorInv(armor.toArray(new ItemStack[0]));
    }
}
