package companySystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.List;

public class System extends JFrame {

    private final JTextField empIdField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField hireDateField = new JTextField();
    private final JButton hireDatePickBtn = new JButton("Pick Date");
    private final JComboBox<String> statusComboBox;
    private final JTextField positionField = new JTextField();

    private final JTable table;
    private final DefaultTableModel tableModel;

    private final JButton insertBtn = new JButton("Insert");
    private final JButton updateBtn = new JButton("Update");
    private final JButton deleteBtn = new JButton("Delete");
    private final JButton feedbackBtn = new JButton("Feedback");

    private final JLabel avatarLabel = new JLabel();

    private final JLabel titleLabel;

    private final JCheckBox[] weekdayChecks = {
            new JCheckBox("Sun"), new JCheckBox("Mon"), new JCheckBox("Tue"),
            new JCheckBox("Wed"), new JCheckBox("Thu"), new JCheckBox("Fri"), new JCheckBox("Sat")
    };

    private final JButton colorBtn = new JButton("Pick Color");
    private Color selectedColor = Color.RED;

    private DefaultTableModel salaryTableModel;
    private JLabel totalSalaryLabel;
    private JLabel averageSalaryLabel;
    private JLabel budgetLeftLabel;

    private final double companyBudget = 100000.0;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public System() {
        setTitle("Employee Management System");
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Use custom background panel as content pane
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        titleLabel = new JLabel("Employee Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(19, 90, 180));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(getWidth(), 60));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(10, 50, 120)));
        bgPanel.add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        // style tabbed pane font
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bgPanel.add(tabbedPane, BorderLayout.CENTER);

        // --- VIEW TAB ---
        JPanel viewPanel = new JPanel(null);
        viewPanel.setOpaque(false);
        String[] columns = {"EmpId", "Name", "Age", "Email", "Status", "Position", "HireDate", "AvailableDays", "Color", "Feedback"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Use striped rows for better office look
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 940, 500);
        scrollPane.getViewport().setBackground(Color.WHITE);
        viewPanel.setLayout(null);
        viewPanel.add(scrollPane);
        tabbedPane.addTab("View", viewPanel);

        // --- MANAGE TAB ---
        JPanel managePanel = new JPanel(new BorderLayout());
        managePanel.setOpaque(false);

        // Form panel with light background for office feel
        JPanel formPanel = new JPanel(new GridBagLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // subtle gradient background
                Graphics2D g2d = (Graphics2D)g;
                int w = getWidth();
                int h = getHeight();
                Color c1 = new Color(240, 248, 255);
                Color c2 = new Color(220, 230, 240);
                GradientPaint gp = new GradientPaint(0,0,c1,0,h,c2);
                g2d.setPaint(gp);
                g2d.fillRect(0,0,w,h);
            }
        };
        formPanel.setBorder(new TitledBorder(null, "Employee Details",
                TitledBorder.LEADING, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(50, 90, 140)));
        GridBagConstraints gbc = new GridBagConstraints();

        int labelWidth = 120;
        int fieldWidth = 170;

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("EmpID:"), gbc);
        gbc.gridy++;
        formPanel.add(createStyledLabel("Name:"), gbc);
        gbc.gridy++;
        formPanel.add(createStyledLabel("Age:"), gbc);
        gbc.gridy++;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridy++;
        formPanel.add(createStyledLabel("Hire Date:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("Status:"), gbc);
        gbc.gridy++;
        formPanel.add(createStyledLabel("Position:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        setFieldProperties(empIdField, fieldWidth);
        formPanel.add(empIdField, gbc);
        gbc.gridy++;
        setFieldProperties(nameField, fieldWidth);
        formPanel.add(nameField, gbc);
        gbc.gridy++;
        setFieldProperties(ageField, fieldWidth);
        formPanel.add(ageField, gbc);
        gbc.gridy++;
        setFieldProperties(emailField, fieldWidth);
        formPanel.add(emailField, gbc);
        gbc.gridy++;
        JPanel hireDatePanel = new JPanel(new BorderLayout(5,0));
        hireDatePanel.setOpaque(false);
        setFieldProperties(hireDateField, fieldWidth - 80);
        hireDatePickBtn.setPreferredSize(new Dimension(80, 28));
        hireDatePanel.add(hireDateField, BorderLayout.CENTER);
        hireDatePanel.add(hireDatePickBtn, BorderLayout.EAST);
        formPanel.add(hireDatePanel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        String[] statuses = {"Active", "Inactive", "Suspended", "Terminated"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setPreferredSize(new Dimension(fieldWidth, 28));
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(statusComboBox, gbc);
        gbc.gridy++;
        setFieldProperties(positionField, fieldWidth);
        formPanel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        JPanel daysPanel = new JPanel();
        daysPanel.setOpaque(false);
        for (JCheckBox cb : weekdayChecks){
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            daysPanel.add(cb);
        }
        formPanel.add(daysPanel, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        colorBtn.setPreferredSize(new Dimension(120, 30));
        formPanel.add(colorBtn, gbc);

        hireDatePickBtn.addActionListener(e -> openDatePickerDialog());

        colorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Employee Color", selectedColor);
            if (c != null) {
                selectedColor = c;
                colorBtn.setBackground(selectedColor);
            }
        });

        managePanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        buttonsPanel.setOpaque(false);
        styleButton(insertBtn);
        styleButton(updateBtn);
        styleButton(deleteBtn);
        styleButton(feedbackBtn);
        buttonsPanel.add(insertBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(feedbackBtn);
        managePanel.add(buttonsPanel, BorderLayout.SOUTH);

        avatarLabel.setIcon(new ImageIcon(new ImageIcon("avatar.jpg")
                .getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH)));
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BorderLayout());
        avatarPanel.setPreferredSize(new Dimension(150, 200));
        avatarPanel.setOpaque(false);
        avatarPanel.add(avatarLabel, BorderLayout.NORTH);
        managePanel.add(avatarPanel, BorderLayout.EAST);

        tabbedPane.addTab("Manage", managePanel);

        // --- SUMMARY TAB ---
        JPanel summaryPanel = new JPanel(null);
        summaryPanel.setOpaque(false);
        JTextField totalEmployeesField = new JTextField();
        totalEmployeesField.setBounds(200, 50, 150, 25);
        totalEmployeesField.setEditable(false);
        JLabel summaryLabel = createStyledLabel("Total Employees:");
        summaryPanel.add(summaryLabel);
        summaryLabel.setBounds(50, 50, 150, 25);
        summaryPanel.add(totalEmployeesField);

        JButton refreshBtn = new JButton("Refresh Summary");
        styleButton(refreshBtn);
        refreshBtn.setBounds(120, 100, 180, 30);
        summaryPanel.add(refreshBtn);
        refreshBtn.addActionListener(e -> {
            int total = tableModel.getRowCount();
            totalEmployeesField.setText(String.valueOf(total));
        });
        tabbedPane.addTab("Summary", summaryPanel);

        // --- CALENDAR TAB ---
        CalendarPanel calendarPanel = new CalendarPanel(tableModel);
        tabbedPane.addTab("Calendar", calendarPanel);

        // --- SALARY CALCULATOR ---
        JPanel salaryPanel = new JPanel(new BorderLayout());
        salaryPanel.setOpaque(false);

        salaryTableModel = new DefaultTableModel(new Object[]{"Employee Name", "Salary", "Tax"}, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
            @Override public Class<?> getColumnClass(int col) {
                return switch(col) {
                    case 1, 2 -> Double.class;
                    default -> String.class;
                };
            }
        };
        JTable salaryTable = new JTable(salaryTableModel);
        salaryTable.setRowHeight(30);
        JScrollPane salaryScrollPane = new JScrollPane(salaryTable);
        salaryPanel.add(salaryScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        bottomPanel.setOpaque(false);
        totalSalaryLabel = new JLabel("Total Salary: 0.00");
        averageSalaryLabel = new JLabel("Average Salary: 0.00");
        budgetLeftLabel = new JLabel("Budget Left: " + String.format("%.2f", companyBudget));
        Font statFont = new Font("Segoe UI Semibold", Font.PLAIN, 14);
        totalSalaryLabel.setFont(statFont);
        averageSalaryLabel.setFont(statFont);
        budgetLeftLabel.setFont(statFont);
        bottomPanel.add(totalSalaryLabel);
        bottomPanel.add(averageSalaryLabel);
        bottomPanel.add(budgetLeftLabel);
        salaryPanel.add(bottomPanel, BorderLayout.SOUTH);

        salaryTableModel.addTableModelListener(e -> {
            if (e.getColumn() == 1 || e.getType() == TableModelEvent.INSERT) {
                recalculateSalaryStats();
            }
        });

        tabbedPane.addTab("Salary Calculator", salaryPanel);

        // Buttons action handlers
        insertBtn.addActionListener(e -> onInsert(calendarPanel));
        updateBtn.addActionListener(e -> onUpdate(calendarPanel));
        deleteBtn.addActionListener(e -> onDelete(calendarPanel));
        feedbackBtn.addActionListener(e -> onFeedback());

        table.getSelectionModel().addListSelectionListener(e -> onSelectTable());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Utility: style labels
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(35, 35, 35));
        return label;
    }

    // Utility: style text fields
    private void setFieldProperties(JTextField field, int width) {
        field.setPreferredSize(new Dimension(width, 28));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170)),
                BorderFactory.createEmptyBorder(5,5,5,5)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.DARK_GRAY);
    }

    // Utility: style buttons
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        btn.setBackground(new Color(30, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(10, 70, 160)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 150, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 120, 215));
            }
        });
    }

    private void onFeedback() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an employee to provide feedback.", "No Employee Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String employeeName = (String) tableModel.getValueAt(row, 1);
        String currentFeedback = "";
        Object feedbackObj = tableModel.getValueAt(row, 9);
        if (feedbackObj != null) {
            currentFeedback = feedbackObj.toString();
        }

        JTextArea feedbackArea = new JTextArea(10, 30);
        feedbackArea.setText(currentFeedback);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        int option = JOptionPane.showConfirmDialog(this, scrollPane, "Feedback for " + employeeName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String feedback = feedbackArea.getText().trim();
            tableModel.setValueAt(feedback, row, 9);
        }
    }

    // Input validation helpers
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.matches("\\d+");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private void onInsert(CalendarPanel calendarPanel) {
        if (!validateInputs()) return;

        String days = getSelectedDays();
        String colorHex = "#" + Integer.toHexString(selectedColor.getRGB()).substring(2);
        String status = (String) statusComboBox.getSelectedItem();
        String hireDate = hireDateField.getText().trim();

        String[] row = {
                empIdField.getText(), nameField.getText(), ageField.getText(), emailField.getText(),
                status, positionField.getText(), hireDate,
                getDayNames(days), colorHex, ""
        };
        tableModel.addRow(row);

        salaryTableModel.addRow(new Object[]{nameField.getText(), 0.0, 0.0});
        recalculateSalaryStats();

        calendarPanel.updateCalendarData(tableModel);

        clearFormFields();
    }

    private void onUpdate(CalendarPanel calendarPanel) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInputs()) return;

        String days = getSelectedDays();
        String colorHex = "#" + Integer.toHexString(selectedColor.getRGB()).substring(2);
        String status = (String) statusComboBox.getSelectedItem();
        String hireDate = hireDateField.getText().trim();

        String existingFeedback = "";
        Object feedbackObj = tableModel.getValueAt(row, 9);
        if (feedbackObj != null) {
            existingFeedback = feedbackObj.toString();
        }

        tableModel.setValueAt(empIdField.getText(), row, 0);
        tableModel.setValueAt(nameField.getText(), row, 1);
        tableModel.setValueAt(ageField.getText(), row, 2);
        tableModel.setValueAt(emailField.getText(), row, 3);
        tableModel.setValueAt(status, row, 4);
        tableModel.setValueAt(positionField.getText(), row, 5);
        tableModel.setValueAt(hireDate, row, 6);
        tableModel.setValueAt(getDayNames(days), row, 7);
        tableModel.setValueAt(colorHex, row, 8);
        tableModel.setValueAt(existingFeedback, row, 9);

        for (int i = 0; i < salaryTableModel.getRowCount(); i++) {
            String existingName = (String) salaryTableModel.getValueAt(i, 0);
            if (existingName.equals(tableModel.getValueAt(row, 1))) {
                salaryTableModel.setValueAt(nameField.getText(), i, 0);
                break;
            }
        }

        recalculateSalaryStats();
        calendarPanel.updateCalendarData(tableModel);

        clearFormFields();
    }

    private boolean validateInputs() {
        if (!isNumeric(empIdField.getText())) {
            JOptionPane.showMessageDialog(this, "EmpID must be numeric.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            empIdField.requestFocus();
            return false;
        }
        if (!isNumeric(ageField.getText())) {
            JOptionPane.showMessageDialog(this, "Age must be numeric.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            ageField.requestFocus();
            return false;
        }
        if (!isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Email must contain '@'.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        if(!isValidDate(hireDateField.getText())){
            JOptionPane.showMessageDialog(this,"Please enter a valid date in format yyyy-MM-dd for Hire Date.","Invalid Date",JOptionPane.ERROR_MESSAGE);
            hireDateField.requestFocus();
            return false;
        }
        return true;
    }

    private void onDelete(CalendarPanel calendarPanel){
        int row = table.getSelectedRow();
        if (row >= 0) {
            String nameToRemove = (String) tableModel.getValueAt(row, 1);
            tableModel.removeRow(row);

            for (int i = 0; i < salaryTableModel.getRowCount(); i++) {
                if (salaryTableModel.getValueAt(i, 0).equals(nameToRemove)) {
                    salaryTableModel.removeRow(i);
                    break;
                }
            }
            recalculateSalaryStats();
            calendarPanel.updateCalendarData(tableModel);
        }
    }

    private void onSelectTable() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            empIdField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            ageField.setText(tableModel.getValueAt(row, 2).toString());
            emailField.setText(tableModel.getValueAt(row, 3).toString());
            String status = tableModel.getValueAt(row, 4).toString();
            statusComboBox.setSelectedItem(status);
            positionField.setText(tableModel.getValueAt(row, 5).toString());
            hireDateField.setText(tableModel.getValueAt(row, 6).toString());

            String days = tableModel.getValueAt(row, 7).toString();
            for (JCheckBox cb : weekdayChecks) {
                cb.setSelected(days.contains(cb.getText()));
            }

            String colorHex = tableModel.getValueAt(row, 8).toString();
            selectedColor = Color.decode(colorHex);
            colorBtn.setBackground(selectedColor);
        }
    }

    private void openDatePickerDialog() {
        JDialog dialog = new JDialog(this, "Pick a Date", true);
        dialog.setLayout(new FlowLayout());
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dialog.add(dateSpinner);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            Date selectedDate = model.getDate();
            if(selectedDate != null) {
                hireDateField.setText(dateFormat.format(selectedDate));
            }
            dialog.dispose();
        });

        dialog.add(okButton);
        dialog.setSize(300,140);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean isValidDate(String dateStr) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void clearFormFields() {
        empIdField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        hireDateField.setText("");
        positionField.setText("");
        statusComboBox.setSelectedIndex(0);
        for (JCheckBox cb : weekdayChecks) {
            cb.setSelected(false);
        }
        selectedColor = Color.RED;
        colorBtn.setBackground(selectedColor);
    }

    private String getSelectedDays() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weekdayChecks.length; i++) {
            if (weekdayChecks[i].isSelected()) sb.append(i).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    private String getDayNames(String csvIndices) {
        if (csvIndices.isEmpty()) return "";
        String[] indices = csvIndices.split(",");
        StringBuilder sb = new StringBuilder();
        for (String idx : indices) {
            int i = Integer.parseInt(idx);
            sb.append(weekdayChecks[i].getText()).append(" ");
        }
        return sb.toString().trim();
    }

    private void recalculateSalaryStats() {
        double total = 0;
        int count = 0;

        for (int i = 0; i < salaryTableModel.getRowCount(); i++) {
            Object val = salaryTableModel.getValueAt(i, 1);
            double salary = 0;
            if (val instanceof Number) {
                salary = ((Number) val).doubleValue();
            } else if (val != null) {
                try {
                    salary = Double.parseDouble(val.toString());
                } catch (NumberFormatException ignored) {
                }
            }

            double tax = salary * 0.10;
            salaryTableModel.setValueAt(tax, i, 2);

            total += salary;
            count++;
        }
        double budgetLeft = companyBudget - total - (total * 0.10);
        if (budgetLeft < 0) {
            budgetLeftLabel.setForeground(Color.RED);
        } else {
            budgetLeftLabel.setForeground(Color.BLACK);
        }
        totalSalaryLabel.setText("Total Salary: " + String.format("%.2f", total));
        averageSalaryLabel.setText("Average Salary: " + (count > 0 ? String.format("%.2f", total / count) : "0.00"));
        budgetLeftLabel.setText("Budget Left: " + String.format("%.2f", budgetLeft));
    }

    // ------------ Calendar Panel with colored dot and legend --------------
    private static class CalendarPanel extends JPanel {
        private final DefaultTableModel employeeModel;
        private final JLabel monthYearLabel;
        private final JButton prevButton, nextButton;
        private final JPanel calendarGrid;
        private final JPanel legendPanel;

        private YearMonth currentYearMonth;
        private Map<Integer, List<EmployeeInfo>> dayEmployeeMap = new HashMap<>();

        private static class EmployeeInfo {
            String name;
            Color color;

            EmployeeInfo(String name, Color color) {
                this.name = name;
                this.color = color;
            }
        }

        CalendarPanel(DefaultTableModel employeeModel) {
            this.employeeModel = employeeModel;
            this.currentYearMonth = YearMonth.now();

            setLayout(new BorderLayout());
            JPanel header = new JPanel(new BorderLayout());

            prevButton = new JButton("<");
            nextButton = new JButton(">");
            monthYearLabel = new JLabel("", SwingConstants.CENTER);
            monthYearLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));

            header.add(prevButton, BorderLayout.WEST);
            header.add(monthYearLabel, BorderLayout.CENTER);
            header.add(nextButton, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            calendarGrid = new JPanel(new GridLayout(0, 7));
            add(calendarGrid, BorderLayout.CENTER);

            legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            legendPanel.setBorder(BorderFactory.createTitledBorder("Employee Legend"));
            add(legendPanel, BorderLayout.SOUTH);

            prevButton.addActionListener(e -> {
                currentYearMonth = currentYearMonth.minusMonths(1);
                refreshCalendar();
            });
            nextButton.addActionListener(e -> {
                currentYearMonth = currentYearMonth.plusMonths(1);
                refreshCalendar();
            });

            updateCalendarData(employeeModel);
            refreshCalendar();
        }

        void updateCalendarData(DefaultTableModel model) {
            dayEmployeeMap.clear();
            Map<String, Color> nameColorMap = new LinkedHashMap<>();
            for (int r = 0; r < model.getRowCount(); r++) {
                String name = model.getValueAt(r, 1).toString();
                String colorStr = model.getValueAt(r, 8).toString();
                Color color;
                try {
                    color = Color.decode(colorStr);
                } catch (Exception e) {
                    color = Color.BLACK;
                }
                nameColorMap.put(name, color);
            }

            for (int r = 0; r < model.getRowCount(); r++) {
                String daysStr = model.getValueAt(r, 7).toString();
                String[] dayNames = daysStr.split(" ");
                String name = model.getValueAt(r, 1).toString();
                Color color = nameColorMap.getOrDefault(name, Color.BLACK);
                for (String day : dayNames) {
                    if (!day.isBlank()) {
                        int dayIndex = dayToIndex(day);
                        if (dayIndex >= 0) {
                            dayEmployeeMap.computeIfAbsent(dayIndex, k -> new ArrayList<>()).add(new EmployeeInfo(name, color));
                        }
                    }
                }
            }
            updateLegend(nameColorMap);
            refreshCalendar();
        }

        private void updateLegend(Map<String, Color> nameColorMap) {
            legendPanel.removeAll();
            for (Map.Entry<String, Color> entry : nameColorMap.entrySet()) {
                JLabel colorBox = new JLabel("  ");
                colorBox.setOpaque(true);
                colorBox.setBackground(entry.getValue());
                colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                colorBox.setPreferredSize(new Dimension(20, 20));

                JLabel nameLabel = new JLabel(entry.getKey());
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

                JPanel entryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                entryPanel.add(colorBox);
                entryPanel.add(nameLabel);

                legendPanel.add(entryPanel);
            }
            legendPanel.revalidate();
            legendPanel.repaint();
        }

        private int dayToIndex(String day) {
            switch (day.toLowerCase()) {
                case "sun": return 0;
                case "mon": return 1;
                case "tue": return 2;
                case "wed": return 3;
                case "thu": return 4;
                case "fri": return 5;
                case "sat": return 6;
                default: return -1;
            }
        }

        private void refreshCalendar() {
            calendarGrid.removeAll();
            String monthDisplay = currentYearMonth.getMonth().toString();
            monthDisplay = monthDisplay.substring(0,1).toUpperCase() + monthDisplay.substring(1).toLowerCase();
            monthYearLabel.setText(monthDisplay + " " + currentYearMonth.getYear());

            String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String d : days) {
                JLabel lbl = new JLabel(d, SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                calendarGrid.add(lbl);
            }

            LocalDate firstOfMonth = currentYearMonth.atDay(1);
            int startDay = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
            int length = currentYearMonth.lengthOfMonth();

            for (int i = 0; i < startDay; i++) calendarGrid.add(new JLabel(""));

            for (int day = 1; day <= length; day++) {
                int dayOfWeek = (startDay + day - 1) % 7;
                JLabel dayLabel = new JLabel();
                dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dayLabel.setVerticalAlignment(SwingConstants.TOP);
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                dayLabel.setOpaque(true);
                dayLabel.setBackground(Color.WHITE);

                List<EmployeeInfo> employees = dayEmployeeMap.get(dayOfWeek);
                if (employees != null && !employees.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html><center>").append(day).append("<br>");
                    int maxDots = Math.min(employees.size(), 5);
                    for (int i = 0; i < maxDots; i++) {
                        EmployeeInfo emp = employees.get(i);
                        String hexColor = String.format("#%06x", emp.color.getRGB() & 0xFFFFFF);
                        sb.append("<span style='color:").append(hexColor).append("; font-size:140%'>&bull;</span> ");
                    }
                    if (employees.size() > maxDots) {
                        sb.append("+").append(employees.size() - maxDots);
                    }
                    sb.append("</center></html>");
                    dayLabel.setText(sb.toString());
                } else {
                    dayLabel.setText(String.valueOf(day));
                }

                calendarGrid.add(dayLabel);
            }
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(System::new);
    }

    // Background panel with vertical gradient for office feel
    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();

            Color color1 = new Color(235, 240, 245);
            Color color2 = new Color(200, 210, 220);

            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}
