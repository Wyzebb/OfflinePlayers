package de.snap20lp.offlineplayers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class OfflinePlayers extends JavaPlugin implements Listener { // todo: Maybe create a config manager. Perhaps even enum options with default values.

    private final double version = 3.0;

    Metrics metrics;
    public static OfflinePlayers getInstance() {
        return getPlugin(OfflinePlayers.class);
    }

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 19973);

        Bukkit.getConsoleSender().sendMessage("§aOfflinePlayers starting in version " + version);
        if (getServer().getPluginManager().getPlugin("LibsDisguises") == null || getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] ERROR: LibsDisguises is not activated! Please install LibsDisguises and ProtocolLib to use this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.saveDefaultConfig();
        try {
            EntityType.valueOf(getConfig().getString("OfflinePlayer.cloneRawEntity"));
            if(OfflinePlayers.getInstance().getConfig().getBoolean("OfflinePlayer.useBlockEntity")) {
                try {
                BlockData block = Material.valueOf(getConfig().getString("OfflinePlayer.cloneEntity")).createBlockData().getMaterial().createBlockData();
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] ERROR: The cloneEntity in the config.yml is not a valid Block Material!");
                    Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] Since you have 'useBlockEntity' enabled the cloneEntity needs to be an Block Material");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            } else {
                EntityType.valueOf(getConfig().getString("OfflinePlayer.cloneEntity"));
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] ERROR: The cloneEntities in the config.yml are not a valid EntityType!");
            Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] Please change the cloneEntities in the config.yml to a valid EntityType!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        CloneManager.getInstance();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.metrics.shutdown();
        CloneManager.getInstance().save();
        if (getServer().getPluginManager().getPlugin("LibsDisguises") != null && getServer().getPluginManager().getPlugin("ProtocolLib") != null)
            CloneManager.getInstance().getOfflinePlayerList().forEach(((uuid, offlinePlayer) -> offlinePlayer.despawnClone())); // todo: Consider having CloneManager handle.
    }
}
