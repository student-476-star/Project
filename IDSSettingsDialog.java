import javax.swing.*;
import java.awt.*;

public class IDSSettingsDialog extends JDialog {
    public IDSSettingsDialog(JFrame parent) {
        super(parent, "IDS Settings", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        JLabel label = new JLabel("Settings dialog placeholder");
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);
        setVisible(true);
    }
}
