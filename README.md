<p align="center">
  <img src="assets/logo.png" alt="Project logo" width="180">
</p>

# ColoCat - Roommate Management Platform

## Overview

ColoCat is a comprehensive Java-based application designed to simplify and streamline the management of shared accommodations. The platform provides an integrated solution for handling authentication, expense tracking, event scheduling, incident reporting, and common space reservations.

## Key Features

- **Authentication & Access Control**: Secure user authentication and roommate management
- **Expense Management**: Automated expense tracking with balance calculations and statistical analysis
- **Event Planning**: Scheduling and coordination of communal activities (meals, social events, household tasks)
- **Incident Tracking**: Report and monitor housing-related incidents with follow-up capabilities
- **Space Reservation**: Book common areas with automated conflict detection

## Technical Architecture

### Design Principles
- **Object-Oriented Design**: Clean separation of concerns with modular package structure
- **Architectural Patterns**: MVC architecture with implementation of Singleton, Service Layer, and DTO patterns
- **Data Persistence**: Java serialization for robust data management
- **User Interface**: Swing-based GUI providing an intuitive and responsive user experience

### Package Organization
- `login`: Authentication and access control
- `events`: Event scheduling and management
- `incidents`: Incident reporting and tracking
- `reservations`: Space reservation and conflict resolution
- `expenses`: Expense tracking and financial management

### Technology Stack
- **Language**: Java
- **GUI Framework**: Swing
- **Data Management**: Java Serialization (colocation.ser)
- **Version Control**: Git, GitHub, GitLab
- **Modeling**: UML

## Development Methodology

The project was developed following agile principles with emphasis on:
- Iterative development cycles (Scrum-inspired approach)
- Collaborative team management using Trello for task tracking
- Version control best practices with Git workflows
- Regular sprint reviews and retrospectives

### Collaboration Tools
- Trello: Project management and sprint planning
- GitLab/GitHub: Code repository and version control
- Google Meet: Remote team synchronization
- WhatsApp: Team communication

## Quality Assurance

- Functional testing of core features
- User interface testing and usability validation
- Integration testing across modules
- Data persistence verification

## Installation & Usage

### Requirements
- Java 8 or higher
- IDE with Swing support (IntelliJ IDEA, Eclipse, or NetBeans)

### Getting Started

```bash
# Clone the repository
git clone https://github.com/Ykuzaa/colocat.git
cd colocat

# Compile the project
javac -d bin src/**/*.java

# Run the application
java -cp bin MainApplication
```

### Data Storage
Application data is persisted in `colocation.ser` using Java serialization. This file is automatically created on first launch.

## Roadmap

- Enhanced reporting and analytics
- Export functionality (PDF, CSV)
- Mobile companion application
- Cloud synchronization capabilities
- Advanced notification system


## License

This project is provided as-is for educational and collaborative use.

## Contact

For inquiries or collaboration opportunities, please reach out through the repository issues section or contact the development team directly.

