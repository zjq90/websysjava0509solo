<template>
    <view class="template-list">
        <view class="template-card" v-for="tpl in templates" :key="tpl.id"
              @click="useTemplate(tpl)"
              @longpress="showActions(tpl)">
            <view class="tpl-header">
                <view class="tpl-icon" :class="tpl.type === 'INCOME' ? 'icon-income' : 'icon-expense'">
                    {{ tpl.category ? tpl.category.icon : '💰' }}
                </view>
                <view class="tpl-info">
                    <view class="tpl-name">{{ tpl.name }}</view>
                    <view class="tpl-desc">
                        {{ tpl.category ? tpl.category.name : '' }}
                        <text v-if="tpl.account"> · {{ tpl.account.name }}</text>
                    </view>
                </view>
                <view class="tpl-amount" :class="tpl.type === 'INCOME' ? 'amount-income' : 'amount-expense'">
                    {{ tpl.type === 'INCOME' ? '+' : '-' }}{{ tpl.amount | formatMoney }}
                </view>
            </view>
            <view class="tpl-footer" v-if="tpl.merchant || tpl.remark">
                <text v-if="tpl.merchant" class="tpl-merchant">{{ tpl.merchant }}</text>
                <text v-if="tpl.remark" class="tpl-remark">{{ tpl.remark }}</text>
            </view>
        </view>

        <view class="empty" v-if="templates.length === 0 && !loading">
            <view class="empty-icon">📋</view>
            <text>暂无记账模板</text>
            <text class="empty-desc">点击下方按钮添加常用账单模板</text>
        </view>

        <view class="fab-btn" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            templates: [],
            loading: false
        }
    },
    onLoad() {
        this.loadTemplates()
    },
    onShow() {
        this.loadTemplates()
    },
    methods: {
        async loadTemplates() {
            this.loading = true
            try {
                this.templates = await this.$api.getTemplates()
                this.$storage.saveTemplates(this.templates)
            } catch (e) {
                console.error('加载模板失败:', e)
                this.templates = this.$storage.getTemplates()
            }
            this.loading = false
        },

        async useTemplate(tpl) {
            uni.vibrateShort()
            uni.showModal({
                title: '使用模板',
                content: `确定使用「${tpl.name}」模板记账吗？`,
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$api.useTemplate(tpl.id)
                            uni.showToast({ title: '记账成功', icon: 'success' })
                        } catch (e) {
                            const billData = {
                                type: tpl.type,
                                amount: tpl.amount,
                                categoryId: tpl.categoryId,
                                accountId: tpl.accountId,
                                transactionTime: new Date().toISOString(),
                                merchant: tpl.merchant,
                                remark: tpl.remark,
                                deviceId: this.$storage.getDeviceId(),
                                clientId: this.$storage.generateClientId()
                            }
                            this.$storage.savePendingBill(billData)
                            uni.showToast({ title: '已保存到本地', icon: 'success' })
                        }
                    }
                }
            })
        },

        showActions(tpl) {
            uni.vibrateShort()
            uni.showActionSheet({
                itemList: ['编辑模板', '删除模板'],
                success: (res) => {
                    if (res.tapIndex === 0) {
                        this.goToEdit(tpl)
                    } else if (res.tapIndex === 1) {
                        this.deleteTemplate(tpl)
                    }
                }
            })
        },

        goToAdd() {
            uni.navigateTo({ url: '/pages/template/template-edit' })
        },

        goToEdit(tpl) {
            uni.navigateTo({ url: '/pages/template/template-edit?id=' + tpl.id })
        },

        deleteTemplate(tpl) {
            uni.showModal({
                title: '确认删除',
                content: `确定要删除「${tpl.name}」模板吗？`,
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$api.deleteTemplate(tpl.id)
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            this.loadTemplates()
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.template-list {
    min-height: 100vh;
    background: #f5f7fa;
    padding: 20rpx;
    padding-bottom: 140rpx;
}

.template-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.tpl-header {
    display: flex;
    align-items: center;
}

.tpl-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
    margin-right: 20rpx;
}

.icon-income {
    background: #f6ffed;
}

.icon-expense {
    background: #fff1f0;
}

.tpl-info {
    flex: 1;
}

.tpl-name {
    font-size: 30rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 8rpx;
}

.tpl-desc {
    font-size: 24rpx;
    color: #999;
}

.tpl-amount {
    font-size: 32rpx;
    font-weight: bold;
}

.amount-income {
    color: #52c41a;
}

.amount-expense {
    color: #ff4d4f;
}

.tpl-footer {
    margin-top: 15rpx;
    padding-top: 15rpx;
    border-top: 1rpx solid #f0f0f0;
    font-size: 24rpx;
    color: #999;
}

.tpl-merchant {
    margin-right: 20rpx;
}

.fab-btn {
    position: fixed;
    right: 40rpx;
    bottom: 120rpx;
    width: 110rpx;
    height: 110rpx;
    background: linear-gradient(135deg, #667eea, #764ba2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 6rpx 20rpx rgba(102, 126, 234, 0.4);
    z-index: 100;
}

.fab-icon {
    color: #fff;
    font-size: 60rpx;
    font-weight: bold;
    line-height: 1;
}

.empty {
    padding: 120rpx 40rpx;
    text-align: center;
    color: #999;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-desc {
    display: block;
    font-size: 24rpx;
    margin-top: 10rpx;
    color: #ccc;
}
</style>
