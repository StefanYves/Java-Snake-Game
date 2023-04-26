import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginForm extends JFrame {

    private static final String USER_FILE = "users.txt";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JLabel errorLabel;

    public LoginForm() {
        super("Login Form");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);

        Container pane = this.getContentPane();
        pane.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidUser(username, password)) {
                    setVisible(false);
                    dispose();
                    new Game();
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            }
        });

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidUsername(username)) {
                    if (saveUser(username, password)) {
                        JOptionPane.showMessageDialog(LoginForm.this, "Registration successful! Please log in.");
                    } else {
                        JOptionPane.showMessageDialog(LoginForm.this, "Error: could not save user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        pane.add(usernameLabel);
        pane.add(usernameField);
        pane.add(passwordLabel);
        pane.add(passwordField);
        pane.add(loginButton);
        pane.add(registerButton);
        pane.add(errorLabel);

        this.setVisible(true);
    }

    private boolean isValidUser(String username, String password) {
        try (Scanner scanner = new Scanner(new File(USER_FILE))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(":");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isValidUsername(String username) {
        // Check if the username is already taken
        // For demo purposes, we only allow one user with the username "user"
        return !USERNAME.equals(username);
    }

    private boolean saveUser(String username, String password) {
        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(username + ":" + password + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}