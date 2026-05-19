package com.pethospital.service;

import com.pethospital.entity.Pet;
import com.pethospital.entity.PetOwner;
import com.pethospital.entity.PetTag;
import com.pethospital.entity.VaccineRecord;
import com.pethospital.repository.PetOwnerRepository;
import com.pethospital.repository.PetRepository;
import com.pethospital.repository.PetTagRepository;
import com.pethospital.repository.VaccineRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetTagRepository petTagRepository;

    @Autowired
    private VaccineRecordRepository vaccineRecordRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    public List<Pet> getPetsByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public List<PetTag> getPetTags(Long petId) {
        return petTagRepository.findByPetId(petId);
    }

    public List<VaccineRecord> getVaccineRecords(Long petId) {
        return vaccineRecordRepository.findByPetId(petId);
    }

    @Transactional
    public PetTag addPetTag(PetTag tag) {
        return petTagRepository.save(tag);
    }

    @Transactional
    public void deletePetTag(Long tagId) {
        petTagRepository.deleteById(tagId);
    }

    @Transactional
    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<PetOwner> getAllPetOwners() {
        return petOwnerRepository.findAll();
    }

    public Optional<PetOwner> getPetOwnerById(Long id) {
        return petOwnerRepository.findById(id);
    }

    @Transactional
    public PetOwner savePetOwner(PetOwner owner) {
        return petOwnerRepository.save(owner);
    }
}
