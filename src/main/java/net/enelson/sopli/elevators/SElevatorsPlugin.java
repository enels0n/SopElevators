package net.enelson.sopli.elevators;

import net.enelson.sopli.elevators.command.SElevatorsCommand;
import net.enelson.sopli.elevators.config.ElevatorSettings;
import net.enelson.sopli.elevators.hook.ACustomBlocksHook;
import net.enelson.sopli.elevators.listener.ElevatorListener;
import net.enelson.sopli.elevators.service.ElevatorService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SElevatorsPlugin extends JavaPlugin {
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
        getCommand("selevators").setExecutor(new SElevatorsCommand(this));
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
