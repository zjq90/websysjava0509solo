<template>
  <el-container class="main-layout">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon size="28"><Wallet /></el-icon>
        <span>个人记账系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="menu"
        background-color="#001529"
        text-color="#ffffffa6"
        active-text-color="#ffffff">
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/transactions">
          <el-icon><Money /></el-icon>
          <span>收支录入</span>
        </el-menu-item>
        <el-menu-item index="/accounts">
          <el-icon><Wallet /></el-icon>
          <span>账户管理</span>
        </el-menu-item>
        <el-menu-item index="/categories">
          <el-icon><Menu /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/tags">
          <el-icon><PriceTag /></el-icon>
          <span>账单标签</span>
        </el-menu-item>
        <el-menu-item index="/transfers">
          <el-icon><Swap /></el-icon>
          <span>转账记录</span>
        </el-menu-item>
        <el-menu-item index="/category-rules">
          <el-icon><Setting /></el-icon>
          <span>分类规则</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span>{{ currentPageTitle }}</span>
        </div>
        <div class="header-right">
          <el-avatar :size="32" style="background-color: #409EFF">A</el-avatar>
          <span style="margin-left: 10px">管理员</span>
        </div>
      </el-header>
      <el-main class="main">
        <router-view v-slot="{ Component }">
          <component :is="Component" />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Wallet, DataAnalysis, Money, Menu, PriceTag, Swap, Setting } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentPageTitle = computed(() => route.meta.title || '个人记账系统')
</script>

<style lang="scss" scoped>
.main-layout {
  height: 100vh;
}

.aside {
  background-color: #001529;
  display: flex;
  flex-direction: column;

  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    border-bottom: 1px solid #1f1f1f;

    span {
      margin-left: 10px;
    }
  }

  .menu {
    flex: 1;
    border-right: none;
  }
}

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-left {
    font-size: 16px;
    font-weight: 500;
  }

  .header-right {
    display: flex;
    align-items: center;
  }
}

.main {
  background-color: #f0f2f5;
  padding: 0;
}
</style>
