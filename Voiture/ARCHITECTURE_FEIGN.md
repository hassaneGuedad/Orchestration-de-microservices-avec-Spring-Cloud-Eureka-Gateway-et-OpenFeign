# Architecture OpenFeign - Communication Inter-Microservices

## Vue d'ensemble

OpenFeign est un client REST déclaratif qui simplifie la communication entre microservices. Dans notre architecture, SERVICE-VOITURE utilise Feign pour communiquer avec SERVICE-CLIENT.

## Flux de Communication

```
┌─────────────────────────────────────────────────────────────────┐
│                     ARCHITECTURE GLOBALE                        │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│              │       │              │       │              │
│  Client HTTP │──────▶│   SERVICE-   │──────▶│   SERVICE-   │
│ (Navigateur) │       │   VOITURE    │ Feign │    CLIENT    │
│              │       │  (Port 8089) │       │              │
└──────────────┘       └──────┬───────┘       └──────────────┘
                              │
                              │
                              ▼
                       ┌──────────────┐
                       │              │
                       │    Eureka    │
                       │    Server    │
                       │  (Port 8761) │
                       │              │
                       └──────────────┘
```

## Détail du Flux d'Appel Feign

```
1. CLIENT HTTP                    2. VOITURE CONTROLLER
   │                                 │
   │ GET /voitures                   │
   │────────────────────────────────▶│
                                     │
                                     │ findAll()
                                     │
                                     ▼
                              3. VOITURE REPOSITORY
                                     │
                                     │ findAll()
                                     │ [Voiture{id_client=1}, ...]
                                     │
                                     ▼
                              4. FEIGN CLIENT
                                     │
                                     │ clientById(1L)
                                     │
                                     │ ┌─────────────────────────────┐
                                     │ │ 1. Résolution via Eureka    │
                                     │ │    SERVICE-CLIENT → URL     │
                                     │ └─────────────────────────────┘
                                     │
                                     │ ┌─────────────────────────────┐
                                     │ │ 2. Appel HTTP REST          │
                                     │ │    GET /clients/1           │
                                     │ └─────────────────────────────┘
                                     │
                                     ▼
                              5. SERVICE-CLIENT
                                     │
                                     │ Retourne Client{id=1, nom="Mouna", age=20}
                                     │
                                     ▼
                              6. VOITURE CONTROLLER
                                     │
                                     │ setClient(client)
                                     │
                                     ▼
   │◀────────────────────────────────│
   │ Response JSON                   │
   │ [Voiture avec Client]           │
   │                                 │
```

## Code détaillé

### 1. Interface ClientService (Feign)

```java
@FeignClient(name="SERVICE-CLIENT")
public interface ClientService {
    @GetMapping(path="/clients/{id}")
    Client clientById(@PathVariable Long id);
}
```

**Explication :**
- `@FeignClient(name="SERVICE-CLIENT")` : Indique à Feign de chercher le service "SERVICE-CLIENT" dans Eureka
- `@GetMapping` : Définit l'endpoint à appeler sur le service distant
- `@PathVariable` : Mappe le paramètre de méthode à la variable d'URL

### 2. VoitureController - Utilisation de Feign

```java
@RestController
public class VoitureController {
    
    @Autowired
    private VoitureRepository voitureRepository;
    
    @Autowired
    private ClientService clientService;  // Injection de l'interface Feign
    
    @GetMapping("/voitures")
    public List<Voiture> findAll() {
        List<Voiture> voitures = voitureRepository.findAll();
        
        // Pour chaque voiture, récupération du client via Feign
        voitures.forEach(voiture -> {
            voiture.setClient(clientService.clientById(voiture.getId_client()));
        });
        
        return voitures;
    }
}
```

### 3. Configuration nécessaire

**DemoApplication.java**
```java
@SpringBootApplication
@EnableFeignClients  // Active le scan des interfaces Feign
public class DemoApplication {
    // ...
}
```

**application.properties**
```properties
spring.cloud.discovery.enabled=true  # Active la découverte Eureka
eureka.instance.hostname=localhost
```

## Avantages d'OpenFeign

1. **Déclaratif** : Pas besoin d'écrire du code pour l'appel HTTP
2. **Load Balancing** : Intégration automatique avec Ribbon (load balancer)
3. **Service Discovery** : Résolution automatique via Eureka
4. **Annotation Spring** : Utilise les annotations familières (@GetMapping, @PostMapping, etc.)
5. **Gestion d'erreurs** : Possibilité d'ajouter des fallbacks

## Comparaison avec RestTemplate

### Avec RestTemplate (Ancienne méthode)
```java
@Autowired
private RestTemplate restTemplate;

public Client getClient(Long id) {
    String url = "http://SERVICE-CLIENT/clients/" + id;
    return restTemplate.getForObject(url, Client.class);
}
```

### Avec Feign (Moderne)
```java
@Autowired
private ClientService clientService;

public Client getClient(Long id) {
    return clientService.clientById(id);
}
```

## Gestion d'erreurs avec Feign (Optionnel)

Pour ajouter un fallback en cas d'erreur :

```java
@Component
public class ClientServiceFallback implements ClientService {
    
    @Override
    public Client clientById(Long id) {
        // Retourne un client par défaut en cas d'erreur
        return new Client(id, "Client Inconnu", 0.0f);
    }
}
```

```java
@FeignClient(name="SERVICE-CLIENT", fallback=ClientServiceFallback.class)
public interface ClientService {
    @GetMapping(path="/clients/{id}")
    Client clientById(@PathVariable Long id);
}
```

## Logs Feign (Debug)

Pour activer les logs Feign, ajouter dans `application.properties` :

```properties
logging.level.com.example.demo.ClientService=DEBUG
```

## Points importants

1. **@Transient** : Le champ `client` dans Voiture est `@Transient` car il n'est pas stocké en base
2. **Eager Loading** : Le client est chargé à chaque requête (pas de cache par défaut)
3. **Performance** : Faire attention aux appels multiples (N+1 problem)
4. **Timeout** : Configurer les timeouts Feign si nécessaire

```properties
feign.client.config.default.connectTimeout=5000
feign.client.config.default.readTimeout=5000
```

