<template>
  <div class="marketing">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="优惠券管理" name="coupon">
        <el-card>
          <div slot="header" class="clearfix">
            <span style="float: left; font-size: 18px; font-weight: bold;">优惠券列表</span>
            <el-button style="float: right" type="primary" @click="showCouponDialog">新增优惠券</el-button>
          </div>
          
          <el-table :data="couponData" border stripe>
            <el-table-column prop="couponCode" label="优惠券编码" width="150"></el-table-column>
            <el-table-column prop="name" label="优惠券名称" width="150"></el-table-column>
            <el-table-column prop="type" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type === 'DISCOUNT'" type="warning">折扣券</el-tag>
                <el-tag v-else type="success">满减券</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="discountValue" label="面值" width="100"></el-table-column>
            <el-table-column prop="totalCount" label="发放数量" width="100"></el-table-column>
            <el-table-column prop="usedCount" label="已使用" width="100"></el-table-column>
            <el-table-column prop="targetUserType" label="目标用户" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.targetUserType === 'EXPIRING'" type="warning">即将到期</el-tag>
                <el-tag v-else type="info">低活跃用户</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">有效</el-tag>
                <el-tag v-else type="info">已失效</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template slot-scope="scope">
                <el-button size="mini" type="success" @click="distributeCoupon(scope.row)">发放</el-button>
                <el-button size="mini" type="danger" @click="deleteCoupon(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="活动管理" name="promotion">
        <el-card>
          <div slot="header" class="clearfix">
            <span style="float: left; font-size: 18px; font-weight: bold;">营销活动列表</span>
            <el-button style="float: right" type="primary" @click="showPromotionDialog">新增活动</el-button>
          </div>
          
          <el-table :data="promotionData" border stripe>
            <el-table-column prop="name" label="活动名称" width="200"></el-table-column>
            <el-table-column prop="type" label="活动类型" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type === 'DISCOUNT'" type="warning">限时折扣</el-tag>
                <el-tag v-else-if="scope.row.type === 'PACKAGE'" type="success">套餐组合</el-tag>
                <el-tag v-else type="info">老用户回馈</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="discountRate" label="折扣率" width="100"></el-table-column>
            <el-table-column prop="targetUserType" label="目标用户" width="120">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.targetUserType === 'ALL'" type="success">全部用户</el-tag>
                <el-tag v-else-if="scope.row.targetUserType === 'OLD'" type="info">老用户</el-tag>
                <el-tag v-else type="warning">新用户</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="participantCount" label="参与人数" width="100"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">进行中</el-tag>
                <el-tag v-else-if="scope.row.status === 'DRAFT'" type="info">草稿</el-tag>
                <el-tag v-else type="danger">已结束</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template slot-scope="scope">
                <el-button size="mini" type="success" @click="startPromotion(scope.row)" v-if="scope.row.status !== 'ACTIVE'">启动</el-button>
                <el-button size="mini" type="warning" @click="endPromotion(scope.row)" v-if="scope.row.status === 'ACTIVE'">结束</el-button>
                <el-button size="mini" type="primary" @click="editPromotion(scope.row)">编辑</el-button>
                <el-button size="mini" type="danger" @click="deletePromotion(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="新增优惠券" :visible.sync="couponDialogVisible" width="600px">
      <el-form :model="couponForm" label-width="120px">
        <el-form-item label="优惠券名称">
          <el-input v-model="couponForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="couponForm.type" placeholder="请选择">
            <el-option label="折扣券" value="DISCOUNT"></el-option>
            <el-option label="满减券" value="FULL_REDUCE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="面值">
          <el-input-number v-model="couponForm.discountValue" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="发放数量">
          <el-input-number v-model="couponForm.totalCount" :min="1" :max="10000"></el-input-number>
        </el-form-item>
        <el-form-item label="目标用户类型">
          <el-select v-model="couponForm.targetUserType" placeholder="请选择">
            <el-option label="即将到期用户" value="EXPIRING"></el-option>
            <el-option label="低活跃用户" value="INACTIVE"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="couponDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCoupon">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="新增活动" :visible.sync="promotionDialogVisible" width="600px">
      <el-form :model="promotionForm" label-width="120px">
        <el-form-item label="活动名称">
          <el-input v-model="promotionForm.name"></el-input>
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="promotionForm.type" placeholder="请选择">
            <el-option label="限时折扣" value="DISCOUNT"></el-option>
            <el-option label="套餐组合" value="PACKAGE"></el-option>
            <el-option label="老用户回馈" value="REWARD"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="折扣率(%)">
          <el-input-number v-model="promotionForm.discountRate" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="目标用户类型">
          <el-select v-model="promotionForm.targetUserType" placeholder="请选择">
            <el-option label="全部用户" value="ALL"></el-option>
            <el-option label="新用户" value="NEW"></el-option>
            <el-option label="老用户" value="OLD"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input type="textarea" v-model="promotionForm.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="promotionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePromotion">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Marketing',
  data() {
    return {
      activeTab: 'coupon',
      couponData: [],
      promotionData: [],
      couponDialogVisible: false,
      promotionDialogVisible: false,
      couponForm: {
        name: '',
        type: 'DISCOUNT',
        discountValue: 10,
        totalCount: 100,
        targetUserType: 'EXPIRING'
      },
      promotionForm: {
        name: '',
        type: 'DISCOUNT',
        discountRate: 90,
        targetUserType: 'ALL',
        description: ''
      }
    }
  },
  mounted() {
    this.loadCoupons()
    this.loadPromotions()
  },
  methods: {
    async loadCoupons() {
      try {
        const res = await this.$http.get('/api/marketing/coupons')
        if (res.data && res.data.data) {
          this.couponData = res.data.data
        }
      } catch (e) {
        console.log('使用模拟数据')
      }
    },
    async loadPromotions() {
      try {
        const res = await this.$http.get('/api/marketing/promotions')
        if (res.data && res.data.data) {
          this.promotionData = res.data.data
        }
      } catch (e) {
        console.log('使用模拟数据')
      }
    },
    showCouponDialog() {
      this.couponDialogVisible = true
    },
    showPromotionDialog() {
      this.promotionDialogVisible = true
    },
    async saveCoupon() {
      try {
        await this.$http.post('/api/marketing/coupons', this.couponForm)
        this.$message.success('创建成功')
        this.couponDialogVisible = false
        this.loadCoupons()
      } catch (e) {
        this.$message.error('创建失败')
      }
    },
    async savePromotion() {
      try {
        await this.$http.post('/api/marketing/promotions', this.promotionForm)
        this.$message.success('创建成功')
        this.promotionDialogVisible = false
        this.loadPromotions()
      } catch (e) {
        this.$message.error('创建失败')
      }
    },
    async distributeCoupon(row) {
      this.$confirm('确认向目标用户发放该优惠券?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.post(`/api/marketing/coupons/${row.id}/distribute`, {
            targetType: row.targetUserType
          })
          this.$message.success('发放成功')
        } catch (e) {
          this.$message.error('发放失败')
        }
      }).catch(() => {})
    },
    async startPromotion(row) {
      try {
        await this.$http.post(`/api/marketing/promotions/${row.id}/start`)
        this.$message.success('活动已启动')
        this.loadPromotions()
      } catch (e) {
        this.$message.error('操作失败')
      }
    },
    async endPromotion(row) {
      try {
        await this.$http.post(`/api/marketing/promotions/${row.id}/end`)
        this.$message.success('活动已结束')
        this.loadPromotions()
      } catch (e) {
        this.$message.error('操作失败')
      }
    },
    editPromotion(row) {
      this.promotionForm = { ...row }
      this.promotionDialogVisible = true
    },
    async deleteCoupon(row) {
      this.$confirm('确认删除该优惠券?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/marketing/coupons/${row.id}`)
          this.$message.success('删除成功')
          this.loadCoupons()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async deletePromotion(row) {
      this.$confirm('确认删除该活动?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/marketing/promotions/${row.id}`)
          this.$message.success('删除成功')
          this.loadPromotions()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.marketing {
  padding: 0;
}
</style>
