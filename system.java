package org.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class System extends JFrame {

    private final JTextField empIdField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField hireDateField = new JTextField();
    private final JTextField statusField = new JTextField();
    private final JTextField positionField = new JTextField();
    private final JTextField taxRateField = new JTextField();
    private final JTextField salaryField = new JTextField();

    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JButton insertBtn = new JButton("Insert");
    private final JButton updateBtn = new JButton("Update");
    private final JButton deleteBtn = new JButton("Delete");

    private final JLabel avatarLabel = new JLabel();
    private final JPanel formPanel = new JPanel();
    private final JScrollPane scrollPane;
    private final JLabel titleLabel;

    public System() {
        setTitle("Employee Management System");
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        titleLabel = new JLabel("Employee Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 102, 204));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 40));
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        JPanel viewPanel = new JPanel(null);
        String[] columns = {"EmpId", "Name", "Age", "Email", "Status", "Position", "Tax_Rate", "Salary", "HireDate"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 800, 400);
        viewPanel.add(scrollPane);
        tabbedPane.addTab("View", viewPanel);

        JPanel managePanel = new JPanel(null);
        formPanel.setLayout(null);
        formPanel.setBounds(20, 20, 550, 250);
        formPanel.setBorder(new TitledBorder("Employee Details"));
        managePanel.add(formPanel);

        int spacingY = 30;
        int leftX = 10;
        int rightX = 280;
        int labelWidth = 100;
        int fieldWidth = 150;

        addLabelAndField(formPanel, "EmpID:", empIdField, leftX, 20, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Name:", nameField, leftX, 20 + spacingY, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Age:", ageField, leftX, 20 + spacingY * 2, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Email:", emailField, leftX, 20 + spacingY * 3, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Hire Date:", hireDateField, leftX, 20 + spacingY * 4, labelWidth, fieldWidth);

        addLabelAndField(formPanel, "Status:", statusField, rightX, 20, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Position:", positionField, rightX, 20 + spacingY, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Tax Rate:", taxRateField, rightX, 20 + spacingY * 2, labelWidth, fieldWidth);
        addLabelAndField(formPanel, "Salary:", salaryField, rightX, 20 + spacingY * 3, labelWidth, fieldWidth);

        insertBtn.setBounds(120, 290, 100, 30);
        updateBtn.setBounds(240, 290, 100, 30);
        deleteBtn.setBounds(360, 290, 100, 30);
        managePanel.add(insertBtn);
        managePanel.add(updateBtn);
        managePanel.add(deleteBtn);

        ImageIcon avatar = new ImageIcon("avatar.jpg");
        avatarLabel.setIcon(new ImageIcon(avatar.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        avatarLabel.setBounds(600, 50, 150, 150);
        managePanel.add(avatarLabel);

        tabbedPane.addTab("Manage", managePanel);

        JPanel summaryPanel = new JPanel(null);

        JLabel totalEmployeesLabel = new JLabel("Total Employees:");
        totalEmployeesLabel.setBounds(50, 50, 150, 25);
        summaryPanel.add(totalEmployeesLabel);

        JTextField totalEmployeesField = new JTextField();
        totalEmployeesField.setBounds(200, 50, 150, 25);
        totalEmployeesField.setEditable(false);
        summaryPanel.add(totalEmployeesField);

        JLabel salarySumLabel = new JLabel("Total Salary:");
        salarySumLabel.setBounds(50, 100, 150, 25);
        summaryPanel.add(salarySumLabel);

        JTextField salarySumField = new JTextField();
        salarySumField.setBounds(200, 100, 150, 25);
        salarySumField.setEditable(false);
        summaryPanel.add(salarySumField);

        JButton refreshBtn = new JButton("Refresh Summary");
        refreshBtn.setBounds(120, 150, 180, 30);
        summaryPanel.add(refreshBtn);

        refreshBtn.addActionListener(e -> {
            int totalEmployees = tableModel.getRowCount();
            double totalSalary = 0.0;
            for (int i = 0; i < totalEmployees; i++) {
                try {
                    totalSalary += Double.parseDouble(tableModel.getValueAt(i, 7).toString());
                } catch (NumberFormatException ex) {
                    // skip
                }
            }
            totalEmployeesField.setText(String.valueOf(totalEmployees));
            salarySumField.setText(String.format("%.2f", totalSalary));
        });

        tabbedPane.addTab("Summary", summaryPanel);

        // ADD CALENDAR TAB
        CalendarPanel calendarPanel = new CalendarPanel(tableModel);
        tabbedPane.addTab("Calendar", calendarPanel);

        insertBtn.addActionListener(e -> {
            String[] data = {
                    empIdField.getText(), nameField.getText(), ageField.getText(),
                    emailField.getText(), statusField.getText(), positionField.getText(),
                    taxRateField.getText(), salaryField.getText(), hireDateField.getText()
            };
            tableModel.addRow(data);
            clearFields();
            calendarPanel.refreshCalendar();
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.setValueAt(empIdField.getText(), selectedRow, 0);
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(ageField.getText(), selectedRow, 2);
                tableModel.setValueAt(emailField.getText(), selectedRow, 3);
                tableModel.setValueAt(statusField.getText(), selectedRow, 4);
                tableModel.setValueAt(positionField.getText(), selectedRow, 5);
                tableModel.setValueAt(taxRateField.getText(), selectedRow, 6);
                tableModel.setValueAt(salaryField.getText(), selectedRow, 7);
                tableModel.setValueAt(hireDateField.getText(), selectedRow, 8);
                clearFields();
                calendarPanel.refreshCalendar();
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
                clearFields();
                calendarPanel.refreshCalendar();
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    empIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    ageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    positionField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    taxRateField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                    salaryField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                    hireDateField.setText(tableModel.getValueAt(selectedRow, 8).toString());
                }
            }
        });
    }

    private void addLabelAndField(JPanel panel, String label, JTextField field, int x, int y, int labelWidth, int fieldWidth) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, labelWidth, 25);
        field.setBounds(x + labelWidth + 5, y, fieldWidth, 25);
        panel.add(lbl);
        panel.add(field);
    }

    private void clearFields() {
        empIdField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        hireDateField.setText("");
        statusField.setText("");
        positionField.setText("");
        taxRateField.setText("");
        salaryField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System frame = new System();
            frame.setVisible(true);
        });
    }
}

// CalendarPanel class (you can place this below the System class or in a separate file)
class CalendarPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel calendarGrid = new JPanel(new GridLayout(0, 7));
    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

    public CalendarPanel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        setLayout(new BorderLayout());

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        JPanel header = new JPanel(new BorderLayout());
        header.add(prevButton, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(calendarGrid, BorderLayout.CENTER);

        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            renderCalendar();
        });

        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            renderCalendar();
        });

        renderCalendar();
    }

    private void renderCalendar() {
        calendarGrid.removeAll();
        Month month = currentMonth.getMonth();
        int year = currentMonth.getYear();
        monthLabel.setText(month + " " + year);

        LocalDate firstDay = currentMonth.withDayOfMonth(1);
        int startDay = firstDay.getDayOfWeek().getValue() % 7;

        for (String d : new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"})
            calendarGrid.add(new JLabel(d, SwingConstants.CENTER));

        for (int i = 0; i < startDay; i++)
            calendarGrid.add(new JLabel());

        int daysInMonth = month.length(Year.isLeap(year));
        Set<Integer> dotDays = getEmployeeDays(currentMonth);

        for (int day = 1; day <= daysInMonth; day++) {
            JLabel label = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            if (dotDays.contains(day)) {
                label.setText("<html><center>" + day + "<br>•</center></html>");
                label.setForeground(Color.RED);
            }
            calendarGrid.add(label);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private Set<Integer> getEmployeeDays(LocalDate monthDate) {
        Set<Integer> days = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String hireDateStr = tableModel.getValueAt(i, 8).toString();
            try {
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);
                if (hireDate.getYear() == monthDate.getYear() && hireDate.getMonth() == monthDate.getMonth()) {
                    days.add(hireDate.getDayOfMonth());
                }
            } catch (Exception ignored) {}
        }
        return days;
    }

    public void refreshCalendar() {
        renderCalendar();
    }
}
