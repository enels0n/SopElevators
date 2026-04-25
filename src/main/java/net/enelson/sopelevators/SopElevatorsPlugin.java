package net.enelson.sopelevators;

import net.enelson.sopelevators.command.SopElevatorsCommand;
import net.enelson.sopelevators.config.ElevatorSettings;
import net.enelson.sopelevators.hook.ACustomBlocksHook;
import net.enelson.sopelevators.listener.ElevatorListener;
import net.enelson.sopelevators.service.ElevatorService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SopElevatorsPlugin extends JavaPlugin {
    private ElevatorSettings settings;
    private ACustomBlocksHook customBlocksHook;
    private ElevatorService elevatorService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.settings = ElevatorSettings.fromConfig(this);
        this.customBlocksHook = new ACustomBlocksHook();
        this.elevatorService = new ElevatorService(this, settings, customBlocksHook);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ElevatorListener(this), this);
        getCommand("sopelevators").setExecutor(new SopElevatorsCommand(this));
    }

    public void reloadPlugin() {
        reloadConfig();
        this.settings = ElevatorSettings.fromConfig(this);
        this.elevatorService = new ElevatorService(this, settings, customBlocksHook);
    }

    public ElevatorSettings getSettings() {
        return settings;
    }

    public ElevatorService getElevatorService() {
        return elevatorService;
    }
}
