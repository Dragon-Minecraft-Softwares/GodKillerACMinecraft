// Created by HelloWorldCoder on 2025/7/27 15:07:17
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import static DragonUtils.utils.plugin;
import org.json.JSONObject;

public class configs
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [configs] &r&ain &r&6&_DragonUtils by DragonMinecraftSoftwares&r&a !&r");
    }

    /**
     * 配置文件路径
     */
    public static String DataFolder;

    /**
     * 配置文件键值
     * @String key 键
     * @Object data 值
     */
    public static class ConfigKeyInfo
    {
        /**
         * 配置文件键值
         * @param key
         * @param data
         */
        public ConfigKeyInfo(String key,Object data)
        {
            this.key=key;
            this.data=data;
        }
        public String key;
        public Object data;
    }

    /**
     * 配置项描述
     * @String path 配置项路径(相对于插件储存目录,前面加/)
     * @String name 键值路径
     */
    public static class ConfigDescribeType
    {
        /**
         * 配置项描述
         * @param path 配置项路径(相对于插件储存目录,前面加/)
         * @param name 键值路径
         */
        public ConfigDescribeType(String path, String name)
        {
            this.path=path;
            this.name=name;
        }
        public String path;
        public String name;
    }
    /**
     * 配置初始化(特殊:此处name代表文件名)
     * @param ConfigList 配置项列表
     */
    public static void init(List<ConfigDescribeType> ConfigList)
    {
        DataFolder=plugin.getDataFolder().getName();
        if(!new File(DataFolder).exists())
        {
            new File(DataFolder).mkdirs();
        }
        for(ConfigDescribeType ConfigDescribe:ConfigList)
        {
            if(!new File(DataFolder+ConfigDescribe.path).exists())
            {
                new File(DataFolder+ConfigDescribe.path).mkdirs();
            }
            if(!new File(DataFolder+ConfigDescribe.path+"/"+ConfigDescribe.name).exists())
            {
                plugin.saveResource(ConfigDescribe.path+ConfigDescribe.name,false);
            }
        }
    }
    /**
     * Json读取
     * @param aim 配置信息
     */
     public static Object readJson(ConfigDescribeType aim)
     {
         try
         {
             JSONObject jsonObject = new JSONObject(Files.readAllBytes(Paths.get(DataFolder+aim.path)));
             try
             {
                 return jsonObject.get(aim.name);
             }
             catch(Exception e)
             {
                 if(aim.name!=null && !aim.name.isEmpty())
                 {
                     String[] keys=aim.name.split("\\.");
                     for(String key:keys)
                     {
                         jsonObject=jsonObject.getJSONObject(key);
                     }
                 }
                 return new ArrayList<>(jsonObject.keySet());
             }
         }
         catch(Exception e)
         {
             e.printStackTrace();
             logging.log(Level.SEVERE,"&6[DragonUtils] &r","&4工具错误!文件大于2GB或产生了IO错误,详情请看StackTrace");
         }
         return "Config Module: Read Json ERROR!";
     }
     /**
     * Json写入
     * @param file 配置文件
     * @param data 键值
     */
     public static boolean writeJson(String file,ConfigKeyInfo data)
     {
         try
         {
             JSONObject jsonObject = new JSONObject(Files.readAllBytes(Paths.get(DataFolder+file)));
             try{jsonObject.remove(data.key);}catch(Exception e){int NoUse=1+1;}
             jsonObject.put(data.key,data.data);
             Files.write(Paths.get(DataFolder+file),jsonObject.toString().getBytes());
             return true;
         }
         catch(Exception e)
         {
             e.printStackTrace();
             logging.log(Level.SEVERE,"&6[DragonUtils] &r","&4工具错误!文件大于2GB或产生了IO错误,详情请看StackTrace");
         }
         return false;
     }
    /**
     * Json删除键值
     * @param target 键值
     */
    public static boolean removeJson(ConfigDescribeType target)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(Files.readAllBytes(Paths.get(DataFolder+target.path)));
            jsonObject.remove(target.name);
            Files.write(Paths.get(DataFolder+target.path),jsonObject.toString().getBytes());
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logging.log(Level.SEVERE,"&6[DragonUtils] &r","&4工具错误!文件大于2GB或产生了IO错误,详情请看StackTrace");
        }
        return false;
    }
    /**
     * 清楚文件
     * @param file 文件路径
     */
    public static boolean clearFile(String file)
    {
        try
        {
            Files.write(Paths.get(DataFolder+file),"".getBytes());
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logging.log(Level.SEVERE,"&6[DragonUtils] &r","&4工具错误!文件大于2GB或产生了IO错误,详情请看StackTrace");
        }
        return false;
    }
}