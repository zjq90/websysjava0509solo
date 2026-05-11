export default {
    formatDate(date, fmt) {
        if (!date) return ''
        const d = new Date(date)
        const o = {
            'M+': d.getMonth() + 1,
            'd+': d.getDate(),
            'h+': d.getHours(),
            'm+': d.getMinutes(),
            's+': d.getSeconds(),
            'q+': Math.floor((d.getMonth() + 3) / 3),
            'S': d.getMilliseconds()
        }
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (d.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        for (const k in o) {
            if (new RegExp('(' + k + ')').test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
            }
        }
        return fmt
    },
    
    maskPhone(phone) {
        if (!phone) return ''
        if (phone.length < 7) return phone
        return phone.substring(0, 3) + '****' + phone.substring(phone.length - 4)
    },
    
    maskIdCard(idCard) {
        if (!idCard) return ''
        if (idCard.length < 8) return idCard
        return idCard.substring(0, 4) + '****' + idCard.substring(idCard.length - 4)
    },
    
    maskName(name) {
        if (!name) return ''
        if (name.length === 1) return name
        if (name.length === 2) return name.charAt(0) + '*'
        return name.charAt(0) + '*' + name.substring(name.length - 1)
    },
    
    getAdmissionStatusText(status) {
        const map = {
            0: '待审核',
            1: '审核通过',
            2: '已入院',
            3: '已出院',
            4: '已取消'
        }
        return map[status] || '未知'
    },
    
    getAdmissionStatusColor(status) {
        const map = {
            0: '#FF9800',
            1: '#4CAF50',
            2: '#2196F3',
            3: '#9E9E9E',
            4: '#F44336'
        }
        return map[status] || '#999'
    },
    
    getPaymentStatusText(status) {
        const map = {
            0: '待支付',
            1: '支付中',
            2: '支付成功',
            3: '支付失败',
            4: '已取消'
        }
        return map[status] || '未知'
    },
    
    getPaymentStatusColor(status) {
        const map = {
            0: '#FF9800',
            1: '#2196F3',
            2: '#4CAF50',
            3: '#F44336',
            4: '#9E9E9E'
        }
        return map[status] || '#999'
    },
    
    formatMoney(amount) {
        if (amount === null || amount === undefined) return '0.00'
        const num = parseFloat(amount)
        return num.toFixed(2)
    },
    
    showToast(title, icon = 'none') {
        uni.showToast({
            title,
            icon,
            duration: 2000
        })
    },
    
    showLoading(title = '加载中...') {
        uni.showLoading({
            title,
            mask: true
        })
    },
    
    hideLoading() {
        uni.hideLoading()
    },
    
    confirm(content, title = '提示') {
        return new Promise((resolve) => {
            uni.showModal({
                title,
                content,
                success: (res) => {
                    resolve(res.confirm)
                }
            })
        })
    }
}
