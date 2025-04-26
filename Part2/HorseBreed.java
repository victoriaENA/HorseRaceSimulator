public enum HorseBreed {
    STANDARDBRED(5.0), //standard endurance
    ARABIAN(7.0), // higher endurance
    MUSTANG(6.0); //best

    private final double endurance;

    HorseBreed(double endurance) {
        this.endurance = endurance;
    }

    public double getEndurance() {
        return endurance;
    }
}