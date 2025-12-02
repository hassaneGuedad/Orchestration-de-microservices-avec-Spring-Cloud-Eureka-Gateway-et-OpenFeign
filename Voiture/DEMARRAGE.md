# Instructions de Démarrage - SERVICE-VOITURE

## Prérequis

Avant de démarrer SERVICE-VOITURE, assurez-vous que les services suivants sont démarrés :

### 1. Eureka Server (Port 8761)
Le serveur Eureka doit être lancé en premier pour permettre la découverte de services.

**Vérification** : http://localhost:8761

### 2. SERVICE-CLIENT
Le microservice CLIENT doit être démarré car SERVICE-VOITURE communique avec lui via Feign.

**Vérification** : http://localhost:{PORT_CLIENT}/clients

## Démarrage de SERVICE-VOITURE

### Option 1 : Script automatique (CMD)
```cmd
start.cmd
```

### Option 2 : Script automatique (PowerShell)
```powershell
.\start.ps1
```

### Option 3 : Commande Maven manuelle (CMD)
```cmd
.\mvnw.cmd spring-boot:run
```

### Option 4 : Commande Maven manuelle (PowerShell)
```powershell
.\mvnw.cmd spring-boot:run
```

### Option 3 : Via IDE
1. Ouvrir le projet dans votre IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Exécuter la classe `DemoApplication.java`

## Ordre de démarrage complet

```
1. Eureka Server (Port 8761)
   ↓
2. SERVICE-CLIENT
   ↓
3. SERVICE-VOITURE (Port 8089)
```

## Vérification du démarrage

### 1. Logs de console
Vous devriez voir dans les logs :

```
**************************
Id est :1
Nom est :Mouna
**************************
**************************
Id est :2
Nom est :Imane
Age est :24.0
**************************
```

Cela confirme que :
- ✅ La connexion avec SERVICE-CLIENT fonctionne
- ✅ Les appels Feign sont opérationnels
- ✅ Les données sont initialisées

### 2. Eureka Dashboard
Ouvrir : http://localhost:8761

Vérifier que **SERVICE-VOITURE** apparaît dans la liste des services enregistrés.

### 3. Test de l'API

#### Récupérer toutes les voitures
```
GET http://localhost:8089/voitures
```

**Réponse attendue :**
```json
[
  {
    "id": 1,
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

#### Récupérer une voiture spécifique
```
GET http://localhost:8089/voitures/1
```

**Réponse attendue :**
```json
{
  "id": 1,
  "marque": "Toyota",
  "matricule": "A 25 333",
  "model": "Corolla",
  "id_client": 1,
  "client": {
    "id": 1,
    "nom": "Mouna",
    "age": 20.0
  }
}
```

## Problèmes courants

### Problème 1 : Erreur de connexion Feign
**Symptôme :** 
```
feign.RetryableException: Connection refused
```

**Solution :**
- Vérifier que SERVICE-CLIENT est démarré
- Vérifier que SERVICE-CLIENT est enregistré dans Eureka

### Problème 2 : Service non enregistré dans Eureka
**Symptôme :** Le service n'apparaît pas dans le dashboard Eureka

**Solution :**
- Vérifier que Eureka Server est démarré
- Vérifier `application.properties` :
  ```properties
  spring.cloud.discovery.enabled=true
  eureka.instance.hostname=localhost
  ```

### Problème 3 : Port 8089 déjà utilisé
**Symptôme :**
```
Port 8089 was already in use
```

**Solution :**
- Changer le port dans `application.properties`
- Ou arrêter l'application qui utilise le port 8089

### Problème 4 : Données non initialisées
**Symptôme :** Appel à `/voitures` retourne une liste vide

**Solution :**
- Vérifier que le `CommandLineRunner` s'est exécuté (voir logs)
- Vérifier que SERVICE-CLIENT retourne bien les clients avec les IDs 1 et 2

## Base de données H2

### Console H2 (Optionnel)

Pour activer la console H2, ajouter dans `application.properties` :

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:voituredb
```

Accès : http://localhost:8089/h2-console

**Connexion :**
- JDBC URL: `jdbc:h2:mem:voituredb`
- Username: `sa`
- Password: (vide)

### Requêtes SQL utiles

```sql
-- Voir toutes les voitures
SELECT * FROM VOITURE;

-- Compter les voitures
SELECT COUNT(*) FROM VOITURE;

-- Voitures par client
SELECT * FROM VOITURE WHERE ID_CLIENT = 1;
```

## Arrêt du service

Pour arrêter le service :
- Dans la console : Appuyer sur `Ctrl + C`
- Via IDE : Utiliser le bouton Stop

## Logs et Debug

Pour activer les logs détaillés, ajouter dans `application.properties` :

```properties
# Logs Spring
logging.level.org.springframework=DEBUG

# Logs Feign
logging.level.com.example.demo.ClientService=DEBUG

# Logs Hibernate (SQL)
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## Tests avec Postman ou curl

### curl - Toutes les voitures
```bash
curl http://localhost:8089/voitures
```

### curl - Voiture spécifique
```bash
curl http://localhost:8089/voitures/1
```

### Postman
Importer la collection depuis le fichier `postman_collection.json` (à créer si nécessaire)

## Prochaines étapes

Après avoir vérifié que SERVICE-VOITURE fonctionne :

1. ✅ Tester la communication Feign
2. ✅ Vérifier l'enregistrement Eureka
3. ✅ Valider les endpoints REST
4. 🔜 Ajouter une Gateway API (optionnel)
5. 🔜 Ajouter Circuit Breaker avec Resilience4j (optionnel)
6. 🔜 Ajouter des tests unitaires et d'intégration

