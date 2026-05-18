const BASE_URL = '/api'

function request(options) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token') || ''
    
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data)
          } else {
            uni.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(res.data)
          }
        } else if (res.statusCode === 401) {
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          })
          uni.navigateTo({
            url: '/pages/login/login'
          })
          reject(res)
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

export default {
  getHomeData() {
    return request({
      url: '/product/home'
    })
  },
  
  getCategories(type) {
    return request({
      url: '/product/categories',
      method: 'GET',
      data: { categoryType: type }
    })
  },
  
  getProductsByCategory(categoryId) {
    return request({
      url: `/product/category/${categoryId}`
    })
  },
  
  getProductDetail(productId) {
    return request({
      url: `/product/${productId}`
    })
  },
  
  searchProducts(keyword) {
    return request({
      url: '/product/search',
      method: 'GET',
      data: { keyword }
    })
  },
  
  getSearchSuggestions(keyword) {
    return request({
      url: '/product/search-suggestions',
      method: 'GET',
      data: { keyword }
    })
  },
  
  login(data) {
    return request({
      url: '/auth/login',
      method: 'POST',
      data
    })
  },
  
  register(data) {
    return request({
      url: '/auth/register',
      method: 'POST',
      data
    })
  },
  
  sendSmsCode(phone, type) {
    return request({
      url: '/auth/send-sms-code',
      method: 'POST',
      data: { phone, type }
    })
  },
  
  getUserInfo() {
    return request({
      url: '/auth/user-info'
    })
  },
  
  updateUserInfo(data) {
    return request({
      url: '/auth/user-info',
      method: 'PUT',
      data
    })
  },
  
  toggleElderMode(elderMode) {
    return request({
      url: '/auth/toggle-elder-mode',
      method: 'POST',
      data: { elderMode }
    })
  },
  
  getAllProducts() {
    return request({
      url: '/product/all'
    })
  },
  
  getProductsByPriceRange(minPrice, maxPrice) {
    return request({
      url: '/product/price-range',
      method: 'GET',
      data: { minPrice, maxPrice }
    })
  },
  
  getAddressList() {
    return request({
      url: '/address/list'
    })
  },
  
  getAddressDetail(addressId) {
    return request({
      url: `/address/${addressId}`
    })
  },
  
  addAddress(data) {
    return request({
      url: '/address/add',
      method: 'POST',
      data
    })
  },
  
  updateAddress(addressId, data) {
    return request({
      url: `/address/${addressId}`,
      method: 'PUT',
      data
    })
  },
  
  deleteAddress(addressId) {
    return request({
      url: `/address/${addressId}`,
      method: 'DELETE'
    })
  },
  
  setDefaultAddress(addressId) {
    return request({
      url: `/address/default/${addressId}`,
      method: 'POST'
    })
  },
  
  createOrder(data) {
    return request({
      url: '/order/create',
      method: 'POST',
      data
    })
  },
  
  getOrderList(status) {
    return request({
      url: '/order/list',
      method: 'GET',
      data: { status }
    })
  },
  
  getOrderDetail(orderId) {
    return request({
      url: `/order/${orderId}`
    })
  },
  
  cancelOrder(orderId) {
    return request({
      url: `/order/cancel/${orderId}`,
      method: 'POST'
    })
  },
  
  confirmReceipt(orderId) {
    return request({
      url: `/order/confirm/${orderId}`,
      method: 'POST'
    })
  },
  
  getAvailableCoupons() {
    return request({
      url: '/coupon/available'
    })
  },
  
  getUserCoupons(status) {
    return request({
      url: '/coupon/my',
      method: 'GET',
      data: { status }
    })
  },
  
  receiveCoupon(couponId) {
    return request({
      url: `/coupon/receive/${couponId}`,
      method: 'POST'
    })
  },
  
  exchangeCoupon(couponId) {
    return request({
      url: `/coupon/exchange/${couponId}`,
      method: 'POST'
    })
  },
  
  getMemberLevels() {
    return request({
      url: '/member/levels'
    })
  },
  
  getMemberInfo() {
    return request({
      url: '/member/info'
    })
  },
  
  getPointRecords() {
    return request({
      url: '/member/points'
    })
  },
  
  exchangeGift(points, giftName) {
    return request({
      url: '/member/exchange',
      method: 'POST',
      data: { points, giftName }
    })
  },
  
  saveDIYBouquet(data) {
    return request({
      url: '/diy/save',
      method: 'POST',
      data
    })
  },
  
  getDIYBouquetList(status) {
    return request({
      url: '/diy/list',
      method: 'GET',
      data: { status }
    })
  },
  
  getDIYBouquetDetail(bouquetId) {
    return request({
      url: `/diy/${bouquetId}`
    })
  },
  
  updateDIYBouquet(bouquetId, data) {
    return request({
      url: `/diy/${bouquetId}`,
      method: 'PUT',
      data
    })
  },
  
  deleteDIYBouquet(bouquetId) {
    return request({
      url: `/diy/${bouquetId}`,
      method: 'DELETE'
    })
  },
  
  getPublicKey() {
    return request({
      url: '/auth/public-key'
    })
  },
  
  submitEnterpriseCustom(data) {
    return request({
      url: '/enterprise/submit',
      method: 'POST',
      data
    })
  }
}
