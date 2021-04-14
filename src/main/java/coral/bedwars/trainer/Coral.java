package coral.bedwars.trainer;

import coral.bedwars.trainer.commands.EnterCommand;
import coral.bedwars.trainer.commands.LocationDebugCommand;
import coral.bedwars.trainer.faster.Nullation;
import coral.bedwars.trainer.faster.Spigot;
import coral.bedwars.trainer.listeners.BridgingListener;
import coral.bedwars.trainer.listeners.EntryListener;
import coral.bedwars.trainer.listeners.LocationsListener;
import coral.bedwars.trainer.listeners.NatureListener;
import coral.bedwars.trainer.packets.listeners.PacketListener;
import coral.bedwars.trainer.scoreboards.BoardsHandler;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public final class Coral extends JavaPlugin {

    private static LuckPerms lp;

    public void onEnable() {
        Spigot.timeStamp(() -> {
            lp = LuckPermsProvider.get();
            new BoardsHandler(this);
            new PacketListener(this);
            registerCommands();
            registerListeners(
               new EntryListener(),
               new NatureListener(),
               new LocationsListener(),
               new BridgingListener(this));
        }, true);
    }

    private void registerCommands() {
        getCommand("bridging").setExecutor(new EnterCommand(this));
        getCommand("debugloc").setExecutor(new LocationDebugCommand());
    }

    @Nullable
    public static Group getGroup(Player player) {
        return Nullation.nonNull(lp.getUserManager().getUser(player.getName()))
                .get(User::getCachedData)
                .get(CachedDataManager::getMetaData)
                .get(CachedMetaData::getPrimaryGroup)
                .get(lp.getGroupManager()::getGroup)
                .finalized();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    public void onDisable() {
        lp = null;
    }

}
