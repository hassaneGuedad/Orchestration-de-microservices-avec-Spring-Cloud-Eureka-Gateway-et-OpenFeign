2️⃣ SERVICE-CLIENT (8088)
   └─ Attend l'enregistrement dans Eureka

3️⃣ SERVICE-VOITURE (8089)
   └─ Attend l'enregistrement dans Eureka

4️⃣ Gateway (8888)
   └─ Attend l'enregistrement dans Eureka
   └─ Découvre SERVICE-CLIENT et SERVICE-VOITURE

5️⃣ Tester via Gateway
   └─ http://localhost:8888/voitures
```

---

## ✅ Checklist finale

- [ ] Eureka Server démarré et accessible
- [ ] SERVICE-CLIENT enregistré dans Eureka
- [ ] SERVICE-VOITURE enregistré dans Eureka
- [ ] Gateway démarré et enregistré dans Eureka
- [ ] Test direct SERVICE-CLIENT : `http://localhost:8088/clients` ✅
- [ ] Test direct SERVICE-VOITURE : `http://localhost:8089/voitures` ✅
- [ ] Test Gateway route courte : `http://localhost:8888/clients` ✅
- [ ] Test Gateway route courte : `http://localhost:8888/voitures` ✅
- [ ] Test Gateway route dynamique : `http://localhost:8888/service-client/clients` ✅
- [ ] Test Gateway route dynamique : `http://localhost:8888/service-voiture/voitures` ✅

---

## 🎉 Résultat final

Vous avez maintenant une **architecture microservices complète** :

✅ **Eureka** : Découverte de services  
✅ **SERVICE-CLIENT** : Gestion des clients  
✅ **SERVICE-VOITURE** : Gestion des voitures  
✅ **Gateway** : Point d'entrée unique  
✅ **Communication Feign** : Entre SERVICE-VOITURE et SERVICE-CLIENT  
✅ **Load Balancing** : Automatique via Eureka  

**Votre architecture microservices est opérationnelle ! 🚀**
# 🌐 ÉTAPE 9 - Configuration Gateway pour SERVICE-VOITURE

## 📋 Vue d'ensemble

La **Spring Cloud Gateway** permet de centraliser l'accès à tous vos microservices via un point d'entrée unique.

```
Client HTTP
    │
    ▼
┌─────────────┐
│   Gateway   │  Port 8888
│             │
└──────┬──────┘
       │
       ├─────────────┬─────────────┐
       │             │             │
┌──────▼──────┐ ┌───▼─────────┐ ┌─▼────────────┐
│  SERVICE-   │ │  SERVICE-   │ │   Autres     │
│  CLIENT     │ │  VOITURE    │ │   services   │
│  8088       │ │  8089       │ │              │
└─────────────┘ └─────────────┘ └──────────────┘
```

---

## 🔧 Option 1 : Configuration statique (application.yml)

### Fichier application.yml de la Gateway

```yaml
server:
  port: 8888

spring:
  application:
    name: GATEWAY-SERVICE
  cloud:
    gateway:
      routes:
        # Route pour SERVICE-CLIENT
        - id: client-service
          uri: http://localhost:8088
          predicates:
            - Path=/clients/**
        
        # Route pour SERVICE-VOITURE
        - id: voiture-service
          uri: http://localhost:8089
          predicates:
            - Path=/voitures/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Tests avec configuration statique

```powershell
# Clients
Invoke-WebRequest http://localhost:8888/clients
Invoke-WebRequest http://localhost:8888/clients/1

# Voitures
Invoke-WebRequest http://localhost:8888/voitures
Invoke-WebRequest http://localhost:8888/voitures/1
Invoke-WebRequest http://localhost:8888/voitures/client/1
```

---

## 🔧 Option 2 : Configuration programmatique (via Bean RouteLocator)

### Classe de configuration Java

```java
package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route pour SERVICE-CLIENT
                .route("client-service", r -> r
                        .path("/clients/**")
                        .uri("lb://SERVICE-CLIENT"))
                
                // Route pour SERVICE-VOITURE
                .route("voiture-service", r -> r
                        .path("/voitures/**")
                        .uri("lb://SERVICE-VOITURE"))
                
                .build();
    }
}
```

**Note** : `lb://SERVICE-CLIENT` utilise le **Load Balancer** d'Eureka

### Tests avec configuration programmatique

```powershell
# Même chose que l'option 1
Invoke-WebRequest http://localhost:8888/clients
Invoke-WebRequest http://localhost:8888/voitures
```

---

## 🔧 Option 3 : Découverte automatique via Eureka (Dynamique)

### Configuration application.yml

```yaml
server:
  port: 8888

spring:
  application:
    name: GATEWAY-SERVICE
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Tests avec découverte automatique

**Format** : `http://localhost:8888/{SERVICE-NAME}/{endpoint}`

```powershell
# SERVICE-CLIENT
Invoke-WebRequest http://localhost:8888/service-client/clients
Invoke-WebRequest http://localhost:8888/service-client/clients/1

# SERVICE-VOITURE
Invoke-WebRequest http://localhost:8888/service-voiture/voitures
Invoke-WebRequest http://localhost:8888/service-voiture/voitures/1
Invoke-WebRequest http://localhost:8888/service-voiture/voitures/client/1
```

**Avec majuscules (si lower-case-service-id = false)** :
```powershell
Invoke-WebRequest http://localhost:8888/SERVICE-CLIENT/clients
Invoke-WebRequest http://localhost:8888/SERVICE-VOITURE/voitures
```

---

## 🎯 Recommandation : Configuration Hybride

Combinez les approches pour plus de flexibilité :

### application.yml

```yaml
server:
  port: 8888

spring:
  application:
    name: GATEWAY-SERVICE
  cloud:
    gateway:
      # Découverte automatique activée
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      
      # Routes personnalisées pour URLs courtes
      routes:
        # Route courte pour SERVICE-CLIENT
        - id: client-service-short
          uri: lb://SERVICE-CLIENT
          predicates:
            - Path=/clients/**
        
        # Route courte pour SERVICE-VOITURE
        - id: voiture-service-short
          uri: lb://SERVICE-VOITURE
          predicates:
            - Path=/voitures/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Avantages

✅ **URLs courtes** : `http://localhost:8888/voitures`  
✅ **URLs avec nom de service** : `http://localhost:8888/service-voiture/voitures`  
✅ **Découverte automatique** : Nouveaux services accessibles automatiquement  

---

## 🚀 Création du projet Gateway

### 1. Créer un nouveau projet Spring Boot

**Dependencies** :
- Spring Cloud Gateway
- Eureka Discovery Client

### 2. pom.xml

```xml
<dependencies>
    <!-- Spring Cloud Gateway -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2023.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3. Classe principale

```java
package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

---

## 🧪 Tests complets (Étape 10)

### 1. Vérifier Eureka

```
http://localhost:8761
```

**Vérifier la présence de** :
- ✅ GATEWAY-SERVICE
- ✅ SERVICE-CLIENT
- ✅ SERVICE-VOITURE

### 2. Tester via Gateway (routes statiques)

```powershell
# SERVICE-CLIENT
Invoke-WebRequest http://localhost:8888/clients
Invoke-WebRequest http://localhost:8888/clients/1

# SERVICE-VOITURE
Invoke-WebRequest http://localhost:8888/voitures
Invoke-WebRequest http://localhost:8888/voitures/1
Invoke-WebRequest http://localhost:8888/voitures/client/1
```

### 3. Tester via Gateway (routes dynamiques)

```powershell
# SERVICE-CLIENT
Invoke-WebRequest http://localhost:8888/service-client/clients
Invoke-WebRequest http://localhost:8888/service-client/clients/1

# SERVICE-VOITURE
Invoke-WebRequest http://localhost:8888/service-voiture/voitures
Invoke-WebRequest http://localhost:8888/service-voiture/voitures/1
Invoke-WebRequest http://localhost:8888/service-voiture/voitures/client/1
```

### 4. Tester les microservices directement

```powershell
# SERVICE-CLIENT (direct)
Invoke-WebRequest http://localhost:8088/clients
Invoke-WebRequest http://localhost:8088/clients/1

# SERVICE-VOITURE (direct)
Invoke-WebRequest http://localhost:8089/voitures
Invoke-WebRequest http://localhost:8089/voitures/1
Invoke-WebRequest http://localhost:8089/voitures/client/1
```

---

## 📊 Tableau récapitulatif des URLs

| Service | Direct | Gateway (court) | Gateway (dynamique) |
|---------|--------|-----------------|---------------------|
| **Clients** | | | |
| Liste | `http://localhost:8088/clients` | `http://localhost:8888/clients` | `http://localhost:8888/service-client/clients` |
| Par ID | `http://localhost:8088/clients/1` | `http://localhost:8888/clients/1` | `http://localhost:8888/service-client/clients/1` |
| **Voitures** | | | |
| Liste | `http://localhost:8089/voitures` | `http://localhost:8888/voitures` | `http://localhost:8888/service-voiture/voitures` |
| Par ID | `http://localhost:8089/voitures/1` | `http://localhost:8888/voitures/1` | `http://localhost:8888/service-voiture/voitures/1` |
| Par Client | `http://localhost:8089/voitures/client/1` | `http://localhost:8888/voitures/client/1` | `http://localhost:8888/service-voiture/voitures/client/1` |

---

## 🔧 Filtres Gateway (Bonus)

Ajoutez des filtres pour logger les requêtes :

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("voiture-service", r -> r
                    .path("/voitures/**")
                    .filters(f -> f
                            .addRequestHeader("X-Gateway", "true")
                            .addResponseHeader("X-Response-Time", String.valueOf(System.currentTimeMillis()))
                    )
                    .uri("lb://SERVICE-VOITURE"))
            .build();
}
```

---

## 🎯 Ordre de démarrage complet

```
1️⃣ Eureka Server (8761)
   └─ http://localhost:8761


