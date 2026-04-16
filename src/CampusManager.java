import java.util.ArrayList;
import java.util.HashMap;

public class CampusManager {
    private HashMap<Integer, Scholar> scholarDirectory;
    private HashMap<Integer, AcademicModule> moduleCatalog;
    private ArrayList<RegistrationRecord> pendingRegistrations;
    private ArrayList<RegistrationRecord> allRegistrations;

    public CampusManager() {
        this.scholarDirectory = new HashMap<>();
        this.moduleCatalog = new HashMap<>();
        this.pendingRegistrations = new ArrayList<>();
        this.allRegistrations = new ArrayList<>();
    }

    public synchronized void addScholar(int id, String name, String email) throws InvalidCampusDataException {
        if (id <= 0) {
            throw new InvalidCampusDataException("Scholar ID hamesha positive hona chahiye.");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidCampusDataException("Name khali nahi ho sakta.");
        }
        if (!email.contains("@")) {
            throw new InvalidCampusDataException("Email galat hai, @ missing.");
        }
        if (scholarDirectory.containsKey(id)) {
            throw new InvalidCampusDataException("Yeh ID pehle se exist karti hai.");
        }

        Scholar newScholar = new Scholar(id, name, email);
        scholarDirectory.put(id, newScholar);
        System.out.println("Scholar added successfully.");
    }

    public synchronized void addModule(int code, String title, double fee, int capacity) throws InvalidCampusDataException {
        if (code <= 0) throw new InvalidCampusDataException("Module code positive hona chahiye.");
        if (title == null || title.trim().isEmpty()) throw new InvalidCampusDataException("Module title khali nahi ho sakta.");
        if (fee < 0) throw new InvalidCampusDataException("Fee negative nahi ho sakti.");
        if (capacity <= 0) throw new InvalidCampusDataException("Capacity zero ya negative nahi ho sakti.");
        if (moduleCatalog.containsKey(code)) throw new InvalidCampusDataException("Yeh Module code pehle se hai.");

        AcademicModule newModule = new AcademicModule(code, title, fee, capacity);
        moduleCatalog.put(code, newModule);
        System.out.println("Academic Module added successfully.");
    }

    public synchronized void enrollScholar(int scholarId, int[] moduleCodes) throws InvalidCampusDataException {
        if (!scholarDirectory.containsKey(scholarId)) {
            throw new InvalidCampusDataException("Scholar ID nahi mila.");
        }

        Scholar scholar = scholarDirectory.get(scholarId);
        
        // 1st Pass validation!
        // Sab kuch pehle validate karenge ki koi data galat na ho.
        ArrayList<AcademicModule> validModules = new ArrayList<>();
        for (int i = 0; i < moduleCodes.length; i++) {
            int code = moduleCodes[i];
            if (!moduleCatalog.containsKey(code)) {
                throw new InvalidCampusDataException("Module code " + code + " galat hai. Transaction cancel!");
            }
            AcademicModule module = moduleCatalog.get(code);

            // BugFix: Stop Duplicate Enrollments inside same transaction
            if (validModules.contains(module)) {
                throw new InvalidCampusDataException("Invalid! Ek hi course me do baar enroll nahi kar sakte. Duplication pakda gaya ID: " + code);
            }

            // checking max capacity (unique feature)
            if (!module.hasAvailableSeats()) {
                throw new InvalidCampusDataException("Enrollment fail hua: Module '" + module.getModuleTitle() + "' puri tarah full ho chuka hai!");
            }
            validModules.add(module);
        }
        
        if (validModules.size() == 0) {
            throw new InvalidCampusDataException("Koi valid module select nahi kiya.");
        }

        // 2nd Pass: Ab actual increment karenge kyun ki sab successful validate ho gaye hain (Atomic behavior).
        RegistrationRecord record = new RegistrationRecord(scholar);
        for(int n=0; n < validModules.size(); n++) {
            AcademicModule mod = validModules.get(n);
            mod.incrementEnrollment();
            record.addModule(mod);
        }

        // ISSUE #2 FIXED: Synchronize specifically pending queue ko taaki thread clash na ho!
        synchronized(pendingRegistrations) {
            pendingRegistrations.add(record);
        }
        
        allRegistrations.add(record);
        System.out.println("Enrollment done! Background me process hone ke liye queue me daal diya.");
    }

    public void displayAllScholars() {
        if (scholarDirectory.isEmpty()) {
            System.out.println("Koi scholar nahi hai.");
            return;
        }
        for (Scholar s : scholarDirectory.values()) {
            System.out.println(s.toString());
        }
    }

    public void displayAllModules() {
        if (moduleCatalog.isEmpty()) {
            System.out.println("Koi module nahi hai.");
            return;
        }
        for (AcademicModule m : moduleCatalog.values()) {
            System.out.println(m.toString());
        }
    }

    public void displayRegistrations() {
        if (allRegistrations.isEmpty()) {
            System.out.println("Koi registration records nahi mile.");
            return;
        }
        for (RegistrationRecord r : allRegistrations) {
            System.out.println(r.toString());
        }
    }

    public ArrayList<RegistrationRecord> getPendingRegistrations() {
        return pendingRegistrations;
    }

    public HashMap<Integer, Scholar> getScholarDirectory() { return scholarDirectory; }
    public HashMap<Integer, AcademicModule> getModuleCatalog() { return moduleCatalog; }
    public ArrayList<RegistrationRecord> getAllRegistrations() { return allRegistrations; }

    public void setScholarDirectory(HashMap<Integer, Scholar> sDir) { this.scholarDirectory = sDir; }
    public void setModuleCatalog(HashMap<Integer, AcademicModule> mCat) { this.moduleCatalog = mCat; }
    public void setAllRegistrations(ArrayList<RegistrationRecord> aReg) { this.allRegistrations = aReg; }
}
/*
Ye file Smart Campus system ka main logic handle karti hai. Isme scholars aur modules 
ko fast access (O(1)) karne ke liye Java HashMaps ka use hua hai. 
Galt user input ko rokne ke liye isme custom exception (InvalidCampusDataException) ka bohot use kiya gaya hai.
*/
