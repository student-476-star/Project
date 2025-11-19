package com.netguard.ids.capture;

import com.netguard.ids.engine.SignatureEngine;
import com.netguard.ids.engine.SignatureEngine.MatchResult;
import com.netguard.ids.model.Alert;
import com.netguard.ids.model.Rule;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.namednumber.TcpPort;

import java.net.InetAddress;
import java.sql.Connection;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PcapCaptor {
    private final PcapHandle handle;
    private final SignatureEngine engine;
    private final Connection dbConn;
    private final java.util.function.Consumer<Alert> alertConsumer;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public PcapCaptor(String nifName, SignatureEngine engine, Connection dbConn, java.util.function.Consumer<Alert> alertConsumer, boolean live) throws PcapNativeException {
        this.engine = engine;
        this.dbConn = dbConn;
        this.alertConsumer = alertConsumer;
        if (live) {
            PcapNetworkInterface nif = Pcaps.getDevByName(nifName);
            if (nif == null) throw new IllegalArgumentException("Network interface not found: " + nifName);
            int snaplen = 65536;
            this.handle = nif.openLive(snaplen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        } else {
            this.handle = null;
            throw new IllegalArgumentException("Use live=true for this class in live mode");
        }
    }

    public void start() throws PcapNativeException, NotOpenException {
        PacketListener listener = packet -> pool.submit(() -> process(packet));
        int max = -1;
        handle.loop(max, listener);
    }

    private void process(Packet packet) {
        try {
            IpV4Packet ipv4 = packet.get(IpV4Packet.class);
            if (ipv4 == null) return;
            InetAddress src = ipv4.getHeader().getSrcAddr();
            InetAddress dst = ipv4.getHeader().getDstAddr();
            TcpPacket tcp = packet.get(TcpPacket.class);
            UdpPacket udp = packet.get(UdpPacket.class);
            byte[] payload = null;
            int srcPort = -1, dstPort = -1;
            if (tcp != null) {
                TcpPacket.Payload p = tcp.getPayload();
                if (p != null) payload = p.getRawData();
                try { srcPort = tcp.getHeader().getSrcPort().valueAsInt(); } catch (Exception ignore) {}
                try { dstPort = tcp.getHeader().getDstPort().valueAsInt(); } catch (Exception ignore) {}
            } else if (udp != null) {
                UdpPacket.Payload p = udp.getPayload();
                if (p != null) payload = p.getRawData();
                try { srcPort = udp.getHeader().getSrcPort().valueAsInt(); } catch (Exception ignore) {}
                try { dstPort = udp.getHeader().getDstPort().valueAsInt(); } catch (Exception ignore) {}
            }
            if (payload == null || payload.length == 0) return;
            // Run signature engine
            List<MatchResult> matches = engine.match(payload);
            for (MatchResult m : matches) {
                Alert a = new Alert();
                a.setRuleId(m.rule.getId());
                a.setRuleName(m.rule.getName());
                a.setSrcIp(src.getHostAddress());
                a.setDstIp(dst.getHostAddress());
                a.setSrcPort(srcPort);
                a.setDstPort(dstPort);
                a.setTimestamp(Instant.now());
                a.setPayloadSnippet(m.snippet);
                // notify consumer (UI) and persist
                alertConsumer.accept(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (handle != null && handle.isOpen()) handle.breakLoop();
        } catch (NotOpenException e) { /* ignore */ }
        pool.shutdownNow();
    }
}
