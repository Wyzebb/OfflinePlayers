package de.snap20lp.offlineplayers.commands;

import de.snap20lp.offlineplayers.CloneManager;
import de.snap20lp.offlineplayers.OfflinePlayer;
import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.time.Instant;
import java.util.*;

/**
 * The OfflinePlayerCommand can be used by admins to help better manage OfflinePlayers.
 *
 * @author Sugaku
 */
public class OfflinePlayerCommand extends BetterCommand {

    /**
     * A map of players who currently have an offline clone.
     */
    private Map<String, OfflinePlayer> offlinePlayers = new HashMap<>();

    /**
     * Creates the OfflinePlayerCommand instance.
     */
    public OfflinePlayerCommand () {
        super("offline-player");
    }

    /**
     * Updates the list of players who are offline by reading the CloneManager's list.
     */
    private void updateOfflinePlayers () {
        Map<UUID, OfflinePlayer> players = CloneManager.getInstance().getOfflinePlayerList();
        offlinePlayers.clear();
        players.forEach((u, p) -> {
            if (p.isDead()) return;
            offlinePlayers.put(p.getOfflinePlayer().getName(), p);
        });
    }

    /**
     * The teleport subcommand.
     *
     * @param player The player to teleport.
     * @param args   The passed arguments.
     */
    private void teleportSubcommand (Player player, String[] args) {
        if (args.length < 2) {
            send(player, ChatColor.RED + "Please specify an offline player's username.");
            return;
        }
        updateOfflinePlayers();
        OfflinePlayer offlinePlayer = offlinePlayers.get(args[1]);
        if (offlinePlayer == null) {
            send(player, ChatColor.RED + "Unknown player.");
            return;
        }
        player.teleport(offlinePlayer.getCloneEntity().getLocation());
        send(player, ChatColor.GREEN + "Teleported you to " + args[1]);
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        if (args.length < 1) return false;
        else if (!player.hasPermission("offline-player.admin")) return true;
        switch (args[0].toLowerCase()) {
            case "tp" -> teleportSubcommand(player, args);
            case "backup" -> {
                send(player, ChatColor.GREEN + "Creating backup!");
                CloneManager.getInstance().save("./plugins/OfflinePlayers/clones - " + DateFormat.getDateTimeInstance().format(Date.from(Instant.now())) + ".bak" );
            }
        }
        return true;
    }

    /**
     * Called whenever an unspecified sender executes this command. This could include console and command blocks.
     *
     * @param sender The sender who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onNonPlayerCommand (CommandSender sender, String[] args) {
        if (args.length < 1) return false;
        else if (!sender.hasPermission("offline-player.admin")) return true;
        switch (args[0]) {
            case "tp" -> send(sender, ChatColor.RED + "Teleporting to offline players can only be done by a player.");
            case "backup" -> {
                send(sender, ChatColor.GREEN + "Creating backup!");
                CloneManager.getInstance().save("./plugins/OfflinePlayers/clones - " + DateFormat.getDateTimeInstance().format(Date.from(Instant.now())) + ".bak" );
            }
        }
        return true;
    }

    /**
     * Called whenever a command sender is trying to tab complete a command.
     *
     * @param sender The sender who is tab completing this command.
     * @param args   The current arguments they have typed.
     */
    @Override
    public List<String> simplifiedTab (CommandSender sender, String[] args) {
        List<String> toReturn = new ArrayList<>();
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("tp")) {
                updateOfflinePlayers();
                toReturn.addAll(offlinePlayers.keySet());
                return everythingStartsWith(toReturn, args[1]);
            }
        } else if (args.length <= 1) {
            toReturn.addAll(Arrays.asList("tp", "backup"));
            return everythingStartsWith(toReturn, args[0]);
        }
        return List.of();
    }
}
