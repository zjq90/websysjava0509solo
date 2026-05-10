package com.websys.config;

import com.websys.entity.Agent;
import com.websys.entity.SystemConfig;
import com.websys.entity.User;
import com.websys.repository.AgentRepository;
import com.websys.repository.SystemConfigRepository;
import com.websys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据初始化类
 * 用于在应用启动时初始化测试数据
 * 
 * @author websys
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initUsers();
            initAgents();
            initSystemConfigs();
        }
    }

    /**
     * 初始化用户数据
     */
    private void initUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("超级管理员");
        admin.setRoleType("ADMIN");
        admin.setPhone("13800138000");
        admin.setEmail("admin@websys.com");
        admin.setStatus(1);
        userRepository.save(admin);

        System.out.println("==========================================");
        System.out.println("已创建超级管理员账户");
        System.out.println("用户名: admin");
        System.out.println("密码: admin123");
        System.out.println("==========================================");
    }

    /**
     * 初始化代理商数据
     */
    private void initAgents() {
        Agent agent1 = new Agent();
        agent1.setAgentName("一级代理商A");
        agent1.setAgentCode("AGENT001");
        agent1.setParentId(null);
        agent1.setLevel(1);
        agent1.setContactName("张三");
        agent1.setContactPhone("13800138001");
        agent1.setContactEmail("agent1@websys.com");
        agent1.setAddress("北京市朝阳区");
        agent1.setStatus(1);
        agentRepository.save(agent1);

        Agent agent2 = new Agent();
        agent2.setAgentName("一级代理商B");
        agent2.setAgentCode("AGENT002");
        agent2.setParentId(null);
        agent2.setLevel(1);
        agent2.setContactName("李四");
        agent2.setContactPhone("13800138002");
        agent2.setContactEmail("agent2@websys.com");
        agent2.setAddress("上海市浦东新区");
        agent2.setStatus(1);
        agentRepository.save(agent2);

        Agent agent3 = new Agent();
        agent3.setAgentName("二级代理商A-1");
        agent3.setAgentCode("AGENT001-001");
        agent3.setParentId(agent1.getId());
        agent3.setLevel(2);
        agent3.setContactName("王五");
        agent3.setContactPhone("13800138003");
        agent3.setContactEmail("agent3@websys.com");
        agent3.setAddress("北京市海淀区");
        agent3.setStatus(1);
        agentRepository.save(agent3);

        Agent agent4 = new Agent();
        agent4.setAgentName("二级代理商B-1");
        agent4.setAgentCode("AGENT002-001");
        agent4.setParentId(agent2.getId());
        agent4.setLevel(2);
        agent4.setContactName("赵六");
        agent4.setContactPhone("13800138004");
        agent4.setContactEmail("agent4@websys.com");
        agent4.setAddress("上海市徐汇区");
        agent4.setStatus(1);
        agentRepository.save(agent4);

        Agent agent5 = new Agent();
        agent5.setAgentName("三级代理商A-1-1");
        agent5.setAgentCode("AGENT001-001-001");
        agent5.setParentId(agent3.getId());
        agent5.setLevel(3);
        agent5.setContactName("钱七");
        agent5.setContactPhone("13800138005");
        agent5.setContactEmail("agent5@websys.com");
        agent5.setAddress("北京市西城区");
        agent5.setStatus(1);
        agentRepository.save(agent5);

        User agentAdmin1 = new User();
        agentAdmin1.setUsername("agentadmin1");
        agentAdmin1.setPassword(passwordEncoder.encode("admin123"));
        agentAdmin1.setRealName("代理商管理员A");
        agentAdmin1.setAgentId(agent1.getId());
        agentAdmin1.setRoleType("AGENT_ADMIN");
        agentAdmin1.setPhone("13800138011");
        agentAdmin1.setEmail("agentadmin1@websys.com");
        agentAdmin1.setStatus(1);
        userRepository.save(agentAdmin1);

        User agentAdmin2 = new User();
        agentAdmin2.setUsername("agentadmin2");
        agentAdmin2.setPassword(passwordEncoder.encode("admin123"));
        agentAdmin2.setRealName("代理商管理员B");
        agentAdmin2.setAgentId(agent2.getId());
        agentAdmin2.setRoleType("AGENT_ADMIN");
        agentAdmin2.setPhone("13800138012");
        agentAdmin2.setEmail("agentadmin2@websys.com");
        agentAdmin2.setStatus(1);
        userRepository.save(agentAdmin2);

        User agentUser1 = new User();
        agentUser1.setUsername("agentuser1");
        agentUser1.setPassword(passwordEncoder.encode("admin123"));
        agentUser1.setRealName("代理商用户A");
        agentUser1.setAgentId(agent1.getId());
        agentUser1.setRoleType("AGENT_USER");
        agentUser1.setPhone("13800138021");
        agentUser1.setEmail("agentuser1@websys.com");
        agentUser1.setStatus(1);
        userRepository.save(agentUser1);
    }

    /**
     * 初始化系统配置数据
     */
    private void initSystemConfigs() {
        SystemConfig config1 = new SystemConfig();
        config1.setConfigGroup("SYSTEM");
        config1.setConfigKey("system.name");
        config1.setConfigValue("Web平台管理系统");
        config1.setConfigName("系统名称");
        config1.setDescription("系统的名称，用于显示在页面标题、登录页等位置");
        config1.setConfigType("STRING");
        config1.setEnabled(1);
        config1.setIsSystem(1);
        config1.setSortOrder(1);
        config1.setUpdateBy("system");
        systemConfigRepository.save(config1);

        SystemConfig config2 = new SystemConfig();
        config2.setConfigGroup("SYSTEM");
        config2.setConfigKey("system.version");
        config2.setConfigValue("1.0.0");
        config2.setConfigName("系统版本");
        config2.setDescription("当前系统的版本号");
        config2.setConfigType("STRING");
        config2.setEnabled(1);
        config2.setIsSystem(1);
        config2.setSortOrder(2);
        config2.setUpdateBy("system");
        systemConfigRepository.save(config2);

        SystemConfig config3 = new SystemConfig();
        config3.setConfigGroup("SYSTEM");
        config3.setConfigKey("system.max-login-failures");
        config3.setConfigValue("5");
        config3.setConfigName("最大登录失败次数");
        config3.setDescription("用户连续登录失败的最大次数，超过后将锁定账户");
        config3.setConfigType("NUMBER");
        config3.setEnabled(1);
        config3.setIsSystem(0);
        config3.setSortOrder(3);
        config3.setUpdateBy("system");
        systemConfigRepository.save(config3);

        SystemConfig config4 = new SystemConfig();
        config4.setConfigGroup("PAYMENT");
        config4.setConfigKey("payment.alipay.enabled");
        config4.setConfigValue("false");
        config4.setConfigName("支付宝支付启用");
        config4.setDescription("是否启用支付宝支付接口");
        config4.setConfigType("BOOLEAN");
        config4.setEnabled(1);
        config4.setIsSystem(0);
        config4.setSortOrder(1);
        config4.setUpdateBy("system");
        systemConfigRepository.save(config4);

        SystemConfig config5 = new SystemConfig();
        config5.setConfigGroup("PAYMENT");
        config5.setConfigKey("payment.wechat.enabled");
        config5.setConfigValue("false");
        config5.setConfigName("微信支付启用");
        config5.setDescription("是否启用微信支付接口");
        config5.setConfigType("BOOLEAN");
        config5.setEnabled(1);
        config5.setIsSystem(0);
        config5.setSortOrder(2);
        config5.setUpdateBy("system");
        systemConfigRepository.save(config5);

        SystemConfig config6 = new SystemConfig();
        config6.setConfigGroup("PAYMENT");
        config6.setConfigKey("payment.alipay.app-id");
        config6.setConfigValue("");
        config6.setConfigName("支付宝APPID");
        config6.setDescription("支付宝开放平台应用ID");
        config6.setConfigType("STRING");
        config6.setEnabled(1);
        config6.setIsSystem(0);
        config6.setSortOrder(3);
        config6.setUpdateBy("system");
        systemConfigRepository.save(config6);

        SystemConfig config7 = new SystemConfig();
        config7.setConfigGroup("SMS");
        config7.setConfigKey("sms.provider");
        config7.setConfigValue("aliyun");
        config7.setConfigName("短信服务商");
        config7.setDescription("短信服务提供商：aliyun-阿里云，tencent-腾讯云");
        config7.setConfigType("STRING");
        config7.setEnabled(1);
        config7.setIsSystem(0);
        config7.setSortOrder(1);
        config7.setUpdateBy("system");
        systemConfigRepository.save(config7);

        SystemConfig config8 = new SystemConfig();
        config8.setConfigGroup("SMS");
        config8.setConfigKey("sms.enabled");
        config8.setConfigValue("false");
        config8.setConfigName("短信服务启用");
        config8.setDescription("是否启用短信服务接口");
        config8.setConfigType("BOOLEAN");
        config8.setEnabled(1);
        config8.setIsSystem(0);
        config8.setSortOrder(2);
        config8.setUpdateBy("system");
        systemConfigRepository.save(config8);

        SystemConfig config9 = new SystemConfig();
        config9.setConfigGroup("SMS");
        config9.setConfigKey("sms.access-key-id");
        config9.setConfigValue("");
        config9.setConfigName("短信服务AccessKeyId");
        config9.setDescription("短信服务的AccessKeyId");
        config9.setConfigType("STRING");
        config9.setEnabled(1);
        config9.setIsSystem(0);
        config9.setSortOrder(3);
        config9.setUpdateBy("system");
        systemConfigRepository.save(config9);

        System.out.println("==========================================");
        System.out.println("已初始化系统配置数据");
        System.out.println("包含系统配置、支付配置、短信配置");
        System.out.println("==========================================");
    }
}
