import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel {

    private JTextField laneField;
    private JTextField lengthField;
    private JButton setTrackButton;

    private JPanel trackDisplay;
    //private RaceGUI race;  // Reference to the Race class

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
                //showTrack();
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


}