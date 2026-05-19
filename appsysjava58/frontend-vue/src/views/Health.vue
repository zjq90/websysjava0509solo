<template>
  <div class="health-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>健康管理</h2>
    </div>

    <div class="health-cards">
      <div class="health-card" @click="$router.push('/health/deworming')">
        <div class="card-header deworming">
          <span class="card-icon">💊</span>
          <span class="card-title">驱虫管理</span>
        </div>
        <div class="card-body">
          <div class="countdown">
            <span class="countdown-label">下次驱虫</span>
            <span class="countdown-days upcoming">{{ dewormingDays }} 天</span>
          </div>
          <div class="last-record">
            <span class="record-label">上次:</span>
            <span class="record-value">福来恩滴剂 - 2024-01-15</span>
          </div>
          <div class="check-indicator">✅ 已完成</div>
        </div>
      </div>

      <div class="health-card">
        <div class="card-header checkup">
          <span class="card-icon">🏥</span>
          <span class="card-title">体检管理</span>
        </div>
        <div class="card-body">
          <div class="countdown">
            <span class="countdown-label">下次体检</span>
            <span class="countdown-days pending">45 天</span>
          </div>
          <div class="last-record">
            <span class="record-label">上次:</span>
            <span class="record-value">常规体检 - 15天前</span>
          </div>
        </div>
      </div>
    </div>

    <div class="tips-section">
      <h3>💡 健康小贴士</h3>
      <ul>
        <li>幼犬每月驱虫一次，成犬每3个月一次</li>
        <li>定期体检可以早发现潜在健康问题</li>
        <li>疫苗接种后一周内注意观察宠物状态</li>
        <li>春夏季节注意体外驱虫，预防蜱虫</li>
      </ul>
    </div>

    <nav class="bottom-nav">
      <div class="nav-item" @click="$router.push('/home')">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </div>
      <div class="nav-item" @click="$router.push('/circle')">
        <span class="nav-icon">📱</span>
        <span class="nav-text">宠物圈</span>
      </div>
      <div class="nav-item" @click="$router.push('/hospital')">
        <span class="nav-icon">🏥</span>
        <span class="nav-text">医院</span>
      </div>
      <div class="nav-item" @click="$router.push('/mine')">
        <span class="nav-icon">👤</span>
        <span class="nav-text">我的</span>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const dewormingDays = ref(20)
</script>

<style scoped>
.health-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 20px 100px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 24px;
}

.health-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 30px;
}

.health-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.health-card:hover {
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  padding: 20px 25px;
  color: white;
}

.card-header.deworming {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-header.checkup {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.card-icon {
  font-size: 32px;
  margin-right: 15px;
}

.card-title {
  font-size: 20px;
  font-weight: bold;
}

.card-body {
  padding: 25px;
}

.countdown {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.countdown-label {
  font-size: 16px;
  color: #666;
}

.countdown-days {
  font-size: 28px;
  font-weight: bold;
}

.countdown-days.upcoming {
  color: #faad14;
}

.countdown-days.pending {
  color: #1890ff;
}

.last-record {
  display: flex;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.record-label {
  font-size: 14px;
  color: #999;
  margin-right: 10px;
}

.record-value {
  font-size: 14px;
  color: #666;
  flex: 1;
}

.check-indicator {
  margin-top: 15px;
  padding: 8px 16px;
  background: #f6ffed;
  color: #52c41a;
  border-radius: 20px;
  font-size: 14px;
  display: inline-block;
}

.tips-section {
  background: #fffbe6;
  border-radius: 16px;
  padding: 25px;
}

.tips-section h3 {
  margin: 0 0 15px;
  font-size: 18px;
  color: #faad14;
}

.tips-section ul {
  margin: 0;
  padding-left: 20px;
}

.tips-section li {
  font-size: 14px;
  color: #8c6b00;
  line-height: 1.8;
  margin-bottom: 8px;
}

.tips-section li:last-child {
  margin-bottom: 0;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 640px;
  display: flex;
  background: white;
  padding: 10px 0;
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  border-radius: 20px 20px 0 0;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  color: #999;
  transition: color 0.3s;
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.nav-text {
  font-size: 12px;
}
</style>