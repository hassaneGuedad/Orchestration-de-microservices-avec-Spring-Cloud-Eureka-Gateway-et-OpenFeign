package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ClientService clientService;

    public Voiture enregistrerVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    public Voiture modifierVoiture(Long id, Voiture voiture) throws Exception {
        Voiture existingVoiture = voitureRepository.findById(id)
                .orElseThrow(() -> new Exception("Voiture non trouvée avec l'ID: " + id));

        if (voiture.getMatricule() != null && !voiture.getMatricule().isEmpty()) {
            existingVoiture.setMatricule(voiture.getMatricule());
        }
        if (voiture.getMarque() != null && !voiture.getMarque().isEmpty()) {
            existingVoiture.setMarque(voiture.getMarque());
        }
        if (voiture.getModel() != null && !voiture.getModel().isEmpty()) {
            existingVoiture.setModel(voiture.getModel());
        }

        return voitureRepository.save(existingVoiture);
    }

    public void supprimerVoiture(Long id) {
        voitureRepository.deleteById(id);
    }
}

