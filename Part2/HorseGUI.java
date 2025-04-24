
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
    private boolean hasFallen;
    private double confidence;         // (between 0 and 1)
    private HorseBreed breed;           // The breed of the horse (e.g., THOROUGHBRED, ARABIAN)
    private String coatColor;
    private HorseEquipment saddle;
    private HorseEquipment horseshoes;
    private HorseEquipment accessory;

    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public HorseGUI(String name, HorseBreed breed, String coatColor, char symbol, double confidence)
    {
        this.name = name;
        this.breed = breed;
        this.coatColor = coatColor;
        this.symbol = symbol;
        this.confidence = confidence;
        distanceTravelled = 0; // Ensure horse starts at position 0
        hasFallen = false;     // Horse has not fallen initially
        this.saddle = HorseEquipment.STANDARD_SADDLE;
        this.horseshoes = HorseEquipment.RUBBER_SHOES;
        this.accessory = HorseEquipment.NONE;
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
     * Returns the breed of the horse
     */
    public HorseBreed getBreed()
    {
        return breed;
    }

    /**
     * Returns the coat color of the horse
     */
    public String getCoatColor()
    {
        return coatColor;
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

    /**
     * Sets the horse’s breed to the specified value
     */
    public void setBreed(HorseBreed newBreed)
    {
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


}
