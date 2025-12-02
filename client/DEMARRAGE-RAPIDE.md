# 🚀 GUIDE DE DÉMARRAGE RAPIDE - MICROSERVICE CLIENT

## ⚡ Démarrage en 3 étapes

### Étape 1 : Vérifier les prérequis ✅
```cmd
java -version
```
→ Doit afficher Java 17 ou supérieur

### Étape 2 : Démarrer le projet 🚀

#### Option A : PowerShell (recommandé) ⭐
```powershell
cd C:\Users\youbitech\Desktop\client
.\mvnw spring-boot:run
```

#### Option B : CMD (Invite de commandes)
```cmd
cd C:\Users\youbitech\Desktop\client
mvnw spring-boot:run
```

#### Option C : Maven (si installé)
```powershell
mvn spring-boot:run
```

### Étape 3 : Vérifier que ça fonctionne ✅

#### A. Tester l'API
Ouvrez votre navigateur :
- **Tous les clients** : http://localhost:8088/clients
- **Client ID 1** : http://localhost:8088/client/1
- **Client ID 2** : http://localhost:8088/client/2
- **Client ID 3** : http://localhost:8088/client/3

#### B. Vérifier Eureka
- **Dashboard Eureka** : http://localhost:8761/
- Cherchez **SERVICE-CLIENT** dans la liste des instances

#### C. Console H2 (base de données)
- **URL** : http://localhost:8088/h2-console
- **JDBC URL** : `jdbc:h2:mem:clientdb`
- **Username** : `sa`
- **Password** : _(laisser vide)_

#### D. Health Check
- **Actuator** : http://localhost:8088/actuator/health

---

## 📊 Résultat attendu

### 1️⃣ GET http://localhost:8088/clients
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

### 2️⃣ GET http://localhost:8088/client/1
```json
{
  "id": 1,
  "nom": "Rabab SELIMANI",
  "age": 23.0
}
### PowerShell (⭐ recommandé)

#### Compiler le projet
```powershell
.\mvnw clean compile
```

#### Créer le JAR
```powershell
.\mvnw clean package
```

#### Exécuter le JAR
```powershell
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

#### Nettoyer le projet
```powershell
.\mvnw clean
```

#### Redémarrer avec modifications
```powershell
.\mvnw spring-boot:run
```
_(Spring DevTools recharge automatiquement)_

---

### CMD (Invite de commandes)

#### Compiler le projet

### 3️⃣ Eureka Dashboard
Vous devriez voir :
```
#### Créer le JAR
SERVICE-CLIENT      n/a (1)     (1)                  UP (1) - SERVICE-CLIENT:8088
```

---
#### Nettoyer le projet
```

### Créer le JAR
```cmd
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

### Nettoyer le projet
```cmd
mvnw clean
```

### Redémarrer avec modifications
```cmd
mvnw spring-boot:run
```
_(Spring DevTools recharge automatiquement)_

---

## 🛠️ Dépannage

### Problème : Port 8088 déjà utilisé
**Solution** : Modifier le port dans `application.properties`
```properties
server.port=8089
```

### Problème : Eureka non accessible
**Solution** : Vérifier qu'Eureka Server est démarré sur port 8761
```cmd
# Vérifier
curl http://localhost:8761
```

### Problème : Base de données vide
**Solution** : Le CommandLineRunner insère automatiquement les données.
Vérifiez les logs :
```
Rabab SELIMANI
Amal RAMI
Samir SAFI
```

### Problème : Erreurs de compilation dans l'IDE
**Solution IntelliJ** :
1. File → Invalidate Caches / Restart
2. Clic droit sur pom.xml → Maven → Reload Project
3. Build → Rebuild Project

---

## 📱 Tester avec curl (Windows CMD)

### Tous les clients
```cmd
curl http://localhost:8088/clients
```

### Client par ID
```cmd
curl http://localhost:8088/client/1
```

### Health check
```cmd
curl http://localhost:8088/actuator/health
```

---

## 🎯 Architecture du projet

```
┌─────────────────────────────────────────┐
│         CLIENT MICROSERVICE             │
│              Port: 8088                 │
└─────────────────────────────────────────┘
                  │
                  │ S'enregistre
                  ▼
┌─────────────────────────────────────────┐
│          EUREKA SERVER                  │
│              Port: 8761                 │
└─────────────────────────────────────────┘
```

### Couches de l'application
```
┌──────────────────┐
│   Controller     │ ← REST API (endpoints)
├──────────────────┤
│    Service       │ ← Logique métier
├──────────────────┤
│   Repository     │ ← Accès données (JPA)
├──────────────────┤
│     Entity       │ ← Modèle de données
└──────────────────┘
         │
         ▼
   ┌─────────┐
   │ H2 (RAM)│
   └─────────┘
```

---

## 📋 Checklist de démarrage

- [ ] Java 17+ installé
- [ ] Maven installé (ou utiliser mvnw)
- [ ] Eureka Server démarré sur 8761
- [ ] Port 8088 libre
- [ ] Projet compilé : `mvnw clean compile`
- [ ] Application démarrée : `mvnw spring-boot:run`
- [ ] Eureka affiche SERVICE-CLIENT
- [ ] http://localhost:8088/clients retourne 3 clients
- [ ] Console H2 accessible

---

## 🎉 C'est prêt !

Votre microservice CLIENT est opérationnel et enregistré dans Eureka !

**Prochaines étapes possibles** :
- Ajouter d'autres microservices
- Implémenter une Gateway API
- Ajouter la communication entre microservices
- Implémenter un Config Server
- Ajouter la sécurité (Spring Security)

**Bon développement ! 🚀**

