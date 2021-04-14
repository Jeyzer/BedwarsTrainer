package coral.bedwars.trainer.listeners;

import coral.bedwars.trainer.settings.Locations;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LocationsListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(Locations.SPAWN);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID || !(event.getEntity() instanceof Player))
            return;

        event.setCancelled(true);
        event.getEntity().teleport(Locations.SPAWN);
    }

}
