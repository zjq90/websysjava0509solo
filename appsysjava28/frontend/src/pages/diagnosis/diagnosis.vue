<template>
    <view class="diagnosis-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="hero-section">
            <text class="hero-title">智能导诊</text>
            <text class="hero-subtitle">描述您的症状，为您推荐合适的科室和医生</text>
        </view>

        <view class="input-section card">
            <view class="input-header">
                <text class="input-label">请描述您的症状</text>
                <text class="char-count">{{ symptomInput.length }}/200</text>
            </view>
            <textarea 
                class="symptom-input" 
                placeholder="例如：头痛、发热、咳嗽..."
                v-model="symptomInput"
                maxlength="200"
            />
            <view class="voice-btn" @click="startVoiceInput">
                <text class="voice-icon">🎤</text>
                <text class="voice-text">语音输入症状</text>
            </view>
        </view>

        <view class="quick-symptoms card">
            <text class="section-title">常见症状</text>
            <view class="symptom-tags">
                <view 
                    class="symptom-tag" 
                    v-for="(symptom, index) in commonSymptoms" 
                    :key="index"
                    @click="addSymptom(symptom)"
                >
                    {{ symptom }}
                </view>
            </view>
        </view>

        <view class="diagnose-btn" @click="doDiagnose" v-if="!diagnosisResult">
            开始诊断
        </view>

        <view class="result-section" v-if="diagnosisResult">
            <view class="result-header card">
                <view class="result-icon">
                    <text>💡</text>
                </view>
                <view class="result-info">
                    <text class="result-title">诊断建议</text>
                    <text class="result-desc">根据您描述的症状，我们为您推荐以下科室和医生</text>
                </view>
            </view>

            <view class="departments card" v-if="diagnosisResult.departments && diagnosisResult.departments.length > 0">
                <text class="section-title">推荐科室</text>
                <view 
                    class="dept-item" 
                    v-for="dept in diagnosisResult.departments" 
                    :key="dept.id"
                    @click="goToDoctor(dept.id, dept.name)"
                >
                    <view class="dept-info">
                        <text class="dept-name">{{ dept.name }}</text>
                        <text class="dept-match">匹配度 {{ dept.matchScore }}%</text>
                    </view>
                    <text class="dept-arrow">></text>
                </view>
            </view>

            <view class="doctors card" v-if="diagnosisResult.doctors && diagnosisResult.doctors.length > 0">
                <text class="section-title">推荐医生</text>
                <view 
                    class="doctor-item" 
                    v-for="doctor in diagnosisResult.doctors" 
                    :key="doctor.id"
                    @click="goToDoctorDetail(doctor.id)"
                >
                    <view class="doctor-avatar">
                        <text>👨‍⚕️</text>
                    </view>
                    <view class="doctor-info">
                        <view class="doctor-header">
                            <text class="doctor-name">{{ doctor.realName }}</text>
                            <text class="doctor-title">{{ doctor.title }}</text>
                        </view>
                        <text class="doctor-department">{{ doctor.departmentName }}</text>
                        <view class="doctor-stats">
                            <text class="star">⭐ {{ doctor.rating }}</text>
                            <text class="match">匹配 {{ doctor.matchScore }}%</text>
                        </view>
                    </view>
                    <text class="doctor-arrow">></text>
                </view>
            </view>

            <view class="re-diagnose-btn" @click="resetDiagnosis">
                重新诊断
            </view>
        </view>
    </view>
</template>

<script>
import { diagnoseSymptoms } from '@/api/diagnosis'

export default {
    data() {
        return {
            symptomInput: '',
            commonSymptoms: [
                '头痛', '发热', '咳嗽', '胃痛', '腰痛',
                '失眠', '心慌', '乏力', '恶心', '关节痛'
            ],
            diagnosisResult: null,
            elderlyMode: false
        }
    },
    onShow() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
    },
    methods: {
        addSymptom(symptom) {
            if (this.symptomInput && !this.symptomInput.endsWith('、')) {
                this.symptomInput += '、'
            }
            this.symptomInput += symptom
        },
        startVoiceInput() {
            uni.showModal({
                title: '语音输入',
                content: '检测到当前为模拟环境\n是否使用文字输入代替语音识别？',
                confirmText: '好的',
                showCancel: false,
                success: () => {
                    uni.showToast({ title: '请直接在输入框输入症状', icon: 'none' })
                }
            })
        },
        async doDiagnose() {
            if (!this.symptomInput.trim()) {
                uni.showToast({ title: '请描述您的症状', icon: 'none' })
                return
            }
            
            uni.showLoading({ title: '分析中...' })
            
            try {
                const res = await diagnoseSymptoms(this.symptomInput)
                this.diagnosisResult = res.data.data || {}
                uni.hideLoading()
            } catch (e) {
                uni.hideLoading()
                console.error(e)
            }
        },
        resetDiagnosis() {
            this.symptomInput = ''
            this.diagnosisResult = null
        },
        goToDoctor(deptId, deptName) {
            uni.navigateTo({ 
                url: '/pages/doctor/doctor?deptId=' + deptId + '&deptName=' + encodeURIComponent(deptName) 
            })
        },
        goToDoctorDetail(id) {
            uni.navigateTo({ url: '/pages/doctor-detail/doctor-detail?id=' + id })
        }
    }
}
</script>

<style scoped>
.diagnosis-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 40rpx;
}

.hero-section {
    background: linear-gradient(135deg, #1677ff 0%, #0958d9 100%);
    padding: 60rpx 30rpx 80rpx;
    color: #ffffff;
}

.hero-title {
    font-size: 40rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 10rpx;
}

.hero-subtitle {
    font-size: 28rpx;
    opacity: 0.9;
}

.input-section {
    margin: -30rpx 30rpx 20rpx;
    position: relative;
    z-index: 10;
}

.input-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15rpx;
}

.input-label {
    font-size: 28rpx;
    color: #333333;
    font-weight: 500;
}

.char-count {
    font-size: 24rpx;
    color: #999999;
}

.symptom-input {
    width: 100%;
    height: 200rpx;
    background: #f5f7fa;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
}

.voice-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 20rpx;
    padding: 20rpx;
    background: #e6f4ff;
    border-radius: 8rpx;
}

.voice-icon {
    font-size: 36rpx;
    margin-right: 10rpx;
}

.voice-text {
    font-size: 28rpx;
    color: #1677ff;
}

.quick-symptoms {
    margin: 20rpx 30rpx;
    padding: 25rpx;
}

.section-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333333;
    display: block;
    margin-bottom: 20rpx;
}

.symptom-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 15rpx;
}

.symptom-tag {
    padding: 12rpx 25rpx;
    background: #f5f7fa;
    border-radius: 30rpx;
    font-size: 26rpx;
    color: #666666;
}

.diagnose-btn {
    margin: 40rpx 30rpx;
    background: #1677ff;
    color: #ffffff;
    text-align: center;
    padding: 30rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.result-section {
    padding: 0 30rpx;
}

.result-header {
    display: flex;
    align-items: center;
    padding: 25rpx;
    margin-bottom: 20rpx;
}

.result-icon {
    width: 80rpx;
    height: 80rpx;
    background: #fff7e6;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-right: 20rpx;
}

.result-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333333;
    display: block;
    margin-bottom: 5rpx;
}

.result-desc {
    font-size: 24rpx;
    color: #999999;
}

.departments,
.doctors {
    padding: 25rpx;
    margin-bottom: 20rpx;
}

.dept-item,
.doctor-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.dept-item:last-child,
.doctor-item:last-child {
    border-bottom: none;
}

.dept-info {
    flex: 1;
}

.dept-name {
    font-size: 28rpx;
    color: #333333;
    display: block;
}

.dept-match {
    font-size: 24rpx;
    color: #52c41a;
}

.dept-arrow,
.doctor-arrow {
    font-size: 28rpx;
    color: #999999;
}

.doctor-avatar {
    width: 100rpx;
    height: 100rpx;
    background: #e6f4ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 50rpx;
    margin-right: 20rpx;
}

.doctor-info {
    flex: 1;
}

.doctor-header {
    display: flex;
    align-items: center;
    margin-bottom: 5rpx;
}

.doctor-name {
    font-size: 28rpx;
    font-weight: 500;
    margin-right: 10rpx;
}

.doctor-title {
    font-size: 22rpx;
    color: #1677ff;
    background: #e6f4ff;
    padding: 2rpx 8rpx;
    border-radius: 4rpx;
}

.doctor-department {
    font-size: 24rpx;
    color: #666666;
    margin-bottom: 5rpx;
}

.doctor-stats {
    display: flex;
    gap: 20rpx;
}

.star {
    font-size: 24rpx;
    color: #faad14;
}

.match {
    font-size: 24rpx;
    color: #52c41a;
}

.re-diagnose-btn {
    margin-top: 40rpx;
    background: #ffffff;
    color: #1677ff;
    border: 2rpx solid #1677ff;
    text-align: center;
    padding: 30rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.elderly-mode .hero-title {
    font-size: 48rpx;
}

.elderly-mode .hero-subtitle,
.elderly-mode .symptom-input,
.elderly-mode .symptom-tag,
.elderly-mode .dept-name,
.elderly-mode .doctor-name {
    font-size: 32rpx;
}
</style>
