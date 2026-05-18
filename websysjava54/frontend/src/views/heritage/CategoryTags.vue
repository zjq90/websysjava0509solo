<template>
  <div class="category-tags">
    <h2>分类标签管理</h2>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header" class="clearfix">
        <span>朝代标签</span>
      </div>
      <el-tag v-for="tag in dynastyTags" :key="tag.id" style="margin: 5px">{{ tag.name }}</el-tag>
    </el-card>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header" class="clearfix">
        <span>材质标签</span>
      </div>
      <el-tag v-for="tag in materialTags" :key="tag.id" type="success" style="margin: 5px">{{ tag.name }}</el-tag>
    </el-card>
    <el-card shadow="hover">
      <div slot="header" class="clearfix">
        <span>用途标签</span>
      </div>
      <el-tag v-for="tag in usageTags" :key="tag.id" type="warning" style="margin: 5px">{{ tag.name }}</el-tag>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'CategoryTags',
  data() {
    return {
      dynastyTags: [],
      materialTags: [],
      usageTags: []
    }
  },
  mounted() {
    this.loadMockData()
  },
  methods: {
    loadMockData() {
      this.dynastyTags = [
        {id: 1, name: '商朝'}, {id: 2, name: '周朝'}, {id: 3, name: '秦朝'},
        {id: 4, name: '汉朝'}, {id: 5, name: '唐朝'}, {id: 6, name: '宋朝'}
      ]
      this.materialTags = [
        {id: 7, name: '青铜'}, {id: 8, name: '瓷器'}, {id: 9, name: '玉器'},
        {id: 10, name: '金器'}, {id: 11, name: '银器'}, {id: 12, name: '木器'}
      ]
      this.usageTags = [
        {id: 13, name: '礼器'}, {id: 14, name: '兵器'}, {id: 15, name: '乐器'},
        {id: 16, name: '饮食器'}, {id: 17, name: '陈设'}, {id: 18, name: '饰品'}
      ]
    }
  }
}
</script>

<style scoped>
.category-tags {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
