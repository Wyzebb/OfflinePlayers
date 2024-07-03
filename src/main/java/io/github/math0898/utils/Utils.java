package io.github.math0898.utils;

import de.snap20lp.offlineplayers.OfflinePlayers;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Substitutes for the main JavaPlugin class when concerned with utils classes and objects.
 *
 * @author Sugaku
 */
public class Utils {

    /**
     * Accessor method to get the plugin in use.
     *
     * @return The active plugin.
     */
    public static JavaPlugin getPlugin () {
        return OfflinePlayers.getInstance();
    }
}