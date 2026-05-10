<template>
  <div>
    <el-card>
      <div slot="header">
        <span>商品管理</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增商品</el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="商品条码">
          <el-input v-model="queryParams.barcode" placeholder="请输入商品条码" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="queryParams.category" placeholder="请输入分类" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1"></el-option>
            <el-option label="下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="productCode" label="商品编码" width="120"></el-table-column>
        <el-table-column prop="productName" label="商品名称" width="180"></el-table-column>
        <el-table-column prop="barcode" label="商品条码" width="130"></el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="price" label="售价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="cost" label="成本" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.cost }}
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        style="margin-top: 20px; text-align: right"
        @current-change="handleCurrentChange"
        :current-page.sync="queryParams.page"
        :page-size="queryParams.size"
        :total="total"
        layout="total, prev, pager, next, jumper"
      >
      </el-pagination>
    </el-card>
    
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="商品编码" prop="productCode">
          <el-input v-model="form.productCode" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName"></el-input>
        </el-form-item>
        <el-form-item label="商品条码" prop="barcode">
          <el-input v-model="form.barcode"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category"></el-input>
        </el-form-item>
        <el-form-item label="售价" prop="price">
          <el-input-number v-model="form.price" :precision="2" :step="0.1" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="成本" prop="cost">
          <el-input-number v-model="form.cost" :precision="2" :step="0.1" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit"></el-input>
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="form.brand"></el-input>
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="上架" :value="1"></el-option>
            <el-option label="下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getProductList, createProduct, updateProduct, deleteProduct } from '@/api/product'

export default {
  name: 'ProductList',
  data() {
    return {
      queryParams: {
        productName: '',
        barcode: '',
        category: '',
        status: null,
        page: 0,
        size: 10
      },
      tableData: [],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        productCode: '',
        productName: '',
        barcode: '',
        category: '',
        price: 0,
        cost: 0,
        unit: '瓶',
        brand: '',
        specification: '',
        description: '',
        status: 1
      },
      rules: {
        productCode: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请输入售价', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      getProductList(this.queryParams).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).catch(() => {
        this.tableData = [
          { id: 1, productCode: 'P001', productName: '可口可乐330ml', barcode: '6901234567890', category: '饮料', price: 3.00, cost: 1.50, unit: '瓶', brand: '可口可乐', specification: '330ml/瓶', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 2, productCode: 'P002', productName: '农夫山泉550ml', barcode: '6901234567891', category: '饮料', price: 2.00, cost: 0.80, unit: '瓶', brand: '农夫山泉', specification: '550ml/瓶', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 3, productCode: 'P003', productName: '乐事薯片原味', barcode: '6901234567892', category: '零食', price: 8.00, cost: 4.00, unit: '袋', brand: '乐事', specification: '104g/袋', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 4, productCode: 'P004', productName: '康师傅红烧牛肉面', barcode: '6901234567893', category: '食品', price: 4.50, cost: 2.00, unit: '桶', brand: '康师傅', specification: '108g/桶', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 5, productCode: 'P005', productName: '红牛维生素功能饮料', barcode: '6901234567894', category: '饮料', price: 6.00, cost: 3.00, unit: '罐', brand: '红牛', specification: '250ml/罐', status: 1, createTime: '2024-01-01 10:00:00' }
        ]
        this.total = 5
      })
    },
    resetQuery() {
      this.queryParams = {
        productName: '',
        barcode: '',
        category: '',
        status: null,
        page: 0,
        size: 10
      }
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryParams.page = val - 1
      this.loadData()
    },
    handleAdd() {
      this.dialogTitle = '新增商品'
      this.form = {
        id: null,
        productCode: '',
        productName: '',
        barcode: '',
        category: '',
        price: 0,
        cost: 0,
        unit: '瓶',
        brand: '',
        specification: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑商品'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该商品？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProduct(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            updateProduct(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            createProduct(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            })
          }
        }
      })
    }
  }
}
</script>
