<template>
    <view class="category-list">
        <view class="type-tabs">
            <view class="tab-item" :class="{ active: currentType === 'EXPENSE' }" @click="currentType = 'EXPENSE'">
                <text>支出分类</text>
            </view>
            <view class="tab-item" :class="{ active: currentType === 'INCOME' }" @click="currentType = 'INCOME'">
                <text>收入分类</text>
            </view>
        </view>

        <view class="category-grid">
            <view class="category-item" v-for="cat in filteredCategories" :key="cat.id"
                  @longpress="showActions(cat)">
                <view class="cat-icon" :style="{ background: cat.color + '20', color: cat.color }">
                    {{ cat.icon }}
                </view>
                <text class="cat-name">{{ cat.name }}</text>
            </view>
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
            categories: [],
            currentType: 'EXPENSE'
        }
    },
    computed: {
        filteredCategories() {
            return this.categories.filter(c => c.type === this.currentType)
        }
    },
    onLoad() {
        this.loadCategories()
    },
    onShow() {
        this.loadCategories()
    },
    methods: {
        async loadCategories() {
            try {
                this.categories = await this.$api.getCategories()
                this.$storage.saveCategories(this.categories)
            } catch (e) {
                console.error('加载分类失败:', e)
                this.categories = this.$storage.getCategories()
            }
        },

        showActions(cat) {
            uni.vibrateShort()
            uni.showActionSheet({
                itemList: ['编辑分类', '删除分类'],
                success: (res) => {
                    if (res.tapIndex === 1) {
                        this.deleteCategory(cat)
                    }
                }
            })
        },

        deleteCategory(cat) {
            uni.showModal({
                title: '确认删除',
                content: `确定要删除「${cat.name}」分类吗？`,
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$api.deleteCategory(cat.id)
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            this.loadCategories()
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        goToAdd() {
            uni.showModal({
                title: '新增分类',
                editable: true,
                placeholderText: '请输入分类名称',
                success: async (res) => {
                    if (res.confirm && res.content) {
                        try {
                            await this.$api.createCategory({
                                name: res.content,
                                type: this.currentType,
                                icon: '💰',
                                color: '#667eea'
                            })
                            uni.showToast({ title: '创建成功', icon: 'success' })
                            this.loadCategories()
                        } catch (e) {
                            uni.showToast({ title: '创建失败', icon: 'none' })
                        }
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.category-list {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.type-tabs {
    display: flex;
    background: #fff;
    padding: 20rpx 40rpx;
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx;
    font-size: 32rpx;
    color: #999;
    border-bottom: 4rpx solid transparent;
}

.tab-item.active {
    color: #667eea;
    border-bottom-color: #667eea;
    font-weight: bold;
}

.category-grid {
    display: flex;
    flex-wrap: wrap;
    padding: 20rpx;
}

.category-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;
}

.cat-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 44rpx;
    margin-bottom: 15rpx;
}

.cat-name {
    font-size: 26rpx;
    color: #333;
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
</style>
