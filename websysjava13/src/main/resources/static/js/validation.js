/**
 * 前端表单校验工具类
 * 提供与后端一致的校验规则
 */
const Validation = {
    rules: {
        username: {
            pattern: /^[a-zA-Z][a-zA-Z0-9_]{2,19}$/,
            message: '用户名格式不正确，必须以字母开头，只能包含字母、数字和下划线，长度3-20个字符'
        },
        password: {
            pattern: /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/,
            message: '密码格式不正确，长度6-20个字符，必须包含至少一个字母和一个数字'
        },
        phone: {
            pattern: /^1[3-9]\d{9}$/,
            message: '手机号格式不正确，请输入11位有效的手机号（如：13812345678）'
        },
        email: {
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            message: '邮箱格式不正确'
        },
        itemCode: {
            pattern: /^[a-z][a-z0-9_]{2,49}$/,
            message: '编码格式不正确，必须以小写字母开头，只能包含小写字母、数字和下划线，长度3-50个字符'
        },
        color: {
            pattern: /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/,
            message: '颜色格式不正确，请输入有效的HEX颜色值（如：#FF5733 或 #fff）'
        },
        required: {
            message: '此项不能为空'
        },
        minLength: (len) => ({
            message: `长度不能少于${len}个字符`
        }),
        maxLength: (len) => ({
            message: `长度不能超过${len}个字符`
        })
    },

    validate(value, rule) {
        if (!rule) return { valid: true };
        
        if (rule.required) {
            if (value === undefined || value === null || value === '') {
                return { valid: false, message: Validation.rules.required.message };
            }
        }

        if (rule.pattern) {
            if (value !== '' && !rule.pattern.test(value)) {
                return { valid: false, message: rule.message };
            }
        }

        if (rule.minLength && value.length < rule.minLength) {
            return { valid: false, message: Validation.rules.minLength(rule.minLength).message };
        }

        if (rule.maxLength && value.length > rule.maxLength) {
            return { valid: false, message: Validation.rules.maxLength(rule.maxLength).message };
        }

        return { valid: true };
    },

    validateUsername(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        const valueTrim = value.trim();
        if (valueTrim.length < 3 || valueTrim.length > 20) {
            return { valid: false, message: '用户名长度必须在3-20个字符之间' };
        }
        
        if (!Validation.rules.username.pattern.test(valueTrim)) {
            return { valid: false, message: Validation.rules.username.message };
        }
        
        return { valid: true };
    },

    validatePassword(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        if (value.length < 6 || value.length > 20) {
            return { valid: false, message: '密码长度必须在6-20个字符之间' };
        }
        
        if (!Validation.rules.password.pattern.test(value)) {
            return { valid: false, message: Validation.rules.password.message };
        }
        
        return { valid: true };
    },

    validatePhone(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        const valueTrim = value.trim();
        if (!Validation.rules.phone.pattern.test(valueTrim)) {
            return { valid: false, message: Validation.rules.phone.message };
        }
        
        return { valid: true };
    },

    validateEmail(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        if (!Validation.rules.email.pattern.test(value)) {
            return { valid: false, message: Validation.rules.email.message };
        }
        
        return { valid: true };
    },

    validateItemCode(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        const valueTrim = value.trim();
        if (valueTrim.length < 3 || valueTrim.length > 50) {
            return { valid: false, message: '编码长度必须在3-50个字符之间' };
        }
        
        if (!Validation.rules.itemCode.pattern.test(valueTrim)) {
            return { valid: false, message: Validation.rules.itemCode.message };
        }
        
        return { valid: true };
    },

    validateColor(value, options = {}) {
        const required = options.required !== false;
        if (!required && !value) return { valid: true };
        
        if (!value) return { valid: false, message: Validation.rules.required.message };
        
        if (!Validation.rules.color.pattern.test(value)) {
            return { valid: false, message: Validation.rules.color.message };
        }
        
        return { valid: true };
    },

    formatPhone(value) {
        if (!value) return value;
        return value.replace(/\D/g, '').slice(0, 11);
    },

    maskPhone(value) {
        const clean = value.replace(/\D/g, '');
        if (clean.length <= 3) return clean;
        if (clean.length <= 7) return clean.slice(0, 3) + '-' + clean.slice(3);
        return clean.slice(0, 3) + '-' + clean.slice(3, 7) + '-' + clean.slice(7, 11);
    },

    showError($field, message) {
        $field.addClass('is-invalid');
        $field.removeClass('is-valid');
        
        let $feedback = $field.siblings('.invalid-feedback');
        if ($feedback.length === 0) {
            $feedback = $('<div class="invalid-feedback"></div>').insertAfter($field);
        }
        $feedback.text(message);
    },

    showSuccess($field) {
        $field.addClass('is-valid');
        $field.removeClass('is-invalid');
        $field.siblings('.invalid-feedback').remove();
    },

    clearValidation($field) {
        $field.removeClass('is-invalid is-valid');
        $field.siblings('.invalid-feedback').remove();
    },

    bindPhoneInput($input) {
        $input.on('input', function() {
            const formatted = Validation.formatPhone($(this).val());
            $(this).val(formatted);
        });
    },

    validateForm($form) {
        let isValid = true;
        
        $form.find('[data-validate]').each(function() {
            const $field = $(this);
            const rule = $field.data('validate');
            const value = $field.val();
            const required = $field.prop('required') || $field.data('required');
            
            let result = { valid: true };
            
            switch (rule) {
                case 'username':
                    result = Validation.validateUsername(value, { required });
                    break;
                case 'password':
                    result = Validation.validatePassword(value, { required });
                    break;
                case 'phone':
                    result = Validation.validatePhone(value, { required });
                    break;
                case 'email':
                    result = Validation.validateEmail(value, { required });
                    break;
                case 'itemCode':
                    result = Validation.validateItemCode(value, { required });
                    break;
                case 'color':
                    result = Validation.validateColor(value, { required });
                    break;
                case 'required':
                    if (!value || value.trim() === '') {
                        result = { valid: false, message: Validation.rules.required.message };
                    }
                    break;
            }
            
            if (!result.valid) {
                isValid = false;
                Validation.showError($field, result.message);
            } else {
                Validation.showSuccess($field);
            }
        });
        
        return isValid;
    }
};
