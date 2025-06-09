
# 🎓 Food Ordering System (Student Project)

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.4-brightgreen.svg)

## 📌 Overview

This is a **student-made backend project** that simulates a simple **Food Ordering System** using Java and Spring Boot with a microservices architecture. The goal of this project is to learn how to structure a multi-service backend, handle basic CRUD operations, and communicate between services using Kafka.

> 📚 Made as part of a learning journey in software engineering and backend development.

---

## ✨ Key Features

* Order food from available restaurants
* Track order status
* Process payments (mocked)
* Restaurants can approve or reject orders
* Customer info is stored and retrievable

---

## 🧱 Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* Apache Kafka
* PostgreSQL
* Docker & Docker Compose
* Maven

---

## 🧭 Project Modules

Each microservice follows a layered structure:

* `domain`: core business logic
* `application`: service logic
* `dataaccess`: JPA repositories
* `messaging`: Kafka interaction
* `container`: Spring Boot main app

Microservices included:

* **Order Service**
* **Payment Service**
* **Restaurant Service**
* **Customer Service**

---

## 🚀 Getting Started

### Prerequisites

* Java 17
* Maven
* Docker & Docker Compose

### Run the App

> This project is meant for educational purposes. For full deployment, each service must be built and run individually.

1. Clone the repository:

   ```bash
   git clone https://github.com/Assia-Aissa/Order_Food_Microservice_app.git
   cd Order_Food_Microservice_app
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Start required services using Docker:

   ```bash
   docker-compose up
   ```

4. Run each microservice manually from its respective directory:

   ```bash
   java -jar target/your-service-name.jar
   ```

---


---

## 📚 What I Learned

* Basics of microservices architecture
* How to use Kafka for service communication
* Creating layered Spring Boot applications
* Managing multiple modules in Maven
* Dockerizing backend apps

