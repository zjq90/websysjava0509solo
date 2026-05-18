package com.heritage.service;

import com.heritage.entity.DesensitizationRule;
import com.heritage.repository.DesensitizationRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DesensitizationService {

    @Autowired
    private DesensitizationRuleRepository ruleRepository;

    public List<DesensitizationRule> findAll() {
        return ruleRepository.findAll();
    }

    public Optional<DesensitizationRule> findById(Long id) {
        return ruleRepository.findById(id);
    }

    public DesensitizationRule save(DesensitizationRule rule) {
        return ruleRepository.save(rule);
    }

    public void deleteById(Long id) {
        ruleRepository.deleteById(id);
    }

    public List<DesensitizationRule> findEnabledRules() {
        return ruleRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    public String desensitize(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        String result = content;
        List<DesensitizationRule> rules = findEnabledRules();
        for (DesensitizationRule rule : rules) {
            Pattern pattern = Pattern.compile(rule.getRegexPattern());
            Matcher matcher = pattern.matcher(result);
            result = matcher.replaceAll(rule.getReplacement());
        }
        return result;
    }

    public String desensitizeIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    public String desensitizePhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    public String desensitizeName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        return name.charAt(0) + "*" + (name.length() > 2 ? name.substring(name.length() - 1) : "");
    }
}
