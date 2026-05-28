<template>
  <view class="profile-container">
    <view class="avatar-section">
      <view class="avatar-wrapper" @click="chooseAvatar">
        <image 
          v-if="formData.avatar" 
          :src="formData.avatar" 
          class="avatar" 
          mode="aspectFill"
        />
        <view v-else class="avatar-placeholder">
          <text class="avatar-text">{{ formData.nickname ? formData.nickname.substring(0, 1) : '用' }}</text>
        </view>
        <view class="avatar-edit">
          <text class="edit-icon">📷</text>
        </view>
      </view>
      <text class="change-avatar-text">点击更换头像</text>
    </view>
    
    <view class="form-section">
      <view class="form-item">
        <text class="item-label">昵称</text>
        <input 
          v-model="formData.nickname" 
          class="item-input" 
          placeholder="请输入昵称"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">手机号</text>
        <input 
          v-model="formData.phone" 
          class="item-input" 
          placeholder="请输入手机号"
          type="number"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">学号</text>
        <input 
          v-model="formData.studentId" 
          class="item-input" 
          placeholder="请输入学号"
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">院系</text>
        <input 
          v-model="formData.department" 
          class="item-input" 
          placeholder="请输入院系"
          maxlength="50"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">专业</text>
        <input 
          v-model="formData.major" 
          class="item-input" 
          placeholder="请输入专业"
          maxlength="50"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">年级</text>
        <input 
          v-model="formData.grade" 
          class="item-input" 
          placeholder="请输入年级"
          maxlength="10"
        />
      </view>
      
      <view class="form-item" @click="chooseGender">
        <text class="item-label">性别</text>
        <view class="item-value">
          <text class="value-text">{{ genderText }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="item-label">邮箱</text>
        <input 
          v-model="formData.email" 
          class="item-input" 
          placeholder="请输入邮箱"
          type="email"
          maxlength="50"
        />
      </view>
      
      <view class="form-item">
        <text class="item-label">个人简介</text>
        <textarea 
          v-model="formData.bio" 
          class="item-textarea" 
          placeholder="介绍一下自己吧..."
          maxlength="200"
          :auto-height="true"
        />
      </view>
    </view>
    
    <view class="action-section">
      <button 
        class="save-btn" 
        :disabled="saving"
        @click="saveProfile"
      >
        {{ saving ? '保存中...' : '保存' }}
      </button>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'
import config from '../../common/config'

export default {
  data() {
    return {
      formData: {
        id: null,
        avatar: '',
        nickname: '',
        phone: '',
        studentId: '',
        department: '',
        major: '',
        grade: '',
        gender: 0,
        email: '',
        bio: ''
      },
      loading: false,
      saving: false
    }
  },
  computed: {
    genderText() {
      const genders = { 0: '保密', 1: '男', 2: '女' }
      return genders[this.formData.gender] || '保密'
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    util,
    
    async loadUserInfo() {
      this.loading = true
      try {
        const res = await api.getUserInfo()
        if (res.code === 200 && res.data) {
          this.formData = {
            ...this.formData,
            ...res.data
          }
        }
      } catch (e) {
        util.toast('加载失败')
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          const tempFilePath = res.tempFilePaths[0]
          try {
            util.showLoading('上传中...')
            const uploadedUrl = await this.uploadImage(tempFilePath)
            this.formData.avatar = uploadedUrl
            util.hideLoading()
          } catch (e) {
            util.hideLoading()
            util.toast('上传失败')
            console.error(e)
          }
        }
      })
    },
    
    async uploadImage(filePath) {
      return new Promise((resolve, reject) => {
        uni.uploadFile({
          url: 'http://localhost:8088/api/upload',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': 'Bearer ' + util.getStorage(config.STORAGE_KEYS.TOKEN)
          },
          success: (uploadRes) => {
            const data = JSON.parse(uploadRes.data)
            if (data.code === 200) {
              resolve(data.data)
            } else {
              reject(new Error(data.message))
            }
          },
          fail: reject
        })
      })
    },
    
    chooseGender() {
      uni.showActionSheet({
        itemList: ['保密', '男', '女'],
        success: (res) => {
          this.formData.gender = res.tapIndex
        }
      })
    },
    
    async saveProfile() {
      if (!this.formData.nickname.trim()) {
        util.toast('请输入昵称')
        return
      }
      
      if (this.formData.phone && !util.validatePhone(this.formData.phone)) {
        util.toast('请输入正确的手机号')
        return
      }
      
      if (this.formData.email && !util.validateEmail(this.formData.email)) {
        util.toast('请输入正确的邮箱')
        return
      }
      
      this.saving = true
      try {
        const res = await api.updateUser(this.formData)
        if (res.code === 200) {
          util.toast('保存成功', 'success')
          
          const userInfo = util.getStorage(config.STORAGE_KEYS.USER_INFO)
          if (userInfo) {
            util.setStorage(config.STORAGE_KEYS.USER_INFO, {
              ...userInfo,
              ...this.formData
            })
          }
          
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        } else {
          util.toast(res.message || '保存失败')
        }
      } catch (e) {
        util.toast('保存失败，请重试')
        console.error(e)
      } finally {
        this.saving = false
      }
    }
  }
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 40rpx;
}

.avatar-section {
  background: #fff;
  padding: 40rpx 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar-wrapper {
  position: relative;
  width: 160rpx;
  height: 160rpx;
  margin-bottom: 20rpx;
}

.avatar,
.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
}

.avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 56rpx;
  color: #fff;
  font-weight: bold;
}

.avatar-edit {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 48rpx;
  height: 48rpx;
  background: #5677fc;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid #fff;
}

.edit-icon {
  font-size: 24rpx;
}

.change-avatar-text {
  font-size: 26rpx;
  color: #999;
}

.form-section {
  background: #fff;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f8f8f8;
}

.form-item:last-child {
  border-bottom: none;
  align-items: flex-start;
}

.item-label {
  width: 140rpx;
  font-size: 28rpx;
  color: #333;
  flex-shrink: 0;
}

.item-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.item-textarea {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  min-height: 100rpx;
}

.item-value {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.value-text {
  font-size: 28rpx;
  color: #999;
  margin-right: 10rpx;
}

.arrow {
  font-size: 32rpx;
  color: #ccc;
}

.action-section {
  padding: 40rpx 30rpx;
}

.save-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
}

.save-btn[disabled] {
  background: #ccc;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
