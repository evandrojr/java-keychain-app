

package com.example.keychainapp.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase.FILETIME;

public class SystemKeychain {
    private static final Logger LOGGER = Logger.getLogger(SystemKeychain.class.getName());
    private static final String OS_NAME = "os.name";
    private static final String SECRET_TOOL = "secret-tool";
    private static final String KWALLETCLI = "kwalletcli";

    private SystemKeychain() { /* utilitário */ }
    public static boolean savePassword(String service, String user, String password) {
        LOGGER.info("[Keychain] savePassword chamado para service='" + service + "', key='" + user + "'");
        String os = System.getProperty(OS_NAME).toLowerCase();
        try {
            if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("security", "add-generic-password", "-a", user, "-s", service, "-w", password, "-U");
                Process p = pb.start();
                return p.waitFor() == 0;
            } else if (os.contains("linux")) {
                if (isCommandAvailable(SECRET_TOOL)) {
                    ProcessBuilder pb = new ProcessBuilder(SECRET_TOOL, "store", "--label=" + service, "service", service, "key", user);
                    Process p = pb.start();
                    OutputStream osOut = p.getOutputStream();
                    osOut.write(password.getBytes(StandardCharsets.UTF_8));
                    osOut.close();
                    return p.waitFor() == 0;
                }
                if (isCommandAvailable(KWALLETCLI)) {
                    ProcessBuilder pb = new ProcessBuilder(KWALLETCLI, "-f", service, "-e", user, "-p");
                    Process p = pb.start();
                    OutputStream osOut = p.getOutputStream();
                    osOut.write(password.getBytes(StandardCharsets.UTF_8));
                    osOut.close();
                    return p.waitFor() == 0;
                }
            } else if (os.contains("win")) {
                // Usa JNA para salvar/atualizar credencial no Windows Credential Manager
                String target = service + ":" + user;
                try {
                    byte[] passwordBytes = (password + "\0").getBytes(StandardCharsets.UTF_16LE);
                    WinCred.CREDENTIAL cred = new WinCred.CREDENTIAL();
                    cred.flags = new DWORD(0);
                    cred.type = new DWORD(WinCred.CRED_TYPE_GENERIC);
                    cred.targetName = target;
                    cred.comment = null;
                    cred.lastWritten = new FILETIME();
                    cred.credentialBlobSize = new DWORD(passwordBytes.length);
                    cred.credentialBlob = new Memory(passwordBytes.length);
                    cred.credentialBlob.write(0, passwordBytes, 0, passwordBytes.length);
                    cred.persist = new DWORD(WinCred.CRED_PERSIST_LOCAL_MACHINE);
                    cred.attributeCount = new DWORD(0);
                    cred.attributes = null;
                    cred.targetAlias = null;
                    cred.userName = user;
                    boolean result = WinCred.INSTANCE.CredWriteW(cred, 0);
                    if (result) {
                        LOGGER.info("[Windows] Credencial salva com sucesso no Credential Manager: " + target);
                    } else {
                        int err = Kernel32.INSTANCE.GetLastError();
                        LOGGER.log(Level.SEVERE, "[Windows] Erro ao salvar credencial: {0}", err);
                    }
                    return result;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "[Windows] Exceção ao acessar o Credential Manager via JNA. Veja o README para instruções.", e);
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[Keychain] Exceção inesperada em savePassword", e);
        }
        return false;
    }

    /**
     * Lista as credenciais do Windows Credential Manager (apenas nomes, não senhas).
     */
    public static String listWindowsCredentials() {
        StringBuilder sb = new StringBuilder();
        String os = System.getProperty(OS_NAME).toLowerCase();
        if (os.contains("win")) {
            try {
                PointerByReference pCredentials = new PointerByReference();
                IntByReference pCount = new IntByReference();
                boolean result = WinCred.INSTANCE.CredEnumerateW(null, 0, pCount, pCredentials);
                if (result) {
                    int count = pCount.getValue();
                    Pointer credArray = pCredentials.getValue();
                    for (int i = 0; i < count; i++) {
                        Pointer pCred = credArray.getPointer((long) i * Native.POINTER_SIZE);
                        WinCred.CREDENTIAL cred = new WinCred.CREDENTIAL(pCred);
                        cred.read();
                        sb.append("Target: ").append(cred.targetName).append(" | Key: ").append(cred.userName).append(System.lineSeparator());
                    }
                    WinCred.INSTANCE.CredFree(credArray);
                } else {
                    int err = Kernel32.INSTANCE.GetLastError();
                    sb.append("Erro ao listar credenciais: ").append(err);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao listar credenciais do Windows Credential Manager", e);
            }
        }
        return sb.toString();
    }

    public static String loadPassword(String service, String user) {
        String os = System.getProperty(OS_NAME).toLowerCase();
        try {
            if (os.contains("mac")) {
                ProcessBuilder pb = new ProcessBuilder("security", "find-generic-password", "-a", user, "-s", service, "-w");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
                String value = reader.readLine();
                p.waitFor();
                return value;
            } else if (os.contains("linux")) {
                if (isCommandAvailable(SECRET_TOOL)) {
                    ProcessBuilder pb = new ProcessBuilder(SECRET_TOOL, "lookup", "service", service, "key", user);
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
                    String value = reader.readLine();
                    p.waitFor();
                    return value;
                }
                if (isCommandAvailable(KWALLETCLI)) {
                    ProcessBuilder pb = new ProcessBuilder(KWALLETCLI, "-f", service, "-r", user);
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
                    String value = reader.readLine();
                    p.waitFor();
                    return value;
                }
            } else if (os.contains("win")) {
                // Usa JNA para ler a senha do Windows Credential Manager
                String target = service + ":" + user;
                try {
                    PointerByReference pCred = new PointerByReference();
                    boolean found = WinCred.INSTANCE.CredReadW(target, WinCred.CRED_TYPE_GENERIC, 0, pCred);
                    if (found) {
                        WinCred.CREDENTIAL cred = new WinCred.CREDENTIAL(pCred.getValue());
                        cred.read();
                        int len = cred.credentialBlobSize.intValue();
                        byte[] passwordBytes = cred.credentialBlob.getByteArray(0, len);
                        String password = new String(passwordBytes, StandardCharsets.UTF_16LE);
                        // Remove null terminator if present
                        int nullIndex = password.indexOf('\0');
                        if (nullIndex != -1) {
                            password = password.substring(0, nullIndex);
                        }
                        WinCred.INSTANCE.CredFree(pCred.getValue());
                        return password;
                    } else {
                        int err = Kernel32.INSTANCE.GetLastError();
                        LOGGER.log(Level.WARNING, "[Windows] Credencial não encontrada: {0}", err);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "[Windows] Erro ao acessar o Credential Manager via JNA. Veja o README para instruções.", e);
                }
                return null;
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
            LOGGER.log(Level.WARNING, "Erro ao verificar comando: {0}", command);
            return false;
        }
    }

    // --- JNA WinCred interface ---
    public static class WinCred {
        public static final int CRED_TYPE_GENERIC = 1;
        public static final int CRED_PERSIST_LOCAL_MACHINE = 2;

        public interface Advapi32 extends Library {
            Advapi32 INSTANCE = Native.load("Advapi32", Advapi32.class);
            boolean CredWriteW(CREDENTIAL cred, int flags);
            boolean CredReadW(String target, int type, int reservedFlag, PointerByReference pCredential);
            boolean CredEnumerateW(String filter, int flags, IntByReference count, PointerByReference pCredentials);
            void CredFree(Pointer cred);
        }
        public static final Advapi32 INSTANCE = Advapi32.INSTANCE;

        public static class CREDENTIAL extends Structure {
            public DWORD flags;
            public DWORD type;
            public String targetName;
            public String comment;
            public FILETIME lastWritten;
            public DWORD credentialBlobSize;
            public Pointer credentialBlob;
            public DWORD persist;
            public DWORD attributeCount;
            public Pointer attributes;
            public String targetAlias;
            public String userName;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList(
                    "flags", "type", "targetName", "comment", "lastWritten", "credentialBlobSize", "credentialBlob", "persist", "attributeCount", "attributes", "targetAlias", "userName"
                );
            }

            public CREDENTIAL() {}
            public CREDENTIAL(Pointer p) { super(p); read(); }
        }
    }
}
