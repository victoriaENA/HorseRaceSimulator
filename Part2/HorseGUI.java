import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class Horse here.
 *
 * @author (Victoria Notarianni)
 * @version (10/04/25)
 */
public class HorseGUI {
    // Fields of class Horse
    private String name;                // The name of the horse (e.g., "PIPPI LONGSTOCKING")
    private char symbol;               // A single Unicode character that represents the horse (e.g., ♘)
    private int distanceTravelled;     // The distance travelled by the horse
    private boolean hasFallen;
    private double confidence;         // (between 0 and 1)
    private HorseBreed breed;           // The breed of the horse (e.g., STANDARDBRED, ARABIAN)
    private String coatColor;
    private HorseEquipment saddle;
    private HorseEquipment horseshoes;
    private HorseEquipment accessory;

    // performance metrics
    private int totalRaces = 0;
    private int racesWon = 0;
    private double totalSpeed = 0.0;
    private List<Double> finishingTimes = new ArrayList<>();
    private transient boolean raceCounted = false;
    private transient boolean winCounted = false;

    //Confidence
    private List<String> currentRaceConfidenceChanges = new ArrayList<>(); // Tracks changes for current race only
    private double currentRaceInitialConfidence; // Starting confidence for current race
    private double lastRaceFinalConfidence;      // Final confidence from previous race


    // Constructor of class Horse

    /**
     * Constructor for objects of class Horse
     */

    public HorseGUI(String name, HorseBreed breed, String coatColor, char symbol, double confidence) {
        this.name = name;
        this.breed = breed;
        this.coatColor = coatColor;
        this.symbol = symbol;
        this.confidence = confidence;  // Set the confidence during creation
        hasFallen = false;
        distanceTravelled = 0;
        this.saddle = HorseEquipment.STANDARD_SADDLE;
        this.horseshoes = HorseEquipment.STANDARD_SHOES;
        this.accessory = HorseEquipment.NONE;

    }


    // Other methods of class Horse

    /**
     * Returns the horse’s confidence rating
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Returns the distance travelled by the horse
     */
    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    /**
     * Returns the name of the horse
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the character used to represent the horse
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns true if the horse has fallen, false otherwise
     */
    public boolean hasFallen() {
        return hasFallen;
    }

    /**
     * Returns the breed of the horse
     */
    public HorseBreed getBreed() {
        return breed;
    }

    /**
     * Returns the coat color of the horse
     */
    public String getCoatColor() {
        return coatColor;
    }


    /**
     * Resets the horse to the start of the race
     */
    public void goBackToStart() {
        distanceTravelled = 0;
        hasFallen = false;
    }

    /**
     * Sets the horse as fallen
     */
    public void fall() {
        hasFallen = true;
    }

    /**
     * Increments the distance travelled by the horse by 1
     */
    public void moveForward() {
        distanceTravelled++;
    }

    /**
     * Sets the confidence rating of the horse to the given value
     */
    public void setConfidence(double newConfidence) {
        if (newConfidence >= 0.1 && newConfidence <= 0.9) {
            confidence = newConfidence;
        } else {
            System.out.println("Invalid confidence: must be between 0.1 and 0.9. Value not updated.");
        }
    }

    /**
     * Sets the horse’s symbol to the specified character
     */
    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }

    /**
     * Sets the horse’s breed to the specified value
     */
    public void setBreed(HorseBreed newBreed) {
        breed = newBreed;
    }

    /**
     * Sets the horse’s coat color to the specified value
     */
    public void setCoatColor(String newColor) {
        this.coatColor = newColor;

    }


    public void setSaddle(HorseEquipment saddle) {
        this.saddle = saddle;
    }

    public void setHorseshoes(HorseEquipment horseshoes) {
        this.horseshoes = horseshoes;
    }

    public void setAccessory(HorseEquipment accessory) {
        this.accessory = accessory;
    }

    public double getSpeedModifier() {
        return saddle.getSpeedModifier() * horseshoes.getSpeedModifier() * accessory.getSpeedModifier();
    }

    public double getStabilityModifier() {
        return saddle.getStabilityModifier() * horseshoes.getStabilityModifier() * accessory.getStabilityModifier();
    }

    public HorseEquipment getSaddle() {
        return saddle;
    }

    public HorseEquipment getHorseshoes() {
        return horseshoes;
    }

    public HorseEquipment getAccessory() {
        return accessory;
    }


    public double getBaseEndurance() {
        return this.getBreed().getEndurance();
    }


    public void incrementRaces() {
        // Only increment if not already incremented for this race
        if (!this.raceCounted) {
            totalRaces++;
            this.raceCounted = true;
        }
    }

    public void incrementWins() {
        if (!this.winCounted) {
            racesWon++;
            this.winCounted = true;
        }
    }

    // reset tracking for new races
    public void resetRaceTracking() {
        this.raceCounted = false;
        this.winCounted = false;
    }


    public void addFinishingTime(double time) {
        finishingTimes.add(time);
    }

    public void addSpeed(double speed) {
        totalSpeed += speed;
    }



    public double getAverageSpeed() {
        return totalRaces > 0 ? totalSpeed / totalRaces : 0;
    }

    public double getWinPercentage() {
        return totalRaces > 0 ? (racesWon * 100.0) / totalRaces : 0;
    }

    /**
     * Returns the horse's performance summary including confidence changes
     */
    public String getPerformanceSummary(int raceLength, double finishingTime) {
        StringBuilder summary = new StringBuilder();
        summary.append(name).append(":\n");

        if (totalRaces > 0) {
            // Status line
            if (distanceTravelled >= raceLength) {
                summary.append("Finishing time: ").append(String.format("%.2fs", finishingTime)).append("\n");
                summary.append("Status: Winner\n");
            } else if (hasFallen) {
                summary.append("Status: Fell\n");
            } else {
                summary.append("Status: DNF\n");  // Did Not Finish (but didn't fall)
            }

            // Performance metrics
            summary.append("Distance: ").append(distanceTravelled).append("/").append(raceLength).append("\n");
            summary.append("Speed: ").append(String.format("%.2f", getAverageSpeed())).append(" units/sec\n");
            summary.append("Win ratio: ").append(racesWon).append("/").append(totalRaces)
                    .append(" (").append(String.format("%.1f", getWinPercentage())).append("%)\n");
            summary.append(getConfidenceChangesString()).append("\n");
        } else {
            summary.append("No races completed\n");
        }

        return summary.toString();
    }

    /**
     * Prepares the horse for a new race, resetting tracking and setting initial confidence
     */
    public void prepareForNewRace() {
        // Save the final confidence from last race if available
        if (!currentRaceConfidenceChanges.isEmpty()) {
            String lastChange = currentRaceConfidenceChanges.get(currentRaceConfidenceChanges.size()-1);
            lastRaceFinalConfidence = Double.parseDouble(lastChange.split(": ")[1].trim());
        }

        // Reset for new race
        currentRaceConfidenceChanges.clear();
        currentRaceInitialConfidence = confidence; // Use current confidence (which may be from last race)
        recordInitialConfidence();
        resetRaceTracking();
    }

    /**
     * Records the initial confidence for the current race
     */
    public void recordInitialConfidence() {
        currentRaceConfidenceChanges.add(String.format("Initial: %.2f", this.confidence));
    }

    /**
     * Records confidence change after track condition adjustment
     */
    public void recordTrackConditionChange() {
        currentRaceConfidenceChanges.add(String.format("Track condition: %.2f", this.confidence));
    }

    /**
     * Records confidence change after falling
     */
    public void recordFallChange() {
        currentRaceConfidenceChanges.add(String.format("After fall: %.2f", this.confidence));
    }

    /**
     * Records confidence change after winning
     */
    public void recordWinChange() {
        currentRaceConfidenceChanges.add(String.format("After win: %.2f", this.confidence));
    }

    /**
     * Returns a formatted string of confidence changes for current race
     */
    public String getConfidenceChangesString() {
        if (currentRaceConfidenceChanges.isEmpty()) {
            return "Confidence: None";
        }
        return "Confidence: " + String.join(" -> ", currentRaceConfidenceChanges);
    }

}



