Saga Pattern with Spring Boot and Kafka
This repository demonstrates a robust microservices architecture using the Saga pattern, Spring Boot, and Apache Kafka. It includes compensation logic, timeout handling, and a shared event module for reliable distributed transactions.

Architecture Overview
Order Service: Accepts and validates orders, initiates the saga, and handles compensation if needed.
Payment Service: Processes payments, publishes success or failure events.
Notification Service: Notifies users of order/payment status.
Shared Events Module: Contains event classes used by all services.
Key Features
Event-driven communication via Kafka
Compensation logic for failed or timed-out steps
Timeout handling for missing acknowledgments
Centralized, reusable event definitions
Clean, production-ready code with logging and error handling
Getting Started
Clone the repository:
git clone https://github.com/jaskaran14/saga-pattern-springboot-kafka.git
cd saga-pattern-springboot-kafka
Start dependencies:
Use the provided docker-compose.yml to start Kafka, Zookeeper, and PostgreSQL.
docker-compose up -d
Build and run each service:
Navigate to each service directory and run:
mvn spring-boot:run
References
Saga Pattern - Microservices.io
Spring Boot Kafka Documentation
License
MIT
