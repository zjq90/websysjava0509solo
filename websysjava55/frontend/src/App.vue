<template>
  <el-container class="layout-container">
    <el-aside width="240px">
      <div style="padding: 20px; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1);">
        <h2 style="color: #fff; font-size: 18px; margin: 0;">🏛️ 文物收藏管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="transparent"
        text-color="#9ca3af"
        active-text-color="#fff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/cultural-relic">
          <el-icon><Collection /></el-icon>
          <span>文物管理</span>
        </el-menu-item>
        <el-menu-item index="/blockchain-node">
          <el-icon><Link /></el-icon>
          <span>区块链节点</span>
        </el-menu-item>
        <el-menu-item index="/iot-device">
          <el-icon><Monitor /></el-icon>
          <span>物联网设备</span>
        </el-menu-item>
        <el-menu-item index="/audit-rule">
          <el-icon><DocumentChecked /></el-icon>
          <span>审核规则</span>
        </el-menu-item>
        <el-menu-item index="/digital-twin">
          <el-icon><MagicStick /></el-icon>
          <span>数字分身</span>
        </el-menu-item>
        <el-menu-item index="/vr-scene">
          <el-icon><View /></el-icon>
          <span>VR博物馆</span>
        </el-menu-item>
        <el-menu-item index="/recognition-model">
          <el-icon><Camera /></el-icon>
          <span>识别模型</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div>
          <span style="font-size: 18px; font-weight: 600; color: #1f2937;">
            {{ pageTitle }}
          </span>
        </div>
        <div style="display: flex; align-items: center; gap: 16px;">
          <el-avatar :size="32" style="background: #3b82f6;">管</el-avatar>
          <span>管理员</span>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta?.title || '文物收藏管理系统')
</script>
