# IV1350 – Object-Oriented Design (KTH)

This repository contains all project work, lab implementations, and additional assignments completed for the course **IV1350: Object-Oriented Design** at **KTH Royal Institute of Technology**.  
The course focuses on designing well-structured, maintainable object-oriented systems using Java and UML.

---

## 📚 Course Overview

**Course:** IV1350 Object-Oriented Design  
**Language:** Java  
**Institution:** KTH Royal Institute of Technology  
**Main Topics:**
- Object-oriented analysis and design
- UML modelling (class and sequence diagrams)
- Design principles: coupling, cohesion, encapsulation
- Layered architecture (Model–View–Controller)
- Design patterns
- Refactoring and testing
- Exception handling and error management

---

## 🧩 System Design Summary

The main project (from Task3 onward) implements a **Point-of-Sale (POS) system**, structured according to layered architecture principles:

### Architecture
- **View layer (`view/`)**  
  Handles user interaction and observer-based feedback.
- **Controller layer (`controller/`)**  
  Acts as the intermediary between view and model. Coordinates sale operations.
- **Model layer (`model/`)**  
  Contains domain logic — classes for `Sale`, `Item`, `Receipt`, `DiscountStrategy`, etc.
- **Integration layer (`integration/`)**  
  Simulates connections to external systems such as inventory, accounting, and printing.
- **DTO layer (`dto/`)**  
  Provides immutable objects for safely transferring data between layers.
- **Utils layer (`utils/`)**  
  Implements logging and revenue observer functionality.
- **Exceptions layer (`exceptions/`)**  
  Contains all custom exceptions for error handling.

---

## 🧠 Key Concepts Demonstrated

- **High Cohesion & Low Coupling:**  
  Each class is focused on a single responsibility, and dependencies between components are minimized.

- **Encapsulation:**  
  All state and behavior are hidden behind public methods and DTOs.

- **Observer Pattern:**  
  Used to track total revenue through file and console observers (`TotalRevenueFileOutput`, `TotalRevenueView`).

- **Strategy Pattern:**  
  Implemented through various discount calculation classes such as:
  - `CustomerDiscountStrategy`
  - `ItemBasedDiscountStrategy`
  - `CompositeDiscountStrategy`
  - `TotalBasedDiscountStrategy`

- **Exception Handling:**  
  Demonstrates robust error control using `DatabaseFailureException` and `ItemNotFoundException`.

- **Unit Testing:**  
  Comprehensive JUnit test suites validate each major class (model, integration, view, etc.).

- **Inheritance vs Composition:**  
  Explored under `inheritanceAndComposition/` to highlight design trade-offs.
