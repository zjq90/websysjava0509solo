<template>
  <div class="page-container">
    <div class="page-header">
      <span>患者管理</span>
      <el-button type="primary" icon="el-icon-plus">新增患者</el-button>
    </div>
    <div class="page-card">
      <div style="text-align: center; padding: 40px;">
        <i class="el-icon-user-solid" style="font-size: 64px; color: #409EFF;"></i>
        <h3 style="margin-top: 20px;">患者管理模块</h3>
        <p style="color: #909399; margin-top: 10px;">此模块用于医生管理患者信息、查看患者病历、开具医嘱等功能</p>
        <el-tag style="margin-top: 20px;">演示功能 - 待开发</el-tag>
      </div>
      <div style="margin-top: 30px;">
        <h4>功能列表：</h4>
        <ul style="margin-top: 10px; color: #606266;">
          <li style="margin: 8px 0;">✅ 患者信息查询与管理</li>
          <li style="margin: 8px 0;">✅ 患者病历查看与编辑</li>
          <li style="margin: 8px 0;">✅ 医嘱开具与管理</li>
          <li style="margin: 8px 0;">✅ 检查检验申请</li>
          <li style="margin: 8px 0;">✅ 检查检验结果查看</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PatientManage'
}
</script>
