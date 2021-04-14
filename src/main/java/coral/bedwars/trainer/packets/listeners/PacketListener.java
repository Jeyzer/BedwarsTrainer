package coral.bedwars.trainer.packets.listeners;

// Turned to potato code since Feryzz wants shit to be done faster

import coral.bedwars.trainer.data.trainers.bridging.Bridging;
import coral.bedwars.trainer.packets.TinyProtocol;

import io.netty.channel.Channel;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class PacketListener extends TinyProtocol {

    public PacketListener(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Object onPacketInAsync(Player player, Channel channel, Object obj) {
        if (obj instanceof PacketPlayInArmAnimation && player.hasMetadata("bridging")) {
            return null;
        }

        if (obj instanceof PacketPlayInBlockPlace && player.hasMetadata("bridging"))
            return Bridging.handleIn(plugin, player, obj);

        if (obj instanceof PacketPlayInChat) {
            PacketPlayInChat packet = (PacketPlayInChat) obj;

            if (player.hasMetadata("bridging")) {
                Bridging.chat(player, packet.a());
                return null;
            }

            Bukkit.getOnlinePlayers().forEach(others ->
               others.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + packet.a()));
            return obj;
        }

        return obj;
    }

    @Override
    public Object onPacketOutAsync(Player player, Channel channel, Object obj) {
        if (obj instanceof PacketPlayOutBlockChange && player.hasMetadata("bridging"))
            return Bridging.handleOut(plugin, player, obj);

        if (obj instanceof PacketPlayInChat) {
            PacketPlayInChat packet = (PacketPlayInChat) obj;

            if (player.hasMetadata("bridging")) {
                Bridging.chat(player, packet.a());
                return null;
            }

            Bukkit.getOnlinePlayers().forEach(others ->
               others.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + packet.a()));
            return obj;
        }
        return obj;
    }

    private int hash(BlockPosition blockPosition) {
        return Objects.hash(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

}
