
/**
 * Creates horse objects
 * 
 * @author (yVictoria Notarianni) 
 * @version (24/04/25)
 */
public class Horse
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
    public Horse(char horseSymbol, String horseName, double horseConfidence)
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


    public void fall()
    {
        
    }
    
    public void goBackToStart()
    {
        
    }

    public void moveForward()
    {
        
    }

    public void setConfidence(double newConfidence)
    {
        
    }
    
    public void setSymbol(char newSymbol)
    {
        
    }
    
}
