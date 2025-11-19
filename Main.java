package com.netguard.ids;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netguard.ids.capture.PcapCaptor;
import com.netguard.ids.db.AlertDao;
import com.netguard.ids.engine.SignatureEngine;
import com.netguard.ids.model.Alert;
import com.netguard.ids.model.Rule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static DefaultTableModel tableModel;

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java -jar netguard-ids.jar <network-interface-name>\nExample interfaces: on Linux: eth0, wlan0. On Windows use the interface name from npcap/winpcap list.");
            System.exit(1);
        }
        String nif = args[0];

        // Load rules
        InputStream in = Main.class.getResourceAsStream("/rules.json");
        List<Rule> rules = mapper.readValue(in, new TypeReference<List<Rule>>(){});

        // Build engine
        SignatureEngine engine = new SignatureEngine(rules);

        // Create H2 in-memory DB (file-based for persistence)
        Path dbFile = Path.of(System.getProperty("user.home"), ".netguard_ids_db");
        Files.createDirectories(dbFile);
        String jdbc = "jdbc:h2:" + dbFile.toAbsolutePath().toString() + "/netguard;AUTO_SERVER=TRUE";
        Connection conn = DriverManager.getConnection(jdbc, "sa", "");
        AlertDao dao = new AlertDao(conn);

        // Setup UI
        SwingUtilities.invokeLater(() -> createAndShowGui());

        // Start captor
        PcapCaptor captor = new PcapCaptor(nif, engine, conn, alert -> {
            try {
                dao.insert(alert);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> addAlertToTable(alert));
        }, true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down capture...");
            captor.stop();
            try { conn.close(); } catch (Exception ignored) {}
        }));

        System.out.println("Starting live capture on interface: " + nif);
        captor.start();
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("NetGuard-IDS - Live Alerts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);

        String[] cols = {"ID","Rule ID","Rule Name","Src IP","Src Port","Dst IP","Dst Port","Timestamp","Snippet"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane sp = new JScrollPane(table);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton exportBtn = new JButton("Export CSV");
        exportBtn.addActionListener(e -> exportCsv());
        top.add(exportBtn);

        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(sp, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void addAlertToTable(Alert a) {
        tableModel.addRow(new Object[]{a.getId(), a.getRuleId(), a.getRuleName(), a.getSrcIp(), a.getSrcPort(), a.getDstIp(), a.getDstPort(), a.getTimestamp(), a.getPayloadSnippet()});
    }

    private static void exportCsv() {
        try {
            java.io.File f = new java.io.File(System.getProperty("user.home"), "netguard_alerts_export.csv");
            try (java.io.PrintWriter pw = new java.io.PrintWriter(f)) {
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    StringBuilder sb = new StringBuilder();
                    for (int c = 0; c < tableModel.getColumnCount(); c++) {
                        Object val = tableModel.getValueAt(r, c);
                        sb.append(escapeCsv(val == null ? "" : val.toString()));
                        if (c < tableModel.getColumnCount() - 1) sb.append(',');
                    }
                    pw.println(sb.toString());
                }
            }
            JOptionPane.showMessageDialog(null, "Exported to: " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Export failed: " + e.getMessage());
        }
    }

    private static String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\n") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
