<template>
    <view class="weather-container">
        <view class="current-weather">
            <view class="weather-header">
                <view class="location-info">
                    <text class="location-icon">📍</text>
                    <text class="location-name">{{ currentWeather.cityName || '当前位置' }}</text>
                </view>
                <view class="weather-date">{{ formatDate(currentWeather.recordDate) }}</view>
            </view>

            <view class="weather-main">
                <view class="weather-icon-big">
                    {{ getWeatherIcon(currentWeather.weatherCondition) }}
                </view>
                <view class="temperature-section">
                    <text class="temperature">{{ currentWeather.temperature || '--' }}</text>
                    <text class="temperature-unit">°C</text>
                </view>
                <view class="weather-condition">
                    {{ getConditionLabel(currentWeather.weatherCondition) }}
                </view>
            </view>

            <view class="weather-details">
                <view class="detail-item">
                    <text class="detail-icon">💧</text>
                    <view class="detail-content">
                        <text class="detail-label">湿度</text>
                        <text class="detail-value">{{ currentWeather.humidity || '--' }}%</text>
                    </view>
                </view>
                <view class="detail-item">
                    <text class="detail-icon">💨</text>
                    <view class="detail-content">
                        <text class="detail-label">风速</text>
                        <text class="detail-value">{{ currentWeather.windSpeed || '--' }} m/s</text>
                    </view>
                </view>
                <view class="detail-item">
                    <text class="detail-icon">🧭</text>
                    <view class="detail-content">
                        <text class="detail-label">风向</text>
                        <text class="detail-value">{{ getWindDirection(currentWeather.windDirection) }}</text>
                    </view>
                </view>
            </view>

            <view class="weather-details">
                <view class="detail-item">
                    <text class="detail-icon">🌡️</text>
                    <view class="detail-content">
                        <text class="detail-label">体感温度</text>
                        <text class="detail-value">{{ currentWeather.feelsLike || '--' }}°C</text>
                    </view>
                </view>
                <view class="detail-item">
                    <text class="detail-icon">☁️</text>
                    <view class="detail-content">
                        <text class="detail-label">云量</text>
                        <text class="detail-value">{{ currentWeather.cloudCover || '--' }}%</text>
                    </view>
                </view>
                <view class="detail-item">
                    <text class="detail-icon">🌧️</text>
                    <view class="detail-content">
                        <text class="detail-label">降水概率</text>
                        <text class="detail-value">{{ currentWeather.precipitation || '--' }}%</text>
                    </view>
                </view>
            </view>

            <view class="record-time">
                数据更新时间: {{ formatDateTime(currentWeather.createdTime) }}
            </view>
        </view>

        <view class="section-title">
            <text class="title-icon">📊</text>
            <text class="title-text">历史气象数据</text>
            <view class="refresh-btn" @click="loadData">
                🔄 刷新
            </view>
        </view>

        <view class="weather-list">
            <view 
                class="weather-item" 
                v-for="weather in weatherList" 
                :key="weather.id"
            >
                <view class="item-left">
                    <view class="item-weather-icon">
                        {{ getWeatherIcon(weather.weatherCondition) }}
                    </view>
                    <view class="item-date-info">
                        <text class="item-date">{{ formatDate(weather.recordDate) }}</text>
                        <text class="item-time">{{ formatTime(weather.recordDate) }}</text>
                    </view>
                </view>

                <view class="item-center">
                    <text class="item-temp">{{ weather.temperature }}°C</text>
                    <text class="item-cond">{{ getConditionLabel(weather.weatherCondition) }}</text>
                </view>

                <view class="item-right">
                    <text class="item-humidity">💧 {{ weather.humidity }}%</text>
                    <text class="item-wind">💨 {{ weather.windSpeed }} m/s</text>
                </view>
            </view>

            <view class="empty-tip" v-if="weatherList.length === 0">
                <text class="empty-icon">🌤️</text>
                <text>暂无历史数据</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            currentWeather: {},
            weatherList: []
        }
    },

    onShow() {
        this.loadData()
    },

    methods: {
        async loadData() {
            try {
                const data = await this.$request({
                    url: '/weather-data',
                    method: 'GET'
                })
                const list = data || []
                
                if (list.length > 0) {
                    this.currentWeather = list[0]
                    this.weatherList = list.slice(1, 10)
                }
            } catch (e) {
                console.error('加载气象数据失败', e)
            }
        },

        getWeatherIcon(condition) {
            const icons = {
                'SUNNY': '☀️',
                'CLOUDY': '☁️',
                'PARTLY_CLOUDY': '⛅',
                'RAINY': '🌧️',
                'STORMY': '⛈️',
                'SNOWY': '❄️',
                'FOGGY': '🌫️',
                'WINDY': '💨'
            }
            return icons[condition] || '☀️'
        },

        getConditionLabel(condition) {
            const labels = {
                'SUNNY': '晴天',
                'CLOUDY': '阴天',
                'PARTLY_CLOUDY': '多云',
                'RAINY': '雨天',
                'STORMY': '暴风雨',
                'SNOWY': '雪天',
                'FOGGY': '雾天',
                'WINDY': '大风'
            }
            return labels[condition] || condition || '未知'
        },

        getWindDirection(direction) {
            const directions = {
                'N': '北风',
                'NE': '东北风',
                'E': '东风',
                'SE': '东南风',
                'S': '南风',
                'SW': '西南风',
                'W': '西风',
                'NW': '西北风'
            }
            return directions[direction] || direction || '--'
        },

        formatDate(dateStr) {
            if (!dateStr) return '今天'
            const date = new Date(dateStr)
            const today = new Date()
            const yesterday = new Date(today)
            yesterday.setDate(yesterday.getDate() - 1)
            
            const isToday = date.toDateString() === today.toDateString()
            const isYesterday = date.toDateString() === yesterday.toDateString()
            
            if (isToday) return '今天'
            if (isYesterday) return '昨天'
            
            return `${date.getMonth() + 1}月${date.getDate()}日`
        },

        formatTime(dateStr) {
            if (!dateStr) return ''
            const date = new Date(dateStr)
            return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
        },

        formatDateTime(dateStr) {
            if (!dateStr) return '--'
            const date = new Date(dateStr)
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
        }
    }
}
</script>

<style>
.weather-container {
    min-height: 100vh;
    background: linear-gradient(180deg, #87CEEB 0%, #E0F7FA 100%);
    padding: 20rpx;
    padding-bottom: 40rpx;
}

.current-weather {
    background: linear-gradient(135deg, rgba(255,255,255,0.9) 0%, rgba(255,255,255,0.7) 100%);
    border-radius: 24rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.weather-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.location-info {
    display: flex;
    align-items: center;
}

.location-icon {
    font-size: 28rpx;
    margin-right: 8rpx;
}

.location-name {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
}

.weather-date {
    font-size: 24rpx;
    color: #666;
}

.weather-main {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;
}

.weather-icon-big {
    font-size: 120rpx;
    margin-bottom: 10rpx;
}

.temperature-section {
    display: flex;
    align-items: flex-start;
}

.temperature {
    font-size: 100rpx;
    font-weight: bold;
    color: #333;
    line-height: 1;
}

.temperature-unit {
    font-size: 36rpx;
    color: #666;
    margin-top: 10rpx;
}

.weather-condition {
    font-size: 32rpx;
    color: #666;
    margin-top: 10rpx;
}

.weather-details {
    display: flex;
    justify-content: space-between;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid rgba(0, 0, 0, 0.05);
}

.detail-item {
    display: flex;
    align-items: center;
    flex: 1;
}

.detail-icon {
    font-size: 28rpx;
    margin-right: 10rpx;
}

.detail-content {
    display: flex;
    flex-direction: column;
}

.detail-label {
    font-size: 22rpx;
    color: #999;
}

.detail-value {
    font-size: 26rpx;
    color: #333;
    font-weight: 500;
}

.record-time {
    text-align: center;
    font-size: 22rpx;
    color: #aaa;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid rgba(0, 0, 0, 0.05);
}

.section-title {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    padding: 0 10rpx;
}

.title-icon {
    font-size: 28rpx;
    margin-right: 10rpx;
}

.title-text {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
}

.refresh-btn {
    margin-left: auto;
    font-size: 24rpx;
    color: #4CAF50;
    background-color: rgba(76, 175, 80, 0.1);
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
}

.weather-list {
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 16rpx;
    overflow: hidden;
}

.weather-item {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.weather-item:last-child {
    border-bottom: none;
}

.item-left {
    display: flex;
    align-items: center;
    width: 40%;
}

.item-weather-icon {
    font-size: 48rpx;
    margin-right: 16rpx;
}

.item-date-info {
    display: flex;
    flex-direction: column;
}

.item-date {
    font-size: 26rpx;
    color: #333;
    font-weight: 500;
}

.item-time {
    font-size: 22rpx;
    color: #999;
    margin-top: 4rpx;
}

.item-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 30%;
}

.item-temp {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.item-cond {
    font-size: 22rpx;
    color: #999;
    margin-top: 4rpx;
}

.item-right {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    width: 30%;
}

.item-humidity {
    font-size: 24rpx;
    color: #666;
}

.item-wind {
    font-size: 24rpx;
    color: #666;
    margin-top: 6rpx;
}

.empty-tip {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 80rpx 0;
}

.empty-icon {
    font-size: 60rpx;
    margin-bottom: 20rpx;
}

.empty-tip text {
    font-size: 28rpx;
    color: #999;
}
</style>
