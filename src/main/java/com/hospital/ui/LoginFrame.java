package com.hospital.ui;

import com.hospital.dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();

        setTitle("Hospital Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Hospital Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        userField = new JTextField();
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField();
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        panel.add(title);
        panel.add(userField);
        panel.add(passField);
        panel.add(buttonPanel);

        add(panel);
    }

    private void login() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (userDAO.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new MainFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username and password.");
            return;
        }

        if (userDAO.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.");
            userField.setText("");
            passField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Try another.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}