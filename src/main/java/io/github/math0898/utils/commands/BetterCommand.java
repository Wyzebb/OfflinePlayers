package io.github.math0898.utils.commands;

import io.github.math0898.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

/**
 * The BetterCommand class trims down on the provided parameters to the CommandExecutor and TabCompleter whilst also
 * providing some frequently used and rewritten methods.
 *
 * @author Sugaku
 */
public abstract class BetterCommand implements CommandExecutor, TabCompleter { // todo: Perhaps include an additional permission check.
    // todo: Perhaps pre-define a list of basic tab completion options and add an additional option to register a call when args.length = 3/4 etc.
    /**
     * The name of this command as written in plugin.yml.
     */
    protected String commandName;

    /**
     * A prefix that should be sent with messages from this command.
     */
    protected String prefix = "";

    /**
     * Creates a new BetterCommand with the given name.
     *
     * @param name The name of the command as written in plugin.yml.
     */
    public BetterCommand (String name) {
        commandName = name;
        register();
    }

    /**
     * Creates a new BetterCommand with the given name and prefix.
     *
     * @param name   The name of the command as written in plugin.yml.
     * @param prefix A prefix that should be sent when sending a message from this command.
     */
    public BetterCommand (String name, String prefix) {
        commandName = name;
        this.prefix = prefix;
        register();
    }

    /**
     * Called whenever specifically a player executes this command.
     *
     * @param player The player who ran this command.
     * @param args   The arguments they passed to the command.
     */
    public abstract boolean onPlayerCommand (Player player, String[] args);

    /**
     * Called whenever an unspecified sender executes this command. This could include console and command blocks.
     *
     * @param sender The sender who ran this command.
     * @param args   The arguments they passed to the command.
     */
    public abstract boolean onNonPlayerCommand (CommandSender sender, String[] args);

    /**
     * Called whenever a command sender is trying to tab complete a command.
     *
     * @param sender The sender who is tab completing this command.
     * @param args   The current arguments they have typed.
     */
    public abstract List<String> simplifiedTab (CommandSender sender, String[] args);

    /**
     * Trims the given list down to only arguments that start with the given substring. Does not mutate the given list.
     *
     * @param list  The list to trim down.
     * @param start The string that everything must start with.
     * @return The trimmed list.
     */
    protected List<String> everythingStartsWith (List<String> list, String start) {
        List<String> listCopy = new java.util.ArrayList<>(List.copyOf(list));
        listCopy.removeIf((s) -> !s.startsWith(start));
        return listCopy;
    }

    /**
     * Registers this command to the PluginManager.
     */
    public void register () {
        PluginCommand command = Bukkit.getPluginCommand(commandName);
        if (command == null) Utils.getPlugin().getLogger().log(Level.SEVERE, "Failed to register command: " + commandName + ". Plugin command not found.");
        else {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    /**
     * Executes the given command, returning its success.
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return True if a valid command, otherwise false.
     */
    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player)
            return onPlayerCommand(player, args);
        else
            return onNonPlayerCommand(sender, args);
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command. For players tab-completing a command inside a command block, this will be the player, not the command block.
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args The arguments passed to the command, including final partial argument to be completed
     * @return A List of possible completions for the final argument, or null to default to the command executor.
     */
    @Override
    public List<String> onTabComplete (CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(commandName))
            return simplifiedTab(sender, args);
        else
            return null;
    }

    /**
     * Sends the provided message to the provided user including a prefix.
     *
     * @param user    The user to send the message to.
     * @param message The message to send.
     */
    protected void send (CommandSender user, String message) {
        send(user, message, true);
    }

    /**
     * Sends the provided message to the provided user, potentially including a prefix.
     *
     * @param user       The user to send the message to.
     * @param message    The message to send.
     * @param sendPrefix Whether a prefix should be sent with this message or not.
     */
    protected void send(CommandSender user, String message, boolean sendPrefix) {
        if (sendPrefix) user.sendMessage(prefix + message);
        else user.sendMessage(ChatColor.GRAY + message);
    }

    /**
     * Parses an Integer in the given array at the given position. Will default to returning 1 if there is no value.
     *
     * @param index  The array index of the param to grab.
     * @param args   The argument array.
     * @param sender The person we should notify if there was an error parsing.
     * @return The integer at that array position or 1 if not present or error.
     */
    protected int getIntegerParam (int index, String[] args, CommandSender sender) {
        int toReturn = 1;
        if (args.length == index + 1) {
            try {
                toReturn = Integer.parseInt(args[index]);
            } catch (Exception e) {
                send(sender, ChatColor.RED + "Is " + args[index] + " a number? Defaulting to 1.");
            }
        }
        return toReturn;
    }
}
