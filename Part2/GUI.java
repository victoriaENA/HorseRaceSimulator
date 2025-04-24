import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JPanel {

    private JTextField laneField;
    private JTextField lengthField;
    private JButton setTrackButton;
    private JButton doneButton;
    private JButton startRaceButton;
    private JPanel trackDisplay;
    private RaceGUI race;  // Reference to the Race class
    private Timer raceTimer;  // Timer to update the race progress
    private JComboBox<String> unitSelector;
    private JComboBox<String> trackSelector;
    private JLabel winnerLabel;
    private boolean raceEnded = false;
    private JButton customizeButton;
    private JPanel customizationPanel;


    public GUI() {
        setLayout(new BorderLayout());

        // Top Panel for input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Number of lanes:"));
        laneField = new JTextField("5", 5);
        inputPanel.add(laneField);

        inputPanel.add(new JLabel("Track length:"));
        lengthField = new JTextField("30", 5);
        inputPanel.add(lengthField);

        inputPanel.add(new JLabel("Unit:"));
        unitSelector = new JComboBox<>(new String[]{"meters", "yards", "centimeters"});
        inputPanel.add(unitSelector);

        inputPanel.add(new JLabel("Track Condition:"));
        trackSelector = new JComboBox<>(new String[]{"DRY", "MUDDY", "ICY"}); //
        inputPanel.add(trackSelector);

        setTrackButton = new JButton("Set Track");
        inputPanel.add(setTrackButton);

        doneButton = new JButton(" Done");
        inputPanel.add(doneButton);
        doneButton.setVisible(false); // Initially hidden

        startRaceButton = new JButton("Start Race");
        startRaceButton.setVisible(false); // Initially hidden
        inputPanel.add(startRaceButton);
        add(inputPanel, BorderLayout.NORTH);

        customizeButton = new JButton("Customize Horses");
        customizeButton.setVisible(false);  // Initially hidden
        inputPanel.add(customizeButton);

        // Track display panel
        trackDisplay = new JPanel();
        trackDisplay.setLayout(new BoxLayout(trackDisplay, BoxLayout.Y_AXIS));

        // Customization panel setup
        customizationPanel = new JPanel();
        customizationPanel.setLayout(new BoxLayout(customizationPanel, BoxLayout.Y_AXIS));
        customizationPanel.setBorder(BorderFactory.createTitledBorder("Horse Customization"));
        customizationPanel.setVisible(false); // initially hidden

        // Center area with BorderLayout for toggleable panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(trackDisplay), BorderLayout.CENTER);
        centerPanel.add(customizationPanel, BorderLayout.EAST); // Add customization panel to the right

        // Add to main layout
        add(centerPanel, BorderLayout.CENTER);

        //Winner message
        winnerLabel = new JLabel(" "); // Empty at first
        winnerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(winnerLabel, BorderLayout.SOUTH); // Place it at the bottom of the race display

        // Event: create track on setTrack
        setTrackButton.addActionListener(e -> {
            showTrack();
            doneButton.setVisible(true);
        });

        // Event: doneButton clicked
        doneButton.addActionListener(e -> {
            startRaceButton.setVisible(true);
            customizeButton.setVisible(true);
            setTrackButton.setVisible(false);
            laneField.setVisible(false);
            lengthField.setVisible(false);
            unitSelector.setVisible(false);
            doneButton.setVisible(false);

            // Hide all labels except "Track Condition"
            for (Component comp : getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component subComp : ((JPanel) comp).getComponents()) {
                        if (subComp instanceof JLabel) {
                            JLabel label = (JLabel) subComp;
                            if (!label.getText().contains("Track Condition")) {
                                label.setVisible(false); // Hide labels except "Track Condition"
                            }
                        }
                    }
                }
            }
        });

        // Event: startRace on start
        startRaceButton.addActionListener(e -> {
            startRace();
            customizeButton.setVisible(false);
        });

        // Event: customizeButton clicked
        customizeButton.addActionListener(e ->
                showCustomizationOptions()
        );


    }

    // Main method to run the GUI
    public static void main(String[] args) {
        startRaceGUI();
    }

    // GUI entry point
    public static void startRaceGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Horse Race Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            GUI racePanel = new GUI();
            frame.add(racePanel);

            frame.setVisible(true);
        });
    }

    private void showTrack() {

        trackDisplay.removeAll();
        winnerLabel.setText(" ");

        int lanes = Integer.parseInt(laneField.getText());
        int length = Integer.parseInt(lengthField.getText());
        String unit = (String) unitSelector.getSelectedItem();
        String selectedCondition = ((String) trackSelector.getSelectedItem()).toUpperCase();
        TrackCondition trackCondition = TrackCondition.valueOf(selectedCondition);

        // Ensure minimum lanes are 2
        lanes = Math.max(lanes, 2);

        race = new RaceGUI(length, lanes, unit, trackCondition);

        for (int lane = 1; lane <= lanes; lane++) {

            HorseGUI horse = new HorseGUI(
                    "Horse " + (lane),
                    HorseBreed.THOROUGHBRED,
                    "Black",
                    '♞',
                    Math.round((0.2 + (Math.random() * 0.6)) * 10) / 10.0
            );

            race.addHorse(horse, lane);

        }

        updateTrackDisplay();  // Show horses at the starting line

    }

    private void startRace() {

        customizationPanel.setVisible(false);

        raceEnded = false;
        winnerLabel.setText(" ");

        String selectedCondition = ((String) trackSelector.getSelectedItem()).toUpperCase();
        TrackCondition trackCondition = TrackCondition.valueOf(selectedCondition);

        race.setTrackCondition(trackCondition);

        // Reset each horse to the starting position
        for (HorseGUI horse : race.getHorses()) {
            if (horse != null) {
                horse.goBackToStart();
            }
        }

        // Adjust confidence now that the race is starting
        for (HorseGUI horse : race.getHorses()) {
            if (horse != null) {
                race.adjustConfidenceForTrack(horse);
            }
        }

        // Timer to animate the race
        raceTimer = new Timer(500, e -> {
            if (!raceEnded) {
                race.moveAllHorses();

                if (race.isFinished()) {
                    raceEnded = true;
                    raceTimer.stop();
                    displayWinner();
                    customizeButton.setVisible(true);
                }

                updateTrackDisplayCustom();
            }
        });

        raceTimer.start();
    }

    private void displayWinner() {
        HorseGUI winner = race.getWinner(); // Retrieve the winning horse
        boolean allFallen = true; // Assume all horses have fallen

        // Check each horse
        for (HorseGUI horse : race.getHorses()) {
            if (horse != null && !horse.hasFallen()) {
                allFallen = false; // At least one horse is still active
                break; // No need to check further
            }
        }

        // Ensure message is displayed correctly
        if (winner != null) {
            race.adjustConfidence(winner, 0.1);
            winnerLabel.setText("And the winner is... " + winner.getName() + "!");
        } else if (allFallen) {
            winnerLabel.setText("All horses have fallen! No winner.");
        }

        // Refresh GUI to make sure label updates
        winnerLabel.revalidate();
        winnerLabel.repaint();
    }


    // Updates the GUI with the current race state
    private void updateTrackDisplay() {
        trackDisplay.removeAll();  // Clear the previous race track

        // Get the race track display as a List of Strings
        List<String> raceTrack = race.getRaceTrackDisplay();

        // Add each line of the race track to the GUI panel
        for (String line : raceTrack) {
            JLabel laneLabel = new JLabel(line);

            laneLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));

            trackDisplay.add(laneLabel);
        }

        // Refresh the GUI
        trackDisplay.revalidate();
        trackDisplay.repaint();
    }


    private void updateTrackDisplayCustom() {
        trackDisplay.removeAll();  // Clear the previous race track

        List<String> raceTrack = race.getRaceTrackDisplay();

        for (int i = 0; i < raceTrack.size(); i++) {
            String line = raceTrack.get(i);

            JLabel laneLabel = new JLabel(line);
            laneLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));

            // Find a horse in this lane if it exists
            HorseGUI horse = race.getHorseInLane(i ); // Ensure `i` maps to correct lane number

            if (horse != null ) {
                Color color = switch (horse.getCoatColor().toLowerCase()) {
                    case "black" -> Color.BLACK;
                    case "white" -> Color.LIGHT_GRAY;
                    case "brown" -> new Color(139, 69, 19);
                    case "grey" -> Color.GRAY;
                    default -> Color.BLACK;
                };
                laneLabel.setForeground(color);
            }

            trackDisplay.add(laneLabel);
        }

        // Refresh the GUI
        trackDisplay.revalidate();
        trackDisplay.repaint();
    }

    private void showCustomizationOptions() {
        customizationPanel.removeAll();  // Clear previous options

        for (HorseGUI horse : race.getHorses()) {
            if (horse != null) {
                JPanel horsePanel = new JPanel(new FlowLayout());

                JLabel horseLabel = new JLabel("Customize " + horse.getName() + ": ");
                JComboBox<HorseBreed> breedSelector = new JComboBox<>(HorseBreed.values());
                breedSelector.setSelectedItem(horse.getBreed());

                // Symbol Color Selector
                JComboBox<String> colorSelector = new JComboBox<>(new String[]{"Black", "White", "Brown", "Grey"});
                colorSelector.setSelectedItem(horse.getCoatColor());  // Set the current coat color

                JTextField symbolInput = new JTextField(1); // Single character input field
                symbolInput.setText(String.valueOf(horse.getSymbol())); // Set current symbol

                JButton confirmButton = new JButton("Set Breed");
                JButton setColorButton = new JButton("Set Color");
                JButton setSymbolButton = new JButton("Set Symbol");

                HorseGUI currentHorse = horse;

                confirmButton.addActionListener(e ->
                        currentHorse.setBreed((HorseBreed) breedSelector.getSelectedItem())
                );

                setColorButton.addActionListener(e -> {
                    String selectedColor = (String) colorSelector.getSelectedItem();
                    currentHorse.setCoatColor(selectedColor);
                    updateTrackDisplayCustom(); // Refresh to show new color

                });

                setSymbolButton.addActionListener(e -> {
                    String input = symbolInput.getText();
                    if (!input.isEmpty()) {
                        horse.setSymbol(input.charAt(0)); // Set the new symbol
                        updateTrackDisplayCustom(); // Refresh the track display
                    }
                });

                horsePanel.add(horseLabel);
                horsePanel.add(breedSelector);
                horsePanel.add(confirmButton);
                horsePanel.add(colorSelector);
                horsePanel.add(setColorButton);
                horsePanel.add(symbolInput);
                horsePanel.add(setSymbolButton);
                customizationPanel.add(horsePanel);
            }
        }

        // Toggle visibility on click
        customizationPanel.setVisible(!customizationPanel.isVisible());

        customizationPanel.revalidate();
        customizationPanel.repaint();
    }

}

