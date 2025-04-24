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
public class Race
{
    private int raceLength;
    private List<Horse> horses; // CHANGE: Use a list instead of fixed lanes
    

   /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack
     * @param numLanes the number of lanes in the race
     */
    public Race(int distance, int numLanes) // CHANGE: Added numLanes parameter
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
    public void addHorse(Horse theHorse, int laneNumber)
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
    public void startRace()
    {
        boolean finished = false;

        for (Horse horse : horses) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }
        printRace(); // Show horses at position 0 before any movement
        int activeCount = 0; //no of horses not fallen

        while (!finished)
        {
            //Move horse
            for (Horse horse : horses) {
                if (horse != null) {
                    moveHorse(horse);
                }
            }

            // Check if any horse won by crossing the finish line
            Horse winner = null;
            for (Horse horse : horses) {
                if (horse != null && raceWonBy(horse)) {
                    winner = horse;
                    break;
                }
            }

            if (winner != null) {
                
                finished = true; //Race ends & loop ends
            } else {
                // Reset active count for the current loop
                activeCount = 0;

                // Count horses that haven't fallen
                for (Horse horse : horses) {
                    if (horse != null && !horse.hasFallen()) {
                        activeCount++;
                    }
                }
                // If all horses have fallen
                if (activeCount == 0) {
                    finished = true;
                }
            }

            //wait for 100 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            printRace();

        }
        printWinner(activeCount);
    }

    /***
     * Print race results after completion -- CHANGE
     */
    private void printWinner(int activeCount)
    {
        if (activeCount == 0) {
            System.out.println("\nAll horses have fallen! No winner!");
        }
        else{
            for (Horse horse : horses) {
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
    private void moveHorse(Horse theHorse)
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
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        for (Horse horse : horses) { // CHANGE: Loop through list instead of fixed lanes
            if (horse != null) {
                printLane(horse);
            } else {
                System.out.print("|");
                multiplePrint(' ', raceLength);  // Printing the empty lane
                System.out.println(" |");
            }
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = (theHorse != null) ? theHorse.getDistanceTravelled() : 0;
        int spacesAfter = raceLength - spacesBefore ;
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print("X");
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter > 0 ? spacesAfter : 0);
        
        //print the | for the end of the track
        System.out.print('|');
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
         for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }
}
