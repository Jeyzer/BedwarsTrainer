package coral.bedwars.trainer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class NatureListener implements Listener {

    @EventHandler
    public void weatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM)
            return;

        event.setCancelled(true);
    }

}
