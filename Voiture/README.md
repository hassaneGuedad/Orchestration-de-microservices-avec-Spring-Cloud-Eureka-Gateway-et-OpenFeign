    "marque": "Toyota",
    "matricule": "A 25 333",
    "model": "Corolla",
    "id_client": 1,
    "client": {
      "id": 1,
      "nom": "Mouna",
      "age": 20.0
    }
  },
  {
    "id": 2,
    "marque": "Renault",
    "matricule": "B 6 3456",
    "model": "Megane",
    "id_client": 1,
    "client": {
      "id": 1,
      "nom": "Mouna",
      "age": 20.0
    }
  },
  {
    "id": 3,
    "marque": "Peugeot",
    "matricule": "A 55 4444",
    "model": "301",
    "id_client": 2,
    "client": {
      "id": 2,
      "nom": "Imane",
      "age": 24.0
    }
  }
]
```

## Données de Test

Le `CommandLineRunner` initialise automatiquement 3 voitures au démarrage :
- Toyota Corolla (matricule: A 25 333) - Client ID 1
- Renault Megane (matricule: B 6 3456) - Client ID 1
- Peugeot 301 (matricule: A 55 4444) - Client ID 2

## Notes Importantes

1. **@Transient** : Le champ `client` dans l'entité Voiture est marqué `@Transient` car il n'est pas persisté en base. Il est rempli dynamiquement via l'appel Feign.

2. **Harmonisation des noms** : Le champ `id_client` utilise le format snake_case. Les méthodes générées par Lombok sont donc `getId_client()` et `setId_client()`.

3. **OpenFeign** : L'annotation `@EnableFeignClients` sur la classe principale active le scan des interfaces Feign.

4. **Eureka** : Le service s'enregistre automatiquement auprès d'Eureka au démarrage.
# SERVICE-VOITURE - Microservice de Gestion des Voitures

## Architecture

Ce microservice fait partie d'une architecture microservices comprenant :
- **Eureka Server** : Service de découverte (registry)
- **SERVICE-CLIENT** : Microservice de gestion des clients
- **SERVICE-VOITURE** : Microservice de gestion des voitures (ce projet)

## Communication

- **OpenFeign** : Communication synchrone entre SERVICE-VOITURE et SERVICE-CLIENT
- **Eureka Discovery** : Découverte automatique des services

## Diagramme de Classes

```
┌─────────────────────┐
│   DemoApplication   │
│  @EnableFeignClients│
└─────────────────────┘
           │
           ├──────────────────────┐
           │                      │
┌──────────▼──────────┐  ┌───────▼────────────┐
│  VoitureController  │  │ CommandLineRunner  │
│   @RestController   │  │ (initialiserBaseH2)│
└──────────┬──────────┘  └────────────────────┘
           │
           ├─────────────┬─────────────┐
           │             │             │
┌──────────▼──────────┐  │  ┌─────────▼─────────┐
│ VoitureRepository   │  │  │  ClientService    │
│   @Repository       │  │  │  @FeignClient     │
└──────────┬──────────┘  │  └─────────┬─────────┘
           │             │            │
┌──────────▼──────────┐  │            │
│     Voiture         │  │            │
│     @Entity         │  │            │
│  ┌──────────────┐   │  │            │
│  │ id           │   │  │            │
│  │ marque       │   │  │            │
│  │ matricule    │   │  │            │
│  │ model        │   │  │            │
│  │ id_client    │   │  │            │
│  │ @Transient   │   │  │            │
│  │ client       │───┼──┼────────────┘
│  └──────────────┘   │  │
└─────────────────────┘  │
                         │
              ┌──────────▼──────────┐
              │      Client         │
              │      (POJO)         │
              │  ┌──────────────┐   │
              │  │ id           │   │
              │  │ nom          │   │
              │  │ age          │   │
              │  └──────────────┘   │
              └─────────────────────┘
```

## Dépendances Maven

- **Spring Boot Starter Data JPA** : Persistance des données
- **H2 Database** : Base de données en mémoire
- **Spring Boot Starter Web** : API REST
- **Eureka Discovery Client** : Client pour Eureka
- **Lombok** : Réduction du code boilerplate
- **OpenFeign** : Client REST déclaratif
- **Spring Boot Starter HATEOAS** : Support HATEOAS

## Configuration

### application.properties
```properties
server.port=8089
spring.application.name=SERVICE-VOITURE
spring.cloud.discovery.enabled=true
eureka.instance.hostname=localhost
```

## Structure des Données

### Entité Voiture
```java
@Entity
public class Voiture {
    @Id @GeneratedValue
    private Long id;
    private String marque;
    private String matricule;
    private String model;
    private Long id_client;
    
    @Transient
    @ManyToOne
    private Client client;
}
```

### POJO Client (côté SERVICE-VOITURE)
```java
public class Client {
    private Long id;
    private String nom;
    private Float age;
}
```

## Points d'API

### GET /voitures
Récupère toutes les voitures avec leurs clients associés

### GET /voitures/{id}
Récupère une voiture spécifique avec son client associé

## Démarrage

### Prérequis
1. **Eureka Server** doit être démarré (port par défaut 8761)
2. **SERVICE-CLIENT** doit être démarré (port configuré)

### Ordre de démarrage
1. Démarrer Eureka Server
2. Démarrer SERVICE-CLIENT
3. Démarrer SERVICE-VOITURE

### Commande Maven
```bash
mvnw spring-boot:run
```

## Test

Une fois tous les services démarrés, accéder à :
- http://localhost:8089/voitures

### Exemple de réponse attendue
```json
[
  {
    "id": 1,

