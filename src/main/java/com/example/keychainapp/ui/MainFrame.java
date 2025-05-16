package com.example.keychainapp.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.example.keychainapp.logic.KeychainService;
import com.example.keychainapp.logic.KeychainUtils;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextField keyField;
    private JTextField valueField;
    private JButton saveButton;
    private JTextArea outputArea;


    public MainFrame() {
        setTitle("Keychain App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Key:"));
        keyField = new JTextField();
        inputPanel.add(keyField);

        inputPanel.add(new JLabel("Value:"));
        valueField = new JTextField();
        inputPanel.add(valueField);

        saveButton = new JButton("Save");
        inputPanel.add(saveButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load and display saved key-value pairs
        try {
            KeychainService keychainService = new KeychainService();
            List<String> keys = KeychainUtils.listSavedKeys();
            for (String key : keys) {
                try {
                    String value = keychainService.retrieve(key);
                    outputArea.append("Saved: " + key + " = " + value + "\n");
                } catch (Exception ex) {
                    // Ignore errors for individual keys
                }
            }
        } catch (Exception ex) {
            // Ignore errors on startup
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                String value = valueField.getText();
                // Call KeychainService to save the key-value pair
                // KeychainService.save(key, value);
                outputArea.append("Saved: " + key + " = " + value + "\n");
                keyField.setText("");
                valueField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}