# Orchestration de microservices avec Spring Cloud (Eureka, Config, Gateway, OpenFeign)

Résumé
------
Ce dépôt rassemble plusieurs TP et exemples pour comprendre et mettre en œuvre les briques clés de Spring Cloud appliquées aux microservices : découverte de services (Eureka), gestion centralisée de la configuration (Spring Cloud Config), API Gateway (Spring Cloud Gateway) et mécanismes de résilience (circuit breakers, timeouts, retries). Le but est d'appréhender où chaque brique s'intègre, comment elles se complètent et quelles bonnes pratiques adopter.

Objectifs
---------
- Comprendre les apports concrets de Spring Cloud pour les microservices.
- Distinguer les briques : Eureka (découverte), Config (configuration), Gateway (routage), Résilience (circuit breaker).
- Savoir où ces briques s’intègrent et comment elles se complètent.

Caractéristiques principales
----------------------------
Spring Cloud fournit des primitives standardisées pour coordonner un ensemble de microservices :
- Découverte, configuration, routage, résilience et observabilité intégrées.
- Réduction du couplage entre services et standardisation des patterns transverses (sécurité, logs, métriques).
- Auto-configuration et conventions avec Spring Boot pour un démarrage rapide et homogène.

Découverte de services avec Eureka
----------------------------------
- Chaque service s’enregistre dynamiquement auprès d'un serveur Eureka (nom logique, host/port, metadata).
- Les clients (par exemple Feign ou Gateway) peuvent résoudre les instances via le registre pour appeler un service par son nom logique.
- Heartbeats et TTL maintiennent la fraîcheur des instances disponibles dans le registre.
- Conséquence : ajout/suppression d'instances à chaud sans reconfiguration manuelle.

Gestion de configuration (Spring Cloud Config)
----------------------------------------------
- Centralisation des propriétés d'application (souvent sur un dépôt Git) : versionnées et auditables.
- Possibilité de rechargement à chaud des propriétés (refresh) pour éviter redéploiements fréquents.
- Évite duplication et divergences entre environnements (dev/test/prod).
- Recommandation : versionner et restreindre l'accès au dépôt de configuration (Git privé, audit).

Résilience et tolérance aux pannes
----------------------------------
- Patterns courants : timeouts, retries, bulkheads, circuit breakers et fallbacks.
- Hystrix : historique, utile pour compréhension ; Resilience4j : recommandé pour les versions récentes de Spring Cloud.
- Positionner les breakers et fallbacks aux frontières des services (API Gateway / clients) pour éviter la propagation d'échecs.

API Gateway (Spring Cloud Gateway)
----------------------------------
- Point d'entrée unique pour les appels externes.
- Routes statiques (URI directes) ou dynamiques via service discovery (ex : lb://NOM-SERVICE utilisant Eureka).
- Peut gérer : CORS, authentification/autorisation, rate limiting, réécriture d'URL, enrichissement/filtrage des requêtes, logs et métriques.

Intégration Spring Boot
-----------------------
- Auto-configuration : conventions pour réduire la configuration manuelle.
- `spring-boot-starter` et starters Cloud rendent l'intégration fluide (Feign, Gateway, Actuator, Security).
- Spring Boot Actuator expose health, metrics, info, readiness/liveness — indispensables pour l'observabilité et l'orchestration.

Avantages clés
--------------
- Simplicité de développement : abstractions qui masquent la complexité distribuée.
- Scalabilité et évolutivité : chaque service scalera indépendamment.
- Gestion centralisée : configurations partagées, observabilité consolidée.
- Intégration transparente avec l'écosystème Spring (Data, Security, Cloud, Feign).

Bonnes pratiques et points d'attention
-------------------------------------
- Noms logiques stables : définir `spring.application.name` unique par service.
- Timeouts obligatoires : pour Feign, WebClient, Gateway — éviter blocages et files d'attente.
- Éviter la propagation d'échecs : combiner circuit breakers + fallbacks.
- Versionner et sécuriser le dépôt de configuration (Git privé, clés d'accès restreintes).
- Observabilité : activer Actuator sur tous les services et ajouter traces distribuées (Sleuth/Zipkin ou OpenTelemetry).
- Monitoring : métriques et alerting pour détecter latence et erreurs (Prometheus/Grafana recommandés).

Résultats attendus
------------------
Après étude et mise en pratique avec les exemples de ce dépôt :
- Vous savez à quoi servent Eureka, Gateway, Config et les mécanismes de résilience.
- Vous comprenez comment les services se découvrent et se routent.
- Vous êtes prêts à configurer concrètement un serveur Eureka et à y enregistrer des clients (Étape suivante).

Où regarder dans ce dépôt
-------------------------
- Exemples et TP client : `client/`
- Exemple démo : `demo/`
- Gateway et configuration de routage : `Gateway_/` (contient `app.yml` et exemples de routes)
- Exemple d'un service métier complet : `Voiture/`

Étape suivante recommandée
--------------------------
1. Démarrer le serveur Eureka (regarder le dossier `demo/` ou `Voiture/` si un serveur y est présent).
2. Lancer un service client et vérifier qu'il s'enregistre (contrôler l'interface d'Eureka ou les logs).
3. Tester un appel via `Gateway_` en utilisant une route de type `lb://NOM-SERVICE` pour valider le routage dynamique.

Ressources et lectures conseillées
---------------------------------
- Documentation Spring Cloud : https://spring.io/projects/spring-cloud
- Resilience4j : https://resilience4j.readme.io/
- Spring Cloud Gateway : https://spring.io/projects/spring-cloud-gateway
- Spring Cloud Config : https://spring.io/projects/spring-cloud-config

Screenshots
-------
<img width="955" height="505" alt="cap1" src="https://github.com/user-attachments/assets/990666a7-7c6b-4409-b1c9-38c8439cd80e" />


---------

<img width="951" height="509" alt="CAP22" src="https://github.com/user-attachments/assets/bb9a979c-c1d6-4a9d-b862-5805c0962bdb" />


---------

<img width="959" height="508" alt="cap33" src="https://github.com/user-attachments/assets/0f8fd409-5ac6-44b1-968a-041232ec07c3" />

---------


<img width="960" height="509" alt="CAP5" src="https://github.com/user-attachments/assets/38b88827-bf4c-4399-bfd7-46b2b1e5f7c3" />


---------

<img width="959" height="506" alt="CAP6" src="https://github.com/user-attachments/assets/1ae4525e-8082-4eee-8b72-7695b08a16cc" />

---------


<img width="958" height="508" alt="CAP7" src="https://github.com/user-attachments/assets/442b534b-ecde-4bed-8fb7-df944df7e201" />

---


