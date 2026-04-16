package com.smartcampus.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegistrationRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Scholar scholar;
    private List<AcademicModule> registeredModules;
    private boolean isProcessed;

    public RegistrationRecord(Scholar scholar) {
        this.scholar = scholar;
        this.registeredModules = new ArrayList<>();
        this.isProcessed = false;
    }

    public Scholar getScholar() { return scholar; }
    
    public List<AcademicModule> getRegisteredModules() { return registeredModules; }
    
    public void addModule(AcademicModule module) {
        this.registeredModules.add(module);
    }

    public boolean isProcessed() { return isProcessed; }
    public void setProcessed(boolean processed) { isProcessed = processed; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registration -> Scholar: ").append(scholar.getFullName());
        sb.append(" | Modules: ");
        for (AcademicModule m : registeredModules) {
            sb.append(m.getModuleTitle()).append(" ");
        }
        sb.append("| Status: ").append(isProcessed ? "Completed" : "Pending");
        return sb.toString();
    }
}
/*
Ye file ek Scholar (student) ko unke selected AcademicModules (courses) ke sath link karti hai. 
Isme multiple modules store karne ke liye ArrayList ka use hua hai aur ek boolean flag hai 
jo background async thread ko process karne ka signal deta hai.
*/
