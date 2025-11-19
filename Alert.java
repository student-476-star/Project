package com.netguard.ids.model;

import java.time.Instant;

public class Alert {
    private long id;
    private String ruleId;
    private String ruleName;
    private String srcIp;
    private int srcPort;
    private String dstIp;
    private int dstPort;
    private Instant timestamp;
    private String payloadSnippet;

    public Alert() { }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getSrcIp() { return srcIp; }
    public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
    public int getSrcPort() { return srcPort; }
    public void setSrcPort(int srcPort) { this.srcPort = srcPort; }
    public String getDstIp() { return dstIp; }
    public void setDstIp(String dstIp) { this.dstIp = dstIp; }
    public int getDstPort() { return dstPort; }
    public void setDstPort(int dstPort) { this.dstPort = dstPort; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getPayloadSnippet() { return payloadSnippet; }
    public void setPayloadSnippet(String payloadSnippet) { this.payloadSnippet = payloadSnippet; }
}
