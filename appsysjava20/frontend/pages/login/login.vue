<template>
    <view class="login-page">
        <view class="login-header">
            <view class="logo">
                <text class="logo-icon">📊</text>
                <text class="logo-text">财务报表系统</text>
            </view>
            <text class="logo-desc">个人业绩 · 经营看板 · 数据管理</text>
        </view>
        
        <view class="login-form">
            <view class="form-item">
                <view class="form-label">员工工号</view>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="employeeNo" 
                    placeholder="请输入员工工号"
                />
            </view>
            
            <view class="form-item">
                <view class="form-label">测试账号选择</view>
                <view class="test-accounts">
                    <view 
                        v-for="(item, index) in testAccounts" 
                        :key="index"
                        class="account-item"
                        :class="{ active: selectedAccount === index }"
                        @click="selectAccount(index)"
                    >
                        <text class="account-name">{{ item.name }}</text>
                        <text class="account-role">{{ item.role }}</text>
                    </view>
                </view>
            </view>
            
            <button class="btn-primary login-btn" @click="handleLogin">
                登录系统
            </button>
        </view>
        
        <view class="login-footer">
            <text class="footer-text">版本 1.0.0 | 技术支持</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            employeeNo: '',
            selectedAccount: 0,
            testAccounts: [
                { name: '王经理', role: '管理人员', no: 'EMP001', id: 1 },
                { name: '销售员A', role: '普通员工', no: 'EMP002', id: 2 },
                { name: '销售员B', role: '普通员工', no: 'EMP003', id: 3 }
            ]
        };
    },
    onLoad() {
        this.selectAccount(0);
    },
    methods: {
        selectAccount(index) {
            this.selectedAccount = index;
            this.employeeNo = this.testAccounts[index].no;
        },
        handleLogin() {
            if (!this.employeeNo) {
                uni.showToast({
                    title: '请选择或输入员工工号',
                    icon: 'none'
                });
                return;
            }
            
            const account = this.testAccounts[this.selectedAccount];
            
            uni.setStorageSync('currentUser', {
                id: account.id,
                employeeNo: account.no,
                name: account.name,
                role: account.role,
                isManager: account.role === '管理人员'
            });
            
            uni.showToast({
                title: '登录成功',
                icon: 'success'
            });
            
            setTimeout(() => {
                uni.switchTab({
                    url: '/pages/index/index'
                });
            }, 1000);
        }
    }
};
</script>

<style scoped>
.login-page {
    min-height: 100vh;
    background: linear-gradient(180deg, #3c9cff 0%, #5ba8ff 50%, #f5f5f5 100%);
    padding: 40rpx;
    box-sizing: border-box;
}

.login-header {
    text-align: center;
    padding-top: 100rpx;
    padding-bottom: 80rpx;
}

.logo {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20rpx;
}

.logo-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
}

.logo-text {
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
}

.logo-desc {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
}

.login-form {
    background: #fff;
    border-radius: 24rpx;
    padding: 40rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.form-item {
    margin-bottom: 36rpx;
}

.form-label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 16rpx;
}

.form-input {
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 28rpx;
    font-size: 30rpx;
    border: 2rpx solid #e8e8e8;
}

.test-accounts {
    display: flex;
    gap: 20rpx;
}

.account-item {
    flex: 1;
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 24rpx;
    text-align: center;
    border: 2rpx solid transparent;
    transition: all 0.3s;
}

.account-item.active {
    background: #ecf5ff;
    border-color: #3c9cff;
}

.account-name {
    display: block;
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 8rpx;
}

.account-item.active .account-name {
    color: #3c9cff;
}

.account-role {
    font-size: 24rpx;
    color: #999;
}

.login-btn {
    margin-top: 20rpx;
}

.login-footer {
    text-align: center;
    margin-top: 80rpx;
}

.footer-text {
    font-size: 24rpx;
    color: #999;
}
</style>
