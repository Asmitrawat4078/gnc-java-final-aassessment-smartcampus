package com.smartcampus.services;

import java.io.*;

public class DataStorageService {
    private static final String FILE_NAME = "campus_data.ser";

    public static void saveData(Object data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
            System.out.println("Data saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static Object loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object data = ois.readObject();
            System.out.println("Data loaded successfully from " + FILE_NAME);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}
/*
Ye class project ka 'Bonus' (File Handling) requirement pura karti hai. Ye Java Serialization 
ka use karke app ka current data ek binary .ser file me save karti hai, aur program 
start hone par wahi data wapas load kar leti hai.
*/
