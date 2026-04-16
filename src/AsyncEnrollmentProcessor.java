import java.util.ArrayList;

public class AsyncEnrollmentProcessor implements Runnable {
    private ArrayList<RegistrationRecord> pendingRegistrations;

    public AsyncEnrollmentProcessor(ArrayList<RegistrationRecord> pendingRegistrations) {
        this.pendingRegistrations = pendingRegistrations;
    }

    @Override
    public void run() {
        System.out.println("[Thread System] Background processing start ho gayi hai...");
        try {
            while(true) {
                RegistrationRecord recordUthaneKeLiye = null;
                
                // ISSUE #2 FIXED: Thread sirf data uthate time list ko lock karega.
                // Uske baad chhod dega, taaki apko app me wait / UI freeze na jhelna pade!
                synchronized (pendingRegistrations) {
                    if (pendingRegistrations.size() > 0) {
                        recordUthaneKeLiye = pendingRegistrations.remove(0); // Pehla task element nikal liya
                    }
                }
                
                if (recordUthaneKeLiye != null) {
                    System.out.println("[Thread System] Processing... Scholar: " + recordUthaneKeLiye.getScholar().getFullName());
                    // Delay ko list se bahar lagaya hai taaki main menu pe rukawat na ho!
                    Thread.sleep(1500); 
                    recordUthaneKeLiye.setProcessed(true);
                    System.out.println("[Thread System] Done! Registration process ho gaya!");
                } else {
                    System.out.println("[Thread System] Queue khali hai. Thread aaram karega.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("[Thread System] Error aayi: " + e.getMessage());
        }
    }
}
/*
Ye class Runnable interface ke through Multithreading implement karti hai. Iska kaam pending 
registrations ko background me asynchronously process karna hai. Network delay ko simulate karne ke liye 
Thread.sleep use hua hai, aur data ko safe rakhne ke liye synchronized block lagaya gaya hai.
*/
