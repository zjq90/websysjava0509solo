<template>
    <view class="container">
        <view class="search-section">
            <view class="search-bar">
                <u-icon name="search" size="30" color="#999" class="search-icon"></u-icon>
                <input 
                    type="text" 
                    v-model="keyword" 
                    placeholder="搜索历史活动"
                    class="search-input"
                    @confirm="handleSearch"
                />
                <u-icon 
                    name="close-circle-fill" 
                    size="28" 
                    color="#ccc" 
                    class="clear-icon"
                    v-if="keyword"
                    @click="clearSearch"
                ></u-icon>
            </view>
        </view>
        
        <view class="filter-section">
            <view class="filter-item" @click="showTimePicker = true">
                <u-icon name="calendar" size="24" color="#666"></u-icon>
                <text>{{ selectedDate || '选择日期' }}</text>
            </view>
            <view class="filter-item" @click="showClubPicker = true">
                <u-icon name="users" size="24" color="#666"></u-icon>
                <text>{{ selectedClubName || '全部社团' }}</text>
            </view>
        </view>
        
        <view class="activity-list">
            <view 
                class="activity-card" 
                v-for="item in list" 
                :key="item.id"
                @click="goToDetail(item.id)"
            >
                <view class="activity-poster" v-if="item.posterUrl">
                    <image :src="item.posterUrl" mode="aspectFill"></image>
                </view>
                <view class="activity-poster placeholder" v-else>
                    <u-icon name="image-fill" size="48" color="#ccc"></u-icon>
                </view>
                
                <view class="activity-content">
                    <view class="activity-title ellipsis">{{ item.name }}</view>
                    
                    <view class="activity-meta">
                        <view class="meta-item">
                            <u-icon name="clock" size="22" color="#999"></u-icon>
                            <text>{{ formatDate(item.startTime) }}</text>
                        </view>
                        <view class="meta-item">
                            <u-icon name="map" size="22" color="#999"></u-icon>
                            <text class="ellipsis">{{ item.location }}</text>
                        </view>
                    </view>
                    
                    <view class="activity-footer">
                        <view class="club-name ellipsis">{{ item.clubName }}</view>
                        <view class="activity-stats">
                            <span class="stat">{{ item.registeredCount || 0 }}人报名</span>
                        </view>
                    </view>
                </view>
                
                <view class="activity-badge">
                    <u-tag 
                        type="info" 
                        :text="getStatusText(item.status)"
                        size="mini"
                    ></u-tag>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="list" text="暂无历史活动"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="list.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
        
        <u-picker 
            :show="showTimePicker" 
            mode="date" 
            :params="dateParams"
            @confirm="onDateConfirm"
            @cancel="showTimePicker = false"
        ></u-picker>
        
        <u-picker 
            :show="showClubPicker" 
            :columns="clubColumns"
            @confirm="onClubConfirm"
            @cancel="showClubPicker = false"
        ></u-picker>
    </view>
</template>

<script setup>
import { ref, reactive, onMounted, onPullDownRefresh } from 'vue'
import { activityApi } from '@/utils/api'
import dayjs from 'dayjs'

const keyword = ref('')
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 10
const hasMore = ref(true)
const list = ref([])

const selectedDate = ref('')
const selectedClubId = ref('')
const selectedClubName = ref('')
const showTimePicker = ref(false)
const showClubPicker = ref(false)

const clubColumns = [
    { text: '全部社团', value: '' },
    { text: '计算机协会', value: '1' },
    { text: '摄影协会', value: '2' },
    { text: '篮球社', value: '3' },
    { text: '文学社', value: '4' },
    { text: '音乐社', value: '5' }
]

const dateParams = {
    year: true,
    month: true,
    day: true
}

const statusMap = {
    ARCHIVED: { text: '已归档' }
}

const getStatusText = (status) => statusMap[status]?.text || '已结束'

const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

const loadData = async (refresh = false) => {
    if (loading.value) return
    
    loading.value = true
    if (refresh) {
        pageNum.value = 1
        hasMore.value = true
        list.value = []
        loadStatus.value = 'loading'
    }
    
    try {
        const params = {
            page: pageNum.value,
            size: pageSize.value,
            archived: true,
            keyword: keyword.value || undefined,
            date: selectedDate.value || undefined,
            clubId: selectedClubId.value || undefined
        }
        
        const res = await activityApi.getList(params)
        const newList = res.content || res.data?.content || []
        
        if (refresh) {
            list.value = newList
        } else {
            list.value = [...list.value, ...newList]
        }
        
        hasMore.value = newList.length >= pageSize.value
        loadStatus.value = hasMore.value ? 'loadmore' : 'nomore'
    } catch (e) {
        loadStatus.value = 'loadmore'
    } finally {
        loading.value = false
        if (refresh) {
            uni.stopPullDownRefresh()
        }
    }
}

const loadMore = () => {
    if (!hasMore.value || loading.value) return
    pageNum.value++
    loadData()
}

const handleSearch = () => {
    loadData(true)
}

const clearSearch = () => {
    keyword.value = ''
    loadData(true)
}

const onDateConfirm = (e) => {
    const value = e.year + '-' + String(e.month).padStart(2, '0') + '-' + String(e.day).padStart(2, '0')
    selectedDate.value = value
    showTimePicker.value = false
    loadData(true)
}

const onClubConfirm = (e) => {
    selectedClubId.value = e.value[0].value
    selectedClubName.value = e.value[0].text
    showClubPicker.value = false
    loadData(true)
}

const goToDetail = (id) => {
    uni.navigateTo({
        url: `/pages/activity/detail?id=${id}`
    })
}

onMounted(() => {
    loadData()
})

onPullDownRefresh(() => {
    loadData(true)
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.search-section {
    background: #fff;
    padding: 20rpx;
    position: sticky;
    top: 0;
    z-index: 100;
    
    .search-bar {
        display: flex;
        align-items: center;
        background: #f5f5f5;
        border-radius: 40rpx;
        padding: 0 24rpx;
        height: 72rpx;
        
        .search-icon {
            margin-right: 12rpx;
        }
        
        .search-input {
            flex: 1;
            font-size: 28rpx;
            height: 72rpx;
        }
        
        .clear-icon {
            margin-left: 12rpx;
        }
    }
}

.filter-section {
    display: flex;
    gap: 16rpx;
    padding: 16rpx 20rpx;
    background: #fff;
    border-top: 1rpx solid #f5f5f5;
    
    .filter-item {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8rpx;
        background: #f5f5f5;
        border-radius: 8rpx;
        padding: 16rpx;
        font-size: 24rpx;
        color: #666;
    }
}

.activity-list {
    padding: 20rpx;
}

.activity-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 16rpx;
    overflow: hidden;
    position: relative;
    
    .activity-poster {
        width: 100%;
        height: 240rpx;
        overflow: hidden;
        
        &.placeholder {
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f0f0f0;
        }
        
        image {
            width: 100%;
            height: 100%;
        }
    }
    
    .activity-content {
        padding: 20rpx;
        
        .activity-title {
            font-size: 30rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 12rpx;
        }
        
        .activity-meta {
            margin-bottom: 12rpx;
            
            .meta-item {
                display: flex;
                align-items: center;
                font-size: 24rpx;
                color: #666;
                margin-bottom: 8rpx;
                
                text {
                    margin-left: 8rpx;
                    flex: 1;
                }
            }
        }
        
        .activity-footer {
            display: flex;
            align-items: center;
            justify-content: space-between;
            
            .club-name {
                font-size: 24rpx;
                color: #3c9cff;
                max-width: 300rpx;
            }
            
            .activity-stats {
                font-size: 24rpx;
                color: #999;
                
                .stat {
                    margin-left: 16rpx;
                }
            }
        }
    }
    
    .activity-badge {
        position: absolute;
        top: 16rpx;
        right: 16rpx;
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
