package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
		return args -> {
			System.out.println("**************************");
			System.out.println("🚀 Initialisation des données de test...");
			System.out.println("**************************");

			// Récupération des clients via Feign
			Client c1 = null;
			Client c2 = null;

			try {
				c2 = clientService.clientById(1L);
				System.out.println("✅ Client 1 trouvé :");
				System.out.println("   Id : " + c2.getId());
				System.out.println("   Nom : " + c2.getNom());
				System.out.println("   Age : " + c2.getAge());
			} catch (Exception e) {
				System.err.println("⚠️  Client avec ID 1 non trouvé dans SERVICE-CLIENT");
				System.err.println("   Raison: " + e.getMessage());
			}

			try {
				c1 = clientService.clientById(2L);
				System.out.println("✅ Client 2 trouvé :");
				System.out.println("   Id : " + c1.getId());
				System.out.println("   Nom : " + c1.getNom());
				System.out.println("   Age : " + c1.getAge());
			} catch (Exception e) {
				System.err.println("⚠️  Client avec ID 2 non trouvé dans SERVICE-CLIENT");
				System.err.println("   Raison: " + e.getMessage());
			}

			System.out.println("**************************");
			System.out.println("💾 Sauvegarde des voitures...");

			// Sauvegarde des voitures (le champ client peut être null)
			Voiture v1 = new Voiture(null, "Toyota", "A 25 333", "Corolla", 1L, c2);
			voitureRepository.save(v1);
			System.out.println("   ✅ Voiture 1: Toyota Corolla (Client ID: 1)");

			Voiture v2 = new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, c2);
			voitureRepository.save(v2);
			System.out.println("   ✅ Voiture 2: Renault Megane (Client ID: 1)");

			Voiture v3 = new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, c1);
			voitureRepository.save(v3);
			System.out.println("   ✅ Voiture 3: Peugeot 301 (Client ID: 2)");

			long count = voitureRepository.count();
			System.out.println("**************************");
			System.out.println("🎉 " + count + " voitures initialisées avec succès!");
			System.out.println("**************************");

			if (count == 0) {
				System.err.println("❌ ERREUR: Aucune voiture n'a été sauvegardée!");
				System.err.println("   Vérifiez la configuration de la base de données H2");
			}
		};
	}

}
