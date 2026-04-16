import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        CampusManager campus = new CampusManager();

        System.out.println(">> SmartCampus System Load ho raha hai...");
        Object loadedData = DataStorageService.loadData();
        if (loadedData != null) {
            try {
                Object[] dataArr = (Object[]) loadedData;

                HashMap<Integer, Scholar> sMap = (HashMap<Integer, Scholar>) dataArr[0];
                HashMap<Integer, AcademicModule> mMap = (HashMap<Integer, AcademicModule>) dataArr[1];
                ArrayList<RegistrationRecord> rList = (ArrayList<RegistrationRecord>) dataArr[2];

                campus.setScholarDirectory(sMap);
                campus.setModuleCatalog(mMap);
                campus.setAllRegistrations(rList);

                // BugFix: Reboot ke baad pendingRegistrations queue khali pad jati thi aur jo
                // process nahi hue the wo fast loop reset ho jate the.
                // Ye sab wapas array me bhejega jinki isProcessed = false hai.
                for (int v = 0; v < rList.size(); v++) {
                    RegistrationRecord checkRecord = rList.get(v);
                    if (!checkRecord.isProcessed()) {
                        campus.getPendingRegistrations().add(checkRecord);
                    }
                }

            } catch (Exception e) {
                System.out.println("Purana data thik se load nahi hua.");
            }
        }

        while (true) {
            System.out.println("\n-------------------------------------------");
            System.out.println("      SMART CAMPUS MANAGEMENT MENU      ");
            System.out.println("-------------------------------------------");
            System.out.println("1. Naya Scholar (Student) Add Karein");
            System.out.println("2. Naya Academic Module (Course) Add Karein");
            System.out.println("3. Scholar ko Module me Enroll Karein");
            System.out.println("4. Sabhi Scholars ki details dekhein");
            System.out.println("5. Sabhi Modules ki details dekhein");
            System.out.println("6. Enrollments check karein");
            System.out.println("7. Pending Registrations Process Karein (Background Thread)");
            System.out.println("8. Data Save kar ke Exit karein");
            System.out.println("-------------------------------------------");
            System.out.print("Apna option choose karein: ");

            int option = 0;
            try {
                option = Integer.parseInt(scan.nextLine());
            } catch (Exception e) {
                System.out.println("Galat input! Please koi number daalein.");
                continue;
            }

            try {
                if (option == 1) {
                    System.out.print("Scholar ID do: ");
                    int id = Integer.parseInt(scan.nextLine());
                    System.out.print("Pura naam do: ");
                    String naam = scan.nextLine();
                    System.out.print("Email ID do: ");
                    String mail = scan.nextLine();
                    campus.addScholar(id, naam, mail);
                } else if (option == 2) {
                    System.out.print("Module Code (ID) do: ");
                    int code = Integer.parseInt(scan.nextLine());
                    System.out.print("Module ka Title: ");
                    String title = scan.nextLine();
                    System.out.print("Fee kitni hai: ");
                    double amt = Double.parseDouble(scan.nextLine());
                    System.out.print("Max students capacity (Seats): ");
                    int seats = Integer.parseInt(scan.nextLine());
                    campus.addModule(code, title, amt, seats);
                } else if (option == 3) {
                    System.out.print("Registration ke liye Scholar ID do: ");
                    int s_id = Integer.parseInt(scan.nextLine());
                    System.out.print("Module codes batao comma laga ke (jaise 101,102): ");
                    String codesInput = scan.nextLine();
                    String[] todneKeBaad = codesInput.split(",");
                    int[] codes = new int[todneKeBaad.length];
                    for (int n = 0; n < todneKeBaad.length; n++) {
                        codes[n] = Integer.parseInt(todneKeBaad[n].trim());
                    }
                    campus.enrollScholar(s_id, codes);
                } else if (option == 4) {
                    System.out.println("\n-- SCHOLARS LIST --");
                    campus.displayAllScholars();
                } else if (option == 5) {
                    System.out.println("\n-- MODULES LIST --");
                    campus.displayAllModules();
                } else if (option == 6) {
                    System.out.println("\n-- ALL ENROLLMENTS --");
                    campus.displayRegistrations();
                } else if (option == 7) {
                    System.out.println("Async thread chalu kar rahe hain...");
                    Thread threadKaKaam = new Thread(new AsyncEnrollmentProcessor(campus.getPendingRegistrations()));
                    threadKaKaam.start();
                    Thread.sleep(200); // Thoda wait taaki menu jaldi na aaye
                } else if (option == 8) {
                    System.out.println("File me data likh rahe hain...");
                    Object[] arrayMeData = new Object[3];
                    arrayMeData[0] = campus.getScholarDirectory();
                    arrayMeData[1] = campus.getModuleCatalog();
                    arrayMeData[2] = campus.getAllRegistrations();

                    DataStorageService.saveData(arrayMeData);
                    System.out.println("Sab save ho gaya. Bye bye!");
                    scan.close();
                    break;
                } else {
                    System.out.println("Galat menu option. Wapas try karein.");
                }
            } catch (InvalidCampusDataException exception) {
                System.out.println(">> Business Rule Error: " + exception.getMessage());
            } catch (NumberFormatException nf) {
                System.out.println(">> Error: Please sirf words mat daaliye jaha number chahiye.");
            } catch (Exception ex) {
                System.out.println(">> Kuch gadbad ho gayi: " + ex.getMessage());
            }
        }
    }
}
/*
 * Ye file system ka main entry point hai jo Menu-Driven program ka requirement
 * fulfill karti hai.
 * Ye Scanner ka use karke console me user input leti hai ek while(true) loop ke
 * andar.
 * Program crash hone se bachane ke liye isme NumberFormatException ko achhe se
 * handle kiya gaya hai.
 */
