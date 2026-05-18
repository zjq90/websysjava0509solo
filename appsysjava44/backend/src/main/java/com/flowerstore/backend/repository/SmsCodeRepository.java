package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 短信验证码数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    /**
     * 根据手机号和类型查询未使用的验证码
     */
    List<SmsCode> findByPhoneAndTypeAndIsUsedOrderByCreateTimeDesc(String phone, String type, Integer isUsed);
}
