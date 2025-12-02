# 🚀 Guide de Démarrage Rapide - SERVICE-VOITURE

## ✅ Compilation réussie !

```
[INFO] BUILD SUCCESS
[INFO] Total time: 8.867 s
```

Le projet compile correctement ! Toutes les dépendances sont présentes et le code est valide.

---

## 📋 Checklist avant de démarrer

Avant de lancer SERVICE-VOITURE, vérifiez que :

- [ ] **Java 17** est installé
  ```powershell
  java -version
  # Doit afficher: java version "17.x.x"
  ```

- [ ] **Eureka Server** est démarré sur le port **8761**
  ```
  http://localhost:8761
  ```

- [ ] **SERVICE-CLIENT** est démarré et enregistré dans Eureka
  ```
  # Vérifiez dans le dashboard Eureka que SERVICE-CLIENT apparaît
  ```

---

## 🎯 Démarrage de SERVICE-VOITURE

### Commande PowerShell (Recommandée)

```powershell
.\mvnw.cmd spring-boot:run
```

### Ou utilisez le script automatique

```powershell
.\start.ps1
```

---

## 📊 Vérification du démarrage

### 1. Logs attendus

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

✅ **Signification** : Les appels Feign vers SERVICE-CLIENT fonctionnent !

### 2. Vérification Eureka

Ouvrir : **http://localhost:8761**

✅ Vérifier que **SERVICE-VOITURE** apparaît dans "Instances currently registered with Eureka"

### 3. Test de l'API

#### Test 1 : Récupérer toutes les voitures

```powershell
# PowerShell
Invoke-WebRequest -Uri http://localhost:8089/voitures | Select-Object -ExpandProperty Content
```

Ou avec curl (si installé) :
```bash
curl http://localhost:8089/voitures
```

Ou simplement dans le navigateur :
```
http://localhost:8089/voitures
```

**Réponse attendue** :
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

#### Test 2 : Récupérer une voiture spécifique

```
http://localhost:8089/voitures/1
```

**Réponse attendue** :
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

---

## ❌ Problèmes courants

### Problème 1 : "Connection refused" lors de l'appel Feign

**Cause** : SERVICE-CLIENT n'est pas démarré

**Solution** :
1. Démarrer SERVICE-CLIENT
2. Attendre qu'il s'enregistre dans Eureka (environ 30 secondes)
3. Redémarrer SERVICE-VOITURE

### Problème 2 : Port 8089 déjà utilisé

**Erreur** :
```
Port 8089 was already in use
```

**Solution** :
```powershell
# Trouver le processus qui utilise le port 8089
netstat -ano | findstr :8089

# Tuer le processus (remplacer PID par le numéro affiché)
taskkill /PID <PID> /F
```

### Problème 3 : Service non visible dans Eureka

**Cause** : Eureka Server non démarré ou configuration incorrecte

**Solution** :
1. Vérifier qu'Eureka Server est accessible : http://localhost:8761
2. Vérifier `application.properties` :
   ```properties
   spring.cloud.discovery.enabled=true
   eureka.instance.hostname=localhost
   ```
3. Attendre 30 secondes (temps d'enregistrement)

### Problème 4 : mvnw n'est pas reconnu (PowerShell)

**Erreur** :
```
mvnw : Le terme «mvnw» n'est pas reconnu...
```

**Solution** : Ajouter `.\` devant la commande
```powershell
# ❌ Incorrect
mvnw spring-boot:run

# ✅ Correct
.\mvnw.cmd spring-boot:run
```

---

## 🎓 Architecture de test complète

```
┌───────────────────────────────────────────────��─────┐
│                  Navigateur Web                     │
│            http://localhost:8089/voitures           │
└──────────────────────┬──────────────────────────────┘
                       │
                       │ HTTP GET
                       ▼
┌─────────────────────────────────────────────────────┐
│              SERVICE-VOITURE (Port 8089)            │
│  ┌─────────────────────────────────────────────┐   │
│  │         VoitureController                   │   │
│  │  GET /voitures → findAll()                  │   │
│  └────────┬────────────────────────────┬───────┘   │
│           │                            │            │
│           ▼                            ▼            │
│  ┌────────────────┐          ┌─────────────────┐   │
│  │ VoitureRepo    │          │ ClientService   │   │
│  │ findAll()      │          │ (Feign Client)  │   │
│  └────────────────┘          └────────┬────────┘   │
│           │                           │             │
│           │                           │ Feign Call  │
│           ▼                           │             │
│  ┌────────────────┐                  │             │
│  │  H2 Database   │                  │             │
│  │  VOITURE Table │                  │             │
│  └────────────────┘                  │             │
└───────────────────────────────────────┼─────────────┘
                                        │
                                        │ via Eureka
                                        ▼
                      ┌─────────────────────────────┐
                      │    Eureka Server (8761)     │
                      │  SERVICE-CLIENT → URL       │
                      └──────────────┬──────────────┘
                                     │
                                     │ Resolved URL
                                     ▼
                      ┌─────────────────────────────┐
                      │  SERVICE-CLIENT             │
                      │  GET /clients/{id}          │
                      │  Returns: Client object     │
                      └─────────────────────────────┘
```

---

## 📝 Commandes utiles

### Démarrer le service
```powershell
.\mvnw.cmd spring-boot:run
```

### Compiler sans démarrer
```powershell
.\mvnw.cmd clean compile
```

### Empaqueter en JAR
```powershell
.\mvnw.cmd clean package
```

### Exécuter le JAR
```powershell
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

### Nettoyer le projet
```powershell
.\mvnw.cmd clean
```

---

## 🔍 Logs importants à surveiller

### ✅ Démarrage réussi
```
Started DemoApplication in X.XXX seconds
```

### ✅ Enregistrement Eureka réussi
```
DiscoveryClient_SERVICE-VOITURE - registration status: 204
```

### ✅ Feign configuré
```
Loaded 1 Feign clients
```

### ✅ Données initialisées
```
**************************
Id est :1
Nom est :Mouna
**************************
```

---

## 🎯 Prochaines étapes

Une fois SERVICE-VOITURE démarré avec succès :

1. ✅ **Tester l'API REST**
   - http://localhost:8089/voitures
   - http://localhost:8089/voitures/1

2. ✅ **Vérifier la communication Feign**
   - Vérifier que les clients sont bien chargés
   - Vérifier les logs pour les appels Feign

3. ✅ **Explorer la console H2** (optionnel)
   - Ajouter dans `application.properties` :
     ```properties
     spring.h2.console.enabled=true
     ```
   - Accéder à : http://localhost:8089/h2-console

4. 🔜 **Ajouter une Gateway API** (optionnel)
   - Pour centraliser les appels

5. 🔜 **Ajouter Circuit Breaker** (optionnel)
   - Pour améliorer la résilience

---

## 📞 Support

**Documentation complète** :
- `README.md` - Vue d'ensemble
- `ARCHITECTURE_FEIGN.md` - Détails OpenFeign
- `DIAGRAMME_CLASSES.md` - Diagrammes UML
- `DEMARRAGE.md` - Instructions complètes

**En cas de problème** :
1. Vérifier les logs
2. Vérifier Eureka Dashboard
3. Vérifier que tous les services sont démarrés dans le bon ordre

---

## ✅ Résumé

Le microservice **SERVICE-VOITURE** est :

✅ **Compilé avec succès** (BUILD SUCCESS)  
✅ **Configuré correctement** (toutes les dépendances présentes)  
✅ **Prêt à démarrer**  

**Commande pour démarrer** :
```powershell
.\mvnw.cmd spring-boot:run
```

**Bonne chance !** 🚀

