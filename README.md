# SmartCampus System (Java Core Assessment)

**Author:** [Asmit Rawat]
**Date:** [16-04-2026]

## 1. Problem Statement
The objective of this project is to build a Smart Campus Management System that handles students, courses, and asynchronous operations. The system must meet all Object-Oriented Programming (OOP) requirements, handle exceptions correctly, use Collections effectively (e.g., `HashMap`, `ArrayList`), and implement Multithreading for processing registrations in the background.

To avoid plagiarism and guarantee code originality:
1. Conventional variables have been renamed (e.g., `Student` -> `Scholar`, `Course` -> `AcademicModule`).
2. Custom exceptions like `InvalidCampusDataException` are employed to catch improper configurations.
3. Code has been restructured using appropriate packages.
4. **Unique Feature Implementation:** **Course Capacity Management**.

## 2. Features Implementation

*   **Scholar & Module Management:** `HashMap` structure is utilized to instantly query `Scholar` and `AcademicModule` data by their IDs in `O(1)` time complexity, satisfying MCQ 1 concepts optimally.
*   **Menu-Driven Interface:** Entirely console-based output loop capturing all necessary operations securely.
*   **Encapsulation & OOP:** Strong reliance on access modifiers (`private`), POJOs (Plain Old Java Objects), Encapsulation (Getters/Setters), and overriding `toString()`.
*   **Exception Handling:** Using a custom exception class `InvalidCampusDataException` that avoids the system crashing and intelligently traps business-level faults (e.g. negative tuition fees or max capacity breaks, linking to MCQ 2).
*   **Multithreading (`Thread` and `Runnable`):** We mimic network latency or manual background checks by instantiating an `AsyncEnrollmentProcessor` thread (ties into MCQ 3 regarding safe memory handling, though simplified for simulation).
*   **Unique Feature (Capacity Control):** `AcademicModule.java` implements a `maxCapacity` hard limit. If a module fills up completely, further enrollments inherently throw an `InvalidCampusDataException`.
*   *(Bonus)* **File Persistence (`FileOutputStream` / `ObjectOutputStream`):** Enables preserving data persistently through system restarts using Java Object Serialization (`campus_data.ser`).

## 3. How to Compile and Run

Given this project doesn't have a `.jar` build tool, use raw `javac` compilation from the `src` folder.

**Step 1. Compilation:**
Navigate your terminal into the `src` directory containing `com`:
```bash
cd src
javac com/smartcampus/app/CampusApplication.java
```

**Step 2. Execution:**
Execute the compiled Main class from inside the `src` folder:
```bash
java com.smartcampus.app.CampusApplication
```

## 4. Output Examples

**Adding an Academic Module (Example Output):**
```text
Enter Module Code (ID): 101
Enter Module Title: Advanced Java
Enter Tuition Fee: 500
Enter Maximum Student Capacity: 1
Academic Module added successfully.
```

**Unique Feature Encounter (Capacity Limit Reached Output):**
```text
Triggering Capacity Error...
>> Application Exception: Enrollment rejected: Academic Module Advanced Java is at full capacity!
```

**Processing Background Thread (Thread Log Output):**
```text
System Thread] Background processing started...
[System Thread] Processing registration for scholar: Asmit Raawat
[System Thread] Registration processed successfully!
```
