package sqrilizzAutoadvertising.commands;

import sqrilizzAutoadvertising.AutoAdvertising;
import sqrilizzAutoadvertising.AdManager;
import sqrilizzAutoadvertising.GradientUtil;
import sqrilizzAutoadvertising.LuckyPermsHook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutoAdCommand implements CommandExecutor, TabCompleter {
    private final AutoAdvertising plugin;
    private final AdManager adManager;

    public AutoAdCommand(AutoAdvertising plugin) {
        this.plugin = plugin;
        this.adManager = plugin.getAdManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§bAutoAdvertising v5.0 §7- /autoad help");
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "reload":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.reload")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                adManager.reload();
                adManager.start();
                sender.sendMessage("§aConfig reloaded.");
                return true;
            case "createad":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.manage.ads")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /autoad createad <id>");
                    return true;
                }
                adManager.addAd(args[1]);
                sender.sendMessage("§aAd created: " + args[1]);
                return true;
            case "deletead":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.manage.ads")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /autoad deletead <id>");
                    return true;
                }
                adManager.deleteAd(args[1]);
                sender.sendMessage("§aAd deleted: " + args[1]);
                return true;
            case "addline":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.manage.lines")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /autoad addline <id> <line>");
                    return true;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) sb.append(args[i]).append(" ");
                adManager.addLine(args[1], sb.toString().trim());
                sender.sendMessage("§aLine added to " + args[1]);
                return true;
            case "removeline":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.manage.lines")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /autoad removeline <id> <index>");
                    return true;
                }
                try {
                    int idx = Integer.parseInt(args[2]) - 1;
                    adManager.removeLine(args[1], idx);
                    sender.sendMessage("§aLine removed from " + args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cIndex must be a number.");
                }
                return true;
            case "setinterval":
                if (!LuckyPermsHook.hasPermission(sender, "autoadvertising.manage.interval")) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /autoad setinterval <seconds>");
                    return true;
                }
                try {
                    int sec = Integer.parseInt(args[1]);
                    adManager.setInterval(sec);
                    sender.sendMessage("§aInterval set: " + sec + " seconds.");
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cPlease enter a number of seconds.");
                }
                return true;
            case "gradient":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cPlayers only.");
                    return true;
                }
                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /autoad gradient #ff0000 #00ff00 Text");
                    return true;
                }
                String from = args[1];
                String to = args[2];
                StringBuilder gradText = new StringBuilder();
                for (int i = 3; i < args.length; i++) gradText.append(args[i]).append(" ");
                String result = GradientUtil.gradient(gradText.toString().trim(), from, to);
                ((Player)sender).sendMessage(result);
                return true;
            case "help":
                sender.sendMessage("§bAutoAdvertising v5.0 §7- Available commands:");
                sender.sendMessage("§7/autoad reload, createad, deletead, addline, removeline, setinterval, gradient");
                return true;
            default:
                sender.sendMessage("§bAutoAdvertising v5.0 §7- /autoad help");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subcommands = Arrays.asList("reload", "createad", "deletead", "addline", "removeline", "setinterval", "gradient", "help");
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String sub : subcommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    result.add(sub);
                }
            }
            return result;
        }
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("deletead") || sub.equals("addline") || sub.equals("removeline")) {
                // Подсказка id объявлений
                return new ArrayList<>(adManager.getAds().keySet());
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("removeline")) {
            // Подсказка индексов строк
            List<String> ids = new ArrayList<>(adManager.getAds().keySet());
            if (ids.contains(args[1])) {
                List<String> lines = adManager.getAds().get(args[1]);
                List<String> idx = new ArrayList<>();
                for (int i = 1; i <= lines.size(); i++) idx.add(String.valueOf(i));
                return idx;
            }
        }
        return Collections.emptyList();
    }
} 