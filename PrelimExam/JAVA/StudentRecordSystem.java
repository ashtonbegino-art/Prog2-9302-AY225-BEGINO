
// Programmer Identifier: [Ashton B. Begino] - [25-1444-956]

// Student Record System - Java Swing Implementation

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentRecordSystem extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtStudentID, txtFirstName, txtLastName, txtLabWork1, txtLabWork2;
    private JTextField txtLabWork3, txtPrelimExam, txtAttendance;
    private JButton btnAdd, btnDelete;

    public StudentRecordSystem() {

        // Set window title with programmer identifier
        setTitle("Student Record System - [Ashton B. Begino] - [25-1444-956]");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();

        // Load data from CSV
        loadDataFromCSV();
    }

    private void initComponents() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table with column names
        String[] columnNames = { "Student ID", "First Name", "Last Name", "Lab Work 1",
                "Lab Work 2", "Lab Work 3", "Prelim Exam", "Attendance Grade" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentID = new JTextField(15);
        inputPanel.add(txtStudentID, gbc);

        // Row 1: First Name
        gbc.gridx = 2;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        txtFirstName = new JTextField(15);
        inputPanel.add(txtFirstName, gbc);

        // Row 2: Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        txtLastName = new JTextField(15);
        inputPanel.add(txtLastName, gbc);

        // Row 2: Lab Work 1
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Lab Work 1:"), gbc);
        gbc.gridx = 3;
        txtLabWork1 = new JTextField(15);
        inputPanel.add(txtLabWork1, gbc);

        // Row 3: Lab Work 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Lab Work 2:"), gbc);
        gbc.gridx = 1;
        txtLabWork2 = new JTextField(15);
        inputPanel.add(txtLabWork2, gbc);

        // Row 3: Lab Work 3
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Lab Work 3:"), gbc);
        gbc.gridx = 3;
        txtLabWork3 = new JTextField(15);
        inputPanel.add(txtLabWork3, gbc);

        // Row 4: Prelim Exam
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Prelim Exam:"), gbc);
        gbc.gridx = 1;
        txtPrelimExam = new JTextField(15);
        inputPanel.add(txtPrelimExam, gbc);

        // Row 4: Attendance Grade
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Attendance:"), gbc);
        gbc.gridx = 3;
        txtAttendance = new JTextField(15);
        inputPanel.add(txtAttendance, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("Add Record");
        btnAdd.addActionListener(e -> addRecord());
        buttonPanel.add(btnAdd);

        btnDelete = new JButton("Delete Record");
        btnDelete.addActionListener(e -> deleteRecord());
        buttonPanel.add(btnDelete);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private void loadDataFromCSV() {
        // Print current working directory for debugging
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

        // Try multiple possible locations for the CSV file
        String[] possiblePaths = {
                "Prog2-9302-AY225-BEGINO/PrelimExam/JAVA/MOCK_DATA.csv"
        };

        boolean fileLoaded = false;
        String loadedFrom = "";

        for (String path : possiblePaths) {
            File file = new File(path);
            System.out.println("Trying path: " + path + " - Exists: " + file.exists());

            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    boolean firstLine = true;
                    int recordCount = 0;

                    while ((line = br.readLine()) != null) {
                        // Skip header line
                        if (firstLine) {
                            firstLine = false;
                            continue;
                        }

                        // Split CSV line
                        String[] data = line.split(",");

                        // Add row to table (ensure we have 8 columns)
                        if (data.length >= 8) {
                            tableModel.addRow(data);
                            recordCount++;
                        }
                    }

                    loadedFrom = file.getAbsolutePath();

                    JOptionPane.showMessageDialog(this,
                            "✓ Data loaded successfully!\n\n" +
                                    "Records loaded: " + recordCount + "\n" +
                                    "File location: " + loadedFrom,
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    fileLoaded = true;
                    break; // Exit loop if file is found and loaded

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error reading file from: " + path + "\n\n" +
                                    "Error: " + e.getMessage(),
                            "I/O Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
            }
        }

        if (!fileLoaded) {
            // Build detailed error message
            StringBuilder message = new StringBuilder();
            message.append("⚠ Error: MOCK_DATA.csv file not found!\n\n");
            message.append("Current Directory:\n");
            message.append(System.getProperty("user.dir")).append("\n\n");
            message.append("Searched in these locations:\n");
            for (String path : possiblePaths) {
                message.append("  • ").append(path).append("\n");
            }
            message.append("\n");
            message.append("SOLUTION:\n");
            message.append("1. Copy MOCK_DATA.csv to the same folder as StudentRecordSystem.class\n");
            message.append("   OR\n");
            message.append("2. Run from the JAVA folder:\n");
            message.append("   cd JAVA\n");
            message.append("   javac StudentRecordSystem.java\n");
            message.append("   java StudentRecordSystem\n");

            JOptionPane.showMessageDialog(this,
                    message.toString(),
                    "File Not Found", JOptionPane.ERROR_MESSAGE);

            System.err.println(message.toString());
        }
    }

    private void addRecord() {
        try {
            // Get values from text fields
            String studentID = txtStudentID.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String labWork1 = txtLabWork1.getText().trim();
            String labWork2 = txtLabWork2.getText().trim();
            String labWork3 = txtLabWork3.getText().trim();
            String prelimExam = txtPrelimExam.getText().trim();
            String attendance = txtAttendance.getText().trim();

            // Validate input
            if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in at least Student ID, First Name, and Last Name!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Set default values for empty fields
            if (labWork1.isEmpty())
                labWork1 = "0";
            if (labWork2.isEmpty())
                labWork2 = "0";
            if (labWork3.isEmpty())
                labWork3 = "0";
            if (prelimExam.isEmpty())
                prelimExam = "0";
            if (attendance.isEmpty())
                attendance = "0";

            // Names should contain letters only
            if (!firstName.matches("[a-zA-Z ]+") || !lastName.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this,
                        "First Name and Last Name must contain LETTERS only!",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int lw1, lw2, lw3, prelim, attend;

            try {
                lw1 = Integer.parseInt(labWork1);
                lw2 = Integer.parseInt(labWork2);
                lw3 = Integer.parseInt(labWork3);
                prelim = Integer.parseInt(prelimExam);
                attend = Integer.parseInt(attendance);

                if (lw1 < 0 || lw1 > 100 ||
                        lw2 < 0 || lw2 > 100 ||
                        lw3 < 0 || lw3 > 100 ||
                        prelim < 0 || prelim > 100 ||
                        attend < 0 || attend > 100) {

                    JOptionPane.showMessageDialog(this,
                            "Grades must be between 0 and 100 only!",
                            "Invalid Grade",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Grades must be NUMBERS only!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add row to table
            Object[] rowData = { studentID, firstName, lastName, labWork1, labWork2,
                    labWork3, prelimExam, attendance };
            tableModel.addRow(rowData);

            // Clear text fields
            clearFields();

            JOptionPane.showMessageDialog(this,
                    "✓ Record added successfully!\n\n" +
                            "Student: " + firstName + " " + lastName + "\n" +
                            "Total records: " + tableModel.getRowCount(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding record: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteRecord() {
        try {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a row to delete!\n\n" +
                                "Click on a row in the table first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get student info for confirmation
            String studentID = tableModel.getValueAt(selectedRow, 0).toString();
            String firstName = tableModel.getValueAt(selectedRow, 1).toString();
            String lastName = tableModel.getValueAt(selectedRow, 2).toString();

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this record?\n\n" +
                            "Student ID: " + studentID + "\n" +
                            "Name: " + firstName + " " + lastName,
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this,
                        "✓ Record deleted successfully!\n\n" +
                                "Remaining records: " + tableModel.getRowCount(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting record: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLabWork1.setText("");
        txtLabWork2.setText("");
        txtLabWork3.setText("");
        txtPrelimExam.setText("");
        txtAttendance.setText("");
    }

    public static void main(String[] args) {
        // Print system information
        System.out.println("=== Student Record System ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("============================\n");

        // Use Swing's event dispatch thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            StudentRecordSystem frame = new StudentRecordSystem();
            frame.setVisible(true);
        });
    }
}