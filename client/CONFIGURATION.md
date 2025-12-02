# ✅ PROJET MICROSERVICE CLIENT - CONFIGURATION COMPLÈTE

## 📦 Structure du projet

```
client/
├── pom.xml                                    ✅ Configuré
├── README.md                                  ✅ Créé
├── .gitignore                                 ✅ Créé
├── test-api.http                              ✅ Créé (tests API)
└── src/
    ├── main/
    │   ├── java/com/example/demo/
    │   │   ├── DemoApplication.java           ✅ Classe principale avec @EnableDiscoveryClient
    │   │   ├── entities/
    │   │   │   └── Client.java                ✅ Entité JPA (id, nom, age)
    │   │   ├── repositories/
    │   │   │   └── ClientRepository.java      ✅ Repository JPA
    │   │   ├── services/
    │   │   │   └── ClientService.java         ✅ NOUVEAU - Couche Service
    │   │   ├── controllers/
    │   │   │   └── ClientController.java      ✅ Controller REST
    │   │   └── exceptions/
    │   │       └── GlobalExceptionHandler.java ✅ NOUVEAU - Gestion des erreurs
    │   └── resources/
    │       └── application.properties         ✅ Configuration Eureka + H2
    └── test/
        └── java/com/example/demo/
            └── DemoApplicationTests.java
```

## 🎯 Modifications effectuées

### 1. ✅ Fichier pom.xml
- **Déjà bien configuré** avec toutes les dépendances :
  - Spring Boot 3.5.8
  - Spring Cloud 2025.0.0
  - Spring Data JPA
  - Eureka Client
  - H2 Database
  - Lombok
  - Actuator

### 2. ✅ application.properties - Configuré
```properties
server.port=8088
spring.application.name=SERVICE-CLIENT
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.hostname=localhost
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.application.name}:${server.port}

# H2 Database
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:clientdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. ✅ DemoApplication.java - Configuré
```java
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initialiserBaseH2(ClientRepository clientRepository) {
        return args -> {
            clientRepository.save(new Client(null, "Rabab SELIMANI", 23f));
            clientRepository.save(new Client(null, "Amal RAMI", 22f));
            clientRepository.save(new Client(null, "Samir SAFI", 22f));
        };
    }
}
```

### 4. ✅ Client.java - Entité
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Float age;
}
```

### 5. ✅ ClientRepository.java
```java
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
```

### 6. ✅ ClientService.java - NOUVEAU
```java
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
    
    public Client updateClient(Long id, Client client) {
        client.setId(id);
        return clientRepository.save(client);
    }
    
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
```

### 7. ✅ ClientController.java
```java
@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    
    @GetMapping("/clients")
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    
    @GetMapping("/client/{id}")
    public Client findById(@PathVariable Long id) throws Exception {
        return clientRepository.findById(id)
            .orElseThrow(() -> new Exception("Client non trouvé"));
    }
}
```

### 8. ✅ GlobalExceptionHandler.java - NOUVEAU
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
```

## 🚀 Commandes pour démarrer

### Option 1 : Avec Maven Wrapper (recommandé)
```cmd
cd C:\Users\youbitech\Desktop\client
mvnw clean install
mvnw spring-boot:run
```

### Option 2 : Avec Maven installé
```cmd
cd C:\Users\youbitech\Desktop\client
mvn clean install
mvn spring-boot:run
```

### Option 3 : Depuis l'IDE
- Exécuter la classe `DemoApplication.java`
- Bouton droit → Run 'DemoApplication'

## 🧪 Tests des endpoints

### 1. Tous les clients
```
GET http://localhost:8088/clients
```

### 2. Client par ID
```
GET http://localhost:8088/client/1
GET http://localhost:8088/client/2
GET http://localhost:8088/client/3
```

### 3. Console H2
```
URL: http://localhost:8088/h2-console
JDBC URL: jdbc:h2:mem:clientdb
Username: sa
Password: (vide)
```

### 4. Actuator
```
http://localhost:8088/actuator/health
```

### 5. Eureka Dashboard
```
http://localhost:8761
```
Vérifiez que **SERVICE-CLIENT** apparaît dans la liste.

## 📋 Prérequis

1. ✅ **Java 17** ou supérieur installé
2. ✅ **Maven** (ou utiliser mvnw inclus)
3. ✅ **Eureka Server** démarré sur port 8761
4. ✅ Port 8088 libre

## 🎯 Données de test initiales

Au démarrage, 3 clients sont automatiquement insérés :
1. **Rabab SELIMANI** - 23 ans
2. **Amal RAMI** - 22 ans
3. **Samir SAFI** - 22 ans

## 📝 Notes importantes

- ⚠️ Si vous voyez des erreurs dans l'IDE, essayez :
  1. **Invalidate Caches / Restart** dans IntelliJ
  2. Réimporter le projet Maven
  3. Rebuild Project
  
- Les avertissements "never used" sont normaux dans l'IDE
- Le projet compile et fonctionne correctement malgré ces avertissements

## ✨ Nouveautés ajoutées

1. ✅ **Couche Service** (ClientService.java) - Architecture complète
2. ✅ **Gestion des exceptions** (GlobalExceptionHandler.java)
3. ✅ **Configuration H2** complète dans application.properties
4. ✅ **README.md** avec documentation complète
5. ✅ **test-api.http** pour tester facilement les endpoints
6. ✅ **.gitignore** pour versionner proprement

## 🎉 Le projet est prêt !

Tous les fichiers sont configurés et le microservice est prêt à être démarré.

