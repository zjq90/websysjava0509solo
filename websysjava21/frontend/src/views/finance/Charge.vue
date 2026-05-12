<template>
  <div class="page-container">
    <div class="page-header">
      <span>收费管理</span>
      <el-button type="primary" icon="el-icon-plus">收费登记</el-button>
    </div>
    <div class="page-card">
      <div style="text-align: center; padding: 40px;">
        <i class="el-icon-s-finance" style="font-size: 64px; color: #E6A23C;"></i>
        <h3 style="margin-top: 20px;">收费管理模块</h3>
        <p style="color: #909399; margin-top: 10px;">此模块用于收费人员进行门诊收费、住院结算、日结对账等功能</p>
        <el-tag style="margin-top: 20px;" type="warning">演示功能 - 待开发</el-tag>
      </div>
      <div style="margin-top: 30px;">
        <h4>功能列表：</h4>
        <ul style="margin-top: 10px; color: #606266;">
          <li style="margin: 8px 0;">✅ 门诊收费登记</li>
          <li style="margin: 8px 0;">✅ 住院押金缴纳</li>
          <li style="margin: 8px 0;">✅ 患者费用明细查询</li>
          <li style="margin: 8px 0;">✅ 多种支付方式支持</li>
          <li style="margin: 8px 0;">✅ 每日收费结算与对账</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChargeManage'
}
</script>
