package sqrilizzAutoadvertising;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoAdvertising extends JavaPlugin {
    private AdManager adManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.adManager = new AdManager(this);
        sqrilizzAutoadvertising.commands.AutoAdCommand command = new sqrilizzAutoadvertising.commands.AutoAdCommand(this);
        getCommand("autoad").setExecutor(command);
        getCommand("autoad").setTabCompleter(command);
        getLogger().info("AutoAdvertising v5.0 enabled!");
    }

    @Override
    public void onDisable() {
        if (adManager != null) adManager.stop();
        getLogger().info("AutoAdvertising v5.0 disabled.");
    }

    public AdManager getAdManager() {
        return adManager;
    }
} 