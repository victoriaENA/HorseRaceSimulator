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



        // Track display panel
        trackDisplay = new JPanel();
        trackDisplay.setLayout(new BoxLayout(trackDisplay, BoxLayout.Y_AXIS));

        //add to Jpanel
        add(inputPanel, BorderLayout.NORTH);
        add(trackDisplay, BorderLayout.CENTER);


        //Winner message
        winnerLabel = new JLabel(" "); // Empty at first
        winnerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(winnerLabel, BorderLayout.SOUTH); // Place it at the bottom of the race display

        // Event: create track on setTrack
        setTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTrack();
                doneButton.setVisible(true);
            }

        });

        // Event
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRaceButton.setVisible(true);

                setTrackButton.setVisible(false);
                laneField.setVisible(false);
                lengthField.setVisible(false);
                unitSelector.setVisible(false);
                doneButton.setVisible(false);


            }

        });

        // Event: startRace on start
        startRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRace();

            }

        });



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
                    '♞', "Horse " + lane,
                    Math.round((0.2 + (Math.random() * 0.6)) * 10) / 10.0
            );


            race.addHorse(horse, lane);


        }


        updateTrackDisplay();  // Show horses at the starting line


    }

    private void startRace() {

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
        raceTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!raceEnded) {
                    race.moveAllHorses();

                    if (race.isFinished()) {
                        raceEnded = true;
                        raceTimer.stop();
                        displayWinner();
                    }

                    updateTrackDisplay();  //change for custom
                }
            }
        });

        raceTimer.start();
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

        // 🔹 Ensure message is displayed correctly
        if (winner != null) {
            race.adjustConfidence(winner, 0.1);
            winnerLabel.setText("And the winner is... " + winner.getName() + "!");
        } else if (allFallen) {
            winnerLabel.setText("All horses have fallen! No winner.");
        }

        // 🔹 Refresh GUI to make sure label updates
        winnerLabel.revalidate();
        winnerLabel.repaint();
    }

}

