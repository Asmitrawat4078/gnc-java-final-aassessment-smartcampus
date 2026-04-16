package com.smartcampus.services;

import com.smartcampus.exceptions.InvalidCampusDataException;
import com.smartcampus.models.AcademicModule;
import com.smartcampus.models.RegistrationRecord;
import com.smartcampus.models.Scholar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampusManager {
    private HashMap<Integer, Scholar> scholarDirectory;
    private HashMap<Integer, AcademicModule> moduleCatalog;
    private List<RegistrationRecord> pendingRegistrations;
    private List<RegistrationRecord> allRegistrations;

    public CampusManager() {
        this.scholarDirectory = new HashMap<>();
        this.moduleCatalog = new HashMap<>();
        this.pendingRegistrations = new ArrayList<>();
        this.allRegistrations = new ArrayList<>();
    }

    public synchronized void addScholar(int id, String name, String email) throws InvalidCampusDataException {
        if (id <= 0) throw new InvalidCampusDataException("Scholar ID must be positive.");
        if (name == null || name.trim().isEmpty()) throw new InvalidCampusDataException("Scholar name cannot be empty.");
        if (!email.contains("@")) throw new InvalidCampusDataException("Invalid email format.");
        if (scholarDirectory.containsKey(id)) throw new InvalidCampusDataException("Scholar ID already exists.");

        scholarDirectory.put(id, new Scholar(id, name, email));
        System.out.println("Scholar added successfully.");
    }

    public synchronized void addModule(int code, String title, double fee, int capacity) throws InvalidCampusDataException {
        if (code <= 0) throw new InvalidCampusDataException("Module code must be positive.");
        if (fee < 0) throw new InvalidCampusDataException("Tuition fee cannot be negative.");
        if (capacity <= 0) throw new InvalidCampusDataException("Capacity must be greater than zero.");
        if (moduleCatalog.containsKey(code)) throw new InvalidCampusDataException("Module code already exists.");

        moduleCatalog.put(code, new AcademicModule(code, title, fee, capacity));
        System.out.println("Academic Module added successfully.");
    }

    public synchronized void enrollScholar(int scholarId, int[] moduleCodes) throws InvalidCampusDataException {
        if (!scholarDirectory.containsKey(scholarId)) {
            throw new InvalidCampusDataException("Scholar not found.");
        }

        Scholar scholar = scholarDirectory.get(scholarId);
        RegistrationRecord record = new RegistrationRecord(scholar);

        for (int code : moduleCodes) {
            if (!moduleCatalog.containsKey(code)) {
                throw new InvalidCampusDataException("Module code " + code + " not found.");
            }
            AcademicModule module = moduleCatalog.get(code);

            if (!module.hasAvailableSeats()) {
                throw new InvalidCampusDataException("Enrollment rejected: Academic Module " + module.getModuleTitle() + " is at full capacity!");
            }
            module.incrementEnrollment();
            record.addModule(module);
        }

        if (record.getRegisteredModules().isEmpty()) {
            throw new InvalidCampusDataException("No valid modules selected for registration.");
        }

        pendingRegistrations.add(record);
        allRegistrations.add(record);
        System.out.println("Enrollment submitted and queued for background processing.");
    }

    public void displayAllScholars() {
        if (scholarDirectory.isEmpty()) {
            System.out.println("No scholars found.");
            return;
        }
        for (Scholar s : scholarDirectory.values()) {
            System.out.println(s);
        }
    }

    public void displayAllModules() {
        if (moduleCatalog.isEmpty()) {
            System.out.println("No academic modules found.");
            return;
        }
        for (AcademicModule m : moduleCatalog.values()) {
            System.out.println(m);
        }
    }

    public void displayRegistrations() {
        if (allRegistrations.isEmpty()) {
            System.out.println("No registrations found.");
            return;
        }
        for (RegistrationRecord r : allRegistrations) {
            System.out.println(r);
        }
    }

    public List<RegistrationRecord> getPendingRegistrations() {
        return pendingRegistrations;
    }

    public HashMap<Integer, Scholar> getScholarDirectory() { return scholarDirectory; }
    public HashMap<Integer, AcademicModule> getModuleCatalog() { return moduleCatalog; }
    public List<RegistrationRecord> getAllRegistrations() { return allRegistrations; }

    public void setScholarDirectory(HashMap<Integer, Scholar> scholarDirectory) { this.scholarDirectory = scholarDirectory; }
    public void setModuleCatalog(HashMap<Integer, AcademicModule> moduleCatalog) { this.moduleCatalog = moduleCatalog; }
    public void setAllRegistrations(List<RegistrationRecord> allRegistrations) { this.allRegistrations = allRegistrations; }
}
/*
Ye file Smart Campus system ka main logic handle karti hai. Isme scholars aur modules 
ko fast access (O(1)) karne ke liye Java HashMaps ka use hua hai. 
Galt user input ko rokne ke liye isme custom exception (InvalidCampusDataException) ka bohot use kiya gaya hai.
*/
