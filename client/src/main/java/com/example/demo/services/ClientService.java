package com.example.demo.services;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client client) {
        client.setId(id);
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}

