<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">欢迎使用 Web 平台管理系统</div>
    </div>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>代理商总数</span>
            <i class="el-icon-s-custom" style="float: right; font-size: 24px; color: #409eff;"></i>
          </div>
          <div class="text item">
            <div style="font-size: 32px; font-weight: bold; color: #409eff;">5</div>
            <div style="color: #909399; margin-top: 5px;">包含多级代理商体系</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用户总数</span>
            <i class="el-icon-user" style="float: right; font-size: 24px; color: #67c23a;"></i>
          </div>
          <div class="text item">
            <div style="font-size: 32px; font-weight: bold; color: #67c23a;">5</div>
            <div style="color: #909399; margin-top: 5px;">包含管理员和代理商用户</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>系统配置</span>
            <i class="el-icon-setting" style="float: right; font-size: 24px; color: #e6a23c;"></i>
          </div>
          <div class="text item">
            <div style="font-size: 32px; font-weight: bold; color: #e6a23c;">9</div>
            <div style="color: #909399; margin-top: 5px;">系统、支付、短信配置</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>操作日志</span>
            <i class="el-icon-document" style="float: right; font-size: 24px; color: #f56c6c;"></i>
          </div>
          <div class="text item">
            <div style="font-size: 32px; font-weight: bold; color: #f56c6c;">-</div>
            <div style="color: #909399; margin-top: 5px;">记录所有关键操作</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>快速导航</span>
          </div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="代理商管理">
              <el-button type="text" @click="$router.push('/agents')">前往管理 &rarr;</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="操作日志">
              <el-button type="text" @click="$router.push('/logs')">查看日志 &rarr;</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="系统配置">
              <el-button type="text" @click="$router.push('/configs')">配置系统 &rarr;</el-button>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>测试账号信息</span>
          </div>
          <el-table :data="testAccounts" border>
            <el-table-column prop="username" label="用户名" width="140"></el-table-column>
            <el-table-column prop="password" label="密码" width="120"></el-table-column>
            <el-table-column prop="role" label="角色"></el-table-column>
            <el-table-column prop="description" label="说明"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      testAccounts: [
        {
          username: 'admin',
          password: 'admin123',
          role: '超级管理员',
          description: '拥有所有权限'
        },
        {
          username: 'agentadmin1',
          password: 'admin123',
          role: '代理商管理员',
          description: '管理一级代理商A'
        },
        {
          username: 'agentadmin2',
          password: 'admin123',
          role: '代理商管理员',
          description: '管理一级代理商B'
        },
        {
          username: 'agentuser1',
          password: 'admin123',
          role: '代理商用户',
          description: '一级代理商A下的普通用户'
        }
      ]
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}

.text.item {
  text-align: center;
  padding: 20px 0;
}
</style>
