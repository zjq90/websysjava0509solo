<template>
  <div class="page-container">
    <div class="page-header">
      <span>病房管理</span>
      <el-button type="primary" icon="el-icon-plus">新增床位</el-button>
    </div>
    <div class="page-card">
      <div style="text-align: center; padding: 40px;">
        <i class="el-icon-office-building" style="font-size: 64px; color: #67C23A;"></i>
        <h3 style="margin-top: 20px;">病房管理模块</h3>
        <p style="color: #909399; margin-top: 10px;">此模块用于护士管理病房床位、患者护理记录、体征数据采集等功能</p>
        <el-tag style="margin-top: 20px;" type="success">演示功能 - 待开发</el-tag>
      </div>
      <div style="margin-top: 30px;">
        <h4>功能列表：</h4>
        <ul style="margin-top: 10px; color: #606266;">
          <li style="margin: 8px 0;">✅ 床位管理与分配</li>
          <li style="margin: 8px 0;">✅ 患者护理记录</li>
          <li style="margin: 8px 0;">✅ 生命体征数据采集</li>
          <li style="margin: 8px 0;">✅ 医嘱执行确认</li>
          <li style="margin: 8px 0;">✅ 患者转床管理</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WardManage'
}
</script>
