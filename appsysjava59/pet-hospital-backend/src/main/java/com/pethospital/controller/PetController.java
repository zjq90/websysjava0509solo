package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Pet;
import com.pethospital.entity.PetOwner;
import com.pethospital.entity.PetTag;
import com.pethospital.entity.VaccineRecord;
import com.pethospital.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pet")
@Tag(name = "宠物管理", description = "宠物档案、疫苗记录、标签管理等接口")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "获取主人的宠物列表")
    public Result<List<Pet>> getPetsByOwnerId(@PathVariable Long ownerId) {
        return Result.success(petService.getPetsByOwnerId(ownerId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取宠物详情")
    public Result<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(Result::success).orElseGet(() -> Result.error("宠物不存在"));
    }

    @GetMapping("/{id}/tags")
    @Operation(summary = "获取宠物标签")
    public Result<List<PetTag>> getPetTags(@PathVariable Long id) {
        return Result.success(petService.getPetTags(id));
    }

    @PostMapping("/{id}/tags")
    @Operation(summary = "添加宠物标签")
    public Result<PetTag> addPetTag(@PathVariable Long id, @RequestBody PetTag tag) {
        tag.setPetId(id);
        return Result.success(petService.addPetTag(tag));
    }

    @DeleteMapping("/tags/{tagId}")
    @Operation(summary = "删除宠物标签")
    public Result<Void> deletePetTag(@PathVariable Long tagId) {
        petService.deletePetTag(tagId);
        return Result.success();
    }

    @GetMapping("/{id}/vaccines")
    @Operation(summary = "获取宠物疫苗记录")
    public Result<List<VaccineRecord>> getVaccineRecords(@PathVariable Long id) {
        return Result.success(petService.getVaccineRecords(id));
    }

    @PostMapping
    @Operation(summary = "创建宠物档案")
    public Result<Pet> createPet(@RequestBody Pet pet) {
        return Result.success(petService.savePet(pet));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新宠物信息")
    public Result<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        pet.setId(id);
        return Result.success(petService.savePet(pet));
    }

    @GetMapping
    @Operation(summary = "获取所有宠物")
    public Result<List<Pet>> getAllPets() {
        return Result.success(petService.getAllPets());
    }

    @GetMapping("/owner")
    @Operation(summary = "获取所有宠物主人")
    public Result<List<PetOwner>> getAllPetOwners() {
        return Result.success(petService.getAllPetOwners());
    }

    @GetMapping("/owner/detail/{id}")
    @Operation(summary = "获取宠物主人详情")
    public Result<PetOwner> getPetOwnerById(@PathVariable Long id) {
        Optional<PetOwner> owner = petService.getPetOwnerById(id);
        return owner.map(Result::success).orElseGet(() -> Result.error("主人信息不存在"));
    }

    @PostMapping("/owner")
    @Operation(summary = "创建宠物主人")
    public Result<PetOwner> createPetOwner(@RequestBody PetOwner owner) {
        return Result.success(petService.savePetOwner(owner));
    }
}
