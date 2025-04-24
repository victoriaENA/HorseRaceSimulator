
/**
 * Creates horse objects
 * 
 * @author (Victoria Notarianni) 
 * @version (24/04/25)
 */
public class HorseGUI
{
    //Fields of class Horse
    private String name;                
    private char symbol;               
    private int distanceTravelled;     
    private boolean hasFallen = false;
    private double confidence;         // (between 0 and 1)
    
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public HorseGUI(char horseSymbol, String horseName, double horseConfidence)
    {
        name = horseName;
        symbol = horseSymbol;
        confidence = horseConfidence;
        distanceTravelled = 0; // Ensure horse starts at position 0
        hasFallen = false;     // Horse has not fallen initially
       
    }
    
    
    //Other methods of class Horse
    
      /**
     * Returns the horse’s confidence rating
     */
    public double getConfidence()
    {
        return confidence;
    }

    /**
     * Returns the distance travelled by the horse
     */
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }

    /**
     * Returns the name of the horse
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the character used to represent the horse
     */
    public char getSymbol()
    {
        return symbol;
    }

    /**
     * Returns true if the horse has fallen, false otherwise
     */
    public boolean hasFallen()
    {
        return hasFallen;
    }


    /**
     * Resets the horse to the start of the race
     */
    public void goBackToStart()
    {
        distanceTravelled = 0;
        hasFallen = false;
    }

    /**
     * Sets the horse as fallen
     */
    public void fall()
    {
        hasFallen = true;
    }

    /**
     * Increments the distance travelled by the horse by 1
     */
    public void moveForward()
    {
        distanceTravelled++;
    }

    /**
     * Sets the confidence rating of the horse to the given value
     */
    public void setConfidence(double newConfidence)
    {
        if (newConfidence >= 0.1 && newConfidence <= 0.9) {
            confidence = newConfidence;
        } else {
            System.out.println("Invalid confidence: must be between 0.1 and 0.9. Value not updated.");
        }
    }

    /**
     * Sets the horse’s symbol to the specified character
     */
    public void setSymbol(char newSymbol)
    {
        symbol = newSymbol;
    }

}
