package de.snap20lp.offlineplayers;

import de.snap20lp.offlineplayers.events.OfflinePlayerDeathEvent;
import de.snap20lp.offlineplayers.events.OfflinePlayerDespawnEvent;
import de.snap20lp.offlineplayers.events.OfflinePlayerHitEvent;
import de.snap20lp.offlineplayers.events.OfflinePlayerSpawnEvent;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;
import github.scarsz.discordsrv.util.DiscordUtil;
import me.libraryaddict.disguise.events.UndisguiseEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * The CloneManager manages creating, saving, loading, and removing clones. Follows the singleton instance model.
 *
 * @author Sugaku
 */
public class CloneManager implements Listener { // todo: Perhaps refactor events into an event manager.

    /**
     * The active CloneManager instance.
     */
    private static CloneManager singleton = null;

    private final HashMap<UUID, OfflinePlayer> offlinePlayerList = new HashMap<>();

    private final HashMap<Integer, OfflinePlayer> entityOfflinePlayerHashMap = new HashMap<>();

    /**
     * A list of death flavour messages.
     */
    private final List<String> deathFlavours;

    /**
     * A long that stores the latest time clone data was saved in a millis since epoch.
     */
    private long lastCloneDataUpdate = System.currentTimeMillis();

    /**
     * Creates a new CloneManager by loading, if present, persistent clones from the drive.
     */
    public CloneManager () { // todo: Might be cleaner to use a custom constructor.
        deathFlavours = OfflinePlayers.getInstance().getConfig().getStringList("OfflinePlayer.death-flavour-messages");
        FileConfiguration save = new YamlConfiguration();
        try {
            save.load("./plugins/OfflinePlayers/clones.yml");
        } catch (IOException | InvalidConfigurationException exception) {
            OfflinePlayers.getInstance().getLogger().log(Level.WARNING, "Failed to load clones from disk. Hopefully first run: " + exception.getMessage());
            return;
        }
        for (String s : save.getKeys(false)) {
            UUID uuid = UUID.fromString(s);
            int currentSeconds = save.getInt(s + ".current-seconds", 0);
            Location location = save.getLocation(s + ".location", new Location(null, 0, 0,0));
            int playerXp = save.getInt(s + ".player-xp", 0);
            double currentHP = save.getDouble(s + ".hp", 0);
            ArrayList<ItemStack> inventory = new ArrayList<>();
            ConfigurationSection section = save.getConfigurationSection(s + ".inventory");
            if (section != null)
                for (String i : section.getKeys(false))
                    inventory.add(save.getItemStack(s + ".inventory." + i, new ItemStack(Material.AIR, 1)));
            ArrayList<ItemStack> armor = new ArrayList<>();
            section = save.getConfigurationSection(s + ".armor");
            if (section != null)
                for (String i : save.getConfigurationSection(s + ".armor").getKeys(false))
                    armor.add(save.getItemStack(s + ".armor." + i, new ItemStack(Material.AIR, 1)));
            ItemStack mainHand = save.getItemStack(s + ".main-hand", new ItemStack(Material.AIR, 1));
            ItemStack offHand = save.getItemStack(s + ".off-hand", new ItemStack(Material.AIR, 1));
            boolean isDead = save.getBoolean(s + ".is-dead", false);
            OfflinePlayer player = new OfflinePlayer(Bukkit.getOfflinePlayer(uuid), currentSeconds, location, playerXp, currentHP, inventory, armor, mainHand, offHand);
            player.setDead(isDead);
            offlinePlayerList.put(uuid, player);
            entityOfflinePlayerHashMap.put(player.getCloneEntity().getEntityId(), player);
        }
    }

    /**
     * Accessor method for the active CloneManager instance.
     *
     * @return The active CloneManager instance.
     */
    public static CloneManager getInstance () {
        if (singleton == null) singleton = new CloneManager();
        return singleton;
    }

    public HashMap<UUID, OfflinePlayer> getOfflinePlayerList () { // todo: Consider changing.
        return offlinePlayerList;
    }

    public HashMap<Integer, OfflinePlayer> getEntityOfflinePlayerHashMap () { // todo: Consider changing.
        return entityOfflinePlayerHashMap;
    }

    /**
     * Saves all the current clones to the disc asynchronously.
     */
    public void saveAsync () {
        lastCloneDataUpdate = System.currentTimeMillis();
//        Bukkit.getScheduler().runTaskAsynchronously(OfflinePlayers.getInstance(), this::save);
    }

    /**
     * Saves all current clones to the disk.
     */
    public void save () {
        save("./plugins/OfflinePlayers/clones.yml");
    }

    /**
     * Saves all current clones to the disk.
     */
    public void save (String path) { // Todo: Might be cleaner to make each save themselves.
        long dataGrabbed = System.currentTimeMillis();
        FileConfiguration save = new YamlConfiguration();
        for (UUID s : getOfflinePlayerList().keySet()) {
            String uuid = s.toString();
            OfflinePlayer player = getOfflinePlayerList().get(s);
            save.set(uuid + ".current-seconds", player.getCurrentSeconds());
            save.set(uuid + ".location", player.getCloneEntity().getLocation());
            save.set(uuid + ".player-xp", player.getPlayerExp());
            save.set(uuid + ".hp", player.getCurrentHP());

            ArrayList<ItemStack> inventory = player.getSavedInventoryContents();

            if (inventory.size() > 36) {
                inventory = new ArrayList<>();
                while (inventory.size() < 36)
                    inventory.add(player.getSavedInventoryContents().get(inventory.size()));
            }

            for (ItemStack i : inventory)
                if (inventory.indexOf(i) < 36) save.set(uuid + ".inventory." + inventory.indexOf(i), i);

            ArrayList<ItemStack> armor = player.getSavedArmorContents();
            for (ItemStack i : armor)
                save.set(uuid + ".armor." + armor.indexOf(i), i);
            save.set(uuid + ".main-hand", player.getMainHand());
            save.set(uuid + ".off-hand", player.getOffHand());
            save.set(uuid + ".is-dead", player.isDead());
        }
        try {
//            if (lastCloneDataUpdate > dataGrabbed) return;
            save.save(path);
            OfflinePlayers.getInstance().getLogger().log(Level.INFO, "Saved clone data.");
        } catch (IOException ioException) {
            OfflinePlayers.getInstance().getLogger().log(Level.WARNING, "Failed to save clone data:" + ioException.getMessage());
        }
        if (OfflinePlayers.getInstance().isEnabled())
            Bukkit.getScheduler().runTaskLaterAsynchronously(OfflinePlayers.getInstance(), () -> CloneManager.getInstance().save(), 60 * 20 * 10);
    }

    @EventHandler
    public void on(EntityResurrectEvent entityResurrectEvent) {
        if (entityOfflinePlayerHashMap.containsKey(entityResurrectEvent.getEntity().getEntityId())) {
            if (entityOfflinePlayerHashMap.get(entityResurrectEvent.getEntity().getEntityId()) == null) {
                return;
            }
            OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(entityResurrectEvent.getEntity().getEntityId());
            if (offlinePlayer.getMainHand().getType() != Material.TOTEM_OF_UNDYING && offlinePlayer.getOffHand().getType() != Material.TOTEM_OF_UNDYING) {
                entityResurrectEvent.setCancelled(true);
                return;
            }

            entityResurrectEvent.getEntity().getWorld().playSound(entityResurrectEvent.getEntity().getLocation(), Sound.ITEM_TOTEM_USE, 100, 1);

            if (offlinePlayer.getMainHand().getType() == Material.TOTEM_OF_UNDYING)
                offlinePlayer.getMainHand().setType(Material.AIR);
            else if (offlinePlayer.getOffHand().getType() == Material.TOTEM_OF_UNDYING)
                offlinePlayer.getOffHand().setType(Material.AIR);

            OfflinePlayer newOfflinePlayer = new OfflinePlayer(offlinePlayer.getOfflinePlayer(), offlinePlayer.getCurrentSeconds(), offlinePlayer.getCloneEntity().getLocation(), offlinePlayer.getPlayerExp(), offlinePlayer.getCurrentHP(), offlinePlayer.getSavedInventoryContents(), offlinePlayer.getSavedArmorContents(), offlinePlayer.getMainHand(), offlinePlayer.getOffHand());


            offlinePlayerList.remove(offlinePlayer.getOfflinePlayer().getUniqueId());
            entityOfflinePlayerHashMap.remove(entityResurrectEvent.getEntity().getEntityId());
            offlinePlayer.despawnClone();


            offlinePlayerList.put(newOfflinePlayer.getOfflinePlayer().getUniqueId(), newOfflinePlayer);
            entityOfflinePlayerHashMap.put(newOfflinePlayer.getCloneEntity().getEntityId(), newOfflinePlayer);
            saveAsync();
        }
    }




    @EventHandler
    public void on(PlayerJoinEvent playerJoinEvent) {

        if (offlinePlayerList.containsKey(playerJoinEvent.getPlayer().getUniqueId())) {
            if (offlinePlayerList.get(playerJoinEvent.getPlayer().getUniqueId()) == null) {
                return;
            }
            OfflinePlayer clone = offlinePlayerList.get(playerJoinEvent.getPlayer().getUniqueId());


            OfflinePlayerDespawnEvent offlinePlayerDespawnEvent = new OfflinePlayerDespawnEvent(clone);
            Bukkit.getPluginManager().callEvent(offlinePlayerDespawnEvent);
            Player player = playerJoinEvent.getPlayer();
            player.teleport(clone.getCloneEntity().getLocation());
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.addPotionEffects(clone.getCloneEntity().getActivePotionEffects());

//            player.getInventory().setItemInMainHand(clone.getCloneEntity().getEquipment().getItemInMainHand());
            player.getInventory().setItemInOffHand(clone.getCloneEntity().getEquipment().getItemInOffHand());
            if (clone.getCloneEntity().hasPotionEffect(PotionEffectType.SLOW) && clone.isCloneHasAI()) {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
            if (clone.isDead()) {
                if (!OfflinePlayers.getInstance().getConfig().getBoolean("OfflinePlayer.cloneKeepItems")) {
                    if (player.getEquipment() != null) {
                        player.getEquipment().clear();
                    }
                }
                player.setLevel(0);
                player.setExp(0.0f);
                player.setHealth(0.0d);
            } else {
                player.setFireTicks(clone.getCloneEntity().getFireTicks());
                AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                double maxHealth = 20.0;
                if (attributeInstance != null)
                    maxHealth = attributeInstance.getValue();
                player.setHealth(Math.min(clone.getCloneEntity().getHealth(), maxHealth));
                clone.getAddedItems().forEach(itemStack -> {
                    if (itemStack != null) {
                        player.getInventory().addItem(itemStack);
                    }
                });
            }
            clone.cancelDespawnTask();
            clone.cancelUpdateTask();
            clone.despawnClone();

            offlinePlayerList.remove(playerJoinEvent.getPlayer().getUniqueId());
            saveAsync();
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent playerQuitEvent) {
        Player quitPlayer = playerQuitEvent.getPlayer();
        if (quitPlayer.getHealth() <= 0) return;
        FileConfiguration config = OfflinePlayers.getInstance().getConfig();
        if (config.getStringList("OfflinePlayer.worldBlacklist").contains(quitPlayer.getWorld().getName())) return;
        if (config.getStringList("OfflinePlayer.game-modeBlacklist").contains(quitPlayer.getGameMode().name())) return;
        if (config.getBoolean("OfflinePlayer.permissions.enabled") && config.getString("OfflinePlayer.permissions.permission") != null)
            if (!quitPlayer.hasPermission(config.getString("OfflinePlayer.permissions.permission", "offlineplayer.clone")))
                return;

        OfflinePlayer offlinePlayer = new OfflinePlayer(quitPlayer,
                new ArrayList<>(Arrays.asList(quitPlayer.getInventory().getContents())),
                quitPlayer.getEquipment() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(quitPlayer.getInventory().getArmorContents())),
                quitPlayer.getEquipment().getItemInMainHand(),
                quitPlayer.getEquipment().getItemInOffHand());
        OfflinePlayerSpawnEvent offlinePlayerSpawnEvent = new OfflinePlayerSpawnEvent(offlinePlayer,
                quitPlayer.getLocation(),
                quitPlayer.getInventory().getContents(),
                quitPlayer.getEquipment().getArmorContents(),
                quitPlayer.getEquipment().getItemInOffHand());
        Bukkit.getPluginManager().callEvent(offlinePlayerSpawnEvent);
        quitPlayer.teleport(offlinePlayerSpawnEvent.getLocation());
        offlinePlayer = new OfflinePlayer(quitPlayer,
                new ArrayList<>(Arrays.asList(offlinePlayerSpawnEvent.getInventory())),
                offlinePlayerSpawnEvent.getArmor() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(offlinePlayerSpawnEvent.getArmor())),
                offlinePlayerSpawnEvent.getInventory()[0]/* offlinePlayerSpawnEvent.getMainHand() */, // todo: Figure out a way to pull this from MultiverseInventories without duplicating items.
                offlinePlayerSpawnEvent.getOffHand());
        offlinePlayer.setSpawnLocation(offlinePlayerSpawnEvent.getLocation());
        offlinePlayer.spawnClone();
        offlinePlayer.despawnClone();
        offlinePlayer.startTimers();
        offlinePlayerList.put(quitPlayer.getUniqueId(), offlinePlayer);
        entityOfflinePlayerHashMap.put(offlinePlayer.getCloneEntity().getEntityId(), offlinePlayer);
        saveAsync();
    }

    @EventHandler // todo: Figure out why this method existed. It seems relatively vestigial to me currently.
    public void on(UndisguiseEvent undisguiseEvent) {
        if(entityOfflinePlayerHashMap.containsKey(undisguiseEvent.getEntity().getEntityId())) {
            OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(undisguiseEvent.getEntity().getEntityId());
            offlinePlayer.replaceCloneStats((LivingEntity) undisguiseEvent.getEntity());
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        if (OfflinePlayers.getInstance().getConfig().getBoolean("OfflinePlayer.cloneItemPickup")) {
            event.getItemDrop().getNearbyEntities(6, 6, 6).forEach(entity -> {
                if (entityOfflinePlayerHashMap.containsKey(entity.getEntityId())) {
                    OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(entity.getEntityId());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(OfflinePlayers.getInstance(), () -> {

                        if(!event.getItemDrop().getNearbyEntities(1, 1, 1).contains(offlinePlayer.getCloneEntity())){
                            return;
                        }

                        offlinePlayer.getAddedItems().add(event.getItemDrop().getItemStack());
                        offlinePlayer.getCloneEntity().getWorld().playSound(offlinePlayer.getCloneEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 100,2);
                        event.getItemDrop().remove();
                        saveAsync();
                    }, 50L);
                }
            });
        }
    }

    @EventHandler
    public void on(EntityCombustEvent event) {
        if (entityOfflinePlayerHashMap.containsKey(event.getEntity().getEntityId())) {
            OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(event.getEntity().getEntityId());
            if (offlinePlayer.getCloneEntity().getLocation().getBlock().getType() != Material.LAVA && offlinePlayer.getCloneEntity().getLocation().getBlock().getType() != Material.FIRE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(EntityTargetEvent entityTargetEvent) {
        if (entityTargetEvent.getTarget() == null)
            return;
        if (entityOfflinePlayerHashMap.containsKey(entityTargetEvent.getTarget().getEntityId())) {
            entityTargetEvent.setCancelled(true);
            entityTargetEvent.setTarget(null);
        }
    }

    private String humanReadableLocation (Location locale) {
        DecimalFormat format = new DecimalFormat("#.0");
        return locale.getWorld().getName() + " (" + format.format(locale.getX()) + ", " + format.format(locale.getY()) + ", " + format.format(locale.getZ()) + ")";
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        OfflinePlayer offlinePlayer = null;
        for (OfflinePlayer value : offlinePlayerList.values()) {
            if(value.getCloneEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
                offlinePlayer = value;
                break;
            }
        }
        if (offlinePlayer != null) {
            OfflinePlayerDeathEvent offlinePlayerDeathEvent = new OfflinePlayerDeathEvent(offlinePlayer);
            Bukkit.getPluginManager().callEvent(offlinePlayerDeathEvent);

            event.getDrops().clear();

            if (!OfflinePlayers.getInstance().getConfig().getBoolean("OfflinePlayer.cloneKeepItems")) {

                for (ItemStack inventoryContent : offlinePlayer.getSavedInventoryContents()) {
                    if (inventoryContent != null) {
                        event.getDrops().add(inventoryContent);
                    }
                }

                offlinePlayer.getAddedItems().forEach(itemStack -> {
                    if (itemStack != null) {
                        event.getDrops().add(itemStack);
                    }
                });

            }
            Player killer = event.getEntity().getKiller();
            AccountLinkManager manager = DiscordSRV.getPlugin().getAccountLinkManager();
            String discordId = manager.getDiscordId(offlinePlayer.getOfflinePlayer().getUniqueId());
            Member guildMember = null;
            if (discordId != null) {
                Guild mainGuild = DiscordSRV.getPlugin().getMainGuild();
                guildMember = mainGuild.getMemberById(discordId);
            }
            if (killer != null) {
                String personalDeathMessage = OfflinePlayers.getInstance().getConfig().getString("OfflinePlayer.death-messages.personal", "You ({killed}) were just killed by {killer}!");
                personalDeathMessage = personalDeathMessage.replace("{killed}", offlinePlayer.getOfflinePlayer().getName());
                personalDeathMessage = personalDeathMessage.replace("{killer}", killer.getName());

                String deathMessage = OfflinePlayers.getInstance().getConfig().getString("OfflinePlayer.death-messages.general", "Offline Player {killed} was killed by {killer}!");
                deathMessage = deathMessage.replace("{killed}", offlinePlayer.getOfflinePlayer().getName());
                deathMessage = deathMessage.replace("{killer}", killer.getName());

                Bukkit.broadcastMessage(ChatColor.RED + deathMessage);
                if (guildMember != null)
                    DiscordUtil.privateMessage(guildMember.getUser(), personalDeathMessage);
            } else {
                String personalDeathMessage = OfflinePlayers.getInstance().getConfig().getString("OfflinePlayer.death-messages.personal-loc", "You ({killed}) just died at {loc}!");
                personalDeathMessage = personalDeathMessage.replace("{killed}", offlinePlayer.getOfflinePlayer().getName());
                personalDeathMessage = personalDeathMessage.replace("{loc}", humanReadableLocation(event.getEntity().getLocation()));

                String deathMessage = OfflinePlayers.getInstance().getConfig().getString("OfflinePlayer.death-messages.general-loc", "Offline Player {killed} died at {loc}!");
                deathMessage = deathMessage.replace("{killed}", offlinePlayer.getOfflinePlayer().getName());
                deathMessage = deathMessage.replace("{loc}", humanReadableLocation(event.getEntity().getLocation()));

                Bukkit.broadcastMessage(ChatColor.RED + deathMessage);
                if (guildMember != null)
                    DiscordUtil.privateMessage(guildMember.getUser(), personalDeathMessage);
            }
            event.setDroppedExp(offlinePlayer.getPlayerExp());
            offlinePlayer.setHidden(true);
            offlinePlayer.despawnClone();
            offlinePlayer.setDead(true);
            final OfflinePlayer staticOffline = offlinePlayer;
            Bukkit.getScheduler().runTask(OfflinePlayers.getInstance(),
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear " + staticOffline.getOfflinePlayer().getName()));
            saveAsync();
        }
    }

    @EventHandler
    public void on (EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.valueOf(OfflinePlayers.getInstance().getConfig().getString("OfflinePlayer.cloneRawEntity")) && entityOfflinePlayerHashMap.containsKey(event.getEntity().getEntityId())) {
            OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(event.getEntity().getEntityId());
            if (!offlinePlayer.isCloneIsHittable()) {
                event.setCancelled(true);
                return;
            }
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_HURT, 100, 1);
        }
    }

    @EventHandler
    public void on (EntityDamageByEntityEvent event) {
        if (entityOfflinePlayerHashMap.containsKey(event.getEntity().getEntityId()) && event.getDamager() instanceof Player damager) {
            OfflinePlayer offlinePlayer = entityOfflinePlayerHashMap.get(event.getEntity().getEntityId());
            if (!offlinePlayer.isCloneIsHittable()) {
                event.setCancelled(true);
            } else {
                OfflinePlayerHitEvent offlinePlayerHitEvent = new OfflinePlayerHitEvent(offlinePlayer, damager);
                Bukkit.getPluginManager().callEvent(offlinePlayerHitEvent);
                damager.playSound(damager.getLocation(), Sound.ENTITY_PLAYER_HURT, 100, 1);
            }
        }
    }
}
