package sqrilizzAutoadvertising;

import org.bukkit.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradientUtil {
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:(#[0-9a-fA-F]{6}):(#[0-9a-fA-F]{6})>(.*?)</gradient>");

    public static String applyGradients(String text) {
        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String from = matcher.group(1);
            String to = matcher.group(2);
            String content = matcher.group(3);
            matcher.appendReplacement(sb, gradient(content, from, to));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String gradient(String text, String fromHex, String toHex) {
        int from = Integer.parseInt(fromHex.substring(1), 16);
        int to = Integer.parseInt(toHex.substring(1), 16);
        int r1 = (from >> 16) & 0xFF, g1 = (from >> 8) & 0xFF, b1 = from & 0xFF;
        int r2 = (to >> 16) & 0xFF, g2 = (to >> 8) & 0xFF, b2 = to & 0xFF;
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            double ratio = len == 1 ? 0 : (double) i / (len - 1);
            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);
            // Используем hex-кодировку для цветов (Spigot/Paper 1.16+)
            sb.append("§x");
            sb.append(String.format("§%x§%x§%x§%x§%x§%x",
                (r >> 4) & 0xF, r & 0xF,
                (g >> 4) & 0xF, g & 0xF,
                (b >> 4) & 0xF, b & 0xF));
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }
} 