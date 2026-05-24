package com.plate.service;

import com.plate.dto.RecordQueryDTO;
import com.plate.entity.RecognitionRecord;
import com.plate.repository.RecognitionRecordRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.opencsv.CSVWriter;

@Service
public class RecognitionRecordService {
    @Autowired
    private RecognitionRecordRepository recordRepository;

    public Page<RecognitionRecord> getRecords(RecordQueryDTO queryDTO) {
        Pageable pageable = PageRequest.of(
                queryDTO.getPage(),
                queryDTO.getSize(),
                Sort.by(Sort.Direction.DESC, "passTime")
        );
        return recordRepository.findByConditions(
                queryDTO.getPlateNumber(),
                queryDTO.getCameraId(),
                queryDTO.getAnomalyType(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                pageable
        );
    }

    public List<RecognitionRecord> getAllRecordsForExport(RecordQueryDTO queryDTO) {
        return recordRepository.findAllByConditions(
                queryDTO.getPlateNumber(),
                queryDTO.getCameraId(),
                queryDTO.getAnomalyType(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime()
        );
    }

    public RecognitionRecord saveRecord(RecognitionRecord record) {
        return recordRepository.save(record);
    }

    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }

    public long getTodayRecognitions() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return recordRepository.countByPassTimeBetween(start, end);
    }

    public long getTodayAnomalyCount() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return recordRepository.countByIsAnomalyTrueAndPassTimeBetween(start, end);
    }

    public byte[] exportToCsv(List<RecognitionRecord> records) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header = {"ID", "车牌号", "通过时间", "摄像头ID", "摄像头名称", "置信度", "异常类型", "是否异常"};
        csvWriter.writeNext(header);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (RecognitionRecord record : records) {
            String[] row = {
                    String.valueOf(record.getId()),
                    record.getPlateNumber(),
                    record.getPassTime().format(formatter),
                    record.getCameraId(),
                    record.getCameraName(),
                    String.valueOf(record.getConfidence()),
                    record.getAnomalyType() != null ? record.getAnomalyType() : "",
                    record.getIsAnomaly() ? "是" : "否"
            };
            csvWriter.writeNext(row);
        }

        csvWriter.close();
        return writer.toString().getBytes("UTF-8");
    }

    public byte[] exportToExcel(List<RecognitionRecord> records) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("识别记录");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("车牌号");
        headerRow.createCell(2).setCellValue("通过时间");
        headerRow.createCell(3).setCellValue("摄像头ID");
        headerRow.createCell(4).setCellValue("摄像头名称");
        headerRow.createCell(5).setCellValue("置信度");
        headerRow.createCell(6).setCellValue("异常类型");
        headerRow.createCell(7).setCellValue("是否异常");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowNum = 1;
        for (RecognitionRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.getId());
            row.createCell(1).setCellValue(record.getPlateNumber());
            row.createCell(2).setCellValue(record.getPassTime().format(formatter));
            row.createCell(3).setCellValue(record.getCameraId());
            row.createCell(4).setCellValue(record.getCameraName());
            row.createCell(5).setCellValue(record.getConfidence());
            row.createCell(6).setCellValue(record.getAnomalyType() != null ? record.getAnomalyType() : "");
            row.createCell(7).setCellValue(record.getIsAnomaly() ? "是" : "否");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
