public class Main {
    public static void main(String[] args) {
        // Create horses 
        Horse horse1 = new Horse('A', "Blaze", 0.5);
        Horse horse2 = new Horse('B', "Lightning", 0.6);
        Horse horse3 = new Horse('C', "Ray", 0.9);

        Race race = new Race(20, 4);
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        race.startRace(); // Run the race

    }
}
