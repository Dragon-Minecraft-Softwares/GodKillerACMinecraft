// Created by HelloWorldCoder on 2025/5/27 13:38 Last Edited 2025/6/1 9:37
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonUtils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utils
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [utils] &r&ain &r&6&_DragonUtils by DragonMinecraftSoftwares&r&a !&r");
    }

    /**
     * 插件实例
     */
    public static Plugin plugin;

    /**
     * 初始化
     * @param plugin1 插件实例
     */
    public static void init(Plugin plugin1)
    {
        plugin=plugin1;
    }

    // 格式化时间戳
    public static String formatTimeStamp(long timestampMillis, String formatstring)
    {
        // 将毫秒时间戳转换为 Instant 对象
        Instant instant = Instant.ofEpochMilli(timestampMillis);
        // 设置时区（默认使用系统默认时区）
        ZoneId zoneId = ZoneId.systemDefault();
        // 转换为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        // 使用指定格式进行格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatstring);
        return localDateTime.format(formatter);
    }

    // 格式化阶段性时间
    public static String formatTimeprd(long time, String formatstring)
    {
        final long YEAR = 1000L * 60 * 60 * 24 * 365;
        final long MONTH = 1000L * 60 * 60 * 24 * 30;
        final long DAY = 1000L * 60 * 60 * 24;
        final long HOUR = 1000L * 60 * 60;
        final long MINUTE = 1000L * 60;
        long years = time / YEAR;
        time %= YEAR;
        long months = time / MONTH;
        time %= MONTH;
        long days = time / DAY;
        time %= DAY;
        long hours = time / HOUR;
        time %= HOUR;
        long minutes = time / MINUTE;
        time %= MINUTE;
        long seconds = time / 1000;
        // 替换格式中的占位符
        formatstring = formatstring.replace("yyyy", String.format("%04d", years));
        formatstring = formatstring.replace("MM", String.format("%02d", months));
        formatstring = formatstring.replace("dd", String.format("%02d", days));
        formatstring = formatstring.replace("HH", String.format("%02d", hours));
        formatstring = formatstring.replace("mm", String.format("%02d", minutes));
        formatstring = formatstring.replace("ss", String.format("%02d", seconds));
        return formatstring;
    }
    
    public static String getPlayerIp(Player player)
    {
        if(player.getAddress()!=null) return player.getAddress().getAddress().getHostAddress();
        return "null";
    }

    // atm machine(jcyang20) 参与的杰作,他修复了这段代码中的一个重大BUG
    public static long parseDurationToMillis(String durationStr) {
        // 匹配时间段格式
        final String REGEX = "(?<years>\\d+)?Y(?<months>\\d+)?M(?<days>\\d+)?D(?<hours>\\d+)?h(?<minutes>\\d+)?m(?<seconds>\\d+)?s";  // 2025/7/10 21:36:10 HelloWorldCoder: 将年月日更改为大写, 防止歧义, 感谢 atm machine 指出
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(durationStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("无效的时间段格式: " + durationStr);
        }
        // 提取各个时间单位，如果不存在则默认为0
        long years = matcher.group("years") != null ? Long.parseLong(matcher.group("years")) : 0;
        long months = matcher.group("months") != null ? Long.parseLong(matcher.group("months")) : 0;
        long days = matcher.group("days") != null ? Long.parseLong(matcher.group("days")) : 0;
        long hours = matcher.group("hours") != null ? Long.parseLong(matcher.group("hours")) : 0;
        long minutes = matcher.group("minutes") != null ? Long.parseLong(matcher.group("minutes")) : 0;
        long seconds = matcher.group("seconds") != null ? Long.parseLong(matcher.group("seconds")) : 0;
        // 转换为毫秒数
        return years * 365L * 24 * 60 * 60 * 1000
                + months * 30L * 24 * 60 * 60 * 1000
                + days * 24 * 60 * 60 * 1000
                + hours * 60 * 60 * 1000
                + minutes * 60 * 1000
                + seconds * 1000;
    }
}
