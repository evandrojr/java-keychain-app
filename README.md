## How to Run with Auto Reload (Linux)


If you want the application to automatically recompile and restart every time you save a `.java` file, you can use the `entr` utility (Linux only):

1. Install entr:
   ```
   sudo apt-get install entr
   ```

2. Run the provided script:
   ```
   ./auto-reload.sh
   ```

Every time you save a Java file, the application will be rebuilt and restarted automatically.

## How to Run the Program

To run the program normally, use:
```
./gradlew run
```

# Java Keychain App

This project is a Java application that allows users to securely store and retrieve sensitive data using the system's encrypted keychain (Linux GNOME/KDE, macOS). The application features a simple graphical interface where users can save and load passwords associated with a user.

## Project Structure

The project is organized as follows:

```
java-keychain-app
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── keychainapp
│   │   │               ├── Main.java          # Application entry point
│   │   │               ├── ui
│   │   │               │   └── MainFrame.java # GUI class
│   │   │               └── logic
│   │   │                   └── SystemKeychain.java # OS keychain integration
│   │   └── resources
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── keychainapp
│       │               └── logic
│       │                   └── KeychainServiceTest.java # Automated tests
│       └── resources
├── build.gradle                # Gradle build script
└── README.md                   # Project documentation
```



## Requirements and Compatibility

- Java 7 or higher
- Gradle

### Supported Operating Systems and Desktop Environments

- **Linux**
  - **Tested distributions:** Ubuntu 20.04+, Fedora 34+, Debian 11+, Arch Linux (2022+)
  - **Supported desktop environments:**
    - GNOME 3.36+
    - KDE Plasma 5.18+
  - **Keychain integration:**
    - GNOME: `secret-tool` (libsecret)
    - KDE: `kwalletcli`

- **macOS**
  - **Tested versions:** macOS 10.15 (Catalina)+, 11 (Big Sur), 12 (Monterey), 13 (Ventura)
  - **Keychain integration:** Built-in `security` command

- **Windows**
  - **Tested versions:** Windows 10, Windows 11
  - **Keychain integration:** Windows Credential Manager (via PowerShell)
  - **Requirements:** PowerShell and the [CredentialManager](https://www.powershellgallery.com/packages/CredentialManager) module

#### Windows Support

On Windows, the application uses the Windows Credential Manager for secure password storage. This requires PowerShell e o módulo CredentialManager.

**Se ocorrer erro ao salvar ou recuperar senhas no Windows:**
- Certifique-se de que o PowerShell está instalado e atualizado.
  - Se não estiver instalado, baixe e instale o PowerShell pelo site oficial: https://github.com/PowerShell/PowerShell
- Instale o módulo CredentialManager:
  1. Abra o PowerShell como Administrador
  2. Execute:
     ```
     Install-Module -Name CredentialManager -Scope CurrentUser
     ```

Após instalar, tente novamente. Não são necessárias outras dependências além de Java e Gradle.

## Installing Dependencies

All Java dependencies are managed automatically by Gradle. The first time you run any Gradle command, dependencies will be downloaded automatically.

If you want to download all dependencies manually, run:
```
./gradlew dependencies
```

No manual installation of Java libraries is required.

### System dependencies for Linux keychain integration

To use the system keychain integration on Linux, you need one of the following utilities installed:

- For GNOME (most Ubuntu, Fedora, etc):
  - `secret-tool` (package: `libsecret-tools`)
  - Install with:
    ```
    sudo apt-get install libsecret-tools
    ```
- For KDE:
  - `kwalletcli` (package: `kwalletcli`)
  - Install with:
    ```
    sudo apt-get install kwalletcli
    ```

On macOS, no extra dependencies are needed (uses the built-in `security` command).

## How to Run the Application

1. Clone the repository:
   ```
   git clone https://github.com/evandrojr/java-keychain-app
   cd java-keychain-app
   ```

2. Build the project using Gradle:
   ```
   ./gradlew build
   ```


3. Run the application:
   ```
   ./gradlew run
   ```

## How to Generate the JAR

To generate a runnable JAR file, use:
```
./gradlew jar
```
The JAR will be created in `build/libs/`. To run it (replace VERSION with the actual version):
```
java -cp build/libs/java-keychain-app-VERSION.jar com.example.keychainapp.Main
```

## How to Run the Tests

To run the automated tests, use the following command:
```
./gradlew test
```

## Contributions

Contributions are welcome! Feel free to open issues or pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.