<script>
import api from './common/api.js'
import storage from './common/storage.js'

export default {
    onLaunch: function() {
        console.log('App Launch - 极速记账启动');
        this.checkNetworkStatus();
        this.initDeviceId();
        this.loadBaseData();
    },
    onShow: function() {
        console.log('App Show');
        this.checkNetworkAndSync();
    },
    onHide: function() {
        console.log('App Hide');
    },
    data: function() {
        return {
            isConnected: true,
            isSyncing: false
        }
    },
    methods: {
        checkNetworkStatus() {
            uni.getNetworkType({
                success: (res) => {
                    this.isConnected = res.networkType !== 'none';
                    console.log('当前网络状态:', this.isConnected ? '已连接' : '已断开');
                }
            });
            uni.onNetworkStatusChange((res) => {
                this.isConnected = res.isConnected;
                if (res.isConnected) {
                    console.log('网络已恢复，开始自动同步');
                    uni.showToast({
                        title: '网络已连接',
                        icon: 'success',
                        duration: 1500
                    });
                    setTimeout(() => {
                        this.autoSyncInBackground();
                    }, 1000);
                } else {
                    console.log('网络已断开');
                    uni.showToast({
                        title: '网络已断开，数据将保存到本地',
                        icon: 'none',
                        duration: 2000
                    });
                }
            });
        },
        initDeviceId() {
            let deviceId = storage.get('deviceId');
            if (!deviceId) {
                deviceId = 'device_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
                storage.set('deviceId', deviceId);
                console.log('生成新设备ID:', deviceId);
            } else {
                console.log('现有设备ID:', deviceId);
            }
        },
        async loadBaseData() {
            try {
                console.log('开始加载基础数据...');
                const [categories, accounts] = await Promise.all([
                    api.getCategories().catch(() => []),
                    api.getAccounts().catch(() => [])
                ]);
                if (categories.length > 0) {
                    storage.saveCategories(categories);
                    console.log('分类数据已缓存:', categories.length, '条');
                }
                if (accounts.length > 0) {
                    storage.saveAccounts(accounts);
                    console.log('账户数据已缓存:', accounts.length, '条');
                }
            } catch (e) {
                console.error('加载基础数据失败:', e);
            }
        },
        checkNetworkAndSync() {
            if (this.isConnected) {
                const pendingBills = storage.getPendingBills();
                if (pendingBills.length > 0 && !this.isSyncing) {
                    console.log('发现待同步账单:', pendingBills.length, '条');
                    uni.showModal({
                        title: '数据同步',
                        content: `发现${pendingBills.length}条待同步账单，是否立即同步？`,
                        confirmText: '立即同步',
                        cancelText: '稍后再说',
                        success: (res) => {
                            if (res.confirm) {
                                this.autoSyncInBackground();
                            }
                        }
                    });
                }
            }
        },
        async autoSyncInBackground() {
            if (this.isSyncing) {
                console.log('已有同步任务进行中，跳过');
                return;
            }
            const pendingBills = storage.getPendingBills();
            if (pendingBills.length === 0) {
                console.log('没有待同步的账单');
                return;
            }
            this.isSyncing = true;
            console.log('开始后台同步，待同步账单数:', pendingBills.length);
            uni.showLoading({ title: '同步中...', mask: false });
            let successCount = 0;
            let failCount = 0;
            for (let i = 0; i < pendingBills.length; i++) {
                const bill = pendingBills[i];
                try {
                    console.log(`正在同步第 ${i + 1}/${pendingBills.length} 条账单`);
                    await api.createBill(bill);
                    storage.removePendingBill(bill.clientId);
                    successCount++;
                } catch (e) {
                    console.error('同步失败:', bill.clientId, e.message);
                    storage.updatePendingBillStatus(bill.clientId, 'FAILED', e.message);
                    failCount++;
                }
            }
            uni.hideLoading();
            this.isSyncing = false;
            storage.cacheRecentBills();
            if (failCount === 0) {
                uni.showToast({
                    title: `同步成功 ${successCount} 条`,
                    icon: 'success'
                });
                console.log(`同步完成: 成功${successCount}条, 失败${failCount}条`);
            } else {
                uni.showModal({
                    title: '同步完成',
                    content: `成功 ${successCount} 条，失败 ${failCount} 条。\n失败的账单可在设置中手动重试。`,
                    showCancel: false,
                    confirmText: '知道了'
                });
                console.warn(`同步完成: 成功${successCount}条, 失败${failCount}条`);
            }
        }
    }
}
</script>

<style>
@import "./common/common.scss";

page {
    background-color: #f5f7fa;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.primary-color {
    color: #667eea;
}

.success-color {
    color: #52c41a;
}

.danger-color {
    color: #f5222d;
}

.warning-color {
    color: #faad14;
}

.card {
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
    margin: 20rpx;
    padding: 30rpx;
}
</style>
