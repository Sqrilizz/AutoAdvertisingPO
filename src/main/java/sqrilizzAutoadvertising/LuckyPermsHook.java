package sqrilizzAutoadvertising;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LuckyPermsHook {
    private static boolean hasLP = false;
    private static Object api = null;

    static {
        Plugin lp = Bukkit.getPluginManager().getPlugin("LuckPerms");
        if (lp != null && lp.isEnabled()) {
            hasLP = true;
            try {
                api = lp.getClass().getMethod("getApi").invoke(lp);
            } catch (Exception ignored) {}
        }
    }

    public static boolean hasPermission(CommandSender sender, String perm) {
        if (hasLP && sender instanceof Player) {
            try {
                Object user = api.getClass().getMethod("getUser", Player.class).invoke(api, sender);
                return (boolean) user.getClass().getMethod("hasPermission", String.class).invoke(user, perm);
            } catch (Exception e) {
                return sender.hasPermission(perm);
            }
        }
        return sender.hasPermission(perm);
    }
} 