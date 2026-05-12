<template>
  <div>
    <h2>📊 医院管理系统 - 首页</h2>
    
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 30px; color: #409EFF; font-weight: bold">{{ stats.patientCount }}</div>
            <div style="color: #909399; margin-top: 10px">患者总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 30px; color: #67C23A; font-weight: bold">{{ stats.hospitalizedCount }}</div>
            <div style="color: #909399; margin-top: 10px">在院患者</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 30px; color: #E6A23C; font-weight: bold">{{ stats.bedCount }}</div>
            <div style="color: #909399; margin-top: 10px">床位总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 30px; color: #F56C6C; font-weight: bold">{{ stats.medicineCount }}</div>
            <div style="color: #909399; margin-top: 10px">药品种类</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>📋 在院患者列表</span>
          </div>
          <el-table :data="hospitalizedList" style="width: 100%" size="small">
            <el-table-column prop="hospitalNo" label="住院号" width="120"></el-table-column>
            <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
            <el-table-column prop="department" label="科室" width="100"></el-table-column>
            <el-table-column prop="bedNo" label="床位号" width="100"></el-table-column>
            <el-table-column prop="admissionDiagnosis" label="入院诊断" show-overflow-tooltip></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>💊 待执行医嘱</span>
          </div>
          <el-table :data="pendingOrders" style="width: 100%" size="small">
            <el-table-column prop="orderNo" label="医嘱号" width="120"></el-table-column>
            <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
            <el-table-column prop="name" label="医嘱名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="category" label="类型" width="80"></el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag size="mini" type="warning">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span>🏥 床位使用情况</span>
          </div>
          <el-row :gutter="10">
            <el-col :span="6" v-for="(bed, index) in bedPreview" :key="index">
              <el-card :body-style="{ padding: '10px', textAlign: 'center' }" 
                       :style="{ background: getBedStatusColor(bed.status) }">
                <div style="font-weight: bold; color: #fff">{{ bed.bedNo }}</div>
                <div style="font-size: 12px; color: #fff">{{ bed.wardName }}</div>
                <div style="font-size: 12px; color: #fff">{{ bed.status }}</div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      stats: {
        patientCount: 0,
        hospitalizedCount: 0,
        bedCount: 0,
        medicineCount: 0
      },
      hospitalizedList: [],
      pendingOrders: [],
      bedPreview: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loadPatients()
      this.loadHospitalizations()
      this.loadBeds()
      this.loadMedicines()
      this.loadPendingOrders()
    },
    loadPatients() {
      this.$http.get('/patient').then(res => {
        if (res.data.code === 200) {
          this.stats.patientCount = res.data.data.length
        }
      })
    },
    loadHospitalizations() {
      this.$http.get('/hospitalization/hospitalized').then(res => {
        if (res.data.code === 200) {
          this.hospitalizedList = res.data.data
          this.stats.hospitalizedCount = res.data.data.length
        }
      })
    },
    loadBeds() {
      this.$http.get('/bed').then(res => {
        if (res.data.code === 200) {
          this.bedPreview = res.data.data.slice(0, 12)
          this.stats.bedCount = res.data.data.length
        }
      })
    },
    loadMedicines() {
      this.$http.get('/medicine').then(res => {
        if (res.data.code === 200) {
          this.stats.medicineCount = res.data.data.length
        }
      })
    },
    loadPendingOrders() {
      this.$http.get('/medical-order').then(res => {
        if (res.data.code === 200) {
          this.pendingOrders = res.data.data.filter(item => item.status === '待执行').slice(0, 5)
        }
      })
    },
    getBedStatusColor(status) {
      const colors = {
        '空闲': '#67C23A',
        '占用': '#409EFF',
        '维修中': '#E6A23C'
      }
      return colors[status] || '#909399'
    }
  }
}
</script>
