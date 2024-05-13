package com.example.miniprojetserviceweb.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.miniprojetserviceweb.Model.Enseignant;
import com.example.miniprojetserviceweb.Repository.EnseignantRepository;

import java.util.List;

@RestController
@RequestMapping("/enseignants")
public class EnseignantController {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Enseignant>> getAllEnseignants() {
        List<Enseignant> enseignants = enseignantRepository.findAll();
        if (!enseignants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(enseignants);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody Enseignant enseignant) {
        Enseignant savedEnseignant = enseignantRepository.save(enseignant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEnseignant);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Long id) {
        Enseignant enseignant = enseignantRepository.findById(id).orElse(null);
        if (enseignant != null) {
            return ResponseEntity.status(HttpStatus.OK).body(enseignant);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody Enseignant enseignantDetails) {
        Enseignant existingEnseignant = enseignantRepository.findById(id).orElse(null);
        if (existingEnseignant != null) {
            existingEnseignant.setNom(enseignantDetails.getNom());
            existingEnseignant.setPrenom(enseignantDetails.getPrenom());
            existingEnseignant.setMatiere(enseignantDetails.getMatiere());
            Enseignant updatedEnseignant = enseignantRepository.save(existingEnseignant);
            return ResponseEntity.status(HttpStatus.OK).body(updatedEnseignant);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        if (enseignantRepository.existsById(id)) {
            enseignantRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
