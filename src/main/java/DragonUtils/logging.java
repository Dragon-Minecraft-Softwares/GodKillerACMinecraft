// Created by HelloWorldCoder on 2025/5/27 13:38
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonUtils;

import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

public final class logging
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [logging] &r&ain &r&6&_DragonUtils by DragonMinecraftSoftwares&r&a !&r");
    }
    public static String ANSIDataBase(char colorcode) // ANSI数据库,手打
    {
        if(colorcode=='0') return "\u001B[30;22m";
        if(colorcode=='1') return "\u001B[34;22m";
        if(colorcode=='2') return "\u001B[32;22m";
        if(colorcode=='3') return "\u001B[36;22m";
        if(colorcode=='4') return "\u001B[31;22m";
        if(colorcode=='5') return "\u001B[35;22m";
        if(colorcode=='6') return "\u001B[33;22m";
        if(colorcode=='7') return "\u001B[37;22m";
        if(colorcode=='8') return "\u001B[30;22m";
        if(colorcode=='9') return "\u001B[34;22m";
        if(colorcode=='a') return "\u001B[32;22m";
        if(colorcode=='b') return "\u001B[36;22m";
        if(colorcode=='c') return "\u001B[31;22m";
        if(colorcode=='d') return "\u001B[35;22m";
        if(colorcode=='e') return "\u001B[33;22m";
        if(colorcode=='f') return "\u001B[37;22m";
        if(colorcode=='k') return "\u001B[5m";
        if(colorcode=='l') return "\u001B[1m";
        if(colorcode=='m') return "\u001B[9m";
        if(colorcode=='n') return "\u001B[4m";
        if(colorcode=='o') return "\u001B[3m";
        if(colorcode=='r') return "\u001B[0m\u001B[0;39m\u001B[24m";
        if(colorcode=='_') return "\u001B[21m";
        if(colorcode=='-') return "&";
        return "\u001B[31;22m\u001B[21m[ERROR]\u001B[0;39m\u001B[31;22m Color Code NOT FOUND: "+colorcode+"!\u001B[0m\u001B[0;39m\u001B[24m";
    }
    public static String ChangeColorcode(String Source)
    {
        return Source.replaceAll("&","§"); // 初始化字符串
    }
    public static String ANSIChange(String Source) // ANSI转换器
    {
        Source=ChangeColorcode(Source);
        while(Source.contains("§")) // 查找
        {
            char colorcode=Source.charAt(Source.indexOf("§")+1);
            Source=Source.replaceAll("§"+colorcode,ANSIDataBase(colorcode)); // 替换
        }
        return Source;
    }
    public static boolean log(Level level,String prefix,String message) // 日志输出
    {
        getLogger().log(level,ANSIChange(prefix+message+"&r"));
        return true;
    }
}