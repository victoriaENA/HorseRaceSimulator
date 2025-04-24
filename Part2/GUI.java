import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel {

    private JTextField laneField;
    private JTextField lengthField;
    private JButton setTrackButton;

    private JPanel trackDisplay;
    private RaceGUI race;  // Reference to the Race class

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


        setTrackButton = new JButton("Set Track");
        inputPanel.add(setTrackButton);


        // Track display panel
        trackDisplay = new JPanel();
        trackDisplay.setLayout(new BoxLayout(trackDisplay, BoxLayout.Y_AXIS));

        //add to Jpanel
        add(inputPanel, BorderLayout.NORTH);
        add(trackDisplay, BorderLayout.CENTER);



        // Event: create track on setTrack
        setTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTrack();
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


        int lanes = Integer.parseInt(laneField.getText());
        int length = Integer.parseInt(lengthField.getText());


        // Ensure minimum lanes are 2
        lanes = Math.max(lanes, 2);


        race = new RaceGUI(length, lanes);

        for (int lane = 1; lane <= lanes; lane++) {


            HorseGUI horse = new HorseGUI(
                    '♞', "Horse " + lane,
                    Math.round((0.2 + (Math.random() * 0.6)) * 10) / 10.0
            );


            race.addHorse(horse, lane);


        }


        updateTrackDisplay();  // Show horses at the starting line


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

}



}