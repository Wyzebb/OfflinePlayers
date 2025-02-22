package de.snap20lp.offlineplayers.depends.integrations;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.*;

public class SpoofedPlayer implements LivingEntity {

    private final String username;
    private final UUID uuid;
    private final Location location;
    private final Entity fallback;

    public SpoofedPlayer (String username, UUID uuid, Location location) {
        this(username, uuid, location, null);
    }

    public SpoofedPlayer (String username, UUID uuid, Location location, Entity fallback) {
        this.username = username;
        this.uuid = uuid;
        this.location = location;
        this.fallback = fallback;
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @return height of the living entity's eyes above its location
     */
    @Override
    public double getEyeHeight() {
        return 0;
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @param ignorePose if set to true, the effects of pose changes, eg
     *                   sneaking and gliding will be ignored
     * @return height of the living entity's eyes above its location
     */
    @Override
    public double getEyeHeight(boolean ignorePose) {
        return 0;
    }

    /**
     * Get a Location detailing the current eye position of the living entity.
     *
     * @return a location at the eyes of the living entity
     */
    @NotNull
    @Override
    public Location getEyeLocation() {
        return null;
    }

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive. This method considers all blocks as 1x1x1 in size.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     * sight
     */
    @NotNull
    @Override
    public List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
        return List.of();
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This method considers all blocks as 1x1x1 in size. To take exact block
     * collision shapes into account, see {@link #getTargetBlockExact(int,
     * FluidCollisionMode)}.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *                    by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     */
    @NotNull
    @Override
    public Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
        return null;
    }

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list. This method
     * considers all blocks as 1x1x1 in size.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *                    null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *                    further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     * line of sight
     */
    @NotNull
    @Override
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
        return List.of();
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return block that the living entity has targeted
     * @see #getTargetBlockExact(int, FluidCollisionMode)
     */
    @Nullable
    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return null;
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance        the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return block that the living entity has targeted
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Nullable
    @Override
    public Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return information on the targeted block, or <code>null</code> if there
     * is no targeted block in range
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return null;
    }

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance        the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return information on the targeted block, or <code>null</code> if there
     * is no targeted block in range
     * @see World#rayTraceBlocks(Location, Vector, double, FluidCollisionMode)
     */
    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    /**
     * Returns the amount of air that the living entity has remaining, in
     * ticks.
     *
     * @return amount of air remaining
     */
    @Override
    public int getRemainingAir() {
        return 0;
    }

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     *
     * @param ticks amount of air remaining
     */
    @Override
    public void setRemainingAir(int ticks) {

    }

    /**
     * Returns the maximum amount of air the living entity can have, in ticks.
     *
     * @return maximum amount of air
     */
    @Override
    public int getMaximumAir() {
        return 0;
    }

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     *
     * @param ticks maximum amount of air
     */
    @Override
    public void setMaximumAir(int ticks) {

    }

    /**
     * Gets the time in ticks until the next arrow leaves the entity's body.
     *
     * @return ticks until arrow leaves
     */
    @Override
    public int getArrowCooldown() {
        return 0;
    }

    /**
     * Sets the time in ticks until the next arrow leaves the entity's body.
     *
     * @param ticks time until arrow leaves
     */
    @Override
    public void setArrowCooldown(int ticks) {

    }

    /**
     * Gets the amount of arrows in an entity's body.
     *
     * @return amount of arrows in body
     */
    @Override
    public int getArrowsInBody() {
        return 0;
    }

    /**
     * Set the amount of arrows in the entity's body.
     *
     * @param count amount of arrows in entity's body
     */
    @Override
    public void setArrowsInBody(int count) {

    }

    /**
     * Returns the living entity's current maximum no damage ticks.
     * <p>
     * This is the maximum duration in which the living entity will not take
     * damage.
     *
     * @return maximum no damage ticks
     */
    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    /**
     * Sets the living entity's current maximum no damage ticks.
     *
     * @param ticks maximum amount of no damage ticks
     */
    @Override
    public void setMaximumNoDamageTicks(int ticks) {

    }

    /**
     * Returns the living entity's last damage taken in the current no damage
     * ticks time.
     * <p>
     * Only damage higher than this amount will further damage the living
     * entity.
     *
     * @return damage taken since the last no damage ticks time period
     */
    @Override
    public double getLastDamage() {
        return 0;
    }

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     *
     * @param damage amount of damage
     */
    @Override
    public void setLastDamage(double damage) {

    }

    /**
     * Returns the living entity's current no damage ticks.
     *
     * @return amount of no damage ticks
     */
    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    /**
     * Sets the living entity's current no damage ticks.
     *
     * @param ticks amount of no damage ticks
     */
    @Override
    public void setNoDamageTicks(int ticks) {

    }

    /**
     * Gets the player identified as the killer of the living entity.
     * <p>
     * May be null.
     *
     * @return killer player, or null if none found
     */
    @Nullable
    @Override
    public Player getKiller() {
        return null;
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     *
     * @param effect PotionEffect to be added
     * @return whether the effect could be added
     */
    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect) {
        return false;
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Only one potion effect can be present for a given {@link
     * PotionEffectType}.
     *
     * @param effect PotionEffect to be added
     * @param force  whether conflicting effects should be removed
     * @return whether the effect could be added
     * @deprecated no need to force since multiple effects of the same type are
     * now supported.
     */
    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
        return false;
    }

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living
     * entity.
     *
     * @param effects the effects to add
     * @return whether all of the effects could be added
     */
    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
        return false;
    }

    /**
     * Returns whether the living entity already has an existing effect of
     * the given {@link PotionEffectType} applied to it.
     *
     * @param type the potion type to check
     * @return whether the living entity has this potion effect active on them
     */
    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType type) {
        return false;
    }

    /**
     * Returns the active {@link PotionEffect} of the specified type.
     * <p>
     * If the effect is not present on the entity then null will be returned.
     *
     * @param type the potion type to check
     * @return the effect active on this entity, or null if not active.
     */
    @Nullable
    @Override
    public PotionEffect getPotionEffect(@NotNull PotionEffectType type) {
        return null;
    }

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     *
     * @param type the potion type to remove
     */
    @Override
    public void removePotionEffect(@NotNull PotionEffectType type) {

    }

    /**
     * Returns all currently active {@link PotionEffect}s on the living
     * entity.
     *
     * @return a collection of {@link PotionEffect}s
     */
    @NotNull
    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return List.of();
    }

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p>
     * This uses the same algorithm that hostile mobs use to find the closest
     * player.
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    @Override
    public boolean hasLineOfSight(@NotNull Entity other) {
        return false;
    }

    /**
     * Returns if the living entity despawns when away from players or not.
     * <p>
     * By default, animals are not removed while other mobs are.
     *
     * @return true if the living entity is removed when away from players
     */
    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    /**
     * Sets whether or not the living entity despawns when away from players
     * or not.
     *
     * @param remove the removal status
     */
    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    /**
     * Gets the inventory with the equipment worn by the living entity.
     *
     * @return the living entity's inventory
     */
    @Nullable
    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    /**
     * Sets whether or not the living entity can pick up items.
     *
     * @param pickup whether or not the living entity can pick up items
     */
    @Override
    public void setCanPickupItems(boolean pickup) {

    }

    /**
     * Gets if the living entity can pick up items.
     *
     * @return whether or not the living entity can pick up items
     */
    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    /**
     * Returns whether the entity is currently leashed.
     *
     * @return whether the entity is leashed
     */
    @Override
    public boolean isLeashed() {
        return false;
    }

    /**
     * Gets the entity that is currently leading this entity.
     *
     * @return the entity holding the leash
     * @throws IllegalStateException if not currently leashed
     */
    @NotNull
    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p>
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
     * Non-living entities excluding leashes will not persist as leash
     * holders.
     *
     * @param holder the entity to leash this entity to, or null to unleash
     * @return whether the operation was successful
     */
    @Override
    public boolean setLeashHolder(@Nullable Entity holder) {
        return false;
    }

    /**
     * Checks to see if an entity is gliding, such as using an Elytra.
     *
     * @return True if this entity is gliding.
     */
    @Override
    public boolean isGliding() {
        return false;
    }

    /**
     * Makes entity start or stop gliding. This will work even if an Elytra
     * is not equipped, but will be reverted by the server immediately after
     * unless an event-cancelling mechanism is put in place.
     *
     * @param gliding True if the entity is gliding.
     */
    @Override
    public void setGliding(boolean gliding) {

    }

    /**
     * Checks to see if an entity is swimming.
     *
     * @return True if this entity is swimming.
     */
    @Override
    public boolean isSwimming() {
        return false;
    }

    /**
     * Makes entity start or stop swimming.
     * <p>
     * This may have unexpected results if the entity is not in water.
     *
     * @param swimming True if the entity is swimming.
     */
    @Override
    public void setSwimming(boolean swimming) {

    }

    /**
     * Checks to see if an entity is currently using the Riptide enchantment.
     *
     * @return True if this entity is currently riptiding.
     */
    @Override
    public boolean isRiptiding() {
        return false;
    }

    /**
     * Returns whether this entity is slumbering.
     *
     * @return slumber state
     */
    @Override
    public boolean isSleeping() {
        return false;
    }

    /**
     * Gets if the entity is climbing.
     *
     * @return if the entity is climbing
     */
    @Override
    public boolean isClimbing() {
        return false;
    }

    /**
     * Sets whether an entity will have AI.
     * <p>
     * The entity will be completely unable to move if it has no AI.
     *
     * @param ai whether the mob will have AI or not.
     */
    @Override
    public void setAI(boolean ai) {

    }

    /**
     * Checks whether an entity has AI.
     * <p>
     * The entity will be completely unable to move if it has no AI.
     *
     * @return true if the entity has AI, otherwise false.
     */
    @Override
    public boolean hasAI() {
        return false;
    }

    /**
     * Makes this entity attack the given entity with a melee attack.
     * <p>
     * Attack damage is calculated by the server from the attributes and
     * equipment of this mob, and knockback is applied to {@code target} as
     * appropriate.
     *
     * @param target entity to attack.
     */
    @Override
    public void attack(@NotNull Entity target) {

    }

    /**
     * Makes this entity swing their main hand.
     * <p>
     * This method does nothing if this entity does not have an animation for
     * swinging their main hand.
     */
    @Override
    public void swingMainHand() {

    }

    /**
     * Makes this entity swing their off hand.
     * <p>
     * This method does nothing if this entity does not have an animation for
     * swinging their off hand.
     */
    @Override
    public void swingOffHand() {

    }

    /**
     * Set if this entity will be subject to collisions with other entities.
     * <p>
     * Exemptions to this rule can be managed with
     * {@link #getCollidableExemptions()}
     *
     * @param collidable collision status
     */
    @Override
    public void setCollidable(boolean collidable) {

    }

    /**
     * Gets if this entity is subject to collisions with other entities.
     * <p>
     * Some entities might be exempted from the collidable rule of this entity.
     * Use {@link #getCollidableExemptions()} to get these.
     * <p>
     * Please note that this method returns only the custom collidable state,
     * not whether the entity is non-collidable for other reasons such as being
     * dead.
     *
     * @return collision status
     */
    @Override
    public boolean isCollidable() {
        return false;
    }

    /**
     * Gets a mutable set of UUIDs of the entities which are exempt from the
     * entity's collidable rule and which's collision with this entity will
     * behave the opposite of it.
     * <p>
     * This set can be modified to add or remove exemptions.
     * <p>
     * For example if collidable is true and an entity is in the exemptions set
     * then it will not collide with it. Similarly if collidable is false and an
     * entity is in this set then it will still collide with it.
     * <p>
     * Note these exemptions are not (currently) persistent.
     *
     * @return the collidable exemption set
     */
    @NotNull
    @Override
    public Set<UUID> getCollidableExemptions() {
        return Set.of();
    }

    /**
     * Returns the value of the memory specified.
     * <p>
     * Note that the value is null when the specific entity does not have that
     * value by default.
     *
     * @param memoryKey memory to access
     * @return a instance of the memory section value or null if not present
     */
    @Nullable
    @Override
    public <T> T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return null;
    }

    /**
     * Sets the value of the memory specified.
     * <p>
     * Note that the value will not be persisted when the specific entity does
     * not have that value by default.
     *
     * @param memoryKey   the memory to access
     * @param memoryValue a typed memory value
     */
    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue) {

    }

    /**
     * Get the category to which this entity belongs.
     * <p>
     * Categories may subject this entity to additional effects, benefits or
     * debuffs.
     *
     * @return the entity category
     */
    @NotNull
    @Override
    public EntityCategory getCategory() {
        return null;
    }

    /**
     * Sets whether the entity is invisible or not.
     *
     * @param invisible If the entity is invisible
     */
    @Override
    public void setInvisible(boolean invisible) {

    }

    /**
     * Gets whether the entity is invisible or not.
     *
     * @return Whether the entity is invisible
     */
    @Override
    public boolean isInvisible() {
        return false;
    }

    /**
     * Gets the specified attribute instance from the object. This instance will
     * be backed directly to the object and any changes will be visible at once.
     *
     * @param attribute the attribute to get
     * @return the attribute instance or null if not applicable to this object
     */
    @Nullable
    @Override
    public AttributeInstance getAttribute(@NotNull Attribute attribute) {
        return null;
    }

    /**
     * Deals the given amount of damage to this entity.
     *
     * @param amount Amount of damage to deal
     */
    @Override
    public void damage(double amount) {

    }

    /**
     * Deals the given amount of damage to this entity, from a specified
     * entity.
     *
     * @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    @Override
    public void damage(double amount, @Nullable Entity source) {

    }

    /**
     * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
     *
     * @return Health represented from 0 to max
     */
    @Override
    public double getHealth() {
        return 0;
    }

    /**
     * Sets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is
     * dead.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     *                                  {@link #getMaxHealth()}
     */
    @Override
    public void setHealth(double health) {

    }

    /**
     * Gets the entity's absorption amount.
     *
     * @return absorption amount from 0
     */
    @Override
    public double getAbsorptionAmount() {
        return 0;
    }

    /**
     * Sets the entity's absorption amount.
     *
     * @param amount new absorption amount from 0
     * @throws IllegalArgumentException thrown if health is {@literal < 0} or
     *                                  non-finite.
     */
    @Override
    public void setAbsorptionAmount(double amount) {

    }

    /**
     * Gets the maximum health this entity has.
     *
     * @return Maximum health
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Override
    public double getMaxHealth() {
        return 0;
    }

    /**
     * Sets the maximum health this entity can have.
     * <p>
     * If the health of the entity is above the value provided it will be set
     * to that value.
     * <p>
     * Note: An entity with a health bar ({@link Player}, {@link EnderDragon},
     * {@link Wither}, etc...} will have their bar scaled accordingly.
     *
     * @param health amount of health to set the maximum to
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Override
    public void setMaxHealth(double health) {

    }

    /**
     * Resets the max health to the original amount.
     *
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Override
    public void resetMaxHealth() {

    }

    /**
     * Gets the entity's current position
     *
     * @return a new copy of Location containing the position of this entity
     */
    @NotNull
    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Stores the entity's current position in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        return location;
    }

    /**
     * Sets this entity's velocity in meters per tick
     *
     * @param velocity New velocity to travel with
     */
    @Override
    public void setVelocity(@NotNull Vector velocity) {

    }

    /**
     * Gets this entity's current velocity
     *
     * @return Current traveling velocity of this entity
     */
    @NotNull
    @Override
    public Vector getVelocity() {
        return null;
    }

    /**
     * Gets the entity's height
     *
     * @return height of entity
     */
    @Override
    public double getHeight() {
        return 0;
    }

    /**
     * Gets the entity's width
     *
     * @return width of entity
     */
    @Override
    public double getWidth() {
        return 0;
    }

    /**
     * Gets the entity's current bounding box.
     * <p>
     * The returned bounding box reflects the entity's current location and
     * size.
     *
     * @return the entity's current bounding box
     */
    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    /**
     * Returns true if the entity is supported by a block. This value is a
     * state updated by the server and is not recalculated unless the entity
     * moves.
     *
     * @return True if entity is on ground.
     * @see Player#isOnGround()
     */
    @Override
    public boolean isOnGround() {
        return false;
    }

    /**
     * Returns true if the entity is in water.
     *
     * @return <code>true</code> if the entity is in water.
     */
    @Override
    public boolean isInWater() {
        return false;
    }

    /**
     * Gets the current world this entity resides in
     *
     * @return World
     */
    @NotNull
    @Override
    public World getWorld() {
        return null;
    }

    /**
     * Sets the entity's rotation.
     * <p>
     * Note that if the entity is affected by AI, it may override this rotation.
     *
     * @param yaw   the yaw
     * @param pitch the pitch
     * @throws UnsupportedOperationException if used for players
     */
    @Override
    public void setRotation(float yaw, float pitch) {

    }

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    @Override
    public boolean teleport(@NotNull Location location) {
        return false;
    }

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @param cause    The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    @Override
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    @Override
    public boolean teleport(@NotNull Entity destination) {
        return false;
    }

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @param cause       The cause of this teleportation
     * @return <code>true</code> if the teleport was successful
     */
    @Override
    public boolean teleport(@NotNull Entity destination, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    /**
     * Returns a list of entities within a bounding box centered around this
     * entity
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return {@code List<Entity>} List of entities nearby
     */
    @NotNull
    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return List.of();
    }

    /**
     * Returns a unique id for this entity
     *
     * @return Entity id
     */
    @Override
    public int getEntityId() {
        return 0;
    }

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @return int fireTicks
     */
    @Override
    public int getFireTicks() {
        return 0;
    }

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return int maxFireTicks
     */
    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @param ticks Current ticks remaining
     */
    @Override
    public void setFireTicks(int ticks) {

    }

    /**
     * Sets if the entity has visual fire (it will always appear to be on fire).
     *
     * @param fire whether visual fire is enabled
     */
    @Override
    public void setVisualFire(boolean fire) {

    }

    /**
     * Gets if the entity has visual fire (it will always appear to be on fire).
     *
     * @return whether visual fire is enabled
     */
    @Override
    public boolean isVisualFire() {
        return false;
    }

    /**
     * Returns the entity's current freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @return int freeze ticks
     */
    @Override
    public int getFreezeTicks() {
        return 0;
    }

    /**
     * Returns the entity's maximum freeze ticks (amount of ticks before it will
     * be fully frozen)
     *
     * @return int max freeze ticks
     */
    @Override
    public int getMaxFreezeTicks() {
        return 0;
    }

    /**
     * Sets the entity's current freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @param ticks Current ticks
     */
    @Override
    public void setFreezeTicks(int ticks) {

    }

    /**
     * Gets if the entity is fully frozen (it has been in powdered snow for max
     * freeze ticks).
     *
     * @return freeze status
     */
    @Override
    public boolean isFrozen() {
        return false;
    }

    /**
     * Mark the entity's removal.
     */
    @Override
    public void remove() {

    }

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it is dead.
     */
    @Override
    public boolean isDead() {
        return false;
    }

    /**
     * Returns false if the entity has died or been despawned for some other
     * reason.
     *
     * @return True if valid.
     */
    @Override
    public boolean isValid() {
        return false;
    }

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
    @Override
    public void sendMessage(@NotNull String message) {

    }

    /**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
    @Override
    public void sendMessage(@NotNull String... messages) {

    }

    /**
     * Sends this sender a message
     *
     * @param sender  The sender of this message
     * @param message Message to be displayed
     */
    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {

    }

    /**
     * Sends this sender multiple messages
     *
     * @param sender   The sender of this message
     * @param messages An array of messages to be displayed
     */
    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {

    }

    /**
     * Gets the {@link Server} that contains this Entity
     *
     * @return Server instance running this Entity
     */
    @NotNull
    @Override
    public Server getServer() {
        return null;
    }

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
    @NotNull
    @Override
    public String getName() {
        return username;
    }

    /**
     * Returns true if the entity gets persisted.
     * <p>
     * By default all entities are persistent. An entity will also not get
     * persisted, if it is riding an entity that is not persistent.
     * <p>
     * The persistent flag on players controls whether or not to save their
     * playerdata file when they quit. If a player is directly or indirectly
     * riding a non-persistent entity, the vehicle at the root and all its
     * passengers won't get persisted.
     * <p>
     * <b>This should not be confused with
     * {@link LivingEntity#setRemoveWhenFarAway(boolean)} which controls
     * despawning of living entities. </b>
     *
     * @return true if this entity is persistent
     */
    @Override
    public boolean isPersistent() {
        return false;
    }

    /**
     * Sets whether or not the entity gets persisted.
     *
     * @param persistent the persistence status
     * @see #isPersistent()
     */
    @Override
    public void setPersistent(boolean persistent) {

    }

    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     *
     * @return an entity
     * @deprecated entities may have multiple passengers, use
     * {@link #getPassengers()}
     */
    @Nullable
    @Override
    public Entity getPassenger() {
        return null;
    }

    /**
     * Set the passenger of a vehicle.
     *
     * @param passenger The new passenger.
     * @return false if it could not be done for whatever reason
     * @deprecated entities may have multiple passengers, use
     * {@link #addPassenger(Entity)}
     */
    @Override
    public boolean setPassenger(@NotNull Entity passenger) {
        return false;
    }

    /**
     * Gets a list of passengers of this vehicle.
     * <p>
     * The returned list will not be directly linked to the entity's current
     * passengers, and no guarantees are made as to its mutability.
     *
     * @return list of entities corresponding to current passengers.
     */
    @NotNull
    @Override
    public List<Entity> getPassengers() {
        return List.of();
    }

    /**
     * Add a passenger to the vehicle.
     *
     * @param passenger The passenger to add
     * @return false if it could not be done for whatever reason
     */
    @Override
    public boolean addPassenger(@NotNull Entity passenger) {
        return false;
    }

    /**
     * Remove a passenger from the vehicle.
     *
     * @param passenger The passenger to remove
     * @return false if it could not be done for whatever reason
     */
    @Override
    public boolean removePassenger(@NotNull Entity passenger) {
        return false;
    }

    /**
     * Check if a vehicle has passengers.
     *
     * @return True if the vehicle has no passengers.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Eject any passenger.
     *
     * @return True if there was a passenger.
     */
    @Override
    public boolean eject() {
        return false;
    }

    /**
     * Returns the distance this entity has fallen
     *
     * @return The distance.
     */
    @Override
    public float getFallDistance() {
        return 0;
    }

    /**
     * Sets the fall distance for this entity
     *
     * @param distance The new distance.
     */
    @Override
    public void setFallDistance(float distance) {

    }

    /**
     * Record the last {@link EntityDamageEvent} inflicted on this entity
     *
     * @param event a {@link EntityDamageEvent}
     */
    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {

    }

    /**
     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
     * This event may have been cancelled.
     *
     * @return the last known {@link EntityDamageEvent} or null if hitherto
     * unharmed
     */
    @Nullable
    @Override
    public EntityDamageEvent getLastDamageCause() {
        return fallback.getLastDamageCause();
    }

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    @NotNull
    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * Gets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities.
     *
     * @return Age of entity
     */
    @Override
    public int getTicksLived() {
        return 0;
    }

    /**
     * Sets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities. May not be less than one
     * tick.
     *
     * @param value Age of entity
     */
    @Override
    public void setTicksLived(int value) {

    }

    /**
     * Performs the specified {@link EntityEffect} for this entity.
     * <p>
     * This will be viewable to all players near the entity.
     * <p>
     * If the effect is not applicable to this class of entity, it will not play.
     *
     * @param type Effect to play.
     */
    @Override
    public void playEffect(@NotNull EntityEffect type) {

    }

    /**
     * Get the type of the entity.
     *
     * @return The entity type.
     */
    @NotNull
    @Override
    public EntityType getType() {
        if (fallback == null) return null;
        return fallback.getType();
    }

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return True if the entity is in a vehicle.
     */
    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle (and
     * is removed from it), true will be returned, otherwise false will be
     * returned.
     *
     * @return True if the entity was in a vehicle.
     */
    @Override
    public boolean leaveVehicle() {
        return false;
    }

    /**
     * Get the vehicle that this player is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return The current vehicle.
     */
    @Nullable
    @Override
    public Entity getVehicle() {
        return null;
    }

    /**
     * Sets whether or not to display the mob's custom name client side. The
     * name will be displayed above the mob similarly to a player.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @param flag custom name or not
     */
    @Override
    public void setCustomNameVisible(boolean flag) {

    }

    /**
     * Gets whether or not the mob's custom name is displayed client side.
     * <p>
     * This value has no effect on players, they will always display their
     * name.
     *
     * @return if the custom name is displayed
     */
    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    /**
     * Sets whether the entity has a team colored (default: white) glow.
     *
     * <b>nb: this refers to the 'Glowing' entity property, not whether a
     * glowing potion effect is applied</b>
     *
     * @param flag if the entity is glowing
     */
    @Override
    public void setGlowing(boolean flag) {

    }

    /**
     * Gets whether the entity is glowing or not.
     *
     * <b>nb: this refers to the 'Glowing' entity property, not whether a
     * glowing potion effect is applied</b>
     *
     * @return whether the entity is glowing
     */
    @Override
    public boolean isGlowing() {
        return false;
    }

    /**
     * Sets whether the entity is invulnerable or not.
     * <p>
     * When an entity is invulnerable it can only be damaged by players in
     * creative mode.
     *
     * @param flag if the entity is invulnerable
     */
    @Override
    public void setInvulnerable(boolean flag) {

    }

    /**
     * Gets whether the entity is invulnerable or not.
     *
     * @return whether the entity is
     */
    @Override
    public boolean isInvulnerable() {
        return false;
    }

    /**
     * Gets whether the entity is silent or not.
     *
     * @return whether the entity is silent.
     */
    @Override
    public boolean isSilent() {
        return false;
    }

    /**
     * Sets whether the entity is silent or not.
     * <p>
     * When an entity is silent it will not produce any sound.
     *
     * @param flag if the entity is silent
     */
    @Override
    public void setSilent(boolean flag) {

    }

    /**
     * Returns whether gravity applies to this entity.
     *
     * @return whether gravity applies
     */
    @Override
    public boolean hasGravity() {
        return false;
    }

    /**
     * Sets whether gravity applies to this entity.
     *
     * @param gravity whether gravity should apply
     */
    @Override
    public void setGravity(boolean gravity) {

    }

    /**
     * Gets the period of time (in ticks) before this entity can use a portal.
     *
     * @return portal cooldown ticks
     */
    @Override
    public int getPortalCooldown() {
        return 0;
    }

    /**
     * Sets the period of time (in ticks) before this entity can use a portal.
     *
     * @param cooldown portal cooldown ticks
     */
    @Override
    public void setPortalCooldown(int cooldown) {

    }

    /**
     * Returns a set of tags for this entity.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @return a set of tags for this entity
     */
    @NotNull
    @Override
    public Set<String> getScoreboardTags() {
        return Set.of();
    }

    /**
     * Add a tag to this entity.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @param tag the tag to add
     * @return true if the tag was successfully added
     */
    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        return false;
    }

    /**
     * Removes a given tag from this entity.
     *
     * @param tag the tag to remove
     * @return true if the tag was successfully removed
     */
    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        return false;
    }

    /**
     * Returns the reaction of the entity when moved by a piston.
     *
     * @return reaction
     */
    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    /**
     * Get the closest cardinal {@link BlockFace} direction an entity is
     * currently facing.
     * <br>
     * This will not return any non-cardinal directions such as
     * {@link BlockFace#UP} or {@link BlockFace#DOWN}.
     * <br>
     * {@link Hanging} entities will override this call and thus their behavior
     * may be different.
     *
     * @return the entity's current cardinal facing.
     * @see Hanging
     * @see Directional#getFacing()
     */
    @NotNull
    @Override
    public BlockFace getFacing() {
        return null;
    }

    /**
     * Gets the entity's current pose.
     *
     * <b>Note that the pose is only updated at the end of a tick, so may be
     * inconsistent with other methods. eg {@link Player#isSneaking()} being
     * true does not imply the current pose will be {@link Pose#SNEAKING}</b>
     *
     * @return current pose
     */
    @NotNull
    @Override
    public Pose getPose() {
        return null;
    }

    /**
     * Get the category of spawn to which this entity belongs.
     *
     * @return the entity´s category spawn
     */
    @NotNull
    @Override
    public SpawnCategory getSpawnCategory() {
        return null;
    }

    @NotNull
    @Override
    public Spigot spigot() {
        return null;
    }

    /**
     * Gets the custom name on a mob or block. If there is no name this method
     * will return null.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @return name of the mob/block or null
     */
    @Nullable
    @Override
    public String getCustomName() {
        return username;
    }

    /**
     * Sets a custom name on a mob or block. This name will be used in death
     * messages and can be sent to the client as a nameplate over the mob.
     * <p>
     * Setting the name to null or an empty string will clear it.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @param name the name to set
     */
    @Override
    public void setCustomName(@Nullable String name) {

    }

    /**
     * Sets a metadata value in the implementing object's metadata store.
     *
     * @param metadataKey      A unique key to identify this metadata.
     * @param newMetadataValue The metadata value to apply.
     * @throws IllegalArgumentException If value is null, or the owning plugin
     *                                  is null
     */
    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {

    }

    /**
     * Returns a list of previously set metadata values from the implementing
     * object's metadata store.
     *
     * @param metadataKey the unique metadata key being sought.
     * @return A list of values, one for each plugin that has set the
     * requested value.
     */
    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return List.of();
    }

    /**
     * Tests to see whether the implementing object contains the given
     * metadata value in its metadata store.
     *
     * @param metadataKey the unique metadata key being queried.
     * @return the existence of the metadataKey within subject.
     */
    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false;
    }

    /**
     * Removes the given metadata value from the implementing object's
     * metadata store.
     *
     * @param metadataKey  the unique metadata key identifying the metadata to
     *                     remove.
     * @param owningPlugin This plugin's metadata value will be removed. All
     *                     other values will be left untouched.
     * @throws IllegalArgumentException If plugin is null
     */
    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {

    }

    /**
     * Checks if this object contains an override for the specified
     * permission, by fully qualified name
     *
     * @param name Name of the permission
     * @return true if the permission is set, otherwise false
     */
    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return false;
    }

    /**
     * Checks if this object contains an override for the specified {@link
     * Permission}
     *
     * @param perm Permission to check
     * @return true if the permission is set, otherwise false
     */
    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return false;
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned.
     *
     * @param name Name of the permission
     * @return Value of the permission
     */
    @Override
    public boolean hasPermission(@NotNull String name) {
        return false;
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned
     *
     * @param perm Permission to get
     * @return Value of the permission
     */
    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return false;
    }

    /**
     * Adds a new {@link PermissionAttachment} with a single permission by
     * name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @return The PermissionAttachment that was just created
     */
    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return null;
    }

    /**
     * Adds a new empty {@link PermissionAttachment} to this object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @return The PermissionAttachment that was just created
     */
    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return null;
    }

    /**
     * Temporarily adds a new {@link PermissionAttachment} with a single
     * permission by name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     */
    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return null;
    }

    /**
     * Temporarily adds a new empty {@link PermissionAttachment} to this
     * object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     */
    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return null;
    }

    /**
     * Removes the given {@link PermissionAttachment} from this object
     *
     * @param attachment Attachment to remove
     * @throws IllegalArgumentException Thrown when the specified attachment
     *                                  isn't part of this object
     */
    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {

    }

    /**
     * Recalculates the permissions for this object, if the attachments have
     * changed values.
     * <p>
     * This should very rarely need to be called from a plugin.
     */
    @Override
    public void recalculatePermissions() {

    }

    /**
     * Gets a set containing all of the permissions currently in effect by
     * this object
     *
     * @return Set of currently effective permissions
     */
    @NotNull
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Set.of();
    }

    /**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */
    @Override
    public boolean isOp() {
        return false;
    }

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */
    @Override
    public void setOp(boolean value) {

    }

    /**
     * Returns a custom tag container capable of storing tags on the object.
     * <p>
     * Note that the tags stored on this container are all stored under their
     * own custom namespace therefore modifying default tags using this
     * {@link PersistentDataHolder} is impossible.
     *
     * @return the persistent metadata container
     */
    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }

    /**
     * Launches a {@link Projectile} from the ProjectileSource.
     *
     * @param projectile class of the projectile to launch
     * @return the launched projectile
     */
    @NotNull
    @Override
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile) {
        return null;
    }

    /**
     * Launches a {@link Projectile} from the ProjectileSource with an
     * initial velocity.
     *
     * @param projectile class of the projectile to launch
     * @param velocity   the velocity with which to launch
     * @return the launched projectile
     */
    @NotNull
    @Override
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
        return null;
    }
}
