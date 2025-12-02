# Microservice CLIENT - Spring Boot avec Eureka

## 📋 Description
Microservice de gestion des clients avec Spring Boot, Spring Cloud Eureka Client et base de données H2.

## 🏗️ Architecture

### Structure des packages
```
src/main/java/com/example/demo/
├── DemoApplication.java          # Classe principale
├── entities/
│   └── Client.java               # Entité JPA
├── repositories/
│   └── ClientRepository.java     # Repository JPA
├── services/
│   └── ClientService.java        # Couche Service (nouvellement créée)
└── controllers/
    └── ClientController.java     # Contrôleur REST
```

## 🛠️ Technologies utilisées
- **Spring Boot 3.5.8**
- **Spring Cloud 2025.0.0**
- **Spring Data JPA**
- **Spring Cloud Netflix Eureka Client**
- **H2 Database** (base de données en mémoire)
- **Lombok** (réduction du code boilerplate)
- **Spring Boot Actuator**

## ⚙️ Configuration

### application.properties
```properties
# Port du serveur
server.port=8088

# Nom du service enregistré dans Eureka
spring.application.name=SERVICE-CLIENT

# Configuration Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.hostname=localhost
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.application.name}:${server.port}

# Base de données H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## 🚀 Démarrage

### Prérequis
1. **Java 17** ou supérieur
2. **Maven** installé
3. **Eureka Server** démarré sur le port 8761

### Commandes
```bash
# Compilation
mvnw clean compile

# Démarrage de l'application
mvnw spring-boot:run
```

## 📡 Endpoints API

### Récupérer tous les clients
```
GET http://localhost:8088/clients
```

**Réponse:**
```json
[
  {
    "id": 1,
    "nom": "Rabab SELIMANI",
    "age": 23.0
  },
  {
    "id": 2,
    "nom": "Amal RAMI",
    "age": 22.0
  },
  {
    "id": 3,
    "nom": "Samir SAFI",
    "age": 22.0
  }
]
```

### Récupérer un client par ID
```
GET http://localhost:8088/client/1
```

**Réponse:**
```json
{
  "id": 1,
  "nom": "Rabab SELIMANI",
  "age": 23.0
}
```

## 💾 Base de données H2

### Accès à la console H2
```
URL: http://localhost:8088/h2-console
JDBC URL: jdbc:h2:mem:clientdb
Username: sa
Password: (laisser vide)
```

## 🔄 Données initiales

Les données suivantes sont automatiquement insérées au démarrage (via CommandLineRunner) :
- Rabab SELIMANI, 23 ans
- Amal RAMI, 22 ans
- Samir SAFI, 22 ans

## 📊 Monitoring

### Actuator endpoints
```
http://localhost:8088/actuator
http://localhost:8088/actuator/health
```

## 🔗 Enregistrement Eureka

Le microservice s'enregistre automatiquement auprès d'Eureka Server sous le nom **SERVICE-CLIENT**.

Vérifiez l'enregistrement sur: `http://localhost:8761`

## 📝 Notes
- Le microservice utilise **@EnableDiscoveryClient** pour l'enregistrement automatique
- La base H2 est en mémoire et se réinitialise à chaque redémarrage
- Lombok génère automatiquement les getters, setters, constructeurs

## 🎯 Test rapide

Après avoir démarré Eureka Server et ce microservice:

1. Vérifiez l'enregistrement: `http://localhost:8761`
2. Testez l'API: `http://localhost:8088/clients`
3. Testez un client spécifique: `http://localhost:8088/client/1`
4. Accédez à la console H2: `http://localhost:8088/h2-console`

