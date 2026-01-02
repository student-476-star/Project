import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IDSDashboard extends JFrame {

    private JLabel lblClock, lblTotalConn, lblBlocked;
    private JTable portScanTable, dosTable;
    private DefaultTableModel portScanModel, dosModel;
    private JTextArea alertArea;

    private javax.swing.Timer clockTimer; 

    public IDSDashboard() {
        setTitle("Intrusion Detection System - Dashboard");
        setSize(1200, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top panel: Logo + User Info + Clock + Logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 230, 241));
        add(topPanel, BorderLayout.NORTH);

        // Logo
        JLabel lblLogo = new JLabel(new ImageIcon("panimalar.jpg"));
        topPanel.add(lblLogo, BorderLayout.WEST);

        // User Info
        JLabel lblUser = new JLabel(
                "<html>Name: Balaji M<br/>Reg No: 211422104069<br/>CSE | Anna University</html>"
        );
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(lblUser, BorderLayout.CENTER);

        // Clock + Logout
        JPanel rightTop = new JPanel(new FlowLayout());
        lblClock = new JLabel();
        lblClock.setFont(new Font("Arial", Font.BOLD, 14));
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> System.exit(0));
        rightTop.add(lblClock);
        rightTop.add(btnLogout);
        topPanel.add(rightTop, BorderLayout.EAST);

        // Center panel: tables and alerts
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(centerSplit, BorderLayout.CENTER);

        // Left: Alerts
        JPanel leftPanel = new JPanel(new BorderLayout());
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        leftPanel.add(new JLabel("Alerts"), BorderLayout.NORTH);
        leftPanel.add(alertScroll, BorderLayout.CENTER);
        centerSplit.setLeftComponent(leftPanel);

        // Right: Tables
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));

        // Port Scan Table
        portScanModel = new DefaultTableModel(new String[]{"IP", "Port Scan Count"}, 0);
        portScanTable = new JTable(portScanModel);
        JScrollPane portScroll = new JScrollPane(portScanTable);
        rightPanel.add(createTitledPanel("Port Scan Table", portScroll));

        // DOS Table
        dosModel = new DefaultTableModel(new String[]{"IP", "Request Count"}, 0);
        dosTable = new JTable(dosModel);
        JScrollPane dosScroll = new JScrollPane(dosTable);
        rightPanel.add(createTitledPanel("DOS Table", dosScroll));

        centerSplit.setRightComponent(rightPanel);
        centerSplit.setDividerLocation(500);

        // Bottom panel: Stats + CSV Export + Settings
        JPanel bottomPanel = new JPanel(new FlowLayout());
        lblTotalConn = new JLabel("Total Connections: 0");
        lblBlocked = new JLabel("Blocked IPs: 0");
        JButton btnExport = new JButton("Export CSV");
        JButton btnSettings = new JButton("Settings");

        // CSV Export stub
        btnExport.addActionListener(e -> {
            IDSReportExporter.exportCSV();
            JOptionPane.showMessageDialog(this, "CSV exported successfully!");
        });

        // Settings dialog stub
        btnSettings.addActionListener(e -> new IDSSettingsDialog(this));

        bottomPanel.add(lblTotalConn);
        bottomPanel.add(lblBlocked);
        bottomPanel.add(btnExport);
        bottomPanel.add(btnSettings);
        add(bottomPanel, BorderLayout.SOUTH);

        // Clock Timer
        clockTimer = new javax.swing.Timer(1000, e -> {
            lblClock.setText(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            updateStats();
        });
        clockTimer.start();

        setVisible(true);
    }

    private JPanel createTitledPanel(String title, JScrollPane scroll) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public synchronized void addAlert(String ip, String attack) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        alertArea.append("[" + time + "] " + ip + " : " + attack + "\n");
    }

    public synchronized void updatePortScan(String ip, int count) {
        boolean found = false;
        for (int i = 0; i < portScanModel.getRowCount(); i++) {
            if (portScanModel.getValueAt(i, 0).equals(ip)) {
                portScanModel.setValueAt(count, i, 1);
                found = true;
                break;
            }
        }
        if (!found) portScanModel.addRow(new Object[]{ip, count});
    }

    public synchronized void updateDOS(String ip, int count) {
        boolean found = false;
        for (int i = 0; i < dosModel.getRowCount(); i++) {
            if (dosModel.getValueAt(i, 0).equals(ip)) {
                dosModel.setValueAt(count, i, 1);
                found = true;
                break;
            }
        }
        if (!found) dosModel.addRow(new Object[]{ip, count});
    }

    private void updateStats() {
        lblTotalConn.setText("Total Connections: " + getTotalConnections());
        lblBlocked.setText("Blocked IPs: " + IPBlocker.getBlockedCount());
    }

    private int getTotalConnections() {
        return dosModel.getRowCount(); // Approximate total connections
    }
}
