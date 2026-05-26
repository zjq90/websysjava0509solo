export default {
    set(key, value) {
        try {
            uni.setStorageSync(key, JSON.stringify(value))
        } catch (e) {
            console.error('存储失败:', e)
        }
    },

    get(key, defaultValue = null) {
        try {
            const value = uni.getStorageSync(key)
            return value ? JSON.parse(value) : defaultValue
        } catch (e) {
            console.error('读取失败:', e)
            return defaultValue
        }
    },

    remove(key) {
        try {
            uni.removeStorageSync(key)
        } catch (e) {
            console.error('删除失败:', e)
        }
    },

    clear() {
        try {
            uni.clearStorageSync()
        } catch (e) {
            console.error('清空失败:', e)
        }
    },

    savePendingBill(bill) {
        const pendingBills = this.get('pendingBills', [])
        bill.clientId = bill.clientId || ('client_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9))
        bill.syncStatus = 'PENDING'
        bill.createdAt = bill.createdAt || new Date().toISOString()
        pendingBills.unshift(bill)
        this.set('pendingBills', pendingBills)
        this.cacheRecentBills()
        return bill
    },

    getPendingBills() {
        return this.get('pendingBills', [])
    },

    removePendingBill(clientId) {
        const pendingBills = this.get('pendingBills', [])
        const index = pendingBills.findIndex(b => b.clientId === clientId)
        if (index > -1) {
            pendingBills.splice(index, 1)
            this.set('pendingBills', pendingBills)
        }
    },

    updatePendingBillStatus(clientId, status, error = null) {
        const pendingBills = this.get('pendingBills', [])
        const bill = pendingBills.find(b => b.clientId === clientId)
        if (bill) {
            bill.syncStatus = status
            bill.syncError = error
            this.set('pendingBills', pendingBills)
        }
    },

    cacheRecentBills(bills = null) {
        const now = Date.now()
        const thirtyDaysAgo = now - 30 * 24 * 60 * 60 * 1000

        if (!bills) {
            const cachedBills = this.get('recentBills', [])
            const pendingBills = this.get('pendingBills', [])
            bills = [...pendingBills, ...cachedBills]
        }

        const recentBills = bills.filter(bill => {
            const billTime = new Date(bill.transactionTime || bill.createdAt).getTime()
            return billTime >= thirtyDaysAgo
        }).sort((a, b) => {
            const timeA = new Date(a.transactionTime || a.createdAt).getTime()
            const timeB = new Date(b.transactionTime || b.createdAt).getTime()
            return timeB - timeA
        })

        this.set('recentBills', recentBills)
        return recentBills
    },

    getRecentBillsFromCache() {
        return this.get('recentBills', [])
    },

    getDeviceId() {
        return uni.getStorageSync('deviceId')
    },

    generateClientId() {
        return 'client_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    },

    saveCategories(categories) {
        this.set('categories', categories)
    },

    getCategories() {
        return this.get('categories', [])
    },

    saveAccounts(accounts) {
        this.set('accounts', accounts)
    },

    getAccounts() {
        return this.get('accounts', [])
    },

    saveTemplates(templates) {
        this.set('templates', templates)
    },

    getTemplates() {
        return this.get('templates', [])
    }
}
