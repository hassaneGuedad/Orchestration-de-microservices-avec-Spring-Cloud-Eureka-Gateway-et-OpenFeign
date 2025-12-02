## 🎓 Bonnes pratiques

### 1. Toujours gérer les erreurs Feign

```java
try {
    Client client = clientService.clientById(id);
} catch (FeignException.NotFound e) {
    // Client non trouvé - gérer le cas
} catch (FeignException e) {
    // Autre erreur Feign
}
```

### 2. Ajouter un fallback Feign (Optionnel)

```java
@Component
public class ClientServiceFallback implements ClientService {
    @Override
    public Client clientById(Long id) {
        return new Client(id, "Client Inconnu", 0f);
    }
}

@FeignClient(name="SERVICE-CLIENT", fallback=ClientServiceFallback.class)
public interface ClientService {
    // ...
}
```

### 3. Rendre l'initialisation optionnelle

```java
@Bean
@Profile("dev")  // Seulement en développement
CommandLineRunner initialiserBaseH2(...) {
    // ...
}
```

---

## 📝 Résumé

**Problème** : CommandLineRunner plante si un client n'existe pas (404)

**Solution** : Gestion des erreurs avec try/catch

**Avantage** : Le service démarre même si SERVICE-CLIENT n'est pas prêt

**Prochaine étape** : Vérifier que SERVICE-CLIENT a bien les clients avec IDs 1 et 2

---

## ✅ Le service devrait maintenant démarrer !

```powershell
.\mvnw.cmd spring-boot:run
```

**Vérifiez les logs pour voir si les clients sont trouvés ou non.**

