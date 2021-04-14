package coral.bedwars.trainer.abstraction.sessions;

import com.google.common.collect.Maps;

import coral.bedwars.trainer.abstraction.interfacing.sessions.TrainingSession;
import coral.bedwars.trainer.data.trainers.bridging.Bridging;
import coral.bedwars.trainer.faster.FastLocation;
import coral.bedwars.trainer.faster.values.MutableValue;
import coral.bedwars.trainer.faster.values.Numeric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MinecraftKey;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;

import java.util.Map;
import java.util.Optional;

import coral.bedwars.trainer.faster.FastLocation.HashPosition;

@Setter(value = AccessLevel.PROTECTED) @RequiredArgsConstructor
public abstract class BridgingSession implements TrainingSession {

    @Getter private final MutableValue<Integer> placed = Numeric.Integers.mutable();
    private final Map<Long, Double> movements = Maps.newHashMap();
    protected final Map<HashPosition, MinecraftKey> worldMask = Maps.newConcurrentMap();
    @Getter private final Bridging type;

    @Getter private long sessionStart;
    @Getter private int attempts;
    @Getter private boolean running;
    @Getter @Setter private boolean smartMode;
    @Getter private double peekSpeed;

    @Setter private Location firstPlace;

    public double getMovementSpeed() {
        movements.entrySet().removeIf(e -> e.getKey() + 500L < System.currentTimeMillis());
        double speed = Numeric.Doubles.sum(movements.values());
        peekSpeed = Math.max(speed, peekSpeed);
        return speed;
    }

    public void movement(Location from, Location to) {
        movements.put(System.currentTimeMillis(), from.distance(to));
    }

    public Optional<IBlockData> fakeBlock(BlockPosition blockPosition) {
        if (!worldMask.containsKey(blockPosition))
            return Optional.empty();

        return Optional.of(Block.REGISTRY.get(worldMask.get(blockPosition)).getBlockData());
    }

    public void removeFakeBlock(BlockPosition blockPosition) {
        worldMask.remove(blockPosition);
    }

    public void setFakeBlock(BlockPosition blockPosition, MinecraftKey minecraftKey) {
        worldMask.put(FastLocation.hash(blockPosition), minecraftKey);
    }

    public Optional<Location> firstPlace() {
        return Optional.ofNullable(firstPlace);
    }

}
