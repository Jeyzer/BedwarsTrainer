package coral.bedwars.trainer.data.trainers.bridging.sessions;

import com.google.common.collect.Sets;
import coral.bedwars.trainer.abstraction.sessions.BridgingSession;
import coral.bedwars.trainer.data.trainers.bridging.Bridging;
import coral.bedwars.trainer.faster.FastLocation;
import coral.bedwars.trainer.faster.Format;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import coral.bedwars.trainer.faster.FastLocation.HashPosition;

public class StraightBridging extends BridgingSession {

    public StraightBridging() {
        super(Bridging.STRAIGHT);
    }

    @Override
    public void stop(Player player) {
        setSessionStart(-1L);
        setRunning(false);
        getPlaced().setValue(0);

        Optional.ofNullable(player).ifPresent(other -> {
            Set<HashPosition> positions = Sets.newHashSet(worldMask.keySet());
            worldMask.clear();

            positions.forEach(blockPosition ->
                other.sendBlockChange(FastLocation.byHash(blockPosition), Material.AIR, (byte) 0));
        });
    }

    @Override
    public void start(Player player) {
        setSessionStart(System.currentTimeMillis());
        setAttempts(getAttempts() + 1);
        setRunning(true);
    }

    @Override
    public void sidebarTemplate(List<String> list) {
        list.add("§r");
        if (isRunning()) {
            list.add("§fCronometro: §e" + Format.timeStamp(getSessionStart()));
        }else{
            list.add("§ePiazza un blocco");
            list.add("§eper iniziare!");
        }
        list.add("§r ");
        list.add("§fDistanza: §b48.3");
        list.add("§fPiazzati: §b" + getPlaced());
        list.add("§fVelocità: §b" + Format.decimal(getMovementSpeed()) + " m/s");
        list.add("§r  ");
        list.add("§fModalità: §7" + getType().getName());
        list.add("§r   ");
        list.add("§eplay.coralmc.it");
    }

}
