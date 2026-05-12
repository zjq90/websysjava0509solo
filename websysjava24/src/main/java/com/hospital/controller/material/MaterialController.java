package com.hospital.controller.material;

import com.hospital.entity.material.Material;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/material")
@Tag(name = "物资管理", description = "物资耗材管理接口")
@CrossOrigin(origins = "*")
public class MaterialController {

    private List<Material> materialList = new ArrayList<>();

    public MaterialController() {
        Material m1 = new Material();
        m1.setId(1L);
        m1.setMaterialCode("MAT001");
        m1.setMaterialName("一次性使用无菌注射器");
        m1.setSpecification("5ml");
        m1.setModel("注射器");
        m1.setManufacturer("某医疗器械公司");
        m1.setRegistrationNo("粤械注准20232660001");
        m1.setPurchasePrice(new BigDecimal("2.50"));
        m1.setUsePrice(new BigDecimal("4.00"));
        m1.setCategory("注射器");
        m1.setStorageCondition("常温");
        m1.setStatus(1);
        m1.setIsHighValue(false);
        materialList.add(m1);

        Material m2 = new Material();
        m2.setId(2L);
        m2.setMaterialCode("MAT002");
        m2.setMaterialName("一次性医用口罩");
        m2.setSpecification("耳挂式");
        m2.setModel("口罩");
        m2.setManufacturer("某医疗器械公司");
        m2.setRegistrationNo("粤械注准20232660002");
        m2.setPurchasePrice(new BigDecimal("0.80"));
        m2.setUsePrice(new BigDecimal("1.50"));
        m2.setCategory("防护用品");
        m2.setStorageCondition("常温");
        m2.setStatus(1);
        m2.setIsHighValue(false);
        materialList.add(m2);

        Material m3 = new Material();
        m3.setId(3L);
        m3.setMaterialCode("MAT003");
        m3.setMaterialName("医用手套");
        m3.setSpecification("M号");
        m3.setModel("防护用品");
        m3.setManufacturer("某医疗器械公司");
        m3.setRegistrationNo("粤械注准20232660003");
        m3.setPurchasePrice(new BigDecimal("1.20"));
        m3.setUsePrice(new BigDecimal("2.00"));
        m3.setCategory("防护用品");
        m3.setStorageCondition("常温");
        m3.setStatus(1);
        m3.setIsHighValue(false);
        materialList.add(m3);

        Material m4 = new Material();
        m4.setId(4L);
        m4.setMaterialCode("MAT004");
        m4.setMaterialName("心脏支架");
        m4.setSpecification("药物洗脱支架");
        m4.setModel("植入耗材");
        m4.setManufacturer("某医疗器械公司");
        m4.setRegistrationNo("粤械注准20232660004");
        m4.setPurchasePrice(new BigDecimal("1200.00"));
        m4.setUsePrice(new BigDecimal("1800.00"));
        m4.setCategory("植入耗材");
        m4.setStorageCondition("常温");
        m4.setStatus(1);
        m4.setIsHighValue(true);
        m4.setUniqueIdRule("SN-" + System.currentTimeMillis());
        materialList.add(m4);

        Material m5 = new Material();
        m5.setId(5L);
        m5.setMaterialCode("MAT005");
        m5.setMaterialName("人工髋关节");
        m5.setSpecification("陶瓷型");
        m5.setModel("植入耗材");
        m5.setManufacturer("某医疗器械公司");
        m5.setRegistrationNo("粤械注准20232660005");
        m5.setPurchasePrice(new BigDecimal("8500.00"));
        m5.setUsePrice(new BigDecimal("12000.00"));
        m5.setCategory("植入耗材");
        m5.setStorageCondition("常温");
        m5.setStatus(1);
        m5.setIsHighValue(true);
        m5.setUniqueIdRule("SN-" + System.currentTimeMillis());
        materialList.add(m5);
    }

    @PostMapping
    @Operation(summary = "新增物资", description = "新增物资耗材信息")
    public ResponseEntity<Material> create(@RequestBody Material material) {
        material.setId((long) (materialList.size() + 1));
        materialList.add(material);
        return ResponseEntity.ok(material);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新物资", description = "更新物资耗材信息")
    public ResponseEntity<Material> update(
            @Parameter(description = "物资ID") @PathVariable Long id,
            @RequestBody Material material) {
        for (int i = 0; i < materialList.size(); i++) {
            if (materialList.get(i).getId().equals(id)) {
                material.setId(id);
                materialList.set(i, material);
                return ResponseEntity.ok(material);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除物资", description = "删除物资耗材信息")
    public ResponseEntity<Void> delete(@Parameter(description = "物资ID") @PathVariable Long id) {
        materialList.removeIf(item -> item.getId().equals(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询物资详情", description = "根据ID查询物资详情")
    public ResponseEntity<Material> getById(@Parameter(description = "物资ID") @PathVariable Long id) {
        Optional<Material> material = materialList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        return material.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码查询物资", description = "根据物资编码查询物资信息")
    public ResponseEntity<Material> getByCode(@Parameter(description = "物资编码") @PathVariable String code) {
        Optional<Material> material = materialList.stream()
                .filter(item -> item.getMaterialCode().equals(code))
                .findFirst();
        return material.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "查询所有物资", description = "查询所有物资列表")
    public ResponseEntity<List<Material>> getAll() {
        return ResponseEntity.ok(materialList);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询物资", description = "分页查询物资列表")
    public ResponseEntity<Map<String, Object>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        int start = page * size;
        int end = Math.min(start + size, materialList.size());
        List<Material> content = materialList.subList(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", materialList.size());
        result.put("totalPages", (int) Math.ceil((double) materialList.size() / size));
        result.put("number", page);
        result.put("size", size);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询物资", description = "根据状态查询物资列表")
    public ResponseEntity<List<Material>> getByStatus(@Parameter(description = "状态(0-停用,1-启用)") @PathVariable Integer status) {
        List<Material> result = new ArrayList<>();
        for (Material material : materialList) {
            if (material.getStatus().equals(status)) {
                result.add(material);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询物资", description = "根据分类查询物资列表")
    public ResponseEntity<List<Material>> getByCategory(@Parameter(description = "物资分类") @PathVariable String category) {
        List<Material> result = new ArrayList<>();
        for (Material material : materialList) {
            if (material.getCategory() != null && material.getCategory().contains(category)) {
                result.add(material);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/high-value/{isHighValue}")
    @Operation(summary = "查询高值耗材", description = "查询是否为高值耗材列表")
    public ResponseEntity<List<Material>> getHighValueMaterials(@Parameter(description = "是否高值耗材") @PathVariable Boolean isHighValue) {
        List<Material> result = new ArrayList<>();
        for (Material material : materialList) {
            if (material.getIsHighValue() != null && material.getIsHighValue().equals(isHighValue)) {
                result.add(material);
            }
        }
        return ResponseEntity.ok(result);
    }
}
