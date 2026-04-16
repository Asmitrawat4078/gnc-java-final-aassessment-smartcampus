package com.smartcampus.services;

import com.smartcampus.models.RegistrationRecord;

import java.util.Iterator;
import java.util.List;

public class AsyncEnrollmentProcessor implements Runnable {
    private final List<RegistrationRecord> pendingRegistrations;

    public AsyncEnrollmentProcessor(List<RegistrationRecord> pendingRegistrations) {
        this.pendingRegistrations = pendingRegistrations;
    }

    @Override
    public void run() {
        System.out.println("[System Thread] Background processing started...");
        try {
            while(true) {
                boolean hasWorked = false;
                
                synchronized (pendingRegistrations) {
                    Iterator<RegistrationRecord> it = pendingRegistrations.iterator();
                    while (it.hasNext()) {
                        RegistrationRecord record = it.next();
                        if (!record.isProcessed()) {
                            System.out.println("[System Thread] Processing registration for scholar: " + record.getScholar().getFullName());
                            Thread.sleep(2000); 
                            record.setProcessed(true);
                            System.out.println("[System Thread] Registration processed successfully!");
                            it.remove();
                            hasWorked = true;
                        }
                    }
                }
                
                if (!hasWorked) {
                    System.out.println("[System Thread] No pending registrations. Exiting processing mode.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.err.println("[System Thread] Processing interrupted: " + e.getMessage());
        }
    }
}
/*
Ye class Runnable interface ke through Multithreading implement karti hai. Iska kaam pending 
registrations ko background me asynchronously process karna hai. Network delay ko simulate karne ke liye 
Thread.sleep use hua hai, aur data ko safe rakhne ke liye synchronized block lagaya gaya hai.
*/
