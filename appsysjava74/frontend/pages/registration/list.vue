<template>
    <view class="container">
        <view class="header">
            <view class="filter-bar">
                <u--input 
                    v-model="searchKey" 
                    placeholder="搜索姓名/学号" 
                    clearable
                    @confirm="loadData"
                    style="flex: 1; margin-right: 16rpx;"
                ></u--input>
                <u-select 
                    v-model="currentStatus" 
                    :list="statusOptions" 
                    :props="{ label: 'label', value: 'value' }"
                    @confirm="loadData"
                >
                    <u-button type="default" size="normal">状态</u-button>
                </u-select>
            </view>
            
            <view class="stats-bar">
                <view class="stat-item">
                    <text class="stat-num text-primary">{{ stats.total }}</text>
                    <text class="stat-label">总报名</text>
                </view>
                <view class="stat-item">
                    <text class="stat-num text-success">{{ stats.approved }}</text>
                    <text class="stat-label">已通过</text>
                </view>
                <view class="stat-item">
                    <text class="stat-num text-warning">{{ stats.pending }}</text>
                    <text class="stat-label">待审核</text>
                </view>
                <view class="stat-item">
                    <text class="stat-num text-danger">{{ stats.rejected }}</text>
                    <text class="stat-label">已拒绝</text>
                </view>
            </view>
        </view>
        
        <view class="action-bar">
            <u-button 
                type="success" 
                size="small" 
                :disabled="selectedIds.length === 0"
                @click="batchApprove"
            >
                批量通过 ({{ selectedIds.length }})
            </u-button>
            <u-button 
                type="danger" 
                size="small" 
                :disabled="selectedIds.length === 0"
                @click="batchReject"
            >
                批量拒绝 ({{ selectedIds.length }})
            </u-button>
            <u-button type="primary" size="small" @click="exportList">导出名单</u-button>
        </view>
        
        <view class="registration-list">
            <view 
                class="reg-item" 
                v-for="item in list" 
                :key="item.id"
                @click="toggleSelect(item)"
            >
                <u-checkbox 
                    :checked="selectedIds.includes(item.id)"
                    @click.stop
                ></u-checkbox>
                
                <view class="reg-info">
                    <view class="reg-header">
                        <text class="reg-name">{{ item.realName }}</text>
                        <u-tag 
                            :type="getStatusType(item.status)" 
                            :text="getStatusText(item.status)"
                            size="mini"
                        ></u-tag>
                    </view>
                    <view class="reg-meta">
                        <text>{{ item.studentNo }}</text>
                        <text>{{ item.department }}</text>
                        <text>{{ item.major }}</text>
                    </view>
                    <view class="reg-meta">
                        <text>{{ item.phone }}</text>
                        <text v-if="item.signInTime" class="signed-in">已签到</text>
                    </view>
                </view>
                
                <view class="reg-actions" v-if="item.status === 'PENDING'">
                    <u-button type="success" size="mini" @click.stop="approve(item)">通过</u-button>
                    <u-button type="danger" size="mini" @click.stop="reject(item)">拒绝</u-button>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="list" text="暂无报名记录"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="list.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
    </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { registrationApi } from '@/utils/api'

const route = useRoute()
const activityId = ref(route.query.activityId)
const searchKey = ref('')
const currentStatus = ref('')
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 20
const hasMore = ref(true)
const list = ref([])
const selectedIds = ref([])

const stats = reactive({
    total: 0,
    approved: 0,
    pending: 0,
    rejected: 0
})

const statusOptions = [
    { label: '全部', value: '' },
    { label: '待审核', value: 'PENDING' },
    { label: '已通过', value: 'APPROVED' },
    { label: '已拒绝', value: 'REJECTED' },
    { label: '已取消', value: 'CANCELLED' }
]

const statusMap = {
    PENDING: { text: '待审核', type: 'warning' },
    APPROVED: { text: '已通过', type: 'success' },
    REJECTED: { text: '已拒绝', type: 'error' },
    CANCELLED: { text: '已取消', type: 'info' }
}

const getStatusText = (status) => statusMap[status]?.text || status
const getStatusType = (status) => statusMap[status]?.type || 'info'

const loadData = async (refresh = false) => {
    if (loading.value) return
    
    loading.value = true
    if (refresh) {
        pageNum.value = 1
        hasMore.value = true
        list.value = []
        selectedIds.value = []
        loadStatus.value = 'loading'
    }
    
    try {
        const params = {
            page: pageNum.value,
            size: pageSize.value,
            keyword: searchKey.value
        }
        if (currentStatus.value) {
            params.status = currentStatus.value
        }
        
        const res = await registrationApi.getActivityList(activityId.value, params)
        const newList = res.content || res.data?.content || []
        
        if (refresh) {
            list.value = newList
        } else {
            list.value = [...list.value, ...newList]
        }
        
        hasMore.value = newList.length >= pageSize.value
        loadStatus.value = hasMore.value ? 'loadmore' : 'nomore'
        
        stats.total = list.value.length
        stats.approved = list.value.filter(i => i.status === 'APPROVED').length
        stats.pending = list.value.filter(i => i.status === 'PENDING').length
        stats.rejected = list.value.filter(i => i.status === 'REJECTED').length
    } catch (e) {
        loadStatus.value = 'loadmore'
    } finally {
        loading.value = false
    }
}

const loadMore = () => {
    if (!hasMore.value || loading.value) return
    pageNum.value++
    loadData()
}

const toggleSelect = (item) => {
    const index = selectedIds.value.indexOf(item.id)
    if (index > -1) {
        selectedIds.value.splice(index, 1)
    } else {
        selectedIds.value.push(item.id)
    }
}

const approve = async (item) => {
    uni.showModal({
        title: '审核通过',
        content: `确定通过 ${item.realName} 的报名申请？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    await registrationApi.audit({
                        registrationId: item.id,
                        status: 'APPROVED',
                        remark: '审核通过'
                    })
                    uni.showToast({ title: '审核通过', icon: 'success' })
                    loadData(true)
                } catch (e) {
                    console.error('审核失败:', e)
                }
            }
        }
    })
}

const reject = async (item) => {
    uni.showModal({
        title: '审核拒绝',
        content: `确定拒绝 ${item.realName} 的报名申请？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    await registrationApi.audit({
                        registrationId: item.id,
                        status: 'REJECTED',
                        remark: '审核拒绝'
                    })
                    uni.showToast({ title: '已拒绝', icon: 'success' })
                    loadData(true)
                } catch (e) {
                    console.error('审核失败:', e)
                }
            }
        }
    })
}

const batchApprove = async () => {
    if (selectedIds.value.length === 0) return
    
    uni.showModal({
        title: '批量审核',
        content: `确定通过选中的 ${selectedIds.value.length} 条报名申请？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    const auditList = selectedIds.value.map(id => ({
                        registrationId: id,
                        status: 'APPROVED',
                        remark: '批量审核通过'
                    }))
                    await registrationApi.batchAudit(auditList)
                    uni.showToast({ title: '批量审核完成', icon: 'success' })
                    loadData(true)
                } catch (e) {
                    console.error('批量审核失败:', e)
                }
            }
        }
    })
}

const batchReject = async () => {
    if (selectedIds.value.length === 0) return
    
    uni.showModal({
        title: '批量拒绝',
        content: `确定拒绝选中的 ${selectedIds.value.length} 条报名申请？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    const auditList = selectedIds.value.map(id => ({
                        registrationId: id,
                        status: 'REJECTED',
                        remark: '批量审核拒绝'
                    }))
                    await registrationApi.batchAudit(auditList)
                    uni.showToast({ title: '批量拒绝完成', icon: 'success' })
                    loadData(true)
                } catch (e) {
                    console.error('批量拒绝失败:', e)
                }
            }
        }
    })
}

const exportList = async () => {
    try {
        const filePath = await registrationApi.exportList(activityId.value, {
            keyword: searchKey.value
        })
        uni.openDocument({
            filePath,
            showMenu: true,
            success: () => {
                uni.showToast({ title: '导出成功', icon: 'success' })
            },
            fail: () => {
                uni.saveImageToPhotosAlbum({
                    filePath,
                    success: () => {
                        uni.showToast({ title: '已保存到相册', icon: 'success' })
                    }
                })
            }
        })
    } catch (e) {
        console.error('导出失败:', e)
    }
}

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.header {
    background: #fff;
    padding: 20rpx;
    
    .filter-bar {
        display: flex;
        align-items: center;
        margin-bottom: 20rpx;
    }
    
    .stats-bar {
        display: flex;
        justify-content: space-around;
        padding: 20rpx 0;
        border-top: 1rpx solid #f0f0f0;
        
        .stat-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            
            .stat-num {
                font-size: 36rpx;
                font-weight: bold;
            }
            
            .stat-label {
                font-size: 24rpx;
                color: #999;
                margin-top: 4rpx;
            }
        }
    }
}

.action-bar {
    display: flex;
    gap: 16rpx;
    padding: 20rpx;
    
    button {
        flex: 1;
    }
}

.registration-list {
    padding: 0 20rpx;
}

.reg-item {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;
    
    .reg-info {
        flex: 1;
        margin-left: 16rpx;
        
        .reg-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 8rpx;
            
            .reg-name {
                font-size: 30rpx;
                font-weight: bold;
                color: #333;
            }
        }
        
        .reg-meta {
            display: flex;
            gap: 20rpx;
            font-size: 24rpx;
            color: #666;
            margin-bottom: 4rpx;
            
            .signed-in {
                color: #5ac725;
            }
        }
    }
    
    .reg-actions {
        display: flex;
        gap: 12rpx;
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
