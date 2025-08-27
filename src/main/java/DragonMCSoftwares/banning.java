// Created by HelloWorldCoder on 2025/5/27 13:38
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonMCSoftwares;

import DragonUtils.configs;
import DragonUtils.logging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import DragonUtils.configs;
import java.util.*;
import java.util.logging.Level;
import static DragonUtils.utils.formatTimeprd;
import static DragonMCSoftwares.GodKillerAnticheat.*;

/**
 * 封禁系统核心类
 * 提供玩家封禁、解封和封禁信息查询等功能
 * 时间使用long类型防止溢出
 */
public class banning
{
    /*
      静态初始化块，在类加载时执行
      记录模块加载信息到日志
     */
    static {
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aSuccessfully Loaded &bModule [banning] &r&ain &r&6&_DragonUtils by DragonMinecraftSoftwares&r&a !&r");
    }

    /**
     * 初始化封禁模块
     */
    public static void init(List<BanListType> banlist,List<BanInfoType> baninfolist)
    {
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aModule initialisation started, please wait......&r");
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aCreating/Reading files......&r");
        List<configs.ConfigDescribeType> FilesNeedInit=List.of();
        FilesNeedInit.add(new configs.ConfigDescribeType("/BanInfoData", "Bans.json"));
        FilesNeedInit.add(new configs.ConfigDescribeType("/BanInfoData", "BanInfo.json"));
        configs.init(FilesNeedInit);
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aReading Bans.json......&r");
        List<Object> infolist= Collections.singletonList(configs.readJson(new configs.ConfigDescribeType("/BanInfoData", "Bans.json")));
        for(Object banId:infolist)
        {
            banlist.add(new BanListType((String)configs.readJson(new configs.ConfigDescribeType("/BanInfoData/Bans.json",(String)banId+".name")),(String)configs.readJson(new configs.ConfigDescribeType("/BanInfoData/Bans.json",(String)banId+".ip")),(Integer) banId));
        }
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aReading BanInfo.json......&r");
        infolist= Collections.singletonList(configs.readJson(new configs.ConfigDescribeType("/BanInfoData", "BanInfo.json")));
        for(Object banId:infolist)
        {
            baninfolist.add(new BanInfoType((long)configs.readJson(new configs.ConfigDescribeType("/BanInfoData/BanInfo.json",(String)banId+".time")),(String)configs.readJson(new configs.ConfigDescribeType("/BanInfoData/BanInfo.json",(String)banId+".reason")),(long)configs.readJson(new configs.ConfigDescribeType("/BanInfoData/BanInfo.json",(String)banId+".duration")),(Integer) banId));
        }
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aRead completed!&r");
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aModule initialisation completed, enjoy!&r");
    }

    /**
     * 储存配置
     * @param banlist 封禁列表
     * @param baninfolist 封禁信息列表
     */
    public static void saveConfig(List<BanListType> banlist,List<BanInfoType> baninfolist)
    {
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aModule saving started, please wait......&r");
        configs.clearFile("/BanInfoData/Bans.json");
        for(BanListType banlistthing:banlist)
        {
            configs.writeJson("/BanInfoData/Bans.json",new configs.ConfigKeyInfo(banlistthing.banId+".name",banlistthing.name));
            configs.writeJson("/BanInfoData/Bans.json",new configs.ConfigKeyInfo(banlistthing.banId+".ip",banlistthing.ip));
        }
        configs.clearFile("/BanInfoData/BanInfo.json");
        for(BanInfoType baninfolistthing:baninfolist)
        {
            configs.writeJson("/BanInfoData/BanInfo.json",new configs.ConfigKeyInfo(baninfolistthing.banId+".time",baninfolistthing.time));
            configs.writeJson("/BanInfoData/BanInfo.json",new configs.ConfigKeyInfo(baninfolistthing.banId+".reason",baninfolistthing.reason));
            configs.writeJson("/BanInfoData/BanInfo.json",new configs.ConfigKeyInfo(baninfolistthing.banId+".duration",baninfolistthing.duration));
        }
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aModule saving completed!&r");
    }

    /**
     * 封禁信息存储类
     * 用于存储玩家的封禁信息
     */
    public static class BanListType // 封禁信息储存
    {
        public BanListType() {}
        public BanListType(String name,String ip,int banId)
        {
            this.name=name;
            this.ip=ip;
            this.banId =banId;
        }
        public String name;      // 玩家名称
        public String ip;        // 玩家IP
        public int banId;
    }
    /**
     * 封禁信息提供类
     * 用于提供玩家的封禁状态和详细信息
     */
    public static class BanInfoType
    {
        public BanInfoType(){}
        public BanInfoType(long time,String reason,long duration,int banId)
        {
            this.time=time;
            this.reason=reason;
            this.duration=duration;
            this.banId=banId;
        }
        public long time;        // 解封时间（时间戳） 
        public String reason;    // 封禁原因
        public long duration;    // 封禁持续时间（毫秒）
        public int banId;        // ban id
    }

    /**
     * 封禁信息返回类
     * 用于返回玩家的封禁状态和详细信息
     */
    public static class BanReturnType // 封禁信息返回
    {
        public BanReturnType()
        {
            this.banned=false;
            this.name="";
            this.ip="";
            this.time=0;
            this.reason="";
            this.duration=0;
            this.pointer=-1;
            this.banId =-1;
            if(debug) logging.log(Level.INFO, banPrefix, "&b&lDebug: Using false root in BanReturnType");
        }
        public BanReturnType(boolean banned, String name, String ip, long time, String reason, long duration, int pointer, int banId) {
            this.banned=banned;
            this.name=name;
            this.ip=ip;
            this.time=time;
            this.reason=reason;
            this.duration=duration;
            this.pointer=pointer;
            this.banId =banId;
            if(debug) logging.log(Level.INFO, banPrefix, "&b&lDebug: Using true root in BanReturnType");
        }
        public boolean banned;  // 是否被封禁
        public String name;     // 玩家名称
        public String ip;       // 玩家IP地址
        public long time;
        public String reason;
        public long duration;
        public int pointer;     // 在封禁列表中的索引
        public int banId;       // ban id
    }

    /**
     * 向封禁列表添加一个封禁记录
     * 
     * @param banlist  封禁列表
     * @param name     玩家名称
     * @param ip       玩家IP地址
     * @param time     解封时间（时间戳）
     * @param reason   封禁原因
     * @param duration 封禁持续时间（毫秒）
     * @return 添加成功返回true
     */
    public static boolean addBan(List<BanListType> banlist, String name, String ip, long time, String reason, long duration)  // 封禁信息添加
    {
        BanListType banListThing=new BanListType(name, ip, baninfolist.size());
        BanInfoType BanInfoThing=new BanInfoType(time, reason, duration, baninfolist.size());
        banlist.add(banListThing);
        baninfolist.add(BanInfoThing);
        return true;
    }
    /**
     * 向封禁列表添加一个封禁记录
     *
     * @param banlist  封禁列表
     * @param name     玩家名称
     * @param ip       玩家IP地址
     * @param banId    封禁处理编号
     * @return 添加成功返回true
     */
    public static boolean addBan(List<BanListType> banlist, String name, String ip, int banId)
    {
        BanListType banListThing=new BanListType(name, ip, banId);
        banlist.add(banListThing);
        return true;
    }
    /**
     * 封禁玩家
     * 
     * @param banlist    封禁列表
     * @param name       玩家名称
     * @param ip         玩家IP地址
     * @param timeUp    封禁时间倍数
     * @param duration   封禁持续时间（毫秒）
     * @param reason     封禁原因
     * @param mercy      控制是否运行宽恕
     * @param timeBefore 封禁历史宽恕时间戳
     * @return 如果执行了解封波，返回解封的数量；否则返回-1
     */
    public static int ban(List<BanListType> banlist, String name, String ip, int timeUp, long duration, String reason, boolean mercy, long timeBefore)
    {
        int count = -1;
        // 如果启用了宽恕，执行解封操作
        if(mercy)
        {
          count = mercyWave(banlist,timeBefore);
        }
        reason = logging.ChangeColorcode(reason);
        int banId=baninfolist.size();
        for(BanListType banListThing : banlist)
        {
            if(banListThing.banId ==banId)
            {
                Player banedplayer = Bukkit.getPlayerExact(name);
                if (banedplayer!=null && banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(banPrefix + "\n&b诛仙!你被封印了!" + "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n" + formatTimeprd(duration, logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒")) + "&r" + "\n&c&l理由: &r&e&n" + reason));
                return count;
            }
            if(banListThing.name.equals(name) || banListThing.ip.equals(ip))
            {
                BanInfoType banInfoThing=baninfolist.get(banListThing.banId);
                BanInfoType newBan=baninfolist.get(banListThing.banId);
                newBan.time=banInfoThing.duration*timeUp+duration+System.currentTimeMillis();
                newBan.reason=reason;
                newBan.duration=banInfoThing.duration*timeUp+duration;
                baninfolist.set(banListThing.banId,newBan);
                if(banListThing.ip.equals(ip))
                {
                    addBan(banlist,name,ip,banListThing.banId);
                }
                if(banListThing.name.equals(name) && !banListThing.ip.equals(ip))
                {
                    banListThing.ip=ip;
                    banlist.set(banlist.indexOf(banListThing), banListThing);
                }
                Player banedplayer= Bukkit.getPlayerExact(name);
                if(banedplayer!=null && banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(banPrefix +"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formatTimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
            }
        }
        addBan(banlist,name,ip,duration+System.currentTimeMillis(),reason,duration);
        Player banedplayer= Bukkit.getPlayerExact(name);
        if(banedplayer!=null && banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(banPrefix +"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formatTimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
        return count;
    }

    /**
     * 解除玩家封禁
     * 
     * @param banId 封禁处理编号
     * @return 如果找到并解封了玩家，返回true；否则返回false
     */
    public static boolean unban(int banId)
    {
        boolean sec = false;
        BanInfoType banInfoThing=baninfolist.get(banId);
        banInfoThing.time=System.currentTimeMillis()-1;
        baninfolist.set(banId,banInfoThing);
        return sec;
    }

    /**
     * 重新匹配
     *
     * @param banlist    封禁列表
     * @param orgBanId   原封禁处理编号
     * @param newBanId   新的封禁处理编号
     * @return true
     */
    static boolean regroupBanList(List<BanListType> banlist, int orgBanId, int newBanId)
    {
        for(BanListType banListThing:banlist)
        {
            if(banListThing.banId == orgBanId)
            {
                banListThing.banId = newBanId;
            }
        }
        return true;
    }
    /**
     * 清理不配对的封禁ID
     *
     * @param banlist    封禁列表
     * @return true
     */
    static boolean cleanBanInfo(List<BanListType> banlist)
    {
        for(BanInfoType banInfoThing:baninfolist)
        {
            if(banInfoThing.banId !=baninfolist.indexOf(banInfoThing))
            {
                regroupBanList(banlist,banInfoThing.banId,baninfolist.indexOf(banInfoThing));
                banInfoThing.banId =baninfolist.indexOf(banInfoThing);
            }
        }
        return true;
    }
    /**
     * 执行解封波，清理过期的封禁记录
     * 
     * @param banlist    封禁列表
     * @param timeBefore 时间阈值，小于此时间的封禁记录将被移除
     * @return 移除的封禁记录数量
     */
    public static int mercyWave(List<BanListType> banlist, long timeBefore)
    {
        int count = 0;
        List<BanInfoType> toRemove = new ArrayList<>();
        Iterator<BanInfoType> iterator = baninfolist.iterator();
        while (iterator.hasNext())
        {
            iterator.next();
            BanInfoType banInfoThing;
            if (!iterator.hasNext())
            {
                break;
            }
            banInfoThing = iterator.next();
            if (banInfoThing.time <= timeBefore + System.currentTimeMillis())
            {
                toRemove.add(banInfoThing);
                count++;
            }
        }
        // 移除所有过期的封禁记录
        try
        {
            banlist.removeAll(toRemove);
        }
        catch(Exception e)
        {
            int Useless=0;
        }
        cleanBanInfo(banlist);
        return count;
    }

    /**
     * 检查封禁记录是否有效
     *
     * @param name    玩家名称
     * @param ip      玩家IP
     * @param list    要检查的封禁记录
     * @return 封禁信息返回对象，包含封禁状态和详细信息
     */
    static BanReturnType banTimeCheck(String name,String ip,BanInfoType list,BanListType listobj)
    {
        if(debug) logging.log(Level.INFO, banPrefix, "&b&lDebug: Find Place: "+banlist.indexOf(listobj));
        return new BanReturnType((list.time > System.currentTimeMillis() || list.duration == 0), name, ip, list.time, list.reason, list.duration,banlist.indexOf(listobj),list.banId);
    }

    public static BanReturnType isBanned(List<BanListType> banlist, String name, String ip)
    {
        if(debug)
        {
            logging.log(Level.INFO, banPrefix, "&b&lDebug: TargetIP: "+ip.split("/")[1] + " "+ip);
            for(BanListType list:banlist)
            {
                logging.log(Level.INFO, banPrefix, "&b&lDebug: FoundIP: "+list.ip.split("/")[0]);
            }
        }
        Optional<BanListType> banListThing = banlist.stream().filter(list->list.name.equalsIgnoreCase(name) || list.ip.split("/")[0].equalsIgnoreCase(ip.split("/")[1])).findFirst();
        if(banListThing.isPresent())
        {
            BanListType list=banListThing.get();
            // 解封时间判定
            return banTimeCheck(list.name,list.ip,baninfolist.get(list.banId),list);
        }
        return new BanReturnType();
    }
}