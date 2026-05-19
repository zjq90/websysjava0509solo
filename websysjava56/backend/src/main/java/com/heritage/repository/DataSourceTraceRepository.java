package com.heritage.repository;

import com.heritage.entity.DataSourceTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSourceTraceRepository extends JpaRepository<DataSourceTrace, Long>, JpaSpecificationExecutor<DataSourceTrace> {

    List<DataSourceTrace> findByDataId(Long dataId);

    List<DataSourceTrace> findByDataCode(String dataCode);

    Page<DataSourceTrace> findBySourceType(String sourceType, Pageable pageable);

    List<DataSourceTrace> findByParentTraceId(Long parentTraceId);
}
