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

### Supported Operating Systems

- **Linux** (tested on distributions with GNOME and KDE, using `secret-tool` or `kwalletcli` for keychain integration)
- **macOS** (uses the built-in `security` command for keychain integration)
- **Windows** (uses Windows Credential Manager via PowerShell)

#### Windows Support

On Windows, the application uses the Windows Credential Manager for secure password storage. This requires PowerShell and the [CredentialManager](https://www.powershellgallery.com/packages/CredentialManager) PowerShell module.

**To install the CredentialManager module:**
1. Open PowerShell as Administrator
2. Run:
   ```
   Install-Module -Name CredentialManager -Scope CurrentUser
   ```

No additional dependencies are required beyond Java and Gradle.

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