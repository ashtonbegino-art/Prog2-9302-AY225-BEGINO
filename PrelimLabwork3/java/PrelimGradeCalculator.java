import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PrelimGradeCalculator extends JFrame {

    private JTextField attendanceField, lab1Field, lab2Field, lab3Field;
    private JTextArea resultArea;
    private JButton calculateButton, clearButton;

    // ===== COLORS =====
    private final Color BG = new Color(20, 22, 26);
    private final Color PANEL = new Color(30, 33, 38);
    private final Color FIELD = new Color(40, 44, 52);
    private final Color BORDER = new Color(70, 75, 90);
    private final Color TEXT = new Color(230, 230, 230);
    private final Color ACCENT = new Color(99, 102, 241);
    private final Color DANGER = new Color(239, 68, 68);

    public PrelimGradeCalculator() {
        setTitle("Prelim Grade Calculator");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 14));

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(root);

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(PANEL);
        inputPanel.setBorder(new TitledBorder(
                new LineBorder(BORDER),
                " Enter Your Grades ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                uiFont,
                TEXT));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        attendanceField = createField();
        lab1Field = createField();
        lab2Field = createField();
        lab3Field = createField();

        // 🔒 AUTO-LOCK INPUTS (HTML-LIKE)
        lockField(attendanceField, 5);
        lockField(lab1Field, 100);
        lockField(lab2Field, 100);
        lockField(lab3Field, 100);

        addRow(inputPanel, c, 0, "Number of Attendances (0–5):", attendanceField);
        addRow(inputPanel, c, 1, "Lab Work 1 Grade (0–100):", lab1Field);
        addRow(inputPanel, c, 2, "Lab Work 2 Grade (0–100):", lab2Field);
        addRow(inputPanel, c, 3, "Lab Work 3 Grade (0–100):", lab3Field);

        // ===== BUTTONS =====
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(PANEL);

        calculateButton = createButton("Calculate", ACCENT);
        clearButton = createButton("Clear", DANGER);

        calculateButton.addActionListener(e -> calculate());
        clearButton.addActionListener(e -> clear());

        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        inputPanel.add(buttonPanel, c);

        root.add(inputPanel, BorderLayout.WEST);

        // ===== RESULT PANEL =====
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(PANEL);
        resultPanel.setBorder(new TitledBorder(
                new LineBorder(BORDER),
                " Results ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                uiFont,
                TEXT));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(FIELD);
        resultArea.setForeground(TEXT);
        resultArea.setCaretColor(TEXT);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(new LineBorder(BORDER));
        scroll.getViewport().setBackground(FIELD);

        resultPanel.add(scroll, BorderLayout.CENTER);
        root.add(resultPanel, BorderLayout.CENTER);
    }

    // ===== INPUT AUTO-FIX (LIKE HTML) =====
    private void lockField(JTextField field, int max) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = field.getText().replaceAll("[^0-9]", "");
                if (text.isEmpty()) {
                    field.setText("");
                    return;
                }

                int value = Integer.parseInt(text);
                if (value > max)
                    value = max;
                if (value < 0)
                    value = 0;

                field.setText(String.valueOf(value));
            }
        });
    }

    // ===== UI HELPERS =====
    private void addRow(JPanel panel, GridBagConstraints c, int y, String label, JTextField field) {
        c.gridx = 0;
        c.gridy = y;
        panel.add(createLabel(label), c);

        c.gridx = 1;
        panel.add(field, c);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        return label;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(6, 8, 6, 8)));
        field.setPreferredSize(new Dimension(160, 30));
        return field;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 0, 8, 0));
        return btn;
    }

    // ===== LOGIC =====
    private void calculate() {
        double att = attendanceField.getText().isEmpty() ? 0 : Double.parseDouble(attendanceField.getText());
        double l1 = lab1Field.getText().isEmpty() ? 0 : Double.parseDouble(lab1Field.getText());
        double l2 = lab2Field.getText().isEmpty() ? 0 : Double.parseDouble(lab2Field.getText());
        double l3 = lab3Field.getText().isEmpty() ? 0 : Double.parseDouble(lab3Field.getText());

        double attPct = (att / 5) * 100;

        if (att < 2) {
            resultArea.setText(String.format(
                    "❌ FAILED\n\nAttendance: %.0f / 5 (%.0f%%)\nMinimum required is 2.",
                    att, attPct));
            return;
        }

        double labAvg = (l1 + l2 + l3) / 3;
        double classStanding = 0.4 * attPct + 0.6 * labAvg;
        double passReq = (75 - 0.7 * classStanding) / 0.3;

        resultArea.setText(String.format(
                "Attendance: %.0f / 5 (%.0f%%)\n" +
                        "Lab Average: %.2f\n" +
                        "Class Standing: %.2f\n\n" +
                        "Required Prelim Exam:\n" +
                        "To Pass: %s",
                att,
                attPct,
                labAvg,
                classStanding,
                passReq > 100 ? "IMPOSSIBLE" : String.format("%.2f", passReq)));
    }

    private void clear() {
        attendanceField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        resultArea.setText("");
        attendanceField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrelimGradeCalculator().setVisible(true));
    }
}
