<template>
  <div class="market-container">
    <!-- 分类筛选 -->
    <div class="category-bar">
      <div
        v-for="(cat, index) in categories"
        :key="index"
        class="category-item"
        :class="{ active: currentCategory === index }"
        @click="switchCategory(index)"
      >
        {{ cat }}
      </div>
    </div>

    <!-- 安全提示 -->
    <el-alert
      title="交易需实名认证 + 人脸识别，资金流向全程监管"
      type="warning"
      :closable="false"
      show-icon
      class="security-alert"
    />

    <!-- 文物列表 -->
    <div class="heritage-grid">
      <div
        class="heritage-card"
        v-for="item in heritageList"
        :key="item.id"
        @click="goToDetail(item.id)"
      >
        <div class="heritage-image-wrapper">
          <img :src="item.image" class="heritage-image" />
          <el-tag v-if="item.isNew" type="danger" size="small" class="new-tag">新品</el-tag>
        </div>
        <div class="heritage-info">
          <div class="heritage-name">{{ item.name }}</div>
          <div class="heritage-category">{{ item.category }}</div>
          <div class="heritage-footer">
            <span class="heritage-price">¥{{ item.price }}</span>
            <span class="heritage-sales">{{ item.sales }}人想要</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentCategory = ref(0)

const categories = ['全部', '瓷器', '青铜器', '书画', '玉器', '杂项']

const heritageList = ref([
  { id: 1, name: '清代青花瓷瓶', category: '瓷器', price: '28,000', image: 'https://picsum.photos/300/300?random=30', sales: 56, isNew: true },
  { id: 2, name: '明代宣德青铜香炉', category: '青铜器', price: '55,000', image: 'https://picsum.photos/300/300?random=31', sales: 32, isNew: false },
  { id: 3, name: '齐白石虾图真迹', category: '书画', price: '120,000', image: 'https://picsum.photos/300/300?random=32', sales: 128, isNew: true },
  { id: 4, name: '新疆和田玉籽料', category: '玉器', price: '35,000', image: 'https://picsum.photos/300/300?random=33', sales: 89, isNew: false },
  { id: 5, name: '清代紫檀木家具', category: '杂项', price: '88,000', image: 'https://picsum.photos/300/300?random=34', sales: 45, isNew: false },
  { id: 6, name: '宋代官窑瓷碗', category: '瓷器', price: '150,000', image: 'https://picsum.photos/300/300?random=35', sales: 67, isNew: true }
])

const switchCategory = (index) => {
  currentCategory.value = index
}

const goToDetail = (id) => {
  router.push(`/heritage/${id}`)
}
</script>

<style scoped>
.market-container {
  padding: 20px;
}

.category-bar {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 8px;
  margin-bottom: 16px;
}

.category-item {
  flex-shrink: 0;
  padding: 8px 20px;
  background: #fff;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.category-item.active {
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  color: #fff;
}

.security-alert {
  margin-bottom: 20px;
}

.heritage-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.heritage-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.heritage-image-wrapper {
  position: relative;
}

.heritage-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.new-tag {
  position: absolute;
  top: 10px;
  left: 10px;
}

.heritage-info {
  padding: 16px;
}

.heritage-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.heritage-category {
  display: inline-block;
  font-size: 12px;
  color: #8B4513;
  background: #f8f5f2;
  padding: 4px 10px;
  border-radius: 12px;
  margin-bottom: 12px;
}

.heritage-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.heritage-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4d4f;
}

.heritage-sales {
  font-size: 12px;
  color: #999;
}

.elder-mode .category-item {
  font-size: 18px;
  padding: 12px 24px;
}

.elder-mode .heritage-name {
  font-size: 18px;
}

.elder-mode .heritage-price {
  font-size: 20px;
}
</style>
