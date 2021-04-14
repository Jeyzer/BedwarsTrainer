package coral.bedwars.trainer.faster;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class FastLocation extends Location {

    private static final String DEFAULT_WORLD = "empty";

    private FastLocation(String world, double x, double y, double z, float yaw, float pitch) {
        super(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public String toString(boolean includeWorld) {
        return (includeWorld ? getWorld().getName() : "") + ", " + getX() + ", " + getY() + ", " + getZ();
    }

    public static FastLocation values(double x, double y, double z) {
        return new FastLocation(DEFAULT_WORLD, x, y, z, -1, -1);
    }

    public static FastLocation values(double x, double y, double z, float yaw, float pitch) {
        return new FastLocation(DEFAULT_WORLD, x, y, z, yaw, pitch);
    }

    public static FastLocation byHash(HashPosition hashPosition) {
        return FastLocation.values(hashPosition.getX(), hashPosition.getY(), hashPosition.getZ());
    }

    public static HashPosition hash(int x, int y, int z) {
        return new HashPosition(x, y, z);
    }

    public static HashPosition hash(BlockPosition blockPosition) {
        return new HashPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    public static boolean isEqual(Location from, Location to, boolean involveWorlds, boolean involveDecimals, boolean involveVertical) {
        if (involveWorlds && !from.getWorld().getName().equals(to.getWorld().getName()))
            return false;

        if (involveDecimals) {
            if (involveVertical) {
                return from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ();
            }

            return from.getX() == to.getX() && from.getZ() == to.getZ();
        }

        if (involveVertical)
            return from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ();

        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
    }

    public static class HashPosition extends BlockPosition {

        public HashPosition(int x, int y, int z) {
            super(x, y, z);
        }

        public int hashCode() {
            return Objects.hash(getX(), getY(), getZ());
        }

    }

}
