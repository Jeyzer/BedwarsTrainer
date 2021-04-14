package coral.bedwars.trainer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EntryListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

}
