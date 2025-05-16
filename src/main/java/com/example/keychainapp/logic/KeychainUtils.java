package com.example.keychainapp.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KeychainUtils {
    public static List<String> listSavedKeys() {
        List<String> keys = new ArrayList<String>();
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String key = file.getName().substring(0, file.getName().length() - 4);
                    keys.add(key);
                }
            }
        }
        return keys;
    }
}
