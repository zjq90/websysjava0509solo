<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">推荐管理</div>
      <div>
        <el-button @click="handleValidate">
          <el-icon><Refresh /></el-icon>
          验证推荐有效性
        </el-button>
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          新增推荐
        </el-button>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="recommendation-tabs">
      <el-tab-pane label="首页推荐" name="home">
        <RecommendationList ref="homeListRef" type="home" />
      </el-tab-pane>
      <el-tab-pane label="分类推荐" name="category">
        <div class="category-selector">
          <el-radio-group v-model="selectedCategory">
            <el-radio-button v-for="cat in categories" :key="cat.id" :value="cat.id">
              {{ cat.name }}
            </el-radio-button>
          </el-radio-group>
        </div>
        <RecommendationList ref="categoryListRef" type="category" :categoryId="selectedCategory" />
      </el-tab-pane>
      <el-tab-pane label="弹窗推荐" name="popup">
        <RecommendationList ref="popupListRef" type="popup" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" title="新增推荐" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="推荐游戏" required>
          <el-select v-model="form.gameId" placeholder="请选择游戏" filterable style="width: 100%;">
            <el-option
              v-for="game in onlineGames"
              :key="game.id"
              :label="game.name"
              :value="game.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序权重">
          <el-input-number v-model="form.sortWeight" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="生效时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item v-if="activeTab === 'popup'" label="展示频率">
          <el-select v-model="form.displayFrequency" style="width: 100%;">
            <el-option label="每次打开" :value="0" />
            <el-option label="每日一次" :value="1" />
            <el-option label="每周一次" :value="7" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRecommendation">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import RecommendationList from './components/RecommendationList.vue'
import { getAllCategories } from '@/api/categoryTag'
import { getGameList } from '@/api/game'
import { saveRecommendation as apiSaveRecommendation, validateRecommendations } from '@/api/recommendation'

const activeTab = ref('home')
const selectedCategory = ref(null)
const categories = ref([])
const onlineGames = ref([])
const dialogVisible = ref(false)
const dateRange = ref([])
const homeListRef = ref(null)
const categoryListRef = ref(null)
const popupListRef = ref(null)
const form = reactive({
  gameId: null,
  sortWeight: 0,
  displayFrequency: 1,
  startTime: null,
  endTime: null
})

const loadCategories = async () => {
  categories.value = await getAllCategories()
  if (categories.value.length > 0) {
    selectedCategory.value = categories.value[0].id
  }
}

const loadOnlineGames = async () => {
  const res = await getGameList({ pageNum: 1, pageSize: 100, status: 1 })
  onlineGames.value = res.list
}

const showAddDialog = () => {
  form.gameId = null
  form.sortWeight = 0
  form.displayFrequency = 1
  dateRange.value = []
  dialogVisible.value = true
}

const refreshCurrentList = () => {
  const refMap = {
    home: homeListRef,
    category: categoryListRef,
    popup: popupListRef
  }
  const currentRef = refMap[activeTab.value]
  if (currentRef?.value?.loadData) {
    currentRef.value.loadData()
  }
}

const refreshAllLists = () => {
  ;[homeListRef, categoryListRef, popupListRef].forEach(ref => {
    if (ref?.value?.loadData) {
      ref.value.loadData()
    }
  })
}

const handleSaveRecommendation = async () => {
  if (!form.gameId) {
    ElMessage.warning('请选择游戏')
    return
  }
  if (dateRange.value && dateRange.value.length === 2) {
    form.startTime = dateRange.value[0]
    form.endTime = dateRange.value[1]
  }
  const selectedGame = onlineGames.value.find(g => g.id === form.gameId)
  await apiSaveRecommendation({
    gameId: form.gameId,
    gameName: selectedGame?.name,
    type: activeTab.value,
    categoryId: activeTab.value === 'category' ? selectedCategory.value : null,
    sortWeight: form.sortWeight,
    displayFrequency: form.displayFrequency,
    startTime: form.startTime,
    endTime: form.endTime,
    status: 1
  })
  ElMessage.success('保存成功')
  dialogVisible.value = false
  refreshCurrentList()
}

const handleValidate = async () => {
  await validateRecommendations()
  ElMessage.success('验证完成，已自动移除无效推荐')
  refreshAllLists()
}

watch(activeTab, () => {
  loadOnlineGames()
})

onMounted(() => {
  loadCategories()
  loadOnlineGames()
})
</script>

<style scoped lang="scss">
.category-selector {
  margin-bottom: 20px;
}
</style>
