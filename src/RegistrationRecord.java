import java.io.Serializable;
import java.util.ArrayList;

public class RegistrationRecord implements Serializable {
    
    private Scholar scholar;
    private ArrayList<AcademicModule> registeredModules;
    private boolean isProcessed;

    public RegistrationRecord(Scholar scholar) {
        this.scholar = scholar;
        this.registeredModules = new ArrayList<>();
        this.isProcessed = false;
    }

    public Scholar getScholar() { return scholar; }
    
    public ArrayList<AcademicModule> getRegisteredModules() { return registeredModules; }
    
    public void addModule(AcademicModule module) {
        this.registeredModules.add(module);
    }

    public boolean isProcessed() { return isProcessed; }
    public void setProcessed(boolean processed) { this.isProcessed = processed; }

    @Override
    public String toString() {
        String msg = "Registration -> Scholar: " + scholar.getFullName() + " | Modules: ";
        for (AcademicModule m : registeredModules) {
            msg += m.getModuleTitle() + ", ";
        }
        msg += " | Status: ";
        if(isProcessed) {
            msg += "Completed";
        } else {
            msg += "Pending";
        }
        return msg;
    }
}
/*
Ye file ek Scholar (student) ko unke selected AcademicModules (courses) ke sath link karti hai. 
Isme multiple modules store karne ke liye ArrayList ka use hua hai aur ek boolean flag hai 
jo background async thread ko process karne ka signal deta hai.
*/
