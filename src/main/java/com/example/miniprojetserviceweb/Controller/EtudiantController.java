package com.example.miniprojetserviceweb.Controller;

import com.example.miniprojetserviceweb.Model.Etudiant;
import com.example.miniprojetserviceweb.Repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @GetMapping("all")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(etudiants);
    }

    @PostMapping("add")
    public ResponseEntity<Etudiant> createEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEtudiant);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        Etudiant etudiant = etudiantRepository.findById(id).orElse(null);
        if (etudiant != null) {
            return ResponseEntity.status(HttpStatus.OK).body(etudiant);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long id, @RequestBody Etudiant etudiantDetails) {
        Etudiant existingEtudiant = etudiantRepository.findById(id).orElse(null);
        if (existingEtudiant != null) {
            existingEtudiant.setNom(etudiantDetails.getNom());
            existingEtudiant.setPrenom(etudiantDetails.getPrenom());
            existingEtudiant.setNbAbsences(etudiantDetails.getNbAbsences());
            existingEtudiant.setReussite(etudiantDetails.isReussite());
            Etudiant updatedEtudiant = etudiantRepository.save(existingEtudiant);
            return ResponseEntity.status(HttpStatus.OK).body(updatedEtudiant);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/tauxabsence")
    public ResponseEntity<Double> getTauxAbsentéisme() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        int totalEtudiants = etudiants.size();
        if (totalEtudiants == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0.0);
        }
        long totalAbsences = etudiants.stream().filter(e -> e.getNbAbsences() > 0).count();
        double tauxAbsence = ((double) totalAbsences / totalEtudiants) * 100.0;
        return ResponseEntity.status(HttpStatus.OK).body(tauxAbsence);
    }

    @GetMapping("/tauxreussite")
    public ResponseEntity<Double> getTauxRéussite() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        int totalEtudiants = etudiants.size();
        if (totalEtudiants == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0.0);
        }
        long totalReussite = etudiants.stream().filter(Etudiant::isReussite).count();
        double tauxReussite = ((double) totalReussite / totalEtudiants) * 100.0;
        return ResponseEntity.status(HttpStatus.OK).body(tauxReussite);
    }
}
