<template>
  <div class="home">
    <h2>欢迎使用文物收藏管理后台系统</h2>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>文物总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #409EFF">{{ stats.totalHeritage || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #67C23A">{{ stats.totalUsers || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>交易总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #E6A23C">{{ stats.totalTransactions || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>待审核</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #F56C6C">{{ stats.pendingAudit || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>快捷操作</span>
          </div>
          <div style="display: flex; flex-direction: column; gap: 10px">
            <el-button type="primary" @click="$router.push('/heritage-audit')">文物审核</el-button>
            <el-button type="success" @click="$router.push('/expert-audit')">专家认证审核</el-button>
            <el-button type="warning" @click="$router.push('/abnormal-transactions')">异常交易处理</el-button>
            <el-button type="danger" @click="$router.push('/report-list')">举报处理</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>系统功能简介</span>
          </div>
          <ul>
            <li>文物管理：审核、分类标签、来源验证</li>
            <li>用户管理：实名认证、专家认证、可疑用户标记</li>
            <li>交易监管：异常监测、资金冻结、举报处理</li>
            <li>数据看板：热力图、统计分析、数据导出</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      stats: {}
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    loadStats() {
      this.$http.get('/dashboard/statistics').then(res => {
        if (res.data.code === 200) {
          this.stats = res.data.data
        }
      })
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}
h2 {
  text-align: center;
  margin-bottom: 30px;
}
ul {
  list-style: none;
  padding: 0;
}
li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}
</style>
