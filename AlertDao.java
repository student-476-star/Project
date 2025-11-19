package com.netguard.ids.db;

import com.netguard.ids.model.Alert;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AlertDao {
    private final Connection conn;

    public AlertDao(Connection conn) throws SQLException {
        this.conn = conn;
        init();
    }

    private void init() throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS alerts (id IDENTITY PRIMARY KEY, rule_id VARCHAR(50), rule_name VARCHAR(255), src_ip VARCHAR(50), src_port INT, dst_ip VARCHAR(50), dst_port INT, ts TIMESTAMP, snippet VARCHAR(1024))");
        }
    }

    public void insert(Alert a) throws SQLException {
        String sql = "INSERT INTO alerts(rule_id, rule_name, src_ip, src_port, dst_ip, dst_port, ts, snippet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getRuleId());
            ps.setString(2, a.getRuleName());
            ps.setString(3, a.getSrcIp());
            ps.setInt(4, a.getSrcPort());
            ps.setString(5, a.getDstIp());
            ps.setInt(6, a.getDstPort());
            ps.setTimestamp(7, Timestamp.from(a.getTimestamp()));
            ps.setString(8, a.getPayloadSnippet());
            ps.executeUpdate();
        }
    }

    public List<Alert> listAll() throws SQLException {
        List<Alert> out = new ArrayList<>();
        try (Statement s = conn.createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT id, rule_id, rule_name, src_ip, src_port, dst_ip, dst_port, ts, snippet FROM alerts ORDER BY ts DESC")) {
                while (rs.next()) {
                    Alert a = new Alert();
                    a.setId(rs.getLong(1));
                    a.setRuleId(rs.getString(2));
                    a.setRuleName(rs.getString(3));
                    a.setSrcIp(rs.getString(4));
                    a.setSrcPort(rs.getInt(5));
                    a.setDstIp(rs.getString(6));
                    a.setDstPort(rs.getInt(7));
                    a.setTimestamp(rs.getTimestamp(8).toInstant());
                    a.setPayloadSnippet(rs.getString(9));
                    out.add(a);
                }
            }
        }
        return out;
    }
}
