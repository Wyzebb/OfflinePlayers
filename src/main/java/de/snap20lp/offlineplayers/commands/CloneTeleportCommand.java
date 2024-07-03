package de.snap20lp.offlineplayers.commands;

import io.github.math0898.utils.commands.BetterCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The CloneTeleportCommand allows players to teleport to any OfflinePlayers.
 *
 * @author Sugaku
 */
public class CloneTeleportCommand extends BetterCommand {

    /**
     * Creates a new BetterCommand with the given name.
     */
    public CloneTeleportCommand () {
        super("offline-teleport");
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    @Override
    public boolean onPlayerCommand (Player player, String[] args) {
        if (!player.hasPermission("offline-players.teleport") && !player.hasPermission("offline-players.admin")) return true;
        // todo: Implement.
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
        send(sender, ChatColor.RED + "This command can only be ran as a player.");
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
        // todo: Implement.
        return List.of();
    }
}
