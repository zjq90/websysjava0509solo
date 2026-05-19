<template>
  <div class="doctor-audit-page">
    <div class="page-card">
      <div class="page-title">医生资质审核</div>
      
      <div class="toolbar">
        <el-select v-model="auditStatusFilter" placeholder="审核状态" style="width: 150px; margin-right: 10px;">
          <el-option label="全部" :value="null" />
          <el-option label="待审核" :value="0" />
          <el-option label="审核通过" :value="1" />
          <el-option label="审核驳回" :value="2" />
        </el-select>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增医生
        </el-button>
      </div>

      <el-table :data="filteredData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="title" label="职称" width="100" />
        <el-table-column prop="licenseNumber" label="执业证号" min-width="150" />
        <el-table-column label="执业证图片" width="120">
          <template #default="{ row }">
            <el-image
              v-if="row.licenseImageUrl"
              :src="row.licenseImageUrl"
              style="width: 80px; height: 60px; cursor: pointer;"
              fit="cover"
              :preview-src-list="[row.licenseImageUrl]"
              @error="imageError"
            />
            <span v-else style="color: #999;">未上传</span>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.auditStatus === 1 ? 'success' : row.auditStatus === 2 ? 'danger' : 'warning'" size="small">
              {{ ['待审核', '审核通过', '审核驳回'][row.auditStatus] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditRemark" label="审核意见" min-width="150" show-overflow-tooltip />
        <el-table-column prop="auditTime" label="审核时间" width="160">
          <template #default="{ row }">
            <span v-if="row.auditTime">{{ formatDate(row.auditTime) }}</span>
            <span v-else style="color: #999;">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="primary" @click="handleAudit(row)" v-if="row.auditStatus === 0">
              审核
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑医生对话框 -->
    <el-dialog v-model="doctorDialogVisible" :title="isEdit ? '编辑医生' : '新增医生'" width="600px">
      <el-form :model="doctorForm" label-width="100px" :rules="doctorRules" ref="doctorFormRef">
        <el-form-item label="医生姓名" prop="name">
          <el-input v-model="doctorForm.name" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="doctorForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="所属科室" prop="departmentId">
          <el-select v-model="doctorForm.departmentId" placeholder="请选择科室" style="width: 100%">
            <el-option v-for="dept in departments" :key="dept.id" :label="dept.name" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="doctorForm.title" placeholder="请输入职称，如：主任医师" />
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-input v-model="doctorForm.specialty" type="textarea" :rows="2" placeholder="请输入专长描述" />
        </el-form-item>
        <el-form-item label="执业证号" prop="licenseNumber">
          <el-input v-model="doctorForm.licenseNumber" placeholder="请输入执业证编号" />
        </el-form-item>
        <el-form-item label="执业证图片" prop="licenseImageUrl">
          <el-upload
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :show-file-list="false"
            accept="image/*"
            name="file"
          >
            <el-button type="primary" size="small">上传图片</el-button>
          </el-upload>
          <div v-if="doctorForm.licenseImageUrl" style="margin-top: 10px;">
            <el-image
              :src="doctorForm.licenseImageUrl"
              style="width: 150px; height: 100px;"
              fit="cover"
              :preview-src-list="[doctorForm.licenseImageUrl]"
            />
            <el-button type="danger" size="small" style="margin-left: 10px;" @click="doctorForm.licenseImageUrl = ''">
              删除
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="doctorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDoctorSubmit">提交</el-button>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog v-model="auditDialogVisible" title="医生资质审核" width="600px">
      <el-form label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医生姓名">
              <span>{{ auditForm.name }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <span>{{ auditForm.phone }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="执业证号">
          <span>{{ auditForm.licenseNumber }}</span>
        </el-form-item>
        <el-form-item label="执业证图片">
          <el-image
            v-if="auditForm.licenseImageUrl"
            :src="auditForm.licenseImageUrl"
            style="width: 100%; max-height: 300px;"
            fit="contain"
            :preview-src-list="[auditForm.licenseImageUrl]"
          />
          <span v-else style="color: #999;">未上传执业证图片</span>
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="1">审核通过</el-radio>
            <el-radio :label="2">审核驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="auditForm.auditRemark" type="textarea" :rows="3" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuditSubmit">提交审核</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { 
  getDoctors, 
  getDoctorsByAuditStatus, 
  createDoctor, 
  updateDoctor, 
  deleteDoctor,
  auditDoctor 
} from '@/api'
import { getDepartments } from '@/api'

const tableData = ref([])
const departments = ref([])
const auditDialogVisible = ref(false)
const doctorDialogVisible = ref(false)
const auditStatusFilter = ref(null)
const isEdit = ref(false)
const doctorFormRef = ref(null)

const uploadUrl = '/api/file/upload'
const uploadHeaders = {}

const auditForm = ref({
  id: null,
  name: '',
  phone: '',
  licenseNumber: '',
  licenseImageUrl: '',
  auditStatus: 1,
  auditRemark: ''
})

const doctorForm = ref({
  id: null,
  name: '',
  phone: '',
  departmentId: null,
  title: '',
  specialty: '',
  licenseNumber: '',
  licenseImageUrl: ''
})

const doctorRules = {
  name: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }],
  licenseNumber: [{ required: true, message: '请输入执业证号', trigger: 'blur' }]
}

const filteredData = computed(() => {
  if (auditStatusFilter.value !== null) {
    return tableData.value.filter(item => item.auditStatus === auditStatusFilter.value)
  }
  return tableData.value
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}

const imageError = () => {
  // 图片加载错误处理
}

// 处理图片路径，确保能通过代理正确访问
const processImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http') || url.startsWith('/api')) {
    return url
  }
  return '/api' + url
}

const loadData = async () => {
  const res = await getDoctors()
  tableData.value = res.data.map(item => ({
    ...item,
    licenseImageUrl: processImageUrl(item.licenseImageUrl)
  }))
}

const loadDepartments = async () => {
  const res = await getDepartments()
  departments.value = res.data
}

const handleAdd = () => {
  isEdit.value = false
  doctorForm.value = {
    id: null,
    name: '',
    phone: '',
    departmentId: null,
    title: '',
    specialty: '',
    licenseNumber: '',
    licenseImageUrl: ''
  }
  doctorDialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  doctorForm.value = {
    id: row.id,
    name: row.name,
    phone: row.phone,
    departmentId: row.departmentId,
    title: row.title,
    specialty: row.specialty,
    licenseNumber: row.licenseNumber,
    licenseImageUrl: row.licenseImageUrl
  }
  doctorDialogVisible.value = true
}

const handleUploadSuccess = (response, file) => {
  console.log('上传响应:', response)
  if (response.code === 200 || response.code === 0) {
    // 确保图片路径包含 /api 前缀以便通过代理正确访问
    let imageUrl = response.data
    if (imageUrl && !imageUrl.startsWith('/api') && !imageUrl.startsWith('http')) {
      imageUrl = '/api' + imageUrl
    }
    doctorForm.value.licenseImageUrl = imageUrl
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || response.message || '上传失败')
  }
}

const handleUploadError = (error) => {
  console.error('上传错误:', error)
  ElMessage.error('图片上传失败: ' + (error.message || '未知错误'))
}

const handleDoctorSubmit = async () => {
  if (!doctorFormRef.value) return
  await doctorFormRef.value.validate(async (valid) => {
    if (valid) {
      if (isEdit.value) {
        await updateDoctor(doctorForm.value.id, doctorForm.value)
        ElMessage.success('更新成功')
      } else {
        await createDoctor(doctorForm.value)
        ElMessage.success('提交成功，请等待审核')
      }
      doctorDialogVisible.value = false
      loadData()
    }
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteDoctor(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {
    // 取消删除
  }
}

const handleAudit = (row) => {
  auditForm.value.id = row.id
  auditForm.value.name = row.name
  auditForm.value.phone = row.phone
  auditForm.value.licenseNumber = row.licenseNumber
  auditForm.value.licenseImageUrl = row.licenseImageUrl
  auditForm.value.auditStatus = 1
  auditForm.value.auditRemark = ''
  auditDialogVisible.value = true
}

const handleAuditSubmit = async () => {
  await auditDoctor(auditForm.value.id, auditForm.value.auditStatus, auditForm.value.auditRemark)
  ElMessage.success('审核成功')
  auditDialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
  loadDepartments()
})
</script>
