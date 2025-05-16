# Java Keychain App

Este projeto é uma aplicação Java que permite ao usuário armazenar e recuperar dados sigilosos de forma segura utilizando o chaveiro criptografado do sistema operacional. A aplicação possui uma interface gráfica simples, onde o usuário pode cadastrar chaves e valores.

## Estrutura do Projeto

O projeto é organizado da seguinte forma:

```
java-keychain-app
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── keychainapp
│   │   │               ├── Main.java          # Ponto de entrada da aplicação
│   │   │               ├── ui
│   │   │               │   └── MainFrame.java # Classe que representa a interface gráfica
│   │   │               └── logic
│   │   │                   └── KeychainService.java # Lógica para salvar e recuperar dados
│   │   └── resources
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── keychainapp
│       │               └── logic
│       │                   └── KeychainServiceTest.java # Testes automatizados
│       └── resources
├── build.gradle                # Script de construção do Gradle
└── README.md                   # Documentação do projeto
```

## Requisitos

- Java 7 ou superior
- Gradle

## Como Executar a Aplicação

1. Clone o repositório:
   ```
   git clone <URL_DO_REPOSITORIO>
   cd java-keychain-app
   ```

2. Compile o projeto usando o Gradle:
   ```
   ./gradlew build
   ```

3. Execute a aplicação:
   ```
   ./gradlew run
   ```

## Como Executar os Testes

Para executar os testes automatizados, utilize o seguinte comando:
```
./gradlew test
```

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para mais detalhes.