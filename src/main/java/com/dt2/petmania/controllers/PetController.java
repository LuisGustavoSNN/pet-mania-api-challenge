package com.dt2.petmania.controllers;

import com.dt2.petmania.entities.Pet;
import com.dt2.petmania.repositories.PetRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetRepository repository;

    @GetMapping
    public List<Pet> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pet buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet não encontrado"));
    }

    @PostMapping
    public ResponseEntity<Pet> cadastrar(@Valid @RequestBody Pet pet) {
        pet.setId(null);
        Pet salvo = repository.save(pet);
        return ResponseEntity.created(URI.create("/api/pets/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/{id}")
    public Pet atualizar(@PathVariable Long id, @Valid @RequestBody Pet dados) {
        Pet pet = buscarPorId(id);
        pet.setNome(dados.getNome());
        pet.setEspecie(dados.getEspecie());
        pet.setRaca(dados.getRaca());
        pet.setIdade(dados.getIdade());
        pet.setSexo(dados.getSexo());
        pet.setPorte(dados.getPorte());
        pet.setPersonalidade(dados.getPersonalidade());
        pet.setFoto(dados.getFoto());
        pet.setDisponivel(dados.isDisponivel());
        return repository.save(pet);
    }
}
