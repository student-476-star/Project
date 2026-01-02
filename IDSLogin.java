import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;

public class IDSLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public IDSLogin() {
        setTitle("IDS Login");
        setSize(400, 250);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblUser, gbc);

        txtUsername = new JTextField();
        gbc.gridx = 1;
        add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblPass, gbc);

        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        add(txtPassword, gbc);

        btnLogin = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> authenticate());

        setVisible(true);
    }

    private void authenticate() {

        String enteredUser = txtUsername.getText().trim();
        String enteredPass = new String(txtPassword.getPassword());

        String correctUser = "balaji";
        String correctPassHash = hashPassword("ice123");

        if (enteredUser.equals(correctUser)
                && hashPassword(enteredPass).equals(correctPassHash)) {

            JOptionPane.showMessageDialog(this, "Login Successful");

            dispose();

            IDSServer.startServer();

            IDSDashboard dashboard = new IDSDashboard();

            new Thread(() -> AttackSimulator.startSimulation(dashboard)).start();

        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        new IDSLogin();
    }
}
