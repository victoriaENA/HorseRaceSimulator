
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
    public void fall()
    {
        
    }
    
    public double getConfidence()
    {
        
    }
    
    public int getDistanceTravelled()
    {
        
    }
    
    public String getName()
    {
        
    }
    
    public char getSymbol()
    {
        
    }
    
    public void goBackToStart()
    {
        
    }
    
    public boolean hasFallen()
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
