<template>
  <div class="heritage-detail">
    <div class="image-section">
      <img src="https://picsum.photos/600/600?random=60" class="main-image" />
    </div>

    <div class="info-card">
      <h1 class="heritage-name">清代青花瓷瓶</h1>
      <div class="heritage-price">¥28,000</div>
      <div class="heritage-tags">
        <el-tag type="success">正品保证</el-tag>
        <el-tag type="warning">可鉴定</el-tag>
        <el-tag type="info">包邮</el-tag>
      </div>
    </div>

    <el-alert
      title="交易需实名认证 + 人脸识别，资金流向全程监管，保障交易安全"
      type="warning"
      :closable="false"
      show-icon
      class="security-alert"
    />

    <div class="detail-card">
      <h3 class="section-title">文物详情</h3>
      <table class="detail-table">
        <tr>
          <td class="label">类别</td>
          <td class="value">瓷器</td>
        </tr>
        <tr>
          <td class="label">年代</td>
          <td class="value">清代康熙年间</td>
        </tr>
        <tr>
          <td class="label">尺寸</td>
          <td class="value">高32cm，口径12cm，底径10cm</td>
        </tr>
        <tr>
          <td class="label">保存状态</td>
          <td class="value">完好，无明显损伤</td>
        </tr>
        <tr>
          <td class="label">鉴定证书</td>
          <td class="value">有国家级文物鉴定证书</td>
        </tr>
      </table>
    </div>

    <div class="detail-card">
      <h3 class="section-title">藏品描述</h3>
      <div class="description">
        这件清代康熙年间的青花瓷瓶，是我多年收藏中的珍品。瓶身造型优美，
        釉色温润如玉，青花发色纯正。瓶身绘有缠枝莲纹，线条流畅，绘画精细，
        是典型的康熙官窑风格。
        <br /><br />
        底款为"大清康熙年制"六字楷书款，字体工整，是康熙时期的标准款识。
        此瓶历经三百余年，保存状态依然完好，是难得一见的精品。
        <br /><br />
        适合私人收藏、博物馆展览、或者作为传家宝传承后代。
      </div>
    </div>

    <div class="seller-card">
      <h3 class="section-title">卖家信息</h3>
      <div class="seller-info">
        <div class="seller-avatar">
          <el-icon :size="24"><User /></el-icon>
        </div>
        <div class="seller-detail">
          <div class="seller-name">收藏爱好者小王</div>
          <div class="seller-stats">
            <span>信誉良好</span>
            <span>已认证卖家</span>
          </div>
        </div>
      </div>
    </div>

    <div class="action-bar">
      <el-button size="large" class="contact-btn">
        <el-icon><ChatDotRound /></el-icon>
        联系卖家
      </el-button>
      <el-button type="primary" size="large" class="buy-btn" @click="handleBuy">
        立即购买
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ElMessage, ElModal } from 'element-plus'
import { User, ChatDotRound } from '@element-plus/icons-vue'

const handleBuy = () => {
  ElModal.confirm({
    title: '确认购买',
    content: '确定要以¥28,000的价格购买这件清代青花瓷瓶吗？',
    confirmButtonText: '确定购买',
    cancelButtonText: '取消',
    onConfirm: () => {
      ElMessage.success('订单创建成功，请完成实名认证和人脸识别后付款')
    }
  })
}
</script>

<style scoped>
.heritage-detail {
  padding-bottom: 80px;
}

.image-section {
  padding: 0;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.info-card {
  background: #fff;
  padding: 24px;
  margin-bottom: 16px;
}

.heritage-name {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin: 0 0 12px;
  line-height: 1.4;
}

.heritage-price {
  font-size: 28px;
  font-weight: bold;
  color: #ff4d4f;
  margin-bottom: 16px;
}

.heritage-tags {
  display: flex;
  gap: 10px;
}

.security-alert {
  margin: 0 20px 16px;
}

.detail-card {
  background: #fff;
  padding: 24px;
  margin: 0 20px 16px;
  border-radius: 12px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-table {
  width: 100%;
}

.detail-table td {
  padding: 12px 0;
  font-size: 15px;
  border-bottom: 1px solid #f8f8f8;
}

.detail-table td.label {
  color: #999;
  width: 100px;
}

.detail-table td.value {
  color: #333;
}

.description {
  font-size: 15px;
  color: #666;
  line-height: 1.8;
}

.seller-card {
  background: #fff;
  padding: 24px;
  margin: 0 20px 16px;
  border-radius: 12px;
}

.seller-info {
  display: flex;
  align-items: center;
}

.seller-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: #f5f0eb;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: #8B4513;
}

.seller-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.seller-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #52c41a;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.contact-btn {
  flex: 1;
  border-color: #8B4513;
  color: #8B4513;
}

.buy-btn {
  flex: 2;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  border: none;
}

.elder-mode .heritage-name {
  font-size: 26px;
}

.elder-mode .heritage-price {
  font-size: 32px;
}

.elder-mode .section-title {
  font-size: 22px;
}

.elder-mode .description {
  font-size: 18px;
}
</style>
