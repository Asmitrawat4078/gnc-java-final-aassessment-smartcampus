package com.smartcampus.models;

import java.io.Serializable;

public class AcademicModule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int moduleCode;
    private String moduleTitle;
    private double tuitionFee;
    
    private int maxCapacity;
    private int currentEnrolled;

    public AcademicModule(int moduleCode, String moduleTitle, double tuitionFee, int maxCapacity) {
        this.moduleCode = moduleCode;
        this.moduleTitle = moduleTitle;
        this.tuitionFee = tuitionFee;
        this.maxCapacity = maxCapacity;
        this.currentEnrolled = 0;
    }

    public int getModuleCode() { return moduleCode; }
    public void setModuleCode(int moduleCode) { this.moduleCode = moduleCode; }

    public String getModuleTitle() { return moduleTitle; }
    public void setModuleTitle(String moduleTitle) { this.moduleTitle = moduleTitle; }

    public double getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(double tuitionFee) { this.tuitionFee = tuitionFee; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getCurrentEnrolled() { return currentEnrolled; }
    
    public synchronized boolean hasAvailableSeats() {
        return currentEnrolled < maxCapacity;
    }

    public synchronized void incrementEnrollment() {
        this.currentEnrolled++;
    }

    @Override
    public String toString() {
        return "AcademicModule [Code: " + moduleCode + ", Title: " + moduleTitle + 
               ", Fee: $" + tuitionFee + ", Capacity: " + currentEnrolled + "/" + maxCapacity + "]";
    }
}
/*
Ye file system me course ya module ko define karti hai. Isme humara unique feature 
(max capacity limit) add kiya gaya hai, jisse module full hone par extra students enroll nahi ho sakte. 
Capacity check karne wale methods synchronized use karte hain taaki ye thread-safe rahe.
*/
