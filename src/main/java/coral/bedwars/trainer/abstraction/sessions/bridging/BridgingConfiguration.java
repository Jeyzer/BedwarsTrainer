package coral.bedwars.trainer.abstraction.sessions.bridging;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(fluent = true, chain = true)
public class BridgingConfiguration {

    private BridgeLength length;
    private BridgeHeight height;
    private BridgeDirection direction;
    private BridgingMode mode;
    private HitDirection hit;

    public enum BridgeLength {
        SHORT,
        MEDIUM,
        LONG,
        MASSIVE
    }

    public enum BridgeHeight {
        NONE,
        FOUR_STACK,
        THREE_STACK,
        TWO_STACK,
        SCORPION
    }

    public enum BridgeDirection {
        FORWARD,
        SEMI_DIAGONAL,
        DIAGONAL
    }

    public enum HitDirection {
        NONE,
        ALL_DIRECTIONS,
        FORWARD,
        SIDEWAYS,
        BACKWARDS
    }

    public enum BridgingMode {
        NORMAL,
        LADDER,
        DISPERSION,
        DOUBLE_PLACE
    }

}