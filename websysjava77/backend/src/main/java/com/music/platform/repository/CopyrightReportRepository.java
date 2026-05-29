package com.music.platform.repository;

import com.music.platform.entity.CopyrightReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CopyrightReportRepository extends JpaRepository<CopyrightReport, Long> {
    List<CopyrightReport> findByMusicId(Long musicId);
    List<CopyrightReport> findByStatus(String status);
}
