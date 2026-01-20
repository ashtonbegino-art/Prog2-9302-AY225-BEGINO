import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceTracker {

    static DefaultTableModel tableModel;
    static JFrame listFrame;

    public static void main(String[] args) {

        // ===== MAIN FRAME =====
        JFrame frame = new JFrame("Attendance Tracker");
        frame.setSize(450, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== LABELS =====
        JLabel nameLabel = new JLabel("Attendance Name:");
        JLabel courseLabel = new JLabel("Course:");
        JLabel yearLabel = new JLabel("Year:");
        JLabel timeLabel = new JLabel("Time In:");
        JLabel signatureLabel = new JLabel("E-Signature:");

        // ===== INPUT FIELDS =====
        JTextField nameField = new JTextField();

        String[] courses = { " Select Course ", "BSIT", "BSCS", "BSIS" };
        JComboBox<String> courseBox = new JComboBox<>(courses);

        String[] years = { " Select Year ", "1st Year", "2nd Year", "3rd Year", "4th Year" };
        JComboBox<String> yearBox = new JComboBox<>(years);

        JTextField timeField = new JTextField();
        JTextField signatureField = new JTextField();

        // Auto values
        timeField.setText(LocalDateTime.now().toString());
        timeField.setEditable(false);

        signatureField.setText(UUID.randomUUID().toString());
        signatureField.setEditable(false);

        JButton enterButton = new JButton("Enter");

        // ===== ADD COMPONENTS =====
        panel.add(nameLabel);
        panel.add(nameField);

        panel.add(courseLabel);
        panel.add(courseBox);

        panel.add(yearLabel);
        panel.add(yearBox);

        panel.add(timeLabel);
        panel.add(timeField);

        panel.add(signatureLabel);
        panel.add(signatureField);

        panel.add(new JLabel());
        panel.add(enterButton);

        frame.add(panel);
        frame.setVisible(true);

        // ===== LIST FRAME (HIDDEN INITIALLY) =====
        listFrame = new JFrame("Attendance List");
        listFrame.setSize(700, 300);
        listFrame.setLocationRelativeTo(null);

        String[] columns = { "Name", "Course", "Year", "Time In", "E-Signature" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        listFrame.add(new JScrollPane(table));

        // ===== ENTER BUTTON ACTION =====
        enterButton.addActionListener(e -> {

            // Validation
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name.");
                return;
            }

            if (courseBox.getSelectedIndex() == 0 || yearBox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(frame, "Please select course and year.");
                return;
            }

            // Auto-save to table
            tableModel.addRow(new Object[] {
                    nameField.getText(),
                    courseBox.getSelectedItem(),
                    yearBox.getSelectedItem(),
                    timeField.getText(),
                    signatureField.getText()
            });

            // Show list frame AFTER save
            listFrame.setVisible(true);

            // Reset fields
            nameField.setText("");
            courseBox.setSelectedIndex(0);
            yearBox.setSelectedIndex(0);
            timeField.setText(LocalDateTime.now().toString());
            signatureField.setText(UUID.randomUUID().toString());
        });
    }
}