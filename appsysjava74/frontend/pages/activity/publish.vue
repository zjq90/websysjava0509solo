<template>
    <view class="container">
        <scroll-view scroll-y class="scroll-container">
            <u--form :model="form" label-width="140rpx">
                <view class="form-section">
                    <view class="section-title">基本信息</view>
                    
                    <u--form-item label="活动海报" prop="posterUrl">
                        <u-upload 
                            :file-list="fileList" 
                            @after-read="afterRead"
                            @delete="deleteFile"
                            :max-count="1"
                            name="poster"
                        ></u-upload>
                    </u--form-item>
                    
                    <u--form-item label="活动名称" prop="name">
                        <u--input v-model="form.name" placeholder="请输入活动名称" maxlength="100"></u--input>
                    </u--form-item>
                    
                    <u--form-item label="活动地点" prop="location">
                        <u--input v-model="form.location" placeholder="请输入活动地点" maxlength="200"></u--input>
                    </u--form-item>
                    
                    <u--form-item label="活动名额" prop="quota">
                        <u--input v-model.number="form.quota" placeholder="请输入活动名额" type="number"></u--input>
                    </u--form-item>
                </view>
                
                <view class="form-section">
                    <view class="section-title">时间设置</view>
                    
                    <u--form-item label="活动开始">
                        <u-datetime-picker 
                            :show="showStartTime" 
                            v-model="form.startTime"
                            mode="datetime"
                            @confirm="confirmStartTime"
                            @cancel="showStartTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.startTime || '请选择开始时间'" 
                            :arrow="true"
                            @click="showStartTime = true"
                        ></u-cell-item>
                    </u--form-item>
                    
                    <u--form-item label="活动结束">
                        <u-datetime-picker 
                            :show="showEndTime" 
                            v-model="form.endTime"
                            mode="datetime"
                            :min-date="form.startTime ? new Date(form.startTime).getTime() : Date.now()"
                            @confirm="confirmEndTime"
                            @cancel="showEndTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.endTime || '请选择结束时间'" 
                            :arrow="true"
                            @click="showEndTime = true"
                        ></u-cell-item>
                    </u--form-item>
                    
                    <u--form-item label="报名开始">
                        <u-datetime-picker 
                            :show="showRegStartTime" 
                            v-model="form.registrationStartTime"
                            mode="datetime"
                            @confirm="confirmRegStartTime"
                            @cancel="showRegStartTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.registrationStartTime || '请选择报名开始时间'" 
                            :arrow="true"
                            @click="showRegStartTime = true"
                        ></u-cell-item>
                    </u--form-item>
                    
                    <u--form-item label="报名结束">
                        <u-datetime-picker 
                            :show="showRegEndTime" 
                            v-model="form.registrationEndTime"
                            mode="datetime"
                            :min-date="form.registrationStartTime ? new Date(form.registrationStartTime).getTime() : Date.now()"
                            @confirm="confirmRegEndTime"
                            @cancel="showRegEndTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.registrationEndTime || '请选择报名结束时间'" 
                            :arrow="true"
                            @click="showRegEndTime = true"
                        ></u-cell-item>
                    </u--form-item>
                    
                    <u--form-item label="签到开始">
                        <u-datetime-picker 
                            :show="showSignInStartTime" 
                            v-model="form.signInStartTime"
                            mode="datetime"
                            @confirm="confirmSignInStartTime"
                            @cancel="showSignInStartTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.signInStartTime || '请选择签到开始时间'" 
                            :arrow="true"
                            @click="showSignInStartTime = true"
                        ></u-cell-item>
                    </u--form-item>
                    
                    <u--form-item label="签到结束">
                        <u-datetime-picker 
                            :show="showSignInEndTime" 
                            v-model="form.signInEndTime"
                            mode="datetime"
                            :min-date="form.signInStartTime ? new Date(form.signInStartTime).getTime() : Date.now()"
                            @confirm="confirmSignInEndTime"
                            @cancel="showSignInEndTime = false"
                        ></u-datetime-picker>
                        <u-cell-item 
                            :title="form.signInEndTime || '请选择签到结束时间'" 
                            :arrow="true"
                            @click="showSignInEndTime = true"
                        ></u-cell-item>
                    </u--form-item>
                </view>
                
                <view class="form-section">
                    <view class="section-title">报名设置</view>
                    
                    <u--form-item label="报名范围">
                        <u-radio-group v-model="form.registrationScope">
                            <u-radio 
                                :custom-style="{ marginRight: '30rpx' }"
                                name="ALL_STUDENTS"
                                label="全校开放"
                            ></u-radio>
                            <u-radio 
                                name="CLUB_MEMBERS_ONLY"
                                label="仅社团成员"
                            ></u-radio>
                        </u-radio-group>
                    </u--form-item>
                    
                    <u--form-item label="是否审核">
                        <u-switch v-model="form.needApproval"></u-switch>
                    </u--form-item>
                </view>
                
                <view class="form-section">
                    <view class="section-title">详细信息</view>
                    
                    <u--form-item label="活动要求">
                        <u--textarea 
                            v-model="form.requirements" 
                            placeholder="请输入活动要求"
                            :maxlength="500"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="活动简介">
                        <u--textarea 
                            v-model="form.description" 
                            placeholder="请输入活动简介"
                            :maxlength="1000"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="联系人">
                        <u--input v-model="form.organizerName" placeholder="请输入联系人姓名"></u--input>
                    </u--form-item>
                    
                    <u--form-item label="联系电话">
                        <u--input v-model="form.organizerPhone" placeholder="请输入联系电话" type="number"></u--input>
                    </u--form-item>
                </view>
            </u--form>
            
            <view style="height: 160rpx;"></view>
        </scroll-view>
        
        <view class="bottom-bar">
            <u-button type="warning" size="large" @click="saveDraft">保存草稿</u-button>
            <u-button type="primary" size="large" :loading="submitting" @click="publishActivity">发布活动</u-button>
        </view>
    </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { activityApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const editId = ref(route.query.id)
const submitting = ref(false)
const fileList = ref([])

const form = reactive({
    name: '',
    location: '',
    quota: 50,
    startTime: '',
    endTime: '',
    registrationStartTime: '',
    registrationEndTime: '',
    signInStartTime: '',
    signInEndTime: '',
    registrationScope: 'ALL_STUDENTS',
    needApproval: false,
    requirements: '',
    description: '',
    organizerName: '',
    organizerPhone: '',
    posterUrl: '',
    clubId: 1
})

const showStartTime = ref(false)
const showEndTime = ref(false)
const showRegStartTime = ref(false)
const showRegEndTime = ref(false)
const showSignInStartTime = ref(false)
const showSignInEndTime = ref(false)

const formatDate = (timestamp) => dayjs(timestamp).format('YYYY-MM-DD HH:mm')

const confirmStartTime = (e) => {
    form.startTime = formatDate(e.value)
    showStartTime.value = false
}

const confirmEndTime = (e) => {
    form.endTime = formatDate(e.value)
    showEndTime.value = false
}

const confirmRegStartTime = (e) => {
    form.registrationStartTime = formatDate(e.value)
    showRegStartTime.value = false
}

const confirmRegEndTime = (e) => {
    form.registrationEndTime = formatDate(e.value)
    showRegEndTime.value = false
}

const confirmSignInStartTime = (e) => {
    form.signInStartTime = formatDate(e.value)
    showSignInStartTime.value = false
}

const confirmSignInEndTime = (e) => {
    form.signInEndTime = formatDate(e.value)
    showSignInEndTime.value = false
}

const afterRead = (event) => {
    const file = event.file
    fileList.value = [{ url: file.url, status: 'uploading', message: '上传中' }]
    uni.uploadFile({
        url: uni.$u.http.config.baseUrl + '/upload',
        filePath: file.url,
        name: 'file',
        header: {
            Authorization: 'Bearer ' + uni.getStorageSync('token')
        },
        success: (res) => {
            const data = JSON.parse(res.data)
            form.posterUrl = data.data || data.url
            fileList.value = [{ url: form.posterUrl, status: 'success', message: '上传成功' }]
        },
        fail: () => {
            fileList.value = [{ url: file.url, status: 'failed', message: '上传失败' }]
            uni.showToast({ title: '上传失败', icon: 'none' })
        }
    })
}

const deleteFile = () => {
    form.posterUrl = ''
    fileList.value = []
}

const validateForm = () => {
    if (!form.name) {
        uni.showToast({ title: '请输入活动名称', icon: 'none' })
        return false
    }
    if (!form.location) {
        uni.showToast({ title: '请输入活动地点', icon: 'none' })
        return false
    }
    if (!form.startTime) {
        uni.showToast({ title: '请选择活动开始时间', icon: 'none' })
        return false
    }
    if (!form.endTime) {
        uni.showToast({ title: '请选择活动结束时间', icon: 'none' })
        return false
    }
    if (!form.registrationStartTime) {
        uni.showToast({ title: '请选择报名开始时间', icon: 'none' })
        return false
    }
    if (!form.registrationEndTime) {
        uni.showToast({ title: '请选择报名结束时间', icon: 'none' })
        return false
    }
    if (!form.organizerName) {
        uni.showToast({ title: '请输入联系人', icon: 'none' })
        return false
    }
    if (!form.organizerPhone) {
        uni.showToast({ title: '请输入联系电话', icon: 'none' })
        return false
    }
    return true
}

const saveDraft = async () => {
    if (!validateForm()) return
    
    submitting.value = true
    try {
        if (editId.value) {
            await activityApi.update(editId.value, { ...form, status: 'DRAFT' })
        } else {
            await activityApi.create({ ...form, status: 'DRAFT' })
        }
        uni.showToast({ title: '保存成功', icon: 'success' })
        setTimeout(() => {
            uni.navigateBack()
        }, 1500)
    } catch (e) {
        console.error('保存失败:', e)
    } finally {
        submitting.value = false
    }
}

const publishActivity = async () => {
    if (!validateForm()) return
    
    submitting.value = true
    try {
        if (editId.value) {
            await activityApi.update(editId.value, form)
            await activityApi.publish(editId.value)
        } else {
            const res = await activityApi.create(form)
            await activityApi.publish(res.id)
        }
        uni.showToast({ title: '发布成功', icon: 'success' })
        setTimeout(() => {
            uni.navigateBack()
        }, 1500)
    } catch (e) {
        console.error('发布失败:', e)
    } finally {
        submitting.value = false
    }
}

const loadEditData = async () => {
    if (!editId.value) return
    try {
        const data = await activityApi.getDetail(editId.value)
        Object.assign(form, data)
        if (data.posterUrl) {
            fileList.value = [{ url: data.posterUrl, status: 'success', message: '上传成功' }]
        }
    } catch (e) {
        console.error('加载数据失败:', e)
    }
}

loadEditData()
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    position: relative;
}

.scroll-container {
    height: calc(100vh - 120rpx);
    padding: 20rpx;
}

.form-section {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    
    .section-title {
        padding: 24rpx;
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
        background: #f8f9fa;
        border-bottom: 1rpx solid #f0f0f0;
    }
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx;
    display: flex;
    gap: 16rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
    
    button {
        flex: 1;
    }
}
</style>
