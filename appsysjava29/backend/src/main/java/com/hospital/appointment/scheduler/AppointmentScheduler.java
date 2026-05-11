package com.hospital.appointment.scheduler;

import com.hospital.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度器
 * 
 * @author hospital
 * @version 1.0.0
 */
@Component
public class AppointmentScheduler {

    @Autowired
    private AppointmentService appointmentService;

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredLocks() {
        appointmentService.releaseExpiredLocks();
    }
}
