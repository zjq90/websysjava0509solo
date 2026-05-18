<template>
  <div>
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="公告管理" name="announcement">
        <el-card shadow="hover">
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span>公告列表</span>
            <el-button type="primary" size="small" @click="handleAddAnnouncement">
              <i class="el-icon-plus"></i> 发布公告
            </el-button>
          </div>

          <el-table :data="announcementList" border stripe style="width: 100%;">
            <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
            <el-table-column prop="title" label="公告标题"></el-table-column>
            <el-table-column prop="type" label="类型" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getAnnouncementType(scope.row.type)" size="mini">
                  {{ getAnnouncementTypeText(scope.row.type) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="topFlag" label="置顶" width="80" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.topFlag === 1" type="danger" size="mini">是</el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">
                  {{ scope.row.status === 1 ? '已发布' : '草稿' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="publishTime" label="发布时间" width="160"></el-table-column>
            <el-table-column label="操作" width="180" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleEditAnnouncement(scope.row)">编辑</el-button>
                <el-button type="text" size="small" @click="handleToggleTop(scope.row)">
                  {{ scope.row.topFlag === 1 ? '取消置顶' : '置顶' }}
                </el-button>
                <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteAnnouncement(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="文章管理" name="article">
        <el-card shadow="hover">
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span>文章列表</span>
            <el-button type="primary" size="small" @click="handleAddArticle">
              <i class="el-icon-plus"></i> 发布文章
            </el-button>
          </div>

          <el-table :data="articleList" border stripe style="width: 100%;">
            <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
            <el-table-column prop="title" label="文章标题"></el-table-column>
            <el-table-column prop="category" label="分类" width="100" align="center">
              <template slot-scope="scope">
                <el-tag size="mini">{{ getArticleCategoryText(scope.row.category) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="viewCount" label="浏览量" width="100" align="center"></el-table-column>
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">
                  {{ scope.row.status === 1 ? '已发布' : '草稿' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="author" label="作者" width="100"></el-table-column>
            <el-table-column prop="publishTime" label="发布时间" width="160"></el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="handleEditArticle(scope.row)">编辑</el-button>
                <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteArticle(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="营销活动" name="campaign">
        <el-card shadow="hover">
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span>营销活动列表</span>
            <el-button type="primary" size="small" @click="handleAddCampaign">
              <i class="el-icon-plus"></i> 新建活动
            </el-button>
          </div>

          <el-table :data="campaignList" border stripe style="width: 100%;">
            <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
            <el-table-column prop="name" label="活动名称"></el-table-column>
            <el-table-column prop="type" label="类型" width="100" align="center">
              <template slot-scope="scope">
                <el-tag type="warning" size="mini">{{ getCampaignTypeText(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="targetTags" label="目标客户标签" width="150" show-overflow-tooltip></el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getCampaignStatusType(scope.row.status)" size="mini">
                  {{ getCampaignStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="totalCount" label="发放/参与" width="100" align="center">
              <template slot-scope="scope">{{ scope.row.totalCount }}/{{ scope.row.usedCount || 0 }}</template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="scope">
                <el-dropdown trigger="click" @command="(status) => handleUpdateCampaignStatus(scope.row, status)">
                  <span class="el-dropdown-link" style="color: #409EFF; cursor: pointer; font-size: 12px;">
                    状态设置<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="1">进行中</el-dropdown-item>
                    <el-dropdown-item :command="2">已结束</el-dropdown-item>
                    <el-dropdown-item :command="3">已取消</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteCampaign(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="announcementDialogTitle" :visible.sync="announcementDialogVisible" width="700px">
      <el-form :model="announcementForm" label-width="100px">
        <el-form-item label="公告标题">
          <el-input v-model="announcementForm.title"></el-input>
        </el-form-item>
        <el-form-item label="公告类型">
          <el-select v-model="announcementForm.type">
            <el-option label="系统通知" :value="1"></el-option>
            <el-option label="活动预告" :value="2"></el-option>
            <el-option label="重要提醒" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容">
          <el-input type="textarea" v-model="announcementForm.content" :rows="8"></el-input>
        </el-form-item>
        <el-form-item label="是否置顶">
          <el-switch v-model="announcementForm.topFlag" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="announcementDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAnnouncement">保存草稿</el-button>
        <el-button type="success" @click="publishAnnouncement">立即发布</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="articleDialogTitle" :visible.sync="articleDialogVisible" width="700px">
      <el-form :model="articleForm" label-width="100px">
        <el-form-item label="文章标题">
          <el-input v-model="articleForm.title"></el-input>
        </el-form-item>
        <el-form-item label="文章分类">
          <el-select v-model="articleForm.category">
            <el-option label="养护知识" :value="1"></el-option>
            <el-option label="花语文化" :value="2"></el-option>
            <el-option label="花艺教程" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文章内容">
          <el-input type="textarea" v-model="articleForm.content" :rows="10"></el-input>
        </el-form-item>
        <el-form-item label="SEO关键词">
          <el-input v-model="articleForm.seoKeywords" placeholder="多个关键词用逗号分隔"></el-input>
        </el-form-item>
        <el-form-item label="SEO描述">
          <el-input type="textarea" v-model="articleForm.seoDescription" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="articleForm.author"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="articleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitArticle">保存草稿</el-button>
        <el-button type="success" @click="publishArticle">立即发布</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="campaignDialogTitle" :visible.sync="campaignDialogVisible" width="600px">
      <el-form :model="campaignForm" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="campaignForm.name"></el-input>
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="campaignForm.type">
            <el-option label="优惠券" :value="1"></el-option>
            <el-option label="折扣活动" :value="2"></el-option>
            <el-option label="满减活动" :value="3"></el-option>
            <el-option label="拼团活动" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标客户标签">
          <el-select v-model="campaignForm.targetTags" multiple placeholder="选择客户标签">
            <el-option label="高频客户" value="高频客户"></el-option>
            <el-option label="企业客户" value="企业客户"></el-option>
            <el-option label="VIP客户" value="VIP客户"></el-option>
            <el-option label="新客户" value="新客户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动时间">
          <el-date-picker
            v-model="campaignDateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="campaignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCampaign">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Content',
  data() {
    return {
      activeTab: 'announcement',
      announcementList: [
        { id: 1, title: '母亲节特惠活动开始啦！全场康乃馨8折优惠', type: 2, topFlag: 1, status: 1, publishTime: '2024-05-01 10:00:00' },
        { id: 2, title: '系统升级通知', type: 1, topFlag: 0, status: 1, publishTime: '2024-05-05 14:30:00' },
        { id: 3, title: '520情人节预订开启，提前预订享7.5折优惠', type: 2, topFlag: 1, status: 1, publishTime: '2024-05-10 09:00:00' },
        { id: 4, title: '配送范围扩大通知', type: 3, topFlag: 0, status: 1, publishTime: '2024-05-12 16:00:00' }
      ],
      articleList: [
        { id: 1, title: '玫瑰花的养护小知识', category: 1, viewCount: 1256, status: 1, author: '花艺小助手', publishTime: '2024-04-20 10:00:00', seoKeywords: '玫瑰花,鲜花养护', seoDescription: '详细介绍玫瑰花的养护方法' },
        { id: 2, title: '不同颜色康乃馨的花语', category: 2, viewCount: 896, status: 1, author: '花艺小助手', publishTime: '2024-04-25 14:00:00', seoKeywords: '康乃馨,花语', seoDescription: '介绍不同颜色康乃馨代表的花语' },
        { id: 3, title: '如何延长鲜花的保鲜期', category: 1, viewCount: 1520, status: 1, author: '花艺小助手', publishTime: '2024-05-02 09:30:00', seoKeywords: '鲜花保鲜,花艺技巧', seoDescription: '分享延长鲜花保鲜期的实用技巧' },
        { id: 4, title: '花艺入门：基础插花技巧', category: 3, viewCount: 756, status: 1, author: '花艺小助手', publishTime: '2024-05-08 11:00:00', seoKeywords: '插花技巧,花艺教程', seoDescription: '零基础学习插花的入门技巧' }
      ],
      campaignList: [
        { id: 1, name: '新用户专享优惠券', type: 1, targetTags: '新客户', status: 1, totalCount: 500, usedCount: 128 },
        { id: 2, name: '企业客户批量采购9折', type: 2, targetTags: '企业客户', status: 1, totalCount: 100, usedCount: 35 },
        { id: 3, name: 'VIP会员专属满减活动', type: 3, targetTags: 'VIP客户', status: 0, totalCount: 200, usedCount: 0 },
        { id: 4, name: '3人拼团享8折优惠', type: 4, targetTags: '高频客户,新客户', status: 1, totalCount: 300, usedCount: 86 }
      ],
      announcementDialogVisible: false,
      announcementDialogTitle: '发布公告',
      announcementForm: { id: null, title: '', type: 1, content: '', topFlag: 0, status: 0 },
      articleDialogVisible: false,
      articleDialogTitle: '发布文章',
      articleForm: { id: null, title: '', category: 1, content: '', seoKeywords: '', seoDescription: '', author: '', status: 0 },
      campaignDialogVisible: false,
      campaignDialogTitle: '新建营销活动',
      campaignForm: { id: null, name: '', type: 1, targetTags: [], status: 0 },
      campaignDateRange: []
    }
  },
  methods: {
    getAnnouncementType(type) {
      const map = { 1: '', 2: 'warning', 3: 'danger' }
      return map[type] || ''
    },
    getAnnouncementTypeText(type) {
      const map = { 1: '系统通知', 2: '活动预告', 3: '重要提醒' }
      return map[type] || '其他'
    },
    getArticleCategoryText(category) {
      const map = { 1: '养护知识', 2: '花语文化', 3: '花艺教程' }
      return map[category] || '其他'
    },
    getCampaignTypeText(type) {
      const map = { 1: '优惠券', 2: '折扣', 3: '满减', 4: '拼团' }
      return map[type] || '其他'
    },
    getCampaignStatusType(status) {
      const map = { 0: 'info', 1: 'success', 2: '', 3: 'danger' }
      return map[status] || ''
    },
    getCampaignStatusText(status) {
      const map = { 0: '未开始', 1: '进行中', 2: '已结束', 3: '已取消' }
      return map[status] || '未知'
    },
    handleAddAnnouncement() {
      this.announcementForm = { id: null, title: '', type: 1, content: '', topFlag: 0, status: 0 }
      this.announcementDialogTitle = '发布公告'
      this.announcementDialogVisible = true
    },
    handleEditAnnouncement(row) {
      this.announcementForm = { ...row }
      this.announcementDialogTitle = '编辑公告'
      this.announcementDialogVisible = true
    },
    handleToggleTop(row) {
      row.topFlag = row.topFlag === 1 ? 0 : 1
      this.$message.success(row.topFlag === 1 ? '已置顶' : '已取消置顶')
    },
    handleDeleteAnnouncement(row) {
      this.$confirm(`确定删除公告"${row.title}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.announcementList = this.announcementList.filter(a => a.id !== row.id)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    submitAnnouncement() {
      if (!this.announcementForm.title || !this.announcementForm.content) {
        this.$message.warning('请填写完整信息')
        return
      }
      if (this.announcementForm.id) {
        const index = this.announcementList.findIndex(a => a.id === this.announcementForm.id)
        if (index !== -1) this.announcementList[index] = { ...this.announcementForm }
      } else {
        this.announcementList.unshift({ ...this.announcementForm, id: Date.now(), publishTime: new Date().toLocaleString() })
      }
      this.$message.success('保存成功')
      this.announcementDialogVisible = false
    },
    publishAnnouncement() {
      this.announcementForm.status = 1
      this.submitAnnouncement()
      this.$message.success('发布成功')
    },
    handleAddArticle() {
      this.articleForm = { id: null, title: '', category: 1, content: '', seoKeywords: '', seoDescription: '', author: '花艺小助手', status: 0 }
      this.articleDialogTitle = '发布文章'
      this.articleDialogVisible = true
    },
    handleEditArticle(row) {
      this.articleForm = { ...row }
      this.articleDialogTitle = '编辑文章'
      this.articleDialogVisible = true
    },
    handleDeleteArticle(row) {
      this.$confirm(`确定删除文章"${row.title}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.articleList = this.articleList.filter(a => a.id !== row.id)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    submitArticle() {
      if (!this.articleForm.title || !this.articleForm.content) {
        this.$message.warning('请填写完整信息')
        return
      }
      if (this.articleForm.id) {
        const index = this.articleList.findIndex(a => a.id === this.articleForm.id)
        if (index !== -1) this.articleList[index] = { ...this.articleForm }
      } else {
        this.articleList.unshift({ ...this.articleForm, id: Date.now(), viewCount: 0, publishTime: new Date().toLocaleString() })
      }
      this.$message.success('保存成功')
      this.articleDialogVisible = false
    },
    publishArticle() {
      this.articleForm.status = 1
      this.submitArticle()
      this.$message.success('发布成功')
    },
    handleAddCampaign() {
      this.campaignForm = { id: null, name: '', type: 1, targetTags: [], status: 0 }
      this.campaignDateRange = []
      this.campaignDialogTitle = '新建营销活动'
      this.campaignDialogVisible = true
    },
    handleDeleteCampaign(row) {
      this.$confirm(`确定删除活动"${row.name}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.campaignList = this.campaignList.filter(c => c.id !== row.id)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    handleUpdateCampaignStatus(row, status) {
      row.status = status
      this.$message.success('状态更新成功')
    },
    submitCampaign() {
      if (!this.campaignForm.name) {
        this.$message.warning('请填写活动名称')
        return
      }
      if (this.campaignForm.id) {
        const index = this.campaignList.findIndex(c => c.id === this.campaignForm.id)
        if (index !== -1) this.campaignList[index] = { ...this.campaignForm }
      } else {
        this.campaignList.unshift({ ...this.campaignForm, id: Date.now(), totalCount: 0, usedCount: 0 })
      }
      this.$message.success('保存成功')
      this.campaignDialogVisible = false
    }
  }
}
</script>

<style scoped>
.el-dropdown-link {
  color: #409EFF;
  cursor: pointer;
}
</style>
