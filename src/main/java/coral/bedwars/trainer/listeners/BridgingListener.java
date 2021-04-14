package coral.bedwars.trainer.listeners;

import coral.bedwars.trainer.data.trainers.bridging.Bridging;
import coral.bedwars.trainer.faster.FastLocation;
import coral.bedwars.trainer.settings.Locations;

import lombok.RequiredArgsConstructor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@RequiredArgsConstructor
public class BridgingListener implements Listener {

    private final Plugin plugin;

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!player.hasMetadata("bridging"))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void playerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (FastLocation.isEqual(from, to, false, true, false)
                || !(player.hasMetadata("bridging")))
            return;

        Optional<Bridging> bridging = Bridging.byName(player.getMetadata("bridging").get(0).asString());

        if (!bridging.isPresent()) {
            player.removeMetadata("bridging", plugin);
            return;
        }

        bridging.get().session(player).ifPresent(session -> {
            if (session.isSmartMode() && System.currentTimeMillis() - session.getSessionStart() < 450L) {
                event.setTo(from);
                return;
            }

            if (to.getY() < 62.5) {
                session.stop(player);
                player.teleport(
                   session.isSmartMode()
                   ? session.firstPlace().isPresent()
                   ? session.firstPlace().get()
                   : Locations.SPAWN
                   : Locations.SPAWN);
            }

            session.movement(from, to);
        });
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!player.hasMetadata("bridging"))
            return;

        Optional<Bridging> bridging = Bridging.byName(player.getMetadata("bridging").get(0).asString());

        if (!bridging.isPresent())
            return;

        bridging.get().endSession(player);
        player.removeMetadata("bridging", plugin);
    }

}
