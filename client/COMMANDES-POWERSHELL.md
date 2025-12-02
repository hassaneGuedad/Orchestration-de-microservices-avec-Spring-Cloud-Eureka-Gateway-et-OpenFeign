# 🚀 COMMANDES POUR DÉMARRER LE PROJET

## 🔵 Pour PowerShell (recommandé sur Windows)

### Démarrage de l'application
```powershell
cd C:\Users\youbitech\Desktop\client
.\mvnw spring-boot:run
```

### Compilation
```powershell
.\mvnw clean compile
```

### Build complet
```powershell
.\mvnw clean install
```

### Créer le JAR
```powershell
.\mvnw clean package
```

### Exécuter le JAR
```powershell
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

### Nettoyer le projet
```powershell
.\mvnw clean
```

---

## 🟢 Pour CMD (Invite de commandes)

### Démarrage de l'application
```cmd
cd C:\Users\youbitech\Desktop\client
mvnw spring-boot:run
```

### Compilation
```cmd
mvnw clean compile
```

### Build complet
```cmd
mvnw clean install
```

---

## ⚡ SOLUTION RAPIDE - DÉMARRER MAINTENANT

### Dans PowerShell (votre cas actuel) :
```powershell
.\mvnw spring-boot:run
```

### OU utilisez Maven si installé :
```powershell
mvn spring-boot:run
```

---

## 📝 Explication

Dans **PowerShell**, pour exécuter un fichier dans le répertoire courant, vous devez préfixer avec `.\`

- ❌ `mvnw spring-boot:run` → Ne fonctionne PAS dans PowerShell
- ✅ `.\mvnw spring-boot:run` → Fonctionne dans PowerShell
- ✅ `mvnw spring-boot:run` → Fonctionne dans CMD

---

## 🎯 Après le démarrage

Une fois l'application démarrée, testez :

### Dans le navigateur :
- http://localhost:8088/clients
- http://localhost:8088/client/1
- http://localhost:8761 (Eureka)
- http://localhost:8088/h2-console
- http://localhost:8088/actuator/health

### Avec PowerShell :
```powershell
# Test de l'API
Invoke-WebRequest -Uri http://localhost:8088/clients | Select-Object -Expand Content

# OU utilisez curl
curl http://localhost:8088/clients
```

---

## 🛑 Pour arrêter l'application

Appuyez sur `Ctrl + C` dans le terminal PowerShell

---

## ✅ COMMANDE À EXÉCUTER MAINTENANT

Copiez et exécutez ceci dans votre PowerShell :

```powershell
.\mvnw spring-boot:run
```

Vous devriez voir :
- Le téléchargement des dépendances Maven (première fois seulement)
- La compilation du projet
- Le démarrage de Spring Boot
- L'inscription dans Eureka
- Le message : "Started DemoApplication in X seconds"

Bon développement ! 🚀

