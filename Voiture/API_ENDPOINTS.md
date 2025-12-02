# 🚀 API REST - SERVICE-VOITURE

## 📋 Endpoints disponibles

### 1️⃣ GET /voitures - Récupérer toutes les voitures

**URL** : `http://localhost:8089/voitures`

**Méthode** : `GET`

**Réponse** : `200 OK`

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

**Test PowerShell** :
```powershell
Invoke-WebRequest http://localhost:8089/voitures | Select-Object -ExpandProperty Content
```

**Test curl** :
```bash
curl http://localhost:8089/voitures
```

---

### 2️⃣ GET /voitures/{id} - Récupérer une voiture par ID

**URL** : `http://localhost:8089/voitures/1`

**Méthode** : `GET`

**Réponse** : `200 OK`

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

**Erreur** : `404 NOT FOUND` si la voiture n'existe pas

```json
"Voiture not found with ID: 999"
```

**Test PowerShell** :
```powershell
Invoke-WebRequest http://localhost:8089/voitures/1
```

---

### 3️⃣ GET /voitures/client/{id} - Récupérer les voitures d'un client

**URL** : `http://localhost:8089/voitures/client/1`

**Méthode** : `GET`

**Réponse** : `200 OK`

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
  }
]
```

**Erreur** : `404 NOT FOUND` si le client n'existe pas

**Test PowerShell** :
```powershell
Invoke-WebRequest http://localhost:8089/voitures/client/1
```

---

### 4️⃣ POST /voitures/{clientId} - Créer une nouvelle voiture

**URL** : `http://localhost:8089/voitures/1`

**Méthode** : `POST`

**Headers** :
- `Content-Type: application/json`

**Body** :
```json
{
  "marque": "BMW",
  "matricule": "C 77 888",
  "model": "X5"
}
```

**Réponse** : `200 OK`

```json
{
  "id": 4,
  "marque": "BMW",
  "matricule": "C 77 888",
  "model": "X5",
  "id_client": 1,
  "client": {
    "id": 1,
    "nom": "Mouna",
    "age": 20.0
  }
}
```

**Erreur** : `404 NOT FOUND` si le client n'existe pas

```json
"Client not found with ID: 999"
```

**Test PowerShell** :
```powershell
$body = @{
    marque = "BMW"
    matricule = "C 77 888"
    model = "X5"
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8089/voitures/1 `
    -Method POST `
    -Body $body `
    -ContentType "application/json"
```

**Test curl** :
```bash
curl -X POST http://localhost:8089/voitures/1 \
  -H "Content-Type: application/json" \
  -d '{"marque":"BMW","matricule":"C 77 888","model":"X5"}'
```

---

### 5️⃣ PUT /voitures/{id} - Modifier une voiture

**URL** : `http://localhost:8089/voitures/1`

**Méthode** : `PUT`

**Headers** :
- `Content-Type: application/json`

**Body** (tous les champs sont optionnels) :
```json
{
  "marque": "Toyota",
  "matricule": "A 99 999",
  "model": "Corolla 2024"
}
```

**Réponse** : `200 OK`

```json
{
  "id": 1,
  "marque": "Toyota",
  "matricule": "A 99 999",
  "model": "Corolla 2024",
  "id_client": 1,
  "client": {
    "id": 1,
    "nom": "Mouna",
    "age": 20.0
  }
}
```

**Erreur** : `404 NOT FOUND` si la voiture n'existe pas

**Test PowerShell** :
```powershell
$body = @{
    matricule = "A 99 999"
    model = "Corolla 2024"
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8089/voitures/1 `
    -Method PUT `
    -Body $body `
    -ContentType "application/json"
```

**Test curl** :
```bash
curl -X PUT http://localhost:8089/voitures/1 \
  -H "Content-Type: application/json" \
  -d '{"matricule":"A 99 999","model":"Corolla 2024"}'
```

---

### 6️⃣ DELETE /voitures/{id} - Supprimer une voiture

**URL** : `http://localhost:8089/voitures/1`

**Méthode** : `DELETE`

**Réponse** : `200 OK`

```json
"Voiture supprimée avec succès"
```

**Erreur** : `404 NOT FOUND` si la voiture n'existe pas

```json
"Voiture not found with ID: 999"
```

**Test PowerShell** :
```powershell
Invoke-WebRequest -Uri http://localhost:8089/voitures/1 -Method DELETE
```

**Test curl** :
```bash
curl -X DELETE http://localhost:8089/voitures/1
```

---

## 🧪 Tests complets avec Postman

### Collection Postman

Créez une collection avec ces requêtes :

1. **GET All Voitures**
   - URL: `http://localhost:8089/voitures`
   - Method: GET

2. **GET Voiture by ID**
   - URL: `http://localhost:8089/voitures/1`
   - Method: GET

3. **GET Voitures by Client**
   - URL: `http://localhost:8089/voitures/client/1`
   - Method: GET

4. **POST Create Voiture**
   - URL: `http://localhost:8089/voitures/1`
   - Method: POST
   - Body (raw JSON):
     ```json
     {
       "marque": "Audi",
       "matricule": "D 12 345",
       "model": "A4"
     }
     ```

5. **PUT Update Voiture**
   - URL: `http://localhost:8089/voitures/1`
   - Method: PUT
   - Body (raw JSON):
     ```json
     {
       "matricule": "A 11 111"
     }
     ```

6. **DELETE Voiture**
   - URL: `http://localhost:8089/voitures/4`
   - Method: DELETE

---

## 🔍 Codes de statut HTTP

| Code | Signification | Quand |
|------|---------------|-------|
| 200 | OK | Succès de l'opération |
| 404 | NOT FOUND | Voiture ou Client introuvable |
| 500 | INTERNAL SERVER ERROR | Erreur serveur |

---

## 📊 Scénarios de test

### Scénario 1 : CRUD complet

```powershell
# 1. Lister toutes les voitures
Invoke-WebRequest http://localhost:8089/voitures

# 2. Créer une nouvelle voiture
$body = '{"marque":"Mercedes","matricule":"E 55 555","model":"Classe C"}' 
Invoke-WebRequest -Uri http://localhost:8089/voitures/1 -Method POST -Body $body -ContentType "application/json"

# 3. Récupérer la voiture créée (supposons ID 4)
Invoke-WebRequest http://localhost:8089/voitures/4

# 4. Modifier la voiture
$body = '{"matricule":"E 99 999"}'
Invoke-WebRequest -Uri http://localhost:8089/voitures/4 -Method PUT -Body $body -ContentType "application/json"

# 5. Supprimer la voiture
Invoke-WebRequest -Uri http://localhost:8089/voitures/4 -Method DELETE
```

### Scénario 2 : Gestion des erreurs

```powershell
# Essayer de récupérer une voiture inexistante
Invoke-WebRequest http://localhost:8089/voitures/999
# Résultat: 404 NOT FOUND

# Essayer de créer une voiture pour un client inexistant
$body = '{"marque":"Test","matricule":"T 11 111","model":"Test"}'
Invoke-WebRequest -Uri http://localhost:8089/voitures/999 -Method POST -Body $body -ContentType "application/json"
# Résultat: 404 NOT FOUND - Client not found
```

---

## 🌐 Accès via Gateway (Étape 9)

Une fois la Gateway configurée, les endpoints seront aussi accessibles via :

### Route statique
```
http://localhost:8888/voitures
http://localhost:8888/voitures/1
http://localhost:8888/voitures/client/1
```

### Route dynamique (via Eureka)
```
http://localhost:8888/SERVICE-VOITURE/voitures
http://localhost:8888/SERVICE-VOITURE/voitures/1
http://localhost:8888/SERVICE-VOITURE/voitures/client/1
```

---

## ✅ Vérifications finales (Étape 10)

### 1. Eureka Dashboard
```
http://localhost:8761
```
✅ Vérifier que **SERVICE-VOITURE** est enregistré

### 2. Microservice direct
```
http://localhost:8089/voitures
http://localhost:8089/voitures/1
http://localhost:8089/voitures/client/1
```

### 3. Via Gateway (après configuration)
```
http://localhost:8888/voitures
http://localhost:8888/SERVICE-VOITURE/voitures
```

---

## 📝 Notes importantes

1. **Champ id_client** : Le code utilise `id_client` (avec underscore) comme dans votre entité
2. **Gestion des erreurs** : Tous les endpoints gèrent les cas d'erreur (404, 500)
3. **Client null** : Si le client n'existe pas dans SERVICE-CLIENT, le champ `client` sera `null` mais la voiture sera quand même retournée
4. **ResponseEntity** : Tous les endpoints retournent `ResponseEntity<Object>` pour une meilleure gestion des erreurs

---

## 🚀 Démarrage et test

```powershell
# 1. Démarrer le service
.\mvnw.cmd spring-boot:run

# 2. Tester l'API
Invoke-WebRequest http://localhost:8089/voitures

# 3. Voir les logs
# Les logs montrent si les clients sont trouvés ou non
```

---

**Votre API REST est maintenant complète avec tous les endpoints CRUD ! 🎉**

