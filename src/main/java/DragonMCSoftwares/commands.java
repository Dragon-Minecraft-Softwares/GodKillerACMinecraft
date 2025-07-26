// 命令处理

package DragonMCSoftwares;

import DragonUtils.logging;
import DragonUtils.utils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static DragonMCSoftwares.GodKillerAnticheat.banlist;
import static DragonMCSoftwares.GodKillerAnticheat.debug;
import static org.bukkit.Bukkit.getOfflinePlayers;
import static org.bukkit.Bukkit.getOnlinePlayers;

import java.util.Objects;
import java.util.logging.Level;

/**
 * 命令处理类
 * 负责注册和处理插件的命令
 */
public class commands
{
    /**
     * 初始化命令
     * 注册命令执行器和命令补全器
     * 
     * @return 初始化成功返回true
     */
    public static boolean commandInit()
    {
        GodKillerAnticheat plugin = GodKillerAnticheat.getPlugin(GodKillerAnticheat.class);  // 获取插件类
        // 注册权限
        Permission banpermission = new Permission("godkilleracmc.bancontrol.ban","允许操作封禁");
        banpermission.setDefault(PermissionDefault.OP);
        plugin.getServer().getPluginManager().addPermission(banpermission);
        Permission unbanpermission = new Permission("godkilleracmc.bancontrol.unban","允许操作解封");
        unbanpermission.setDefault(PermissionDefault.OP);
        plugin.getServer().getPluginManager().addPermission(unbanpermission);
        Permission debugpermission = new Permission("godkilleracmc.system.debug","允许操作调试");
        debugpermission.setDefault(PermissionDefault.OP);
        plugin.getServer().getPluginManager().addPermission(debugpermission);
        // 注册命令
        try
        {
            Objects.requireNonNull(plugin.getCommand("ban")).setExecutor(new banCommandExecute());
            Objects.requireNonNull(plugin.getCommand("ban")).setTabCompleter(new banCommandComplete());
            Objects.requireNonNull(plugin.getCommand("ban")).setUsage("/ban <玩家> <原因> <时间>(秒,永封0)");
            List<String> aliases = new ArrayList<>();
            aliases.add("totalban");
            aliases.add("tempban");
            aliases.add("ban-ip");
            Objects.requireNonNull(plugin.getCommand("ban")).setAliases(aliases);
            Objects.requireNonNull(plugin.getCommand("unban")).setExecutor(new unbanCommandExecute());
            Objects.requireNonNull(plugin.getCommand("unban")).setTabCompleter(new unbanCommandComplete());
            Objects.requireNonNull(plugin.getCommand("unban")).setUsage("/unban <玩家>");
            aliases = new ArrayList<>();
            aliases.add("unban-ip");
            Objects.requireNonNull(plugin.getCommand("unban")).setAliases(aliases);
            Objects.requireNonNull(plugin.getCommand("gacdebug")).setExecutor(new debugCommandExecute());
        }
        catch(Exception e)
        {
            logging.log(Level.WARNING,GodKillerAnticheat.chatPrefix,"命令注册故障,请向Dragon Minecraft Softwares反馈: "+e);
        }
        return true;
    }
    /**
     * ban命令执行器
     * 处理玩家封禁命令的执行
     */
    public static class banCommandExecute implements CommandExecutor   // 封禁执行
    {
        /**
         * 处理命令执行
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param label   命令标签
         * @param args    命令参数
         * @return 命令执行成功返回true，否则返回false
         */
        public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))  // 权限检查
                {
                    try
                    {
                        Player banedplayer=Bukkit.getPlayer(args[0]);
                        // 计算封禁时长（秒转毫秒）
                        long dut = 0;
                        if (args.length >= 3) dut = Long.parseLong(args[2]) * 1000;
                        if(banedplayer!=null) banning.ban(banlist,args[0],utils.getPlayerIp(banedplayer),0,dut,args[1],false,System.currentTimeMillis());
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&a&l成功将玩家 &e"+args[0]+" &a以&b "+args[1]+" &a为理由封印&9 "+args[2]+" &a秒&r"));
                        return true;
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&4&l缺少参数！&r"));
                        return false;
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&4&l参数格式错误！&r"));
                        return false;
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&4&l未知错误！: " + e + "&r"));
                        return false;
                    }
                }
                else {
                    sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&r&6&l诛啥仙啊,你看你配吗??"));
                }
            }
            return false;
        }
    }
  
    /**
     * ban命令补全器
     * 提供命令参数的自动补全功能
     */
    public static class banCommandComplete implements TabCompleter // 命令补全
    {
         /**
         * 处理命令补全
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param label   命令标签
         * @param args    当前已输入的参数
         * @return 补全建议列表，如果没有建议则返回null
         */
        public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))
                {
                    if(args.length==1) return getOnlinePlayers().stream().map(Player::getName).toList();
                    if(args.length==2) return Collections.singletonList("请输入封禁理由");
                    if(args.length==3) return Collections.singletonList("请输入封禁时间(单位: 秒, 0永封, 不填默认0)");
                    return Collections.singletonList("填充完成,请执行");
                }
                else return Collections.singletonList("你没有权限执行这个指令");
            }
            return null;
        }
    }

    public static class unbanCommandExecute implements CommandExecutor   // 解封执行
    {
        public boolean onCommand(CommandSender sender,Command command,@NotNull String label,@NotNull String[] args)
        {
            if (sender.hasPermission("godkilleracmc.bancontrol.unban"))
            {
                try
                {
                    for(String Person:args)
                    {
                        for (banning.BanListType banlistThing:banlist)
                        {
                            if(banlistThing.name.equals(Person))
                            {
                                banning.unban(banlistThing.banId);
                                break;
                            }
                        }
                    }
                    return true;
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&4&l缺少参数！&r"));
                }
                catch (Exception e)
                {
                    sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix + "&4&l未知错误！&r"));
                }
            }
            else
            {
                sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix+"&r&6&l解啥封印啊,你看你配吗??"));
            }
            return false;
        }
    }

    public static class unbanCommandComplete implements TabCompleter // 解封补全
    {
        public List<String> onTabComplete(CommandSender sender,Command command,@NotNull String label,@NotNull String[] args)
        {
            if (sender.hasPermission("godkilleracmc.bancontrol.unban"))
            {
                List<String> Players=new ArrayList<String>();
                for(banning.BanListType banlistThing:banlist)
                {
                    Players.add(banlistThing.name);
                }
                return Players;
            }
            return Collections.singletonList("你没有权限执行这个指令");
        }
    }

    public static class debugCommandExecute implements CommandExecutor   // Debug Switch
    {
        public boolean onCommand(CommandSender sender,Command command,@NotNull String label,@NotNull String[] args)
        {
            if (sender.hasPermission("godkilleracmc.system.debug"))
            {
                debug=!debug;
                return true;
            }
            else
            {
                sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatPrefix+"&r&6&lPermission Denied"));
            }
            return false;
        }
    }
}
