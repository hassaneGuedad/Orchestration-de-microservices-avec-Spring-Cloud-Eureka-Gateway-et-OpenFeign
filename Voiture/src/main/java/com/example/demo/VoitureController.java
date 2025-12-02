package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private VoitureService voitureService;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/voitures", produces = {"application/json"})
    public ResponseEntity<Object> findAll() {
        try {
            List<Voiture> voitures = voitureRepository.findAll();
            // Charger les clients pour chaque voiture
            voitures.forEach(voiture -> {
                try {
                    Client client = clientService.clientById(voiture.getId_client());
                    voiture.setClient(client);
                } catch (Exception e) {
                    System.err.println("⚠️ Client ID " + voiture.getId_client() + " non trouvé pour la voiture " + voiture.getId());
                    voiture.setClient(null);
                }
            });
            return ResponseEntity.ok(voitures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture Introuvable"));

            // Charger le client associé
            try {
                Client client = clientService.clientById(voiture.getId_client());
                voiture.setClient(client);
            } catch (Exception e) {
                System.err.println("⚠️ Client ID " + voiture.getId_client() + " non trouvé");
                voiture.setClient(null);
            }

            return ResponseEntity.ok(voiture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }

    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<Object> findByClient(@PathVariable Long id) {
        try {
            // Vérifier que le client existe
            Client client = clientService.clientById(id);

            if (client != null) {
                // Récupérer les voitures du client
                List<Voiture> voitures = voitureRepository.findByIdClient(id);

                // Charger le client pour chaque voiture
                voitures.forEach(voiture -> voiture.setClient(client));

                return ResponseEntity.ok(voitures);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Client not found with ID: " + id);
        }
    }

    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Object> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        try {
            // Vérifier que le client existe
            Client client = clientService.clientById(clientId);

            if (client != null) {
                voiture.setClient(client);
                voiture.setId_client(clientId);
                Voiture savedVoiture = voitureService.enregistrerVoiture(voiture);
                return ResponseEntity.ok(savedVoiture);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Voiture updatedVoiture) {
        try {
            Voiture existingVoiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            if (updatedVoiture.getMatricule() != null && !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }
            if (updatedVoiture.getMarque() != null && !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }
            if (updatedVoiture.getModel() != null && !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }

            Voiture savedVoiture = voitureRepository.save(existingVoiture);

            // Charger le client pour la réponse
            try {
                Client client = clientService.clientById(savedVoiture.getId_client());
                savedVoiture.setClient(client);
            } catch (Exception e) {
                savedVoiture.setClient(null);
            }

            return ResponseEntity.ok(savedVoiture);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }

    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found with ID: " + id));

            voitureService.supprimerVoiture(id);
            return ResponseEntity.ok("Voiture supprimée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }
}

