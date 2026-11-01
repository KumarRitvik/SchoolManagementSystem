# School Management System

A **Microservices-Based School Management System** built using Java and Spring Boot, designed to simplify and manage different academic and administrative operations within a school.

The project follows a modular microservices architecture, where individual services handle specific business responsibilities and communicate through synchronous and asynchronous communication mechanisms.

This project is an evolving software engineering initiative with significant scope for expansion, optimization, and integration of additional real-world features.

## Overview

The School Management System aims to bring multiple school operations into a unified application, including student management, teacher management, academic administration, assignments, enrolments, notifications, and user authentication.

Rather than building everything into a single monolithic application, the system is divided into independently structured services. This makes it easier to maintain, extend, test, and evolve individual components as the application's requirements grow.

## Architecture and Microservices

The current implementation consists of the following services:

| Service | Responsibility |
|---|---|
| API Gateway | Central entry point for client requests and request routing |
| Eureka Server | Service discovery and registration |
| User Service | User management and authentication |
| Student Service | Student records and enrolment management |
| Teacher Service | Teacher records and assignment management |
| Academic Service | Classroom and subject management |
| SMS Service | SMS notifications and event consumption |
| Calendar Service | Google Calendar integration and holiday event publishing |

## Technology Stack

**Backend**
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Spring Cloud
- Spring Security
- JWT Authentication

**Microservices and Communication**
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- OpenFeign
- RestTemplate
- Apache Kafka
- Kafka Producers and Consumers
- Retry Mechanism and Dead Letter Topics

**Database and Persistence**
- MySQL
- Hibernate ORM

**Frontend**
- Thymeleaf
- HTML
- CSS

**Integrations**
- Google Calendar API
- Twilio SMS API
- Email notifications

**Build and Development**
- Maven
- IntelliJ IDEA
- Git and GitHub

## Key Features

- Student and teacher management.
- Classroom and subject administration.
- Student enrolment management.
- Teacher assignment management.
- RESTful API development.
- DTO-based data transfer.
- Centralized exception handling.
- File and image upload support.
- Service discovery through Eureka.
- API Gateway-based request routing.
- Inter-service communication using Feign and RestTemplate.
- Asynchronous event-driven communication using Apache Kafka.
- Multiple Kafka consumer groups.
- Retry handling and Dead Letter Topics.
- Event processing and duplicate-event checks.
- Email notifications.
- SMS notifications using Twilio.
- Google Calendar integration for holiday-related events.
- JWT-based authentication and authorization components.

## Event-Driven Architecture

Apache Kafka is used to support asynchronous communication between services.

For example:

1. Student Service publishes a student creation event.
2. Independent Kafka consumer groups receive the event.
3. Notification-related consumers can process the event and trigger corresponding actions.
4. Retry mechanisms and Dead Letter Topics provide a mechanism for handling unsuccessful event processing.

This architecture allows additional consumers and independent event-processing capabilities to be introduced as the system evolves.

## Scope for Future Evolution

This project is not intended to remain limited to its current implementation. It provides a foundation that can be expanded into a much broader school management platform.

Potential areas of future development include:

- **Examination Management:** Exam scheduling, marks entry, grading, report cards and academic performance analysis.
- **Attendance Management:** Student and teacher attendance tracking, absence notifications and attendance reports.
- **Fee Management:** Fee structures, payment tracking, outstanding dues, receipts and payment gateway integration.
- **Timetable Management:** Automated timetable generation, classroom allocation and teacher scheduling.
- **Parent Portal:** Parent access to student performance, attendance, assignments and notifications.
- **Learning Management:** Study materials, online assignments, assessments and digital learning resources.
- **Analytics and Reporting:** Academic dashboards, attendance statistics, fee collection reports and institutional analytics.
- **Cloud Deployment:** Containerization with Docker, orchestration with Kubernetes and deployment on cloud infrastructure.
- **Observability:** Centralized logging, distributed tracing, metrics collection and monitoring using tools such as Prometheus and Grafana.
- **Resilience and Scalability:** Circuit breakers, fault tolerance, service-level monitoring and improved distributed-system reliability.

These are potential directions for future development, not claims that all these capabilities are already implemented.

## Project Philosophy

The primary objective is to build a practical, continuously evolving backend system while exploring real-world software engineering concepts.

The project focuses on:

- Modular and maintainable architecture.
- Separation of business responsibilities.
- Service-to-service communication.
- Event-driven system design.
- Security and authentication.
- Fault handling and reliability.
- Extensibility and scalability.

## Current Development Status

The core microservices and their integrations have been developed and published in this repository.

The project remains open to further enhancements, architectural improvements, testing, optimization, and new feature development.

**The long-term vision is to evolve this application from a collection of school administration services into a comprehensive, scalable and extensible school management platform.**

---

*Built with Java, Spring Boot, Microservices and a continuous-learning mindset.*
