const BASE_URL = 'http://localhost:8080'

const request = (options) => {
	return new Promise((resolve, reject) => {
		const token = uni.getStorageSync('token')

		uni.request({
			url: BASE_URL + options.url,
			method: options.method || 'GET',
			data: options.data || {},
			header: {
				'Content-Type': 'application/json',
				...(token ? { 'Authorization': 'Bearer ' + token } : {})
			},
			success: (res) => {
				if (res.statusCode === 200) {
					if (res.data.code === 200) {
						resolve(res.data.data)
					} else if (res.data.code === 401) {
						uni.showToast({
							title: '登录已过期，请重新登录',
							icon: 'none'
						})
						uni.removeStorageSync('token')
						uni.removeStorageSync('userInfo')
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login'
							})
						}, 1500)
						reject(res.data)
					} else {
						uni.showToast({
							title: res.data.message || '请求失败',
							icon: 'none'
						})
						reject(res.data)
					}
				} else {
					uni.showToast({
						title: '网络错误',
						icon: 'none'
					})
					reject(res)
				}
			},
			fail: (err) => {
				uni.showToast({
					title: '网络连接失败',
					icon: 'none'
				})
				reject(err)
			}
		})
	})
}

export default request
