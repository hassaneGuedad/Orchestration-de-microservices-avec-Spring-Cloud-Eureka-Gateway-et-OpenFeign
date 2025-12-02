VoitureRepository
    │
    └─── manages ───▶ Voiture
    │
Voiture
    │
    └─── contains ──▶ Client (transient)
    │
ClientService
    │
    └─── returns ───▶ Client
```

## Patrons de conception utilisés

### 1. Repository Pattern
- **VoitureRepository** : Abstraction de l'accès aux données

### 2. DTO Pattern (Data Transfer Object)
- **Client** : Transfert de données entre microservices

### 3. Facade Pattern
- **ClientService** : Interface simplifiée pour accéder à SERVICE-CLIENT

### 4. Dependency Injection
- **@Autowired** : Injection de dépendances Spring

### 5. Active Record Pattern (via JPA)
- **Voiture** : Entité JPA avec mapping objet-relationnel

## Technologies et Frameworks

```
┌─────────────────────────────────────────────────────┐
│                  Spring Boot 3.5.8                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌───────────────┐  ┌──────────────┐               │
│  │  Spring Web   │  │  Spring Data │               │
│  │     REST      │  │     JPA      │               │
│  └───────────────┘  └──────────────┘               │
│                                                     │
│  ┌───────────────┐  ┌──────────────┐               │
│  │ Spring Cloud  │  │   Lombok     │               │
│  │   OpenFeign   │  │              │               │
│  └───────────────┘  └──────────────┘               │
│                                                     │
│  ┌───────────────┐  ┌──────────────┐               │
│  │    Eureka     │  │      H2      │               │
│  │    Client     │  │   Database   │               │
│  └───────────────┘  └──────────────┘               │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## Annotations utilisées

| Annotation | Classe/Méthode | Rôle |
|-----------|----------------|------|
| @SpringBootApplication | DemoApplication | Configuration Spring Boot |
| @EnableFeignClients | DemoApplication | Active Feign |
| @Entity | Voiture | Entité JPA |
| @Data | Voiture, Client | Génère getters/setters (Lombok) |
| @AllArgsConstructor | Voiture, Client | Constructeur avec tous les paramètres |
| @NoArgsConstructor | Voiture, Client | Constructeur sans paramètre |
| @Id | Voiture.id | Clé primaire |
| @GeneratedValue | Voiture.id | Auto-incrémentation |
| @Transient | Voiture.client | Non persisté en base |
| @ManyToOne | Voiture.client | Relation logique |
| @Repository | VoitureRepository | Bean Spring Repository |
| @FeignClient | ClientService | Client Feign |
| @RestController | VoitureController | Contrôleur REST |
| @Autowired | Attributs | Injection de dépendances |
| @GetMapping | Méthodes | Endpoint HTTP GET |
| @PathVariable | Paramètres | Variable d'URL |
| @Bean | initialiserBaseH2 | Bean Spring |

## Structure des packages

```
com.example.demo
│
├── DemoApplication.java          (Main + Configuration)
│
├── Voiture.java                  (Entity)
├── Client.java                   (DTO)
│
├── VoitureRepository.java        (Data Access)
├── ClientService.java            (Feign Client)
│
└── VoitureController.java        (REST Controller)
```
# Diagramme de Classes - SERVICE-VOITURE

## Vue d'ensemble UML

```
┌────────────────────────────────────────────────────────────────────────┐
│                         DemoApplication                                │
├────────────────────────────────────────────────────────────────────────┤
│ - @SpringBootApplication                                               │
│ - @EnableFeignClients                                                  │
├────────────────────────────────────────────────────────────────────────┤
│ + main(String[] args) : void                                           │
│ + initialiserBaseH2(VoitureRepository, ClientService) : CommandLineRunner│
└────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
         ┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
         │ VoitureController│  │ ClientService│  │ VoitureRepository│
         └──────────────────┘  └──────────────┘  └──────────────────┘
                    │               │               │
                    │               │               │
                    ▼               ▼               ▼
         ┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
         │     Voiture      │  │    Client    │  │  JpaRepository   │
         └──────────────────┘  └──────────────┘  └──────────────────┘
```

## Détail des Classes

### 1. DemoApplication
```java
┌─────────────────────────────────────────────────────────────┐
│                      <<Application>>                        │
│                     DemoApplication                         │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @SpringBootApplication                                    │
│ - @EnableFeignClients                                       │
├─────────────────────────────────────────────────────────────┤
│ + main(args: String[]) : void                               │
│ + initialiserBaseH2(                                        │
│     voitureRepository: VoitureRepository,                   │
│     clientService: ClientService                            │
│   ) : CommandLineRunner                                     │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- Point d'entrée de l'application
- Configuration Spring Boot
- Activation des clients Feign
- Initialisation des données de test

---

### 2. Voiture (Entity)
```java
┌─────────────────────────────────────────────────────────────┐
│                       <<Entity>>                            │
│                        Voiture                              │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @Entity                                                   │
│ - @Data                                                     │
│ - @AllArgsConstructor                                       │
│ - @NoArgsConstructor                                        │
├─────────────────────────────────────────────────────────────┤
│ - id: Long              [@Id, @GeneratedValue]              │
│ - marque: String                                            │
│ - matricule: String                                         │
│ - model: String                                             │
│ - id_client: Long                                           │
│ - client: Client        [@Transient, @ManyToOne]            │
├─────────────────────────────────────────────────────────────┤
│ + getId() : Long                                            │
│ + setId(id: Long) : void                                    │
│ + getMarque() : String                                      │
│ + setMarque(marque: String) : void                          │
│ + getMatricule() : String                                   │
│ + setMatricule(matricule: String) : void                    │
│ + getModel() : String                                       │
│ + setModel(model: String) : void                            │
│ + getId_client() : Long                                     │
│ + setId_client(id_client: Long) : void                      │
│ + getClient() : Client                                      │
│ + setClient(client: Client) : void                          │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- Entité JPA représentant une voiture
- Persistance en base de données H2
- Relation avec Client (chargement dynamique via Feign)

**Notes importantes :**
- `@Transient` : Le champ `client` n'est PAS persisté en base
- `@ManyToOne` : Annotation de relation (logique uniquement)
- `id_client` : Clé étrangère persistée en base

---

### 3. Client (POJO)
```java
┌─────────────────────────────────────────────────────────────┐
│                        <<POJO>>                             │
│                         Client                              │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @Data                                                     │
│ - @AllArgsConstructor                                       │
│ - @NoArgsConstructor                                        │
├─────────────────────────────────────────────────────────────┤
│ - id: Long                                                  │
│ - nom: String                                               │
│ - age: Float                                                │
├─────────────────────────────────────────────────────────────┤
│ + getId() : Long                                            │
│ + setId(id: Long) : void                                    │
│ + getNom() : String                                         │
│ + setNom(nom: String) : void                                │
│ + getAge() : Float                                          │
│ + setAge(age: Float) : void                                 │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- DTO (Data Transfer Object) pour recevoir les données de SERVICE-CLIENT
- Pas d'entité JPA (pas de persistance locale)
- Utilisé uniquement pour la sérialisation/désérialisation JSON

---

### 4. VoitureRepository (Interface)
```java
┌─────────────────────────────────────────────────────────────┐
│                    <<Interface>>                            │
│                  VoitureRepository                          │
│            extends JpaRepository<Voiture, Long>             │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @Repository                                               │
├─────────────────────────────────────────────────────────────┤
│ + findByIdClient(id: Long) : List<Voiture>                  │
│                                                             │
│ Héritées de JpaRepository:                                  │
│ + findAll() : List<Voiture>                                 │
│ + findById(id: Long) : Optional<Voiture>                    │
│ + save(voiture: Voiture) : Voiture                          │
│ + deleteById(id: Long) : void                               │
│ + count() : long                                            │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- Accès aux données (DAO pattern)
- Méthodes CRUD héritées de JpaRepository
- Requêtes personnalisées (findByIdClient)

---

### 5. ClientService (Interface Feign)
```java
┌─────────────────────────────────────────────────────────────┐
│                    <<Interface>>                            │
│                    ClientService                            │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @FeignClient(name="SERVICE-CLIENT")                       │
├─────────────────────────────────────────────────────────────┤
│ + clientById(id: Long) : Client                             │
│   [@GetMapping(path="/clients/{id}")]                       │
│   [@PathVariable]                                           │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- Client REST déclaratif pour SERVICE-CLIENT
- Communication inter-microservices
- Résolution automatique via Eureka

---

### 6. VoitureController (REST Controller)
```java
┌─────────────────────────────────────────────────────────────┐
│                   <<RestController>>                        │
│                   VoitureController                         │
├─────────────────────────────────────────────────────────────┤
│ Annotations:                                                │
│ - @RestController                                           │
���─────────────────────────────────────────────────────────────┤
│ - voitureRepository: VoitureRepository  [@Autowired]        │
│ - clientService: ClientService          [@Autowired]        │
├─────────────────────────────────────────────────────────────┤
│ + findAll() : List<Voiture>                                 │
│   [@GetMapping("/voitures")]                                │
│                                                             │
│ + findById(id: Long) : Voiture throws Exception             │
│   [@GetMapping("/voitures/{id}")]                           │
│   [@PathVariable]                                           │
└─────────────────────────────────────────────────────────────┘
```

**Responsabilités :**
- Exposition des endpoints REST
- Orchestration entre Repository et ClientService
- Enrichissement des voitures avec leurs clients

---

## Diagramme de Séquence - GET /voitures

```
Client HTTP    VoitureController    VoitureRepository    ClientService    SERVICE-CLIENT
    │                 │                     │                  │                │
    │ GET /voitures   │                     │                  │                │
    │────────────────▶│                     │                  │                │
    │                 │                     │                  │                │
    │                 │ findAll()           │                  │                │
    │                 │────────────────────▶│                  │                │
    │                 │                     │                  │                │
    │                 │ [Voiture1, Voiture2]│                  │                │
    │                 │◀────────────────────│                  │                │
    │                 │                     │                  │                │
    │                 │ forEach(voiture)    │                  │                │
    │                 │─────┐               │                  │                │
    │                 │     │ clientById(1) │                  │                │
    │                 │     │──────────────────────────────────▶│                │
    │                 │     │               │                  │ GET /clients/1 │
    │                 │     │               │                  │───────────────▶│
    │                 │     │               │                  │                │
    │                 │     │               │                  │ Client{id=1}   │
    │                 │     │               │                  │◀───────────────│
    │                 │     │ Client{id=1}  │                  │                │
    │                 │     │◀──────────────────────────────────│                │
    │                 │     │ setClient()   │                  │                │
    │                 │◀────┘               │                  │                │
    │                 │                     │                  │                │
    │ [Voitures+Clients]                    │                  │                │
    │◀────────────────│                     │                  │                │
    │                 │                     │                  │                │
```

## Relations entre les classes

```
DemoApplication
    │
    ├─── uses ──────▶ VoitureRepository
    ├─── uses ──────▶ ClientService
    │
VoitureController
    │
    ├─── uses ──────▶ VoitureRepository
    ├─── uses ──────▶ ClientService
    └─── returns ───▶ Voiture, List<Voiture>
    │

