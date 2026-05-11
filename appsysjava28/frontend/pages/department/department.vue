<template>
    <view class="department-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="search-section">
            <view class="search-box">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    placeholder="搜索科室名称..." 
                    v-model="searchKeyword"
                    @confirm="handleSearch"
                />
            </view>
        </view>

        <view class="content">
            <view class="dept-item card" v-for="dept in departmentTree" :key="dept.id">
                <view class="dept-header" @click="toggleDept(dept.id)">
                    <text class="dept-icon">🏥</text>
                    <text class="dept-name">{{ dept.name }}</text>
                    <text class="expand-icon">{{ expandedDepts.includes(dept.id) ? '▼' : '▶' }}</text>
                </view>
                <view class="dept-children" v-if="expandedDepts.includes(dept.id) && dept.children">
                    <view 
                        class="child-item" 
                        v-for="child in dept.children" 
                        :key="child.id"
                        @click="goToDoctors(child.id, child.name)"
                    >
                        <text class="child-name">{{ child.name }}</text>
                        <text class="arrow">></text>
                    </view>
                </view>
                <view class="dept-footer" v-if="!dept.children || dept.children.length === 0">
                    <view class="quick-action" @click="goToDoctors(dept.id, dept.name)">
                        <text class="action-text">查看医生</text>
                        <text class="action-arrow">></text>
                    </view>
                </view>
            </view>

            <view class="empty-state" v-if="departmentTree.length === 0 && !loading">
                <text class="empty-icon">🏥</text>
                <text class="empty-text">暂无科室数据</text>
            </view>
        </view>
    </view>
</template>

<script>
import { getDepartmentTree, searchDepartments } from '@/api/department'

export default {
    data() {
        return {
            searchKeyword: '',
            departmentTree: [],
            expandedDepts: [],
            loading: false,
            elderlyMode: false
        }
    },
    onShow() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadDepartments()
    },
    methods: {
        async loadDepartments() {
            this.loading = true
            try {
                const res = await getDepartmentTree()
                this.departmentTree = res.data.data || []
            } catch (e) {
                console.error(e)
            } finally {
                this.loading = false
            }
        },
        toggleDept(deptId) {
            const index = this.expandedDepts.indexOf(deptId)
            if (index > -1) {
                this.expandedDepts.splice(index, 1)
            } else {
                this.expandedDepts.push(deptId)
            }
        },
        async handleSearch() {
            if (!this.searchKeyword.trim()) {
                this.loadDepartments()
                return
            }
            try {
                const res = await searchDepartments(this.searchKeyword)
                this.departmentTree = res.data.data || []
                this.expandedDepts = []
            } catch (e) {
                console.error(e)
            }
        },
        goToDoctors(deptId, deptName) {
            uni.navigateTo({ 
                url: '/pages/doctor/doctor?deptId=' + deptId + '&deptName=' + encodeURIComponent(deptName) 
            })
        }
    }
}
</script>

<style scoped>
.department-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 40rpx;
}

.search-section {
    background: #ffffff;
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.search-box {
    display: flex;
    align-items: center;
    background: #f5f7fa;
    border-radius: 40rpx;
    padding: 20rpx 30rpx;
}

.search-icon {
    font-size: 30rpx;
    margin-right: 15rpx;
}

.search-input {
    flex: 1;
    font-size: 28rpx;
}

.content {
    padding: 30rpx;
}

.dept-item {
    margin-bottom: 20rpx;
}

.dept-header {
    display: flex;
    align-items: center;
    padding: 25rpx 0;
    border-bottom: 1rpx solid #f0f00;
}

.dept-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.dept-name {
    flex: 1;
    font-size: 32rpx;
    font-weight: 500;
    color: #333333;
}

.expand-icon {
    font-size: 24rpx;
    color: #999999;
}

.dept-children {
    padding-left: 60rpx;
}

.child-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 25rpx 0;
    border-bottom: 1rpx solid #f0f00;
}

.child-name {
    font-size: 28rpx;
    color: #666666;
}

.arrow {
    font-size: 28rpx;
    color: #999999;
}

.dept-footer {
    padding-top: 20rpx;
}

.quick-action {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx;
    background: #e6f4ff;
    border-radius: 8rpx;
}

.action-text {
    font-size: 28rpx;
    color: #1677ff;
}

.action-arrow {
    font-size: 28rpx;
    color: #1677ff;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999999;
}

.elderly-mode .dept-name {
    font-size: 38rpx;
}

.elderly-mode .child-name {
    font-size: 34rpx;
}
</style>
