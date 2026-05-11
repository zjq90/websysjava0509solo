<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="form-card">
      <view class="form-item">
        <text class="label">姓名 <text class="required">*</text></text>
        <input 
          class="input" 
          v-model="form.patientName" 
          placeholder="请输入就诊人姓名"
        />
      </view>

      <view class="form-item">
        <text class="label">身份证号 <text class="required">*</text></text>
        <input 
          class="input" 
          v-model="form.idCard" 
          placeholder="请输入身份证号"
          maxlength="18"
        />
      </view>

      <view class="form-item">
        <text class="label">手机号 <text class="required">*</text></text>
        <input 
          class="input" 
          v-model="form.phone" 
          type="number"
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>

      <view class="form-item">
        <text class="label">与本人关系</text>
        <view class="relation-list">
          <view 
            class="relation-item" 
            v-for="rel in relations" 
            :key="rel.value"
            :class="{ active: form.relation === rel.value }"
            @click="form.relation = rel.value"
          >
            <text>{{ rel.label }}</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <text class="label">性别</text>
        <view class="gender-list">
          <view 
            class="gender-item" 
            :class="{ active: form.gender === 'MALE' }"
            @click="form.gender = 'MALE'"
          >
            <text>男</text>
          </view>
          <view 
            class="gender-item" 
            :class="{ active: form.gender === 'FEMALE' }"
            @click="form.gender = 'FEMALE'"
          >
            <text>女</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <text class="label">出生日期</text>
        <picker mode="date" :value="form.birthDate" @change="onDateChange">
          <view class="date-picker">
            <text v-if="form.birthDate">{{ form.birthDate }}</text>
            <text class="placeholder" v-else>请选择出生日期</text>
            <text class="arrow">></text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">设为默认就诊人</text>
        <switch :checked="form.isDefault === 1" @change="onDefaultChange" color="#1890ff" />
      </view>
    </view>

    <view class="bottom-bar">
      <view class="submit-btn" @click="handleSubmit" :class="{ disabled: submitting }">
        <text>{{ submitting ? '保存中...' : '保存' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onLoad } from 'vue'
import { useStore } from 'vuex'
import { patientApi } from '@/utils/api'

const store = useStore()

const patientId = ref(null)
const submitting = ref(false)

const relations = [
  { label: '本人', value: '本人' },
  { label: '配偶', value: '配偶' },
  { label: '父亲', value: '父亲' },
  { label: '母亲', value: '母亲' },
  { label: '儿子', value: '儿子' },
  { label: '女儿', value: '女儿' },
  { label: '其他', value: '其他' }
]

const form = reactive({
  patientName: '',
  idCard: '',
  phone: '',
  relation: '本人',
  gender: 'MALE',
  birthDate: '',
  isDefault: 0
})

const elderMode = computed(() => store.getters.elderMode)

onLoad((options) => {
  patientId.value = options.id
})

const onDateChange = (e) => {
  form.birthDate = e.detail.value
}

const onDefaultChange = (e) => {
  form.isDefault = e.detail.value ? 1 : 0
}

const loadPatient = async () => {
  if (!patientId.value) return
  
  try {
    const patient = await patientApi.getById(patientId.value)
    Object.assign(form, {
      patientName: patient.patientName || '',
      idCard: patient.idCard || '',
      phone: patient.phone || '',
      relation: patient.relation || '本人',
      gender: patient.gender || 'MALE',
      birthDate: patient.birthDate || '',
      isDefault: patient.isDefault || 0
    })
  } catch (e) {
    console.error('加载就诊人失败:', e)
  }
}

const validateForm = () => {
  if (!form.patientName) {
    uni.showToast({ title: '请输入姓名', icon: 'none' })
    return false
  }
  if (!form.idCard || form.idCard.length !== 18) {
    uni.showToast({ title: '请输入正确的身份证号', icon: 'none' })
    return false
  }
  if (!form.phone || form.phone.length !== 11) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return false
  }
  return true
}

const handleSubmit = async () => {
  if (!validateForm() || submitting.value) return

  submitting.value = true
  try {
    if (patientId.value) {
      await patientApi.update(patientId.value, form)
      uni.showToast({ title: '修改成功', icon: 'success' })
    } else {
      await patientApi.add(form)
      uni.showToast({ title: '添加成功', icon: 'success' })
    }
    
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (e) {
    uni.showToast({ title: e.message || '保存失败', icon: 'none' })
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadPatient()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.form-card {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 24rpx;
}

.form-item {
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
  display: flex;
  flex-direction: column;
}

.form-item:last-child {
  border-bottom: none;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
}

.required {
  color: #ff4d4f;
}

.input {
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.relation-list,
.gender-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.relation-item,
.gender-item {
  padding: 16rpx 32rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 26rpx;
  color: #666;
}

.relation-item.active,
.gender-item.active {
  background: #1890ff;
  color: #fff;
}

.date-picker {
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 28rpx;
  color: #333;
}

.placeholder {
  color: #999;
}

.arrow {
  color: #999;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  height: 88rpx;
  line-height: 88rpx;
  background: #1890ff;
  color: #fff;
  text-align: center;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.submit-btn.disabled {
  opacity: 0.6;
}

.elder-mode-enabled {
  .label {
    font-size: 32rpx;
  }
  
  .input,
  .date-picker {
    font-size: 32rpx;
    height: 100rpx;
  }
  
  .submit-btn {
    font-size: 36rpx;
    height: 100rpx;
    line-height: 100rpx;
  }
}
</style>
