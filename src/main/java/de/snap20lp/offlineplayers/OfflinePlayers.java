package de.snap20lp.offlineplayers;

import de.snap20lp.offlineplayers.commands.OfflinePlayerCommand;
import de.snap20lp.offlineplayers.depends.APIManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class OfflinePlayers extends JavaPlugin { // todo: Maybe create a config manager. Perhaps even enum options with default values.

    Metrics metrics;
    public static OfflinePlayers getInstance() {
        return getPlugin(OfflinePlayers.class);
    }

    /**
     * Called extra early on in the plugin loading process. Used to register WorldGuard flags.
     */
    public void onLoad () {
        APIManager.getInstance().delegatedOnLoad();
    }

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 19973);
        Bukkit.getConsoleSender().sendMessage("§aOfflinePlayers starting in version " + getDescription().getVersion());
        this.saveDefaultConfig();
        APIManager.getInstance().delegatedOnEnabled();
        Bukkit.getPluginManager().registerEvents(CloneManager.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(new MortalMaker(), this);
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard"))
            Bukkit.getPluginManager().registerEvents(new EventProtector(), this);
        if (getServer().getPluginManager().getPlugin("LibsDisguises") == null || getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            Bukkit.getConsoleSender().sendMessage("§4[OfflinePlayers] ERROR: LibsDisguises is not activated! Please install LibsDisguises and ProtocolLib to use this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
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
        new OfflinePlayerCommand();
    }

    @Override
    public void onDisable() {
        this.metrics.shutdown();
        CloneManager.getInstance().save();
        if (getServer().getPluginManager().getPlugin("LibsDisguises") != null && getServer().getPluginManager().getPlugin("ProtocolLib") != null)
            CloneManager.getInstance().getOfflinePlayerList().forEach(((uuid, offlinePlayer) -> offlinePlayer.despawnClone())); // todo: Consider having CloneManager handle.
    }
}
