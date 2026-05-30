<template>
  <div class="page-container">
    <div class="page-header">
      <h3>举报处理</h3>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 28px; color: #e6a23c; font-weight: bold">
              {{ statistics?.statusDistribution?.pending || 0 }}
            </div>
            <div style="color: #909399; margin-top: 5px">待处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 28px; color: #67c23a; font-weight: bold">
              {{ statistics?.statusDistribution?.confirmed || 0 }}
            </div>
            <div style="color: #909399; margin-top: 5px">已确认违规</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 28px; color: #909399; font-weight: bold">
              {{ statistics?.statusDistribution?.rejected || 0 }}
            </div>
            <div style="color: #909399; margin-top: 5px">已驳回</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 28px; color: #409EFF; font-weight: bold">
              {{ statistics?.avgHandleTime?.toFixed(2) || 0 }}
            </div>
            <div style="color: #909399; margin-top: 5px">平均处理时长(分钟)</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">举报类型分布</div>
          <div ref="typeChart" style="height: 250px"></div>
        </el-card>
      </el-col>
    </el-row>

    <div class="search-bar">
      <el-select v-model="searchForm.status" placeholder="处理状态" clearable style="width: 150px">
        <el-option label="待处理" :value="0" />
        <el-option label="已确认违规" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
      <el-select v-model="searchForm.reasonType" placeholder="举报类型" clearable style="width: 150px">
        <el-option label="色情" value="porn" />
        <el-option label="暴力" value="violence" />
        <el-option label="广告" value="advertise" />
        <el-option label="其他" value="other" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="reportId" label="举报ID" width="120" />
      <el-table-column prop="reportType" label="举报对象" width="100">
        <template #default="{ row }">
          {{ getReportTypeText(row.reportType) }}
        </template>
      </el-table-column>
      <el-table-column prop="targetName" label="对象名称" width="200" />
      <el-table-column prop="reasonType" label="举报原因" width="100">
        <template #default="{ row }">
          {{ getReasonTypeText(row.reasonType) }}
        </template>
      </el-table-column>
      <el-table-column prop="reasonDetail" label="详情" min-width="150" show-overflow-tooltip />
      <el-table-column prop="reporterName" label="举报人" width="120" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :class="getReportStatusClass(row.status)">
            {{ getReportStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="举报时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 0"
            type="success"
            link
            @click="handleConfirm(row)"
          >
            确认违规
          </el-button>
          <el-button
            v-if="row.status === 0"
            type="warning"
            link
            @click="handleReject(row)"
          >
            驳回
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.pageNum"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: flex-end"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import { getReportList, handleReport, getReportStatistics } from '../../api/audit'

const loading = ref(false)
const tableData = ref([])
const statistics = ref(null)
const searchForm = reactive({
  status: null,
  reasonType: ''
})
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})
const typeChart = ref(null)
let typeChartInstance = null

const formatDate = (date) => {
  if (!date) return '-'
  return date
}

const getReportTypeText = (type) => {
  const map = { user: '用户', comment: '评论', game: '游戏' }
  return map[type] || type
}

const getReasonTypeText = (type) => {
  const map = { porn: '色情', violence: '暴力', advertise: '广告', other: '其他' }
  return map[type] || type
}

const getReportStatusClass = (status) => {
  const classes = ['status-tag-pending', 'status-tag-banned', 'status-tag-normal']
  return classes[status] || ''
}

const getReportStatusText = (status) => {
  const texts = ['待处理', '已确认违规', '已驳回']
  return texts[status] || '未知'
}

const fetchStatistics = async () => {
  try {
    const res = await getReportStatistics()
    if (res.data.code === 200) {
      statistics.value = res.data.data
      nextTick(() => {
        initChart()
      })
    }
  } catch (error) {
    console.error('获取统计数据失败')
  }
}

const initChart = () => {
  if (!typeChart.value || !statistics.value) return
  if (typeChartInstance) {
    typeChartInstance.dispose()
  }
  typeChartInstance = echarts.init(typeChart.value)
  const data = statistics.value.typeDistribution || {}
  const option = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%', left: 'center' },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: {
          label: { show: true, fontSize: 16, fontWeight: 'bold' }
        },
        data: [
          { value: data.porn || 0, name: '色情', itemStyle: { color: '#f56c6c' } },
          { value: data.violence || 0, name: '暴力', itemStyle: { color: '#e6a23c' } },
          { value: data.advertise || 0, name: '广告', itemStyle: { color: '#409EFF' } },
          { value: data.other || 0, name: '其他', itemStyle: { color: '#909399' } }
        ]
      }
    ]
  }
  typeChartInstance.setOption(option)
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getReportList(params)
    if (res.data.code === 200) {
      tableData.value = res.data.data.records
      pagination.total = res.data.data.total
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.status = null
  searchForm.reasonType = ''
  pagination.pageNum = 1
  fetchData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定该举报内容违规吗？将执行相应处罚', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await handleReport({
      reportId: row.reportId,
      status: 1,
      handlerId: 'ADMIN001',
      handlerName: '系统管理员'
    })
    if (res.data.code === 200) {
      ElMessage.success('已确认违规')
      fetchData()
      fetchStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleReject = async (row) => {
  try {
    await ElMessageBox.confirm('确定驳回该举报吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await handleReport({
      reportId: row.reportId,
      status: 2,
      handlerId: 'ADMIN001',
      handlerName: '系统管理员'
    })
    if (res.data.code === 200) {
      ElMessage.success('已驳回')
      fetchData()
      fetchStatistics()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  fetchData()
  fetchStatistics()
})
</script>
