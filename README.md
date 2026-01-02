# NetGuard-IDS --- Complete Documentation

NetGuard-IDS is a lightweight Java-based Intrusion Detection System (IDS) featuring real-time packet capture, signature-based detection,
alert logging, an interactive Swing GUI dashboard, and persistent H2
database storage.

------------------------------------------------------------------------

## 1. Introduction

Intrusion Detection Systems (IDS) are critical components of modern network security infrastructures. They continuously monitor network or system activity to identify malicious behavior, policy violations, or abnormal usage patterns. Traditional IDS solutions such as Snort and Suricata rely primarily on signature-based detection, while modern research increasingly focuses on hybrid approaches that combine signatures with behavioral or anomaly-based analysis.

NetGuard-IDS is designed as an educational and research-oriented IDS framework that demonstrates core IDS concepts in a clean and understandable Java implementation. Rather than focusing solely on packet capture, NetGuard-IDS emphasizes the end-to-end detection pipeline:

traffic ingestion (live or simulated),

rule-based inspection,

traffic profiling,

alert generation,

visualization, and

reporting.

The project structure mirrors architectures described in academic IDS literature, where separation of concerns is a key principle. Detection logic, traffic handling, alert management, and user interaction are implemented as independent modules, allowing future extensions such as machine-learning-based detection, protocol-aware inspection, or distributed deployment.

NetGuard-IDS therefore serves both as:

a functional IDS prototype, and

a research platform for experimenting with detection techniques discussed in contemporary IDS publications.

## 2. Objective

Monitor incoming network or simulated traffic

Detect malicious or suspicious behavior using rules

Identify abnormal traffic using profile-based thresholds

Log and persist detected alerts

Provide authenticated access to the system

Display alerts in real time using a GUI

Export detection results for offline analysis and reporting

## 3. Key Features

✔ Modular IDS architecture

✔ Rule-based detection engine

✔ Traffic profiling & anomaly thresholds

✔ Multi-threaded worker processing

✔ Swing-based monitoring dashboard

✔ User authentication & settings management

✔ Alert logging and report exporting

## 4. Architecture Overview

NetGuard-IDS follows a layered IDS architecture, commonly described in IDS research, where each layer has a distinct responsibility.

### 4.1 High-Level Architecture
┌─────────────────────────────────────────────┐
│                 Presentation Layer          │
│                                             │
│  IDSDashboard | IDSLogin | Settings Dialog  │
│  (Visualization & Control)                  │
└──────────────────────▲──────────────────────┘
                       │
┌──────────────────────┴──────────────────────┐
│               Alert & Reporting Layer        │
│                                             │
│  IDSLogger | IDSReportExporter               │
│  (Alert storage, export, reporting)          │
└──────────────────────▲──────────────────────┘
                       │
┌──────────────────────┴──────────────────────┐
│              Detection & Analysis Layer      │
│                                             │
│  IDSRuleEngine | TrafficProfile              │
│  (Signature + profile-based detection)       │
└──────────────────────▲──────────────────────┘
                       │
┌──────────────────────┴──────────────────────┐
│              Processing & Control Layer      │
│                                             │
│  IDSServer | IDSWorker                       │
│  (Task dispatching, multithreading)          │
└──────────────────────▲──────────────────────┘
                       │
┌──────────────────────┴──────────────────────┐
│               Traffic Source Layer           │
│                                             │
│  AttackSimulator | Live Capture (future)    │
│  (Traffic input generation)                  │
└─────────────────────────────────────────────┘

### 4.2 Architectural Rationale

Layered separation improves maintainability and extensibility

Detection logic is isolated from UI and traffic ingestion

Workers enable scalability, reflecting real IDS engines

Simulation support allows controlled testing of rules

Architecture aligns with hybrid IDS models described in IEEE literature

## 5. Project Modules
### 5.1 IDSServer

Central dispatcher of traffic events

Accepts traffic from simulators or capture modules

Assigns tasks to worker threads

Acts as the system’s control backbone

### 5.2 IDSWorker

Runs in parallel threads

Processes individual packets or traffic sessions

Invokes rule engine and traffic profile checks

Improves throughput and responsiveness

### 5.3 IDSRuleEngine

Implements signature-based detection

Matches patterns, keywords, or indicators

Core component for known attack detection

Easily extendable to regex or structured rules

### 5.4 TrafficProfile

Maintains baseline behavior statistics

Detects deviations from normal traffic patterns

Represents anomaly-based detection concepts

Supports threshold-based alerts

### 5.5 AttackSimulator

Generates synthetic attack traffic

Enables repeatable testing of detection logic

Useful for academic demonstrations and evaluations

### 5.6 IDSLogger

Centralized alert logging

Records timestamps, rule matches, sources, and descriptions

Supports file-based or future database storage

### 5.7 IDSDashboard

Swing-based graphical interface

Displays alerts in real time

Shows system status and logs

Entry point for analysts and users

### 5.8 IDSLogin & IDSSettingsDialog

User authentication mechanism

Restricts system access

Allows runtime configuration updates

### 5.9 IDSReportExporter

Exports alerts to CSV or text formats

Enables offline analysis and reporting

Suitable for academic submissions and audits

## 6. Main Program Flow (IDSDashboard.java)

User authenticates via IDSLogin

Configuration is loaded using IDSConfig

IDSServer initializes worker threads

Traffic source (simulator or capture) starts

Workers analyze traffic via IDSRuleEngine

TrafficProfile checks anomalies

Alerts generated and logged

Dashboard updates in real time

Reports exported on demand

## 7. Rules & Configuration Format

Example conceptual rule:

{
  "id": 101,
  "name": "Brute Force Attempt",
  "pattern": "failed_login",
  "threshold": 10,
  "protocol": "TCP"
}


pattern: signature indicator

threshold: anomaly trigger value

protocol: protocol filter

## 8. Logging & Persistence

Alerts stored in log files (e.g., ids_log.txt)

Each entry includes:

Timestamp

Rule name

Source / destination

Description

Design supports future database integration

## 9. GUI Documentation
Dashboard Features

Alert table with sorting

Real-time updates

Authentication screen

Settings configuration dialog

Export controls

## 10. Detection Workflows
Signature-Based Detection

Extract traffic features

Match against rules

Trigger alert on match

Profile-Based Detection

Build baseline traffic profile

Monitor deviations

Trigger anomaly alert

## 11. Research Alignment

NetGuard-IDS reflects IDS research principles:

Hybrid detection (signature + anomaly)

Modular, layered design

Separation of analysis and visualization

Support for experimentation and extension

## 12. Future Enhancements

Machine-learning classifiers

Live packet capture integration

Distributed IDS nodes

Snort/YARA rule compatibility

Real-time traffic graphs

REST API access