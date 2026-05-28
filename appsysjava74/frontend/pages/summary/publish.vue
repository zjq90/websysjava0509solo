<template>
    <view class="container">
        <scroll-view scroll-y class="scroll-container">
            <u--form :model="form" label-width="140rpx">
                <view class="form-section">
                    <view class="section-title">基本信息</view>
                    
                    <u--form-item label="活动名称">
                        <u--input 
                            v-model="activityName" 
                            placeholder="请选择活动" 
                            disabled
                            suffix-icon="arrow-down"
                            @click="showActivityPicker = true"
                        ></u--input>
                    </u--form-item>
                    
                    <u--form-item label="总结标题">
                        <u--input v-model="form.title" placeholder="请输入总结标题"></u--input>
                    </u--form-item>
                    
                    <u--form-item label="活动照片">
                        <u-upload 
                            :file-list="fileList" 
                            @after-read="afterRead"
                            @delete="deleteFile"
                            :max-count="9"
                            multiple
                            width="150"
                            height="150"
                        ></u-upload>
                    </u--form-item>
                </view>
                
                <view class="form-section">
                    <view class="section-title">总结内容</view>
                    
                    <u--form-item label="活动亮点">
                        <u--textarea 
                            v-model="form.highlights" 
                            placeholder="请输入活动亮点"
                            :maxlength="500"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="活动内容">
                        <u--textarea 
                            v-model="form.content" 
                            placeholder="请输入活动详细内容"
                            :maxlength="2000"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="不足之处">
                        <u--textarea 
                            v-model="form.shortcomings" 
                            placeholder="请输入活动不足之处"
                            :maxlength="500"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="改进措施">
                        <u--textarea 
                            v-model="form.improvements" 
                            placeholder="请输入改进措施"
                            :maxlength="500"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                    
                    <u--form-item label="活动成果">
                        <u--textarea 
                            v-model="form.achievements" 
                            placeholder="请输入活动成果"
                            :maxlength="500"
                            :auto-height="true"
                            :border="false"
                        ></u--textarea>
                    </u--form-item>
                </view>
                
                <view class="form-section">
                    <view class="section-title">发布设置</view>
                    
                    <u--form-item label="同步到社团">
                        <u-switch v-model="form.syncToClubPage"></u-switch>
                    </u--form-item>
                    
                    <u--form-item label="对外公开">
                        <u-switch v-model="form.isPublic"></u-switch>
                    </u--form-item>
                </view>
            </u--form>
            
            <view style="height: 160rpx;"></view>
        </scroll-view>
        
        <view class="bottom-bar">
            <u-button type="primary" size="large" :loading="submitting" @click="publishSummary">
                发布总结
            </u-button>
        </view>
        
        <u-picker 
            :show="showActivityPicker" 
            :columns="activityOptions" 
            keyName="name"
            @confirm="confirmActivity"
            @cancel="showActivityPicker = false"
        ></u-picker>
    </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { summaryApi, activityApi } from '@/utils/api'

const route = useRoute()
const activityId = ref(route.query.activityId)
const submitting = ref(false)
const showActivityPicker = ref(false)
const fileList = ref([])
const activityName = ref('')
const activityOptions = ref([])

const form = reactive({
    activityId: null,
    title: '',
    content: '',
    highlights: '',
    shortcomings: '',
    improvements: '',
    achievements: '',
    photos: '',
    syncToClubPage: true,
    isPublic: true
})

const loadActivities = async () => {
    try {
        const res = await activityApi.getList({ page: 1, size: 100, status: 'COMPLETED' })
        const list = res.content || res.data?.content || []
        activityOptions.value = list.map(item => ({
            name: item.name,
            id: item.id
        }))
        
        if (activityId.value) {
            const activity = activityOptions.value.find(a => a.id === Number(activityId.value))
            if (activity) {
                activityName.value = activity.name
                form.activityId = activity.id
            }
        }
    } catch (e) {
        console.error('加载活动列表失败:', e)
    }
}

const confirmActivity = (e) => {
    form.activityId = e.value[0].id
    activityName.value = e.value[0].name
    showActivityPicker.value = false
}

const afterRead = (event) => {
    const files = Array.isArray(event.file) ? event.file : [event.file]
    files.forEach(file => {
        fileList.value.push({ url: file.url, status: 'uploading', message: '上传中' })
        uni.uploadFile({
            url: uni.$u.http.config.baseUrl + '/upload',
            filePath: file.url,
            name: 'file',
            header: {
                Authorization: 'Bearer ' + uni.getStorageSync('token')
            },
            success: (res) => {
                const data = JSON.parse(res.data)
                const fileUrl = data.data || data.url
                const index = fileList.value.findIndex(f => f.url === file.url)
                if (index > -1) {
                    fileList.value[index] = { url: fileUrl, status: 'success', message: '上传成功' }
                }
                form.photos = fileList.value.filter(f => f.status === 'success').map(f => f.url).join(',')
            },
            fail: () => {
                const index = fileList.value.findIndex(f => f.url === file.url)
                if (index > -1) {
                    fileList.value[index] = { url: file.url, status: 'failed', message: '上传失败' }
                }
            }
        })
    })
}

const deleteFile = (item) => {
    const index = fileList.value.findIndex(f => f.url === item.url)
    if (index > -1) {
        fileList.value.splice(index, 1)
    }
    form.photos = fileList.value.filter(f => f.status === 'success').map(f => f.url).join(',')
}

const validateForm = () => {
    if (!form.activityId) {
        uni.showToast({ title: '请选择活动', icon: 'none' })
        return false
    }
    if (!form.title) {
        uni.showToast({ title: '请输入总结标题', icon: 'none' })
        return false
    }
    if (!form.content) {
        uni.showToast({ title: '请输入活动内容', icon: 'none' })
        return false
    }
    return true
}

const publishSummary = async () => {
    if (!validateForm()) return
    
    submitting.value = true
    try {
        await summaryApi.create(form)
        await summaryApi.publish(form.activityId)
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

onMounted(() => {
    loadActivities()
})
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
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
}
</style>
