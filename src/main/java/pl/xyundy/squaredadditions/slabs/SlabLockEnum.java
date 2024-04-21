package pl.xyundy.squaredadditions.slabs;

public enum SlabLockEnum {
    DEFAULT_AUTO,
    BOTTOM_SLAB,
    TOP_SLAB,
    NORTH_SLAB_VERTICAL,
    SOUTH_SLAB_VERTICAL,
    EAST_SLAB_VERTICAL,
    WEST_SLAB_VERTICAL,
    VANILLA_PLACEMENT;

    public static final SlabLockEnum[] POSITION_VALUES = values();

    public SlabLockEnum loopForward() {
        return POSITION_VALUES[(ordinal() + 1) % POSITION_VALUES.length];
    }

    public SlabLockEnum loopBackward() {
        return POSITION_VALUES[(ordinal() - 1  + POSITION_VALUES.length) % POSITION_VALUES.length];
    }

    public SlabLockEnum loop(boolean sneaking) {
        if (sneaking) {
            return loopBackward();
        } else {
            return loopForward();
        }
    }
}