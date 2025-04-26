public enum HorseEquipment {
    // Saddles
    LIGHT_SADDLE( 0.95, 1.05),  // Faster but higher fall risk
    HEAVY_SADDLE( 1.1, 0.9),    // Slower but more stable
    STANDARD_SADDLE( 1.0, 1.0),

    // Horseshoes
    STEEL_SHOES( 1.05, 0.95),
    ALUMINUM_SHOES( 0.9, 1.1),
    STANDARD_SHOES(1.0, 1.0),

    // Accessories
    RACING_BLANKET( 1.0, 1.0),
    LUCKY_HAT( 1.0, 0.95),
    NONE(1.0, 1.0);

    private final double speedModifier;
    private final double stabilityModifier; //measures endurance

    HorseEquipment( double speedModifier, double stabilityModifier) {
        this.speedModifier = speedModifier;
        this.stabilityModifier = stabilityModifier;
    }

    // Getters
    public double getSpeedModifier() { return speedModifier; }
    public double getStabilityModifier() { return stabilityModifier; }
}