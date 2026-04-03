package net.enelson.sopli.elevators.command;

import net.enelson.sopli.elevators.SElevatorsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class SElevatorsCommand implements CommandExecutor {
    private final SElevatorsPlugin plugin;

    public SElevatorsCommand(SElevatorsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("selevators.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "SElevators configuration reloaded.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " reload");
        return true;
    }
}
