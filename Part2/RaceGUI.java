import java.util.concurrent.TimeUnit;
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
    private List<HorseGUI> horses; // CHANGE: Use a list instead of fixed lanes
    
   /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack
     * @param numLanes the number of lanes in the race
     */
    public RaceGUI(int distance, int numLanes) // CHANGE: Added numLanes parameter
    {
        // initialise instance variables
        raceLength = distance;
        horses = new ArrayList<>(numLanes); // CHANGE: Initialize list dynamically
        // Fill the list with null to represent empty lanes
        for (int i = 0; i < numLanes; i++) {
            horses.add(null);
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

    /***
     * Print race results after completion -- CHANGE
     */
    private void printWinner(int activeCount)
    {
        if (activeCount == 0) {
            System.out.println("\nAll horses have fallen! No winner!");
        }
        else{
            for (HorseGUI horse : horses) {
                if (horse != null && raceWonBy(horse)) {
                    System.out.println("And the winner is... " + horse.getName());
                    return;
                }
            }
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(HorseGUI theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                adjustConfidence(theHorse, -0.1); //confidence decreases when fall
            }
        }
    }

    // Adjust horse confidence while ensuring it stays between 0 and 1
    private void adjustConfidence(HorseGUI horse, double adjustment) {
        double newConfidence = Math.max(0.1, Math.min(0.9, horse.getConfidence() + adjustment));
        newConfidence =   Math.round(newConfidence * 10.0) / 10.0;
        horse.setConfidence(newConfidence);
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
}
