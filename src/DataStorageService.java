import java.io.*;

public class DataStorageService {
    private static final String FILE_NAME = "campus_data.ser";

    public static void saveData(Object data) {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_NAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
            System.out.println("Data save ho gaya file me -> " + FILE_NAME);
        } catch (Exception e) {
            System.out.println("Data save karne me dikkat: " + e.getMessage());
        }
    }

    public static Object loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return null; // Pehli baar run hone pe file nahi hoti
        }

        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object data = ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Data wapas load ho gaya hai " + FILE_NAME + " se.");
            return data;
        } catch (Exception e) {
            System.out.println("Data load karne me problem: " + e.getMessage());
            return null;
        }
    }
}
/*
Ye class project ka 'Bonus' (File Handling) requirement pura karti hai. Ye Java Serialization 
ka use karke app ka current data ek binary .ser file me save karti hai, aur program 
start hone par wahi data wapas load kar leti hai.
*/
