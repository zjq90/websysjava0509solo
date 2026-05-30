<template>
  <div class="stat-cards">
    <div class="stat-card blue">
      <div class="stat-icon">
        <el-icon :size="32"><User /></el-icon>
      </div>
      <div class="stat-info">
        <div class="stat-value">{{ stats?.todayOnlineUsers || 0 }}</div>
        <div class="stat-label">今日在线用户</div>
      </div>
    </div>
    <div class="stat-card green">
      <div class="stat-icon">
        <el-icon :size="32"><UserFilled /></el-icon>
      </div>
      <div class="stat-info">
        <div class="stat-value">{{ stats?.todayNewUsers || 0 }}</div>
        <div class="stat-label">今日新增用户</div>
      </div>
    </div>
    <div class="stat-card orange">
      <div class="stat-icon">
        <el-icon :size="32"><VideoPlay /></el-icon>
      </div>
      <div class="stat-info">
        <div class="stat-value">{{ stats?.todayGameLaunches || 0 }}</div>
        <div class="stat-label">今日游戏启动次数</div>
      </div>
    </div>
    <div class="stat-card purple">
      <div class="stat-icon">
        <el-icon :size="32"><Money /></el-icon>
      </div>
      <div class="stat-info">
        <div class="stat-value">¥{{ stats?.todayAdRevenue || 0 }}</div>
        <div class="stat-label">今日广告收益</div>
      </div>
    </div>
  </div>
  <div class="extra-stats">
    <div class="extra-stat">
      <span class="label">游戏总数</span>
      <span class="value">{{ stats?.totalGames || 0 }}</span>
    </div>
    <div class="extra-stat">
      <span class="label">已上线</span>
      <span class="value success">{{ stats?.onlineGames || 0 }}</span>
    </div>
    <div class="extra-stat">
      <span class="label">待审核</span>
      <span class="value warning">{{ stats?.pendingGames || 0 }}</span>
    </div>
    <div class="extra-stat">
      <span class="label">用户总数</span>
      <span class="value">{{ stats?.totalUsers || 0 }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  stats: {
    type: Object,
    default: null
  }
})
</script>

<style scoped lang="scss">
.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;

  .stat-card {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 20px;
    border-radius: 8px;
    color: #fff;

    .stat-icon {
      opacity: 0.9;
    }

    .stat-value {
      font-size: 28px;
      font-weight: 700;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 13px;
      opacity: 0.9;
    }
  }
}

.extra-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;

  .extra-stat {
    background: #f5f7fa;
    padding: 12px 16px;
    border-radius: 6px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .label {
      color: #909399;
      font-size: 13px;
    }

    .value {
      font-size: 18px;
      font-weight: 600;
      color: #303133;

      &.success {
        color: #67c23a;
      }

      &.warning {
        color: #e6a23c;
      }
    }
  }
}
</style>
