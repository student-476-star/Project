package com.netguard.ids.model;

public class Rule {
    private String id;
    private String name;
    private boolean enabled;
    private String severity;
    private String proto;
    private String match_location;
    private String pattern;
    private String pattern_type;
    private boolean case_sensitive;
    private String description;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getProto() { return proto; }
    public void setProto(String proto) { this.proto = proto; }
    public String getMatch_location() { return match_location; }
    public void setMatch_location(String match_location) { this.match_location = match_location; }
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
    public String getPattern_type() { return pattern_type; }
    public void setPattern_type(String pattern_type) { this.pattern_type = pattern_type; }
    public boolean isCase_sensitive() { return case_sensitive; }
    public void setCase_sensitive(boolean case_sensitive) { this.case_sensitive = case_sensitive; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
