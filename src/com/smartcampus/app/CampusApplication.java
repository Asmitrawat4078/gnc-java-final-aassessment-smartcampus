package com.smartcampus.app;

import com.smartcampus.exceptions.InvalidCampusDataException;
import com.smartcampus.services.AsyncEnrollmentProcessor;
import com.smartcampus.services.CampusManager;
import com.smartcampus.services.DataStorageService;

import java.util.Scanner;

public class CampusApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CampusManager manager = new CampusManager();

        System.out.println("Initializing SmartCampus System...");
        Object loadedData = DataStorageService.loadData();
        if (loadedData != null && loadedData instanceof Object[]) {
            Object[] dataArr = (Object[]) loadedData;
            try {
                @SuppressWarnings("unchecked")
                java.util.HashMap<Integer, com.smartcampus.models.Scholar> sMap = 
                    (java.util.HashMap<Integer, com.smartcampus.models.Scholar>) dataArr[0];
                
                @SuppressWarnings("unchecked")
                java.util.HashMap<Integer, com.smartcampus.models.AcademicModule> mMap = 
                    (java.util.HashMap<Integer, com.smartcampus.models.AcademicModule>) dataArr[1];
                
                @SuppressWarnings("unchecked")
                java.util.List<com.smartcampus.models.RegistrationRecord> rList = 
                    (java.util.List<com.smartcampus.models.RegistrationRecord>) dataArr[2];
                
                manager.setScholarDirectory(sMap);
                manager.setModuleCatalog(mMap);
                manager.setAllRegistrations(rList);
            } catch (ClassCastException e) {
                System.out.println("Warning: Could not cast loaded data correctly.");
            }
        }

        while (true) {
            System.out.println("\n=============================================");
            System.out.println("      SMART CAMPUS MANAGEMENT SYSTEM      ");
            System.out.println("=============================================");
            System.out.println("1. Register New Scholar (Student)");
            System.out.println("2. Create Academic Module (Course)");
            System.out.println("3. Enroll Scholar in Module");
            System.out.println("4. View All Scholars");
            System.out.println("5. View All Academic Modules");
            System.out.println("6. View All Registrations");
            System.out.println("7. Process Pending Registrations (Async Thread)");
            System.out.println("8. Save and Exit");
            System.out.println("=============================================");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid numerical option.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Scholar ID: ");
                        int scholarId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Full Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Contact Email: ");
                        String email = scanner.nextLine();
                        manager.addScholar(scholarId, name, email);
                        break;
                    case 2:
                        System.out.print("Enter Module Code (ID): ");
                        int modCode = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Module Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Tuition Fee: ");
                        double fee = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter Maximum Student Capacity: ");
                        int capacity = Integer.parseInt(scanner.nextLine());
                        manager.addModule(modCode, title, fee, capacity);
                        break;
                    case 3:
                        System.out.print("Enter Scholar ID for Enrollment: ");
                        int sId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter comma-separated Module Codes (e.g. 101,102): ");
                        String codesInput = scanner.nextLine();
                        String[] codesStr = codesInput.split(",");
                        int[] moduleCodes = new int[codesStr.length];
                        for (int i = 0; i < codesStr.length; i++) {
                            moduleCodes[i] = Integer.parseInt(codesStr[i].trim());
                        }
                        manager.enrollScholar(sId, moduleCodes);
                        break;
                    case 4:
                        System.out.println("\n--- SCHOLAR DIRECTORY ---");
                        manager.displayAllScholars();
                        break;
                    case 5:
                        System.out.println("\n--- ACADEMIC MODULES ---");
                        manager.displayAllModules();
                        break;
                    case 6:
                        System.out.println("\n--- REGISTRATIONS ---");
                        manager.displayRegistrations();
                        break;
                    case 7:
                        System.out.println("Triggering background processing thread...");
                        Thread processingThread = new Thread(new AsyncEnrollmentProcessor(manager.getPendingRegistrations()));
                        processingThread.start();
                        Thread.sleep(100); 
                        break;
                    case 8:
                        System.out.println("Saving data to file...");
                        Object[] dataToSave = new Object[] {
                            manager.getScholarDirectory(),
                            manager.getModuleCatalog(),
                            manager.getAllRegistrations()
                        };
                        DataStorageService.saveData(dataToSave);
                        System.out.println("Exiting System. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InvalidCampusDataException e) {
                System.out.println(">> Application Exception: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(">> Input Error: Please enter numerical values where expected.");
            } catch (Exception e) {
                System.out.println(">> Unexpected Error: " + e.getMessage());
            }
        }
    }
}
/*
Ye file system ka main entry point hai jo Menu-Driven program ka requirement fulfill karti hai. 
Ye Scanner ka use karke console me user input leti hai ek while(true) loop ke andar. 
Program crash hone se bachane ke liye isme NumberFormatException ko achhe se handle kiya gaya hai.
*/
