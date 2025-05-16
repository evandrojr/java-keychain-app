## How to Run with Auto Reload (Linux)

If you want the application to automatically recompile and restart every time you save a `.java` file, you can use the `entr` utility (Linux only):

1. Install entr:
   ```
   sudo apt-get install entr
   ```

2. In the project root, run:
   ```
   find src/main/java -name '*.java' | entr -r ./gradlew run
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

## Requirements

- Java 7 or higher
- Gradle

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