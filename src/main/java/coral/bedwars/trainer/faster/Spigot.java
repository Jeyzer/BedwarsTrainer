package coral.bedwars.trainer.faster;

import net.minecraft.server.v1_8_R3.EnumDirection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.minecraft.server.v1_8_R3.EnumDirection.*;

public class Spigot {

    private Spigot() {
        throw new AssertionError("Nope.");
    }

    public static String colors(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> colors(List<String> list) {
        List<String> result = new ArrayList<>();

        for (String s : list) {
            String colors = colors(s);
            result.add(colors);
        }

        return result;
    }

    public static void timeStamp(Runnable runnable, boolean nano) {
        if (nano) {
            long now = System.nanoTime();
            runnable.run();
            Bukkit.getLogger().info(System.nanoTime() - now + "ns");
            return;
        }

        long now = System.currentTimeMillis();
        runnable.run();
        log(System.currentTimeMillis() - now + " ms");
    }

    public static void log(Object... objects) {
        for (Object o : objects)
            Bukkit.getLogger().info(o.toString());
    }

    public static EnumDirection direction(int i) {
        return i == 5 ? EAST
           : i == 4 ? WEST
           : i == 3 ? SOUTH
           : i == 2 ? NORTH
           : i == 1 ? UP
           : DOWN;
    }

}
