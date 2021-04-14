package coral.bedwars.trainer.scoreboards;

import com.google.common.collect.Maps;
import coral.bedwars.trainer.faster.Nullation;
import coral.bedwars.trainer.settings.Values;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class BoardsHandler {

    private static final String TITLE = "§b§lCORAL§f§lMC";

    private final Map<UUID, CoralScoreboard> boards = Maps.newConcurrentMap();

    public BoardsHandler(Plugin plugin) {
        new SidebarRunnable().runTaskTimerAsynchronously(plugin, 5L, Values.Intervals.SIDEBAR);
        Bukkit.getServer().getPluginManager().registerEvents(new BoardsListener(), plugin);

        Bukkit.getOnlinePlayers().forEach(this::createBoard);
    }

    /**
     * Creates a board to a player.
     *
     * @param player the player
     * @return the newly created board
     */
    public CoralScoreboard createBoard(Player player) {
        return createBoard(player, null, TITLE);
    }

    /**
     * Creates a board to a player, using a predefined scoreboard.
     *
     * @param player     the player
     * @param scoreboard the scoreboard to use
     * @param name       the name of the board
     * @return the newly created board
     */
    public CoralScoreboard createBoard(Player player, Scoreboard scoreboard, String name) {
        deleteBoard(player);

        CoralScoreboard coralScoreboard = new CoralScoreboard(this, player, TITLE);
        boards.put(player.getUniqueId(), coralScoreboard);
        return coralScoreboard;
    }

    /**
     * Deletes the board of a player.
     *
     * @param player the player
     */
    public void deleteBoard(Player player) {
        Nullation.nonNull(boards.get(player.getUniqueId()))
                .then(CoralScoreboard::delete);
    }

    /**
     * Removes the board of a player from the boards map.<br>
     * <b>WARNING: Do not use this to delete the board of a player!</b>
     *
     * @param player the player
     */
    public void removeBoard(Player player) {
        boards.remove(player.getUniqueId());
    }

    /**
     * Checks if the player has a board.
     *
     * @param player the player
     * @return <code>true</code> if the player has a board, otherwise <code>false</code>
     */
    public boolean hasBoard(Player player) {
        return boards.containsKey(player.getUniqueId());
    }

    /**
     * Gets the board of a player.
     *
     * @param player the player
     * @return the player board, or null if the player has no board
     */
    @Nullable
    public CoralScoreboard getBoard(Player player) {
        return boards.get(player.getUniqueId());
    }

    @RequiredArgsConstructor
    private class BoardsListener implements Listener {

        private final BoardsHandler handler = BoardsHandler.this;

        @EventHandler(priority = EventPriority.HIGHEST)
        public void playerJoin(PlayerJoinEvent event) {
            createBoard(event.getPlayer());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void playerQuit(PlayerQuitEvent event) {
            deleteBoard(event.getPlayer());
        }
    }

    private class SidebarRunnable extends BukkitRunnable {

        @Override
        public void run() {
            boards.values().forEach(CoralScoreboard::refresh);
        }

    }

}
