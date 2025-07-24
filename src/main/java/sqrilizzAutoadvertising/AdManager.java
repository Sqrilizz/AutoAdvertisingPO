package sqrilizzAutoadvertising;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;
import sqrilizzAutoadvertising.GradientUtil;

public class AdManager {
    private final AutoAdvertising plugin;
    private BukkitTask task;
    private int interval;
    private String prefix;
    private Map<String, List<String>> ads;
    private final Random random = new Random();

    public AdManager(AutoAdvertising plugin) {
        this.plugin = plugin;
        reload();
        start();
    }

    public void reload() {
        ((JavaPlugin)plugin).reloadConfig();
        FileConfiguration config = ((JavaPlugin)plugin).getConfig();
        this.interval = config.getInt("interval", 60);
        this.prefix = ""; // Префикс убран
        this.ads = new LinkedHashMap<>();
        if (config.isConfigurationSection("ads")) {
            for (String id : config.getConfigurationSection("ads").getKeys(false)) {
                List<String> lines = config.getStringList("ads." + id);
                ads.put(id, new ArrayList<>(lines));
            }
        }
    }

    public void start() {
        stop();
        if (interval <= 0 || ads.isEmpty()) return;
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::broadcastRandomAd, interval * 20L, interval * 20L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void broadcastRandomAd() {
        if (ads.isEmpty()) return;
        List<String> keys = new ArrayList<>(ads.keySet());
        String adId = keys.get(random.nextInt(keys.size()));
        sendAd(adId);
    }

    public void sendAd(String adId) {
        List<String> lines = ads.get(adId);
        if (lines == null) return;
        for (String line : lines) {
            String msg = color(line);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(msg);
            }
        }
    }

    public void setInterval(int seconds) {
        this.interval = seconds;
        ((JavaPlugin)plugin).getConfig().set("interval", seconds);
        ((JavaPlugin)plugin).saveConfig();
        start();
    }

    public int getInterval() {
        return interval;
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<String, List<String>> getAds() {
        return ads;
    }

    public void addAd(String id) {
        if (!ads.containsKey(id)) {
            ads.put(id, new ArrayList<>());
            ((JavaPlugin)plugin).getConfig().set("ads." + id, new ArrayList<>());
            ((JavaPlugin)plugin).saveConfig();
        }
    }

    public void deleteAd(String id) {
        ads.remove(id);
        ((JavaPlugin)plugin).getConfig().set("ads." + id, null);
        ((JavaPlugin)plugin).saveConfig();
    }

    public void addLine(String id, String line) {
        List<String> lines = ads.get(id);
        if (lines != null) {
            lines.add(line);
            ((JavaPlugin)plugin).getConfig().set("ads." + id, lines);
            ((JavaPlugin)plugin).saveConfig();
        }
    }

    public void removeLine(String id, int index) {
        List<String> lines = ads.get(id);
        if (lines != null && index >= 0 && index < lines.size()) {
            lines.remove(index);
            ((JavaPlugin)plugin).getConfig().set("ads." + id, lines);
            ((JavaPlugin)plugin).saveConfig();
        }
    }

    public static String color(String msg) {
        msg = GradientUtil.applyGradients(msg);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
} 