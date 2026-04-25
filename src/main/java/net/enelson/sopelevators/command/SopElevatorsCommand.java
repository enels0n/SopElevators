package net.enelson.sopelevators.command;

import net.enelson.sopelevators.SopElevatorsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class SopElevatorsCommand implements CommandExecutor {
    private final SopElevatorsPlugin plugin;

    public SopElevatorsCommand(SopElevatorsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("sopelevators.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "SopElevators configuration reloaded.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " reload");
        return true;
    }
}
