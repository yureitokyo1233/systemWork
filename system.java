package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class system extends JFrame {
    private static final long serialVersionUID = 1L;
    private java.util.List<Employee> employees = new ArrayList<>();
    private DefaultListModel<String> viewListModel = new DefaultListModel<>();
    private JList<String> viewList = new JList<>(viewListModel);

    // These need to be fields so refreshLists() can access them
    private DefaultListModel<String> removeListModel = new DefaultListModel<>();
    private JList<String> removeList = new JList<>(removeListModel);
    private JComboBox<String> editCombo = new JComboBox<>();

    public system() {
        setTitle("Employee Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // View Tab
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(new JScrollPane(viewList), BorderLayout.CENTER);
        tabbedPane.addTab("View", viewPanel);

        // Add Tab
        JPanel addPanel = new JPanel(new GridLayout(5, 2));
        JTextField addIdField = new JTextField();
        JTextField addNameField = new JTextField();
        JTextField addJobField = new JTextField();
        JTextField addSalaryField = new JTextField();
        JButton addButton = new JButton("Add Employee");
        addPanel.add(new JLabel("ID:"));
        addPanel.add(addIdField);
        addPanel.add(new JLabel("Name:"));
        addPanel.add(addNameField);
        addPanel.add(new JLabel("Job Description:"));
        addPanel.add(addJobField);
        addPanel.add(new JLabel("Salary:"));
        addPanel.add(addSalaryField);
        addPanel.add(new JLabel());
        addPanel.add(addButton);
        tabbedPane.addTab("Add", addPanel);

        // Remove Tab
        JPanel removePanel = new JPanel(new BorderLayout());
        removePanel.add(new JScrollPane(removeList), BorderLayout.CENTER);
        JButton removeButton = new JButton("Remove Selected");
        removePanel.add(removeButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Remove", removePanel);

        // Edit Tab
        JPanel editPanel = new JPanel(new GridLayout(6, 2));
        JTextField editNameField = new JTextField();
        JTextField editJobField = new JTextField();
        JTextField editSalaryField = new JTextField();
        JButton editButton = new JButton("Save Changes");
        editPanel.add(new JLabel("Select Employee:"));
        editPanel.add(editCombo);
        editPanel.add(new JLabel("Name:"));
        editPanel.add(editNameField);
        editPanel.add(new JLabel("Job Description:"));
        editPanel.add(editJobField);
        editPanel.add(new JLabel("Salary:"));
        editPanel.add(editSalaryField);
        editPanel.add(new JLabel());
        editPanel.add(editButton);
        tabbedPane.addTab("Edit", editPanel);

        // Add Employee Action
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(addIdField.getText());
                if (employees.stream().anyMatch(emp -> emp.id == id)) {
                    JOptionPane.showMessageDialog(this, "ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = addNameField.getText();
                String job = addJobField.getText();
                double salary = Double.parseDouble(addSalaryField.getText());
                Employee emp = new Employee(id, name, job, salary);
                employees.add(emp);
                refreshLists();
                addIdField.setText("");
                addNameField.setText("");
                addJobField.setText("");
                addSalaryField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Remove Employee Action
        removeButton.addActionListener(e -> {
            int idx = removeList.getSelectedIndex();
            if (idx != -1) {
                employees.remove(idx);
                refreshLists();
            }
        });

        // Edit ComboBox Selection
        editCombo.addActionListener(e -> {
            int idx = editCombo.getSelectedIndex();
            if (idx != -1 && idx < employees.size()) {
                Employee emp = employees.get(idx);
                editNameField.setText(emp.name);
                editJobField.setText(emp.jobDescription);
                editSalaryField.setText(String.valueOf(emp.salary));
            }
        });

        // Edit Employee Action
        editButton.addActionListener(e -> {
            int idx = editCombo.getSelectedIndex();
            if (idx != -1 && idx < employees.size()) {
                try {
                    Employee emp = employees.get(idx);
                    emp.name = editNameField.getText();
                    emp.jobDescription = editJobField.getText();
                    emp.salary = Double.parseDouble(editSalaryField.getText());
                    refreshLists();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid salary.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(tabbedPane);
        refreshLists();
    }

    // Move this method outside the constructor
    private void refreshLists() {
        viewListModel.clear();
        removeListModel.clear();
        editCombo.removeAllItems();
        for (Employee emp : employees) {
            String info = "ID: " + emp.id + " | Name: " + emp.name + " | Job: " + emp.jobDescription + " | Salary: " + emp.salary;
            viewListModel.addElement(info);
            removeListModel.addElement(info);
            editCombo.addItem("ID: " + emp.id + " - " + emp.name);
        }
    }

    // Employee class
    static class Employee {
        int id;
        String name;
        String jobDescription;
        double salary;

        Employee(int id, String name, String jobDescription, double salary) {
            this.id = id;
            this.name = name;
            this.jobDescription = jobDescription;
            this.salary = salary;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new system().setVisible(true));
    }
}
