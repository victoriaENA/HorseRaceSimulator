import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

/**
 *A horse race with a configurable number of lanes for given distance
 * 
 * @author Victoria Notarianni
 * @version 2.0
 */
public class RaceGUI
{
    private int raceLength;
    private List<HorseGUI> horses;
    private String unit; // Unit for race length scaling
    private TrackCondition trackCondition;// CHANGE: Use a list instead of fixed lanes
    
   /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack
     * @param numLanes the number of lanes in the race
     */
   public RaceGUI(int distance, int numLanes, String unit, TrackCondition trackCondition) // CHANGE: Added numLanes parameter
    {
        this.unit = unit;
        this.raceLength = scaleLengthByUnit(distance, unit);
        this.trackCondition = trackCondition;
        horses = new ArrayList<>(numLanes); // CHANGE: Initialize list dynamically

        // Fill the list with null to represent empty lanes
        for (int i = 0; i < numLanes; i++) {
            horses.add(null);
        }
    }

    /**
     * Scales the race length based on the chosen unit
     *
     * @param baseLength the original race length
     * @param unit the selected unit of measurement
     * @return the scaled race length
     */
    private int scaleLengthByUnit(int baseLength, String unit) {
        switch (unit.toLowerCase()) {
            case "yards":
                return (int) Math.round(baseLength * 0.8); // Adjusting for yards
            case "centimeters":
                return (int) Math.round(baseLength * 0.6); // Adjusting for centimeters
            case "meters":
            default:
                return baseLength; // Default is meters (unchanged)
        }
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(HorseGUI theHorse, int laneNumber)
    {
        if (laneNumber > 0 && laneNumber <= horses.size()) // CHANGE: Validate lane number
        {
            horses.set(laneNumber - 1, theHorse); // CHANGE: Adjust index for zero-based list
        }
        else
        {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }

    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    /*public void startRace()
    {
        boolean finished = false;

        for (HorseGUI horse : horses) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }
        printRace(); // Show horses at position 0 before any movement
        int activeCount = 0; //no of horses not fallen

        while (!finished)
        {
            //Move horse
            for (HorseGUI horse : horses) {
                if (horse != null) {
                    moveHorse(horse);
                }
            }

            // Check if any horse won by crossing the finish line
            HorseGUI winner = null;
            for (HorseGUI horse : horses) {
                if (horse != null && raceWonBy(horse)) {
                    winner = horse;
                    break;
                }
            }

            if (winner != null) {
                adjustConfidence(winner, 0.1); //  Increase confidence for winner
                finished = true; //Race ends & loop ends
            } else {
                // Reset active count for the current loop
                activeCount = 0;
                
                // Count horses that haven't fallen
                for (HorseGUI horse : horses) {
                    if (horse != null && !horse.hasFallen()) {
                        activeCount++;
                    }
                }
                // If all horses have fallen
                if (activeCount == 0) {
                    finished = true;
                }
            }

            //wait for 500 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            printRace();

        }

        printWinner(activeCount);
    } */

    public void adjustConfidenceForTrack(HorseGUI horse) {
        double confidenceAdjustment = 0.0;

        switch (trackCondition) {
            case MUDDY:
                confidenceAdjustment = -0.1; // Slight nerves in mud
                break;
            case ICY:
                confidenceAdjustment = -0.2; // Very nervous on ice
                break;
            case DRY:
                confidenceAdjustment = 0.1; // Boost from good conditions
                break;
        }

        adjustConfidence(horse, confidenceAdjustment);
    }


    public void moveAllHorses() {
        for (HorseGUI horse : horses) {
            if (horse != null) {
                moveHorse(horse);  // Calls the private method internally
            }
        }
    }

    /**
     * Modifies horse movement based on track conditions and confidence
     *
     * @param theHorse the horse to be moved
     */
    private void moveHorse(HorseGUI theHorse) {
        if (!theHorse.hasFallen()) {
            double speedModifier = 1.0;
            double fallRiskModifier = 1.0;

            // Adjust values based on track conditions
            switch (trackCondition) {
                case MUDDY:
                    speedModifier = 0.7;  // Slower speed
                    fallRiskModifier = 1.2; // Slightly higher fall chance
                    break;
                case ICY:
                    speedModifier = 0.9;  // Almost normal speed
                    fallRiskModifier = 1.5; // Higher fall risk
                    break;
                case DRY:
                    speedModifier = 1.2;  // Faster movement
                    fallRiskModifier = 0.8; // Reduced fall risk
                    break;
            }

            // Apply equipment modifiers
            speedModifier *= theHorse.getSpeedModifier();
            fallRiskModifier *= theHorse.getStabilityModifier();

            // Adjust horse movement
            if (Math.random() < (theHorse.getConfidence() * speedModifier)) {
                theHorse.moveForward();
            }

            // Adjust fall chance
            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence() * fallRiskModifier)) {
                theHorse.fall();
                adjustConfidence(theHorse, -0.1);
            }
        }
    }

    // Adjust horse confidence while ensuring it stays between 0 and 1
    public void adjustConfidence(HorseGUI horse, double adjustment) {
        double newConfidence = Math.max(0.1, Math.min(0.9, horse.getConfidence() + adjustment));
        newConfidence =   Math.round(newConfidence * 100.0) / 100.0;
        horse.setConfidence(newConfidence);
    }

    public HorseGUI getWinner() {
        for (HorseGUI horse : horses) {
            if (horse != null && raceWonBy(horse)) {
                return horse;  // Returns the winning horse
            }
        }
        return null;  // No winner yet
    }
        
    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(HorseGUI theHorse)
    {
        return theHorse.getDistanceTravelled() >= raceLength; // CHANGE: Allow distance to exceed race length
    }

    /**
     * Creates a formatted list of race track display strings for GUI rendering
     * @return List of strings representing the track display
     */
    public List<String> getRaceTrackDisplay() {
        List<String> trackDisplay = new ArrayList<>();
        int trackWidth = raceLength + 2; // Track length plus borders

        // Top border
        trackDisplay.add(multiplePrint('=', trackWidth));

        // Generate lane representations
        for (HorseGUI horse : horses) {
            StringBuilder lane = new StringBuilder();
            lane.append('|'); // Start lane border

            int spacesBefore = (horse != null) ? horse.getDistanceTravelled() : 0;
            int spacesAfter = raceLength - spacesBefore;

            // Add spaces before the horse
            lane.append(multiplePrint(' ', spacesBefore));

            // Add horse symbol or fallen marker
            lane.append((horse != null) ? (horse.hasFallen() ? "X" : horse.getSymbol()) : " ");

            // Add spaces after the horse
            lane.append(multiplePrint(' ', spacesAfter));

            lane.append('|'); // End lane border

            // Include horse details if present
            if (horse != null) {
                lane.append(" " + horse.getName() + " (" + horse.getConfidence() + ")");
            }

            trackDisplay.add(lane.toString());
        }

        // Bottom border
        trackDisplay.add(multiplePrint('=', trackWidth));

        return trackDisplay;
    }

    public boolean isFinished() {
        boolean anyHorseFinished = false;
        boolean allHorsesFallen = true;

        for (HorseGUI horse : horses) {
            if (horse != null) {
                if (horse.getDistanceTravelled() >= raceLength) {
                    anyHorseFinished = true;
                }
                if (!horse.hasFallen()) {
                    allHorsesFallen = false;
                }
            }
        }

        return anyHorseFinished || allHorsesFallen;
    }

    /**
     * Generates a repeated string of a given character
     *
     * @param aChar the character to repeat
     * @param times the number of repetitions
     * @return the formatted string
     */
    private String multiplePrint(char aChar, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(aChar);
        }
        return result.toString();
    }

    public List<HorseGUI> getHorses() {
        if (horses == null) {
            horses = new ArrayList<>(); // Ensure an empty list is returned instead of null
        }
        return horses;
    }

    // Setter method for track condition
    public void setTrackCondition(TrackCondition trackCondition) {
        this.trackCondition = trackCondition;

    }

    public HorseGUI getHorseInLane(int laneNumber) {
        if (laneNumber > 0 && laneNumber <= horses.size()) {
            return horses.get(laneNumber - 1); // Adjust for zero-based index
        }
        return null; // No horse in this lane or invalid lane number
    }
}
