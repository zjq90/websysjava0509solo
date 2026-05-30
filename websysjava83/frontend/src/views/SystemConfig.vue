<template>
  <div class="system-config">
    <el-card class="config-card">
      <div slot="header">
        <span style="font-size: 16px; font-weight: 500">站点配置</span>
      </div>
      <el-form ref="siteForm" :model="siteConfig" label-width="120px">
        <el-form-item label="站点名称" prop="siteName">
          <el-input v-model="siteConfig.siteName" placeholder="请输入站点名称"></el-input>
        </el-form-item>
        <el-form-item label="站点Logo" prop="siteLogo">
          <el-input v-model="siteConfig.siteLogo" placeholder="请输入Logo地址"></el-input>
        </el-form-item>
        <el-form-item label="版权信息" prop="copyright">
          <el-input v-model="siteConfig.copyright" placeholder="请输入版权信息"></el-input>
        </el-form-item>
        <el-form-item label="客服邮箱" prop="contactEmail">
          <el-input v-model="siteConfig.contactEmail" placeholder="请输入客服邮箱"></el-input>
        </el-form-item>
        <el-form-item label="客服电话" prop="contactPhone">
          <el-input v-model="siteConfig.contactPhone" placeholder="请输入客服电话"></el-input>
        </el-form-item>
        <el-form-item label="微信链接" prop="wechatLink">
          <el-input v-model="siteConfig.wechatLink" placeholder="请输入微信链接"></el-input>
        </el-form-item>
        <el-form-item label="微博链接" prop="weiboLink">
          <el-input v-model="siteConfig.weiboLink" placeholder="请输入微博链接"></el-input>
        </el-form-item>
        <el-form-item label="Twitter链接" prop="twitterLink">
          <el-input v-model="siteConfig.twitterLink" placeholder="请输入Twitter链接"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSiteConfig">保存站点配置</el-button>
          <el-button @click="resetSiteConfig">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="config-card" style="margin-top: 20px">
      <div slot="header">
        <span style="font-size: 16px; font-weight: 500">SEO配置</span>
      </div>
      <el-form ref="seoForm" :model="seoConfig" label-width="120px">
        <el-form-item label="页面标题" prop="seoTitle">
          <el-input v-model="seoConfig.seoTitle" placeholder="请输入页面标题"></el-input>
        </el-form-item>
        <el-form-item label="页面描述" prop="seoDescription">
          <el-input
            type="textarea"
            :rows="3"
            v-model="seoConfig.seoDescription"
            placeholder="请输入页面描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="关键词" prop="seoKeywords">
          <el-input
            type="textarea"
            :rows="2"
            v-model="seoConfig.seoKeywords"
            placeholder="请输入关键词，多个关键词用逗号分隔"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSeoConfig">保存SEO配置</el-button>
          <el-button @click="resetSeoConfig">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'SystemConfig',
  data() {
    return {
      siteConfig: {
        siteName: '',
        siteLogo: '',
        copyright: '',
        contactEmail: '',
        contactPhone: '',
        wechatLink: '',
        weiboLink: '',
        twitterLink: ''
      },
      seoConfig: {
        seoTitle: '',
        seoDescription: '',
        seoKeywords: ''
      },
      originalConfig: null
    }
  },
  mounted() {
    this.fetchConfig()
  },
  methods: {
    async fetchConfig() {
      try {
        const config = await this.$api.getSystemConfig()
        this.originalConfig = config
        this.siteConfig = {
          siteName: config.siteName || '',
          siteLogo: config.siteLogo || '',
          copyright: config.copyright || '',
          contactEmail: config.contactEmail || '',
          contactPhone: config.contactPhone || '',
          wechatLink: config.wechatLink || '',
          weiboLink: config.weiboLink || '',
          twitterLink: config.twitterLink || ''
        }
        this.seoConfig = {
          seoTitle: config.seoTitle || '',
          seoDescription: config.seoDescription || '',
          seoKeywords: config.seoKeywords || ''
        }
      } catch (error) {
        console.error('获取配置失败:', error)
      }
    },
    async saveSiteConfig() {
      try {
        const configData = {
          ...this.siteConfig,
          ...this.seoConfig
        }
        await this.$api.updateSystemConfig(configData)
        this.$message.success('站点配置保存成功')
        this.fetchConfig()
      } catch (error) {
        console.error('保存配置失败:', error)
      }
    },
    async saveSeoConfig() {
      try {
        const configData = {
          ...this.siteConfig,
          ...this.seoConfig
        }
        await this.$api.updateSystemConfig(configData)
        this.$message.success('SEO配置保存成功')
        this.fetchConfig()
      } catch (error) {
        console.error('保存配置失败:', error)
      }
    },
    resetSiteConfig() {
      if (this.originalConfig) {
        this.siteConfig = {
          siteName: this.originalConfig.siteName || '',
          siteLogo: this.originalConfig.siteLogo || '',
          copyright: this.originalConfig.copyright || '',
          contactEmail: this.originalConfig.contactEmail || '',
          contactPhone: this.originalConfig.contactPhone || '',
          wechatLink: this.originalConfig.wechatLink || '',
          weiboLink: this.originalConfig.weiboLink || '',
          twitterLink: this.originalConfig.twitterLink || ''
        }
      }
    },
    resetSeoConfig() {
      if (this.originalConfig) {
        this.seoConfig = {
          seoTitle: this.originalConfig.seoTitle || '',
          seoDescription: this.originalConfig.seoDescription || '',
          seoKeywords: this.originalConfig.seoKeywords || ''
        }
      }
    }
  }
}
</script>

<style scoped>
.system-config {
  padding: 20px;
}

.config-card {
  max-width: 800px;
}
</style>
