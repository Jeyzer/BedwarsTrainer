package coral.bedwars.trainer.data.trainers.bridging;

import com.google.common.collect.Maps;
import coral.bedwars.trainer.abstraction.sessions.BridgingSession;
import coral.bedwars.trainer.abstraction.interfacing.Nominable;
import coral.bedwars.trainer.data.trainers.bridging.sessions.StraightBridging;

import coral.bedwars.trainer.faster.Spigot;
import coral.bedwars.trainer.packets.Reflection;
import coral.bedwars.trainer.settings.Items;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public enum Bridging implements Nominable {

    STRAIGHT("Rettilineo", StraightBridging.class);

    private final @Getter String name;
    private final Class<? extends BridgingSession> implementation;

    private final Map<Integer, BridgingSession> sessions = Maps.newHashMap();

    public static Optional<Bridging> byName(String name) {
        return Optional.of(Bridging.valueOf(name.toUpperCase()));
    }

    public void endSession(Player player) {
        Validate.notNull(player, "Null players can't have training sessions");
        Optional.of(sessions.get(player.getEntityId())).ifPresent(session -> {
            session.stop(player);
            sessions.remove(player.getEntityId());
        });
    }

    @SneakyThrows
    public BridgingSession newSession(Player player) {
        Validate.notNull(player, "Null players can't have training sessions");
        player.getInventory().setItem(5, Items.SmartMode.DISABLED);
        BridgingSession session = implementation.getConstructor().newInstance();
        sessions.put(player.getEntityId(), session);
        return session;
    }

    public Optional<BridgingSession> session(Player player) {
        return Optional.ofNullable(sessions.get(player.getEntityId()));
    }

    public static int sum() {
        int size = 0;

        for (Bridging bridging : Bridging.values())
            size += bridging.sessions.size();

        return size;
    }

    public static Object handleOut(Plugin plugin, Player player, Object obj) {
        Optional<Bridging> bridging = Bridging.byName(player.getMetadata("bridging").get(0).asString());

        if (!bridging.isPresent()) {
            player.removeMetadata("bridging", plugin);
            return obj;
        }

        PacketPlayOutBlockChange packet = (PacketPlayOutBlockChange) obj;
        BlockPosition blockPosition = Reflection.getField(PacketPlayOutBlockChange.class, "a", BlockPosition.class).from(packet);

        if (blockPosition.getX() > 14)
            return packet;

        bridging.get().session(player).flatMap(bridgingSession ->
           bridgingSession.fakeBlock(blockPosition)).ifPresent(data -> packet.block = data);

        return packet;
    }

    public static Object handleIn(Plugin plugin, Player player, Object obj) {
        PacketPlayInBlockPlace packet = (PacketPlayInBlockPlace) obj;
        Optional<Bridging> bridging = Bridging.byName(player.getMetadata("bridging").get(0).asString());

        if (!bridging.isPresent()) {
            player.removeMetadata("bridging", plugin);
            return obj;
        }

        org.bukkit.inventory.PlayerInventory playerInventory = player.getInventory();
        Optional<BridgingSession> optional = bridging.get().session(player);

        if (!optional.isPresent()) {
            player.removeMetadata("bridging", plugin);
            return null;
        }

        BridgingSession session = optional.get();

        switch (playerInventory.getHeldItemSlot()) {
            case 5:
                session.setSmartMode(!session.isSmartMode());
                player.sendMessage("§fHai " + (session.isSmartMode() ? "§aabilitato" : "§cdisabilitato") + "§f la modalità intelligente.");
                playerInventory.setItem(5, session.isSmartMode() ? Items.SmartMode.ENABLED : Items.SmartMode.DISABLED);
            case 7:
            case 9:
        }

        BlockPosition blockPosition = packet.a();
        ItemStack itemStack = packet.getItemStack();

        if ((blockPosition.getX() == -1 && blockPosition.getZ() == -1) || itemStack == null)
            return null;

        blockPosition = blockPosition.shift(Spigot.direction(packet.getFace()));

        if (blockPosition.getX() < 13)
            return null;

        if (!session.isRunning()) {
            session.start(player);

            if (session.isSmartMode())
                session.setFirstPlace(player.getLocation().clone());
        }

        session.setFakeBlock(blockPosition, new MinecraftKey(packet.getItemStack().getItem().getName()));
        session.getPlaced().increment();

        org.bukkit.inventory.ItemStack hand = player.getItemInHand();

        if (hand.getAmount() == 1) {
            player.setItemInHand(null);
            return null;
        }

        hand.setAmount(hand.getAmount() - 1);
        return null;
    }

    public static void chat(Player player, String message) {
        Bukkit.getOnlinePlayers().forEach(others -> {
            if (!others.hasMetadata("bridging"))
                return;

            others.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message);
        });
    }

}
