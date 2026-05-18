<template>
  <el-container style="height: 100%;">
    <el-aside width="200px" style="background-color: #2f4050;">
      <div style="color: white; text-align: center; padding: 20px 0; font-size: 18px; font-weight: bold;">
        🌸 花店管理
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        router
        background-color="#2f4050"
        text-color="#a7b1c2"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-data-line"></i>
          <span slot="title">数据看板</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <i class="el-icon-goods"></i>
          <span slot="title">商品管理</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <i class="el-icon-document"></i>
          <span slot="title">订单管理</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <i class="el-icon-user"></i>
          <span slot="title">客户管理</span>
        </el-menu-item>
        <el-menu-item index="/analytics">
          <i class="el-icon-s-data"></i>
          <span slot="title">数据分析</span>
        </el-menu-item>
        <el-menu-item index="/content">
          <i class="el-icon-document-copy"></i>
          <span slot="title">内容管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header style="background-color: #ffffff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: space-between;">
        <div style="font-size: 16px; font-weight: bold; color: #333;">
          {{ $route.meta.title }}
        </div>
        <div style="display: flex; align-items: center;">
          <el-avatar icon="el-icon-user-solid" style="margin-right: 10px;"></el-avatar>
          <span>管理员</span>
        </div>
      </el-header>
      
      <el-main style="background-color: #f3f3f4; overflow-y: auto;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout'
}
</script>

<style scoped>
.el-menu-vertical {
  border-right: none;
}
</style>