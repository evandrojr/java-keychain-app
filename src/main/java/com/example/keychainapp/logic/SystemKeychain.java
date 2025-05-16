package com.example.keychainapp.logic;

import java.io.*;

public class SystemKeychain {
    public static boolean savePassword(String service, String user, String password) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("security", "add-generic-password", "-a", user, "-s", service, "-w", password, "-U");
                Process p = pb.start();
                return p.waitFor() == 0;
            } else if (os.contains("linux")) {
                if (isCommandAvailable("secret-tool")) {
                    ProcessBuilder pb = new ProcessBuilder("secret-tool", "store", "--label=" + service, "service", service, "user", user);
                    Process p = pb.start();
                    OutputStream osOut = p.getOutputStream();
                    osOut.write(password.getBytes("UTF-8"));
                    osOut.close();
                    return p.waitFor() == 0;
                }
                if (isCommandAvailable("kwalletcli")) {
                    ProcessBuilder pb = new ProcessBuilder("kwalletcli", "-f", service, "-e", user, "-p");
                    Process p = pb.start();
                    OutputStream osOut = p.getOutputStream();
                    osOut.write(password.getBytes("UTF-8"));
                    osOut.close();
                    return p.waitFor() == 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String loadPassword(String service, String user) {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("security", "find-generic-password", "-a", user, "-s", service, "-w");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
                String password = reader.readLine();
                p.waitFor();
                return password;
            } else if (os.contains("linux")) {
                if (isCommandAvailable("secret-tool")) {
                    ProcessBuilder pb = new ProcessBuilder("secret-tool", "lookup", "service", service, "user", user);
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
                    String password = reader.readLine();
                    p.waitFor();
                    return password;
                }
                if (isCommandAvailable("kwalletcli")) {
                    ProcessBuilder pb = new ProcessBuilder("kwalletcli", "-f", service, "-r", user);
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
                    String password = reader.readLine();
                    p.waitFor();
                    return password;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isCommandAvailable(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("which", command);
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
