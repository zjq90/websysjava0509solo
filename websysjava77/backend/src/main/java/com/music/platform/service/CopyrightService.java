package com.music.platform.service;

import com.music.platform.entity.Copyright;
import com.music.platform.entity.CopyrightReport;
import com.music.platform.repository.CopyrightReportRepository;
import com.music.platform.repository.CopyrightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CopyrightService {
    private final CopyrightRepository copyrightRepository;
    private final CopyrightReportRepository copyrightReportRepository;

    public Copyright createDeclaration(Long musicId, Long userId, String type, String declaration, Boolean isOriginal) {
        Copyright copyright = new Copyright();
        copyright.setMusicId(musicId);
        copyright.setUserId(userId);
        copyright.setType(type);
        copyright.setDeclaration(declaration);
        copyright.setIsOriginal(isOriginal != null ? isOriginal : true);
        if (isOriginal != null && isOriginal) {
            copyright.setOriginalCert("CERT-" + System.currentTimeMillis() + "-" + musicId);
        }
        return copyrightRepository.save(copyright);
    }

    public Optional<Copyright> getByMusicId(Long musicId) {
        return copyrightRepository.findByMusicId(musicId);
    }

    public CopyrightReport reportInfringement(Long copyrightId, Long reporterId, Long musicId, String reason, String evidence) {
        CopyrightReport report = new CopyrightReport();
        report.setCopyrightId(copyrightId);
        report.setReporterId(reporterId);
        report.setMusicId(musicId);
        report.setReason(reason);
        report.setEvidence(evidence);
        report.setStatus("pending");

        Copyright copyright = copyrightRepository.findById(copyrightId).orElse(null);
        if (copyright != null) {
            copyright.setReportCount(copyright.getReportCount() + 1);
            copyrightRepository.save(copyright);
        }

        return copyrightReportRepository.save(report);
    }

    public List<CopyrightReport> getPendingReports() {
        return copyrightReportRepository.findByStatus("pending");
    }

    public List<CopyrightReport> getReportsByMusicId(Long musicId) {
        return copyrightReportRepository.findByMusicId(musicId);
    }

    public CopyrightReport updateReportStatus(Long reportId, String status) {
        CopyrightReport report = copyrightReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        return copyrightReportRepository.save(report);
    }
}
