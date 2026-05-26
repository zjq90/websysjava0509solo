<template>
    <view class="account-edit">
        <view class="form-section">
            <view class="form-item">
                <text class="label">账户名称</text>
                <view class="value">
                    <input class="input" v-model="form.name" placeholder="如：招商银行" />
                </view>
            </view>

            <view class="form-item">
                <text class="label">账户类型</text>
                <view class="value">
                    <view class="type-radio">
                        <view class="radio-item" :class="{ active: form.type === 'ASSET' }" @click="form.type = 'ASSET'">
                            <text class="radio-dot"></text>
                            <text>资产</text>
                        </view>
                        <view class="radio-item" :class="{ active: form.type === 'LIABILITY' }" @click="form.type = 'LIABILITY'">
                            <text class="radio-dot"></text>
                            <text>负债</text>
                        </view>
                    </view>
                </view>
            </view>

            <view class="form-item">
                <text class="label">余额</text>
                <view class="value">
                    <text class="currency">¥</text>
                    <input class="input amount-input" type="digit" v-model="form.balance" placeholder="0.00" />
                </view>
            </view>

            <view class="form-item" @click="showIconPicker = true">
                <text class="label">图标</text>
                <view class="value">
                    <text class="icon-preview">{{ form.icon }}</text>
                    <text class="arrow">›</text>
                </view>
            </view>

            <view class="form-item" @click="showColorPicker = true">
                <text class="label">颜色</text>
                <view class="value">
                    <view class="color-preview" :style="{ background: form.color }"></view>
                    <text class="arrow">›</text>
                </view>
            </view>

            <view class="form-item">
                <text class="label">备注</text>
                <view class="value">
                    <input class="input" v-model="form.remark" placeholder="添加备注" />
                </view>
            </view>
        </view>

        <view class="bottom-bar" v-if="form.id">
            <button class="btn-delete" @click="deleteAccount">删除账户</button>
            <button class="btn-save" @click="saveAccount">保存修改</button>
        </view>
        <view class="bottom-bar" v-else>
            <button class="btn-save" @click="saveAccount">创建账户</button>
        </view>

        <view class="picker-mask" v-if="showIconPicker" @click="showIconPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择图标</text>
                    <text class="picker-close" @click="showIconPicker = false">×</text>
                </view>
                <scroll-view scroll-y class="picker-body">
                    <view class="icon-grid">
                        <view class="icon-item" v-for="(icon, index) in iconList" :key="index"
                              :class="{ selected: form.icon === icon }"
                              @click="selectIcon(icon)">
                            <text class="icon-text">{{ icon }}</text>
                        </view>
                    </view>
                </scroll-view>
            </view>
        </view>

        <view class="picker-mask" v-if="showColorPicker" @click="showColorPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择颜色</text>
                    <text class="picker-close" @click="showColorPicker = false">×</text>
                </view>
                <view class="picker-body">
                    <view class="color-grid">
                        <view class="color-item" v-for="(color, index) in colorList" :key="index"
                              :class="{ selected: form.color === color }"
                              :style="{ background: color }"
                              @click="selectColor(color)">
                            <text class="check" v-if="form.color === color">✓</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            form: {
                id: null,
                name: '',
                type: 'ASSET',
                balance: '',
                icon: '💰',
                color: '#667eea',
                remark: ''
            },
            showIconPicker: false,
            showColorPicker: false,
            iconList: ['💰', '💳', '🏦', '💵', '🪙', '📱', '💎', '🏠', '🚗', '✈️', '🎁', '💼', '📚', '🎮', '🎵', '🍔'],
            colorList: ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#43e97b', '#38f9d7', '#fa709a', '#fee140', '#ff9a9e', '#a18cd1']
        }
    },
    onLoad(options) {
        if (options.id) {
            this.loadAccount(options.id)
        }
    },
    methods: {
        async loadAccount(id) {
            try {
                const acc = await this.$api.getAccountById(id)
                this.form = {
                    id: acc.id,
                    name: acc.name,
                    type: acc.type,
                    balance: acc.balance.toString(),
                    icon: acc.icon || '💰',
                    color: acc.color || '#667eea',
                    remark: acc.remark || ''
                }
            } catch (e) {
                console.error('加载账户失败:', e)
                uni.showToast({ title: '加载失败', icon: 'none' })
            }
        },

        selectIcon(icon) {
            this.form.icon = icon
            this.showIconPicker = false
        },

        selectColor(color) {
            this.form.color = color
            this.showColorPicker = false
        },

        async saveAccount() {
            if (!this.form.name) {
                uni.showToast({ title: '请输入账户名称', icon: 'none' })
                return
            }
            if (!this.form.balance) {
                uni.showToast({ title: '请输入余额', icon: 'none' })
                return
            }

            const data = {
                ...this.form,
                balance: parseFloat(this.form.balance)
            }

            try {
                if (this.form.id) {
                    await this.$api.updateAccount(this.form.id, data)
                    uni.showToast({ title: '保存成功', icon: 'success' })
                } else {
                    await this.$api.createAccount(data)
                    uni.showToast({ title: '创建成功', icon: 'success' })
                }
                setTimeout(() => {
                    uni.navigateBack()
                }, 1000)
            } catch (e) {
                uni.showToast({ title: '保存失败', icon: 'none' })
            }
        },

        deleteAccount() {
            uni.showModal({
                title: '确认删除',
                content: '确定要删除这个账户吗？相关的账单记录不会被删除。',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$api.deleteAccount(this.form.id)
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            setTimeout(() => {
                                uni.navigateBack()
                            }, 1000)
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
.account-edit {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.form-section {
    background: #fff;
    margin-top: 20rpx;
}

.form-item {
    display: flex;
    align-items: center;
    padding: 30rpx 40rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.form-item:last-child {
    border-bottom: none;
}

.label {
    width: 120rpx;
    font-size: 30rpx;
    color: #666;
}

.value {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 30rpx;
    color: #333;
}

.input {
    flex: 1;
    text-align: right;
    font-size: 30rpx;
}

.amount-input {
    text-align: right;
}

.currency {
    color: #667eea;
    font-weight: bold;
    margin-right: 10rpx;
}

.arrow {
    color: #ccc;
    font-size: 36rpx;
    margin-left: 10rpx;
}

.icon-preview {
    font-size: 40rpx;
}

.color-preview {
    width: 50rpx;
    height: 50rpx;
    border-radius: 50%;
}

.type-radio {
    display: flex;
    gap: 40rpx;
}

.radio-item {
    display: flex;
    align-items: center;
    font-size: 28rpx;
    color: #999;
}

.radio-item.active {
    color: #667eea;
}

.radio-dot {
    width: 28rpx;
    height: 28rpx;
    border: 4rpx solid #ccc;
    border-radius: 50%;
    margin-right: 10rpx;
    box-sizing: border-box;
}

.radio-item.active .radio-dot {
    border-color: #667eea;
    background: #667eea;
    box-shadow: inset 0 0 0 4rpx #fff;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx 40rpx;
    display: flex;
    gap: 20rpx;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-save {
    flex: 1;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    border: none;
}

.btn-delete {
    width: 200rpx;
    height: 88rpx;
    line-height: 88rpx;
    background: #fff;
    color: #ff4d4f;
    border: 2rpx solid #ff4d4f;
    border-radius: 44rpx;
    font-size: 28rpx;
}

.picker-mask {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    display: flex;
    align-items: flex-end;
}

.picker-content {
    background: #fff;
    width: 100%;
    max-height: 70vh;
    border-radius: 20rpx 20rpx 0 0;
}

.picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
    font-size: 32rpx;
    font-weight: bold;
}

.picker-close {
    font-size: 48rpx;
    color: #999;
    line-height: 1;
}

.picker-body {
    max-height: 50vh;
    padding: 20rpx;
}

.icon-grid {
    display: flex;
    flex-wrap: wrap;
}

.icon-item {
    width: 25%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 30rpx 0;
    border-radius: 12rpx;
}

.icon-item.selected {
    background: #f0f5ff;
}

.icon-text {
    font-size: 48rpx;
}

.color-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
    padding: 20rpx;
}

.color-item {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.color-item.selected {
    box-shadow: 0 0 0 6rpx rgba(102, 126, 234, 0.3);
}

.check {
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;
}
</style>
