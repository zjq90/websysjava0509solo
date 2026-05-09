package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.SeedBatch;
import com.inventory.entity.Store;
import com.inventory.entity.Transfer;
import com.inventory.entity.Warehouse;
import com.inventory.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 调拨服务层
 * 功能：处理库存调拨业务逻辑，支持仓库间、门店间调拨
 */
@Service
@Transactional
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private InventoryService inventoryService;

    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    public Optional<Transfer> findById(Long id) {
        return transferRepository.findById(id);
    }

    public Optional<Transfer> findByCode(String code) {
        return transferRepository.findByTransferCode(code);
    }

    public List<Transfer> findByStatus(String status) {
        return transferRepository.findByStatus(status);
    }

    public Transfer createTransfer(Transfer transfer) {
        String code = "DB" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        transfer.setTransferCode(code);
        transfer.setStatus("PENDING");
        return transferRepository.save(transfer);
 }

    public boolean approveTransfer(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer == null || !"PENDING".equals(transfer.getStatus())) {
            return false;
        }

        SeedBatch batch = transfer.getSeedBatch();
        int quantity = transfer.getQuantity();

        Warehouse fromWarehouse = transfer.getFromWarehouse();
        Store fromStore = transfer.getFromStore();
        Warehouse toWarehouse = transfer.getToWarehouse();
        Store toStore = transfer.getToStore();

        boolean success = inventoryService.outbound(batch, fromWarehouse, fromStore, quantity);
        if (!success) {
            return false;
        }

        inventoryService.inbound(batch, toWarehouse, toStore, quantity);

        transfer.setStatus("COMPLETED");
        transferRepository.save(transfer);
        return true;
    }

    public boolean rejectTransfer(Long transferId, String remark) {
        Transfer transfer = transferRepository.findById(transferId).orElse(null);
        if (transfer == null || !"PENDING".equals(transfer.getStatus())) {
            return false;
        }
        transfer.setStatus("REJECTED");
        if (remark != null) {
            transfer.setRemark(remark);
        }
        transferRepository.save(transfer);
        return true;
    }

    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }
}
