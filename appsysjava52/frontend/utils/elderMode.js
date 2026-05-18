/**
 * 长辈模式工具类
 * 提供长辈模式的字体放大、操作简化等功能
 */

const ELDER_MODE_KEY = 'elderMode'
const FONT_SCALE_KEY = 'fontScale'

// 默认配置
const DEFAULT_CONFIG = {
  fontScale: 1.3,
  buttonScale: 1.2,
  simplifiedUI: true,
  voiceInput: true,
  highContrast: false
}

// 缓存当前配置
let currentConfig = null

/**
 * 初始化长辈模式配置
 */
function initConfig() {
  if (currentConfig) return
  
  try {
    const savedConfig = uni.getStorageSync('elderModeConfig')
    currentConfig = savedConfig ? JSON.parse(savedConfig) : { ...DEFAULT_CONFIG }
  } catch (e) {
    currentConfig = { ...DEFAULT_CONFIG }
  }
}

/**
 * 检查是否启用长辈模式
 */
export function isElderModeEnabled() {
  return uni.getStorageSync(ELDER_MODE_KEY) == 1
}

/**
 * 切换长辈模式
 */
export function toggleElderMode(enabled) {
  uni.setStorageSync(ELDER_MODE_KEY, enabled ? 1 : 0)
  applyElderMode()
  return enabled
}

/**
 * 应用长辈模式样式
 */
export function applyElderMode() {
  const enabled = isElderModeEnabled()
  const config = getElderModeConfig()
  
  // 设置CSS变量
  const rootStyle = {}
  
  if (enabled) {
    rootStyle['--font-scale'] = config.fontScale
    rootStyle['--button-scale'] = config.buttonScale
    rootStyle['--font-size-base'] = `${28 * config.fontScale}rpx`
    rootStyle['--font-size-small'] = `${24 * config.fontScale}rpx`
    rootStyle['--font-size-large'] = `${32 * config.fontScale}rpx`
    rootStyle['--font-size-xlarge'] = `${36 * config.fontScale}rpx`
    rootStyle['--button-height'] = `${88 * config.buttonScale}rpx`
    rootStyle['--button-radius'] = '16rpx'
  } else {
    rootStyle['--font-scale'] = 1
    rootStyle['--button-scale'] = 1
    rootStyle['--font-size-base'] = '28rpx'
    rootStyle['--font-size-small'] = '24rpx'
    rootStyle['--font-size-large'] = '32rpx'
    rootStyle['--font-size-xlarge'] = '36rpx'
    rootStyle['--button-height'] = '88rpx'
    rootStyle['--button-radius'] = '12rpx'
  }
  
  // 在实际项目中可以通过CSS变量控制全局样式
  console.log('长辈模式已' + (enabled ? '启用' : '禁用'), rootStyle)
}

/**
 * 获取长辈模式配置
 */
export function getElderModeConfig() {
  initConfig()
  return { ...currentConfig }
}

/**
 * 设置长辈模式配置
 */
export function setElderModeConfig(config) {
  initConfig()
  currentConfig = { ...currentConfig, ...config }
  uni.setStorageSync('elderModeConfig', JSON.stringify(currentConfig))
  applyElderMode()
}

/**
 * 重置长辈模式配置
 */
export function resetElderModeConfig() {
  currentConfig = { ...DEFAULT_CONFIG }
  uni.setStorageSync('elderModeConfig', JSON.stringify(currentConfig))
  applyElderMode()
}

/**
 * 获取缩放后的字体大小
 */
export function getScaledFontSize(baseSize) {
  const config = getElderModeConfig()
  const scale = isElderModeEnabled() ? config.fontScale : 1
  return baseSize * scale
}

/**
 * 获取缩放后的按钮大小
 */
export function getScaledButtonSize(baseSize) {
  const config = getElderModeConfig()
  const scale = isElderModeEnabled() ? config.buttonScale : 1
  return baseSize * scale
}

/**
 * 语音输入（模拟）
 */
export function startVoiceInput() {
  return new Promise((resolve, reject) => {
    uni.showModal({
      title: '语音输入',
      content: '请说出您要输入的内容',
      confirmText: '开始录音',
      success: (res) => {
        if (res.confirm) {
          // 模拟语音识别
          setTimeout(() => {
            uni.showToast({
              title: '录音中...',
              icon: 'loading',
              duration: 1500
            })
            
            setTimeout(() => {
              resolve('模拟语音识别结果')
            }, 1500)
          }, 500)
        } else {
          reject(new Error('用户取消'))
        }
      }
    })
  })
}

/**
 * 简化确认对话框（长辈模式专用）
 */
export function elderConfirm(title, content, options = {}) {
  return new Promise((resolve) => {
    uni.showModal({
      title,
      content,
      confirmText: options.confirmText || '确认',
      cancelText: options.cancelText || '取消',
      success: (res) => {
        resolve(res.confirm)
      }
    })
  })
}

/**
 * 分步引导（长辈模式操作引导）
 */
export function startGuideStep(stepIndex, steps) {
  if (!steps.length === 0) return Promise.resolve()
  
  const step = steps[stepIndex]
  if (!step) return Promise.resolve()
  
  return new Promise((resolve) => {
    uni.showModal({
      title: `步骤 ${stepIndex + 1}/${steps.length}`,
      content: step.content,
      showCancel: stepIndex > 0,
      confirmText: stepIndex === steps.length - 1 ? '完成' : '下一步',
      cancelText: '上一步',
      success: (res) => {
        if (res.confirm) {
          if (stepIndex === steps.length - 1) {
            resolve()
          } else {
            resolve(startGuideStep(stepIndex + 1, steps))
          }
        } else if (stepIndex > 0) {
          resolve(startGuideStep(stepIndex - 1, steps))
        }
      }
    })
  })
}

export default {
  isElderModeEnabled,
  toggleElderMode,
  applyElderMode,
  getElderModeConfig,
  setElderModeConfig,
  resetElderModeConfig,
  getScaledFontSize,
  getScaledButtonSize,
  startVoiceInput,
  elderConfirm,
  startGuideStep
}
