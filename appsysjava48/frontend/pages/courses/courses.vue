<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">专业课程</text>
    </view>

    <view class="category-bar">
      <view 
        v-for="cat in categories" 
        :key="cat"
        class="category-item"
        :class="{ active: activeCategory === cat }"
        @click="activeCategory = cat"
      >
        <text>{{ cat }}</text>
      </view>
    </view>

    <view class="course-list">
      <view 
        v-for="course in filteredCourses" 
        :key="course.id" 
        class="course-card"
        @click="viewCourse(course)"
      >
        <view class="course-cover">
          <text class="cover-icon">{{ course.icon }}</text>
        </view>
        <view class="course-info">
          <text class="course-title">{{ course.title }}</text>
          <text class="course-meta">{{ course.instructor }} · {{ course.lessons }}课时 · {{ course.duration }}</text>
          <text class="course-desc">{{ course.description }}</text>
          <view class="course-tags">
            <text class="tag" v-for="(tag, idx) in course.tags.slice(0, 2)" :key="idx">{{ tag }}</text>
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
      isElderMode: false,
      activeCategory: '全部',
      categories: ['全部', '认知行为', '精神分析', '家庭治疗', '危机干预']
    }
  },
  computed: {
    courses() {
      return [
        { 
          id: 1, 
          title: '认知行为疗法(CBT)入门与实践', 
          icon: '🧠',
          instructor: '王医生', 
          lessons: 12, 
          duration: '24小时',
          description: '系统学习认知行为疗法的基本理论和实践技巧，掌握常见心理问题的CBT干预方法。',
          tags: ['CBT', '入门', '实操']
        },
        { 
          id: 2, 
          title: '抑郁症的识别与干预策略', 
          icon: '💙',
          instructor: '李医生', 
          lessons: 8, 
          duration: '16小时',
          description: '深入了解抑郁症的临床表现、诊断标准，掌握各种干预策略和危机处理方法。',
          tags: ['抑郁', '干预', '临床']
        },
        { 
          id: 3, 
          title: '焦虑障碍的心理咨询实务', 
          icon: '😰',
          instructor: '王医生', 
          lessons: 10, 
          duration: '20小时',
          description: '涵盖广泛性焦虑、惊恐障碍、社交焦虑等各种焦虑障碍的评估和咨询技术。',
          tags: ['焦虑', '实务', '评估']
        },
        { 
          id: 4, 
          title: '家庭系统理论与治疗技术', 
          icon: '👨‍👩‍👧‍👦',
          instructor: '张医生', 
          lessons: 15, 
          duration: '30小时',
          description: '学习家庭系统理论，掌握家庭治疗的核心技术和常见家庭问题的处理方法。',
          tags: ['家庭', '系统', '治疗']
        },
        { 
          id: 5, 
          title: '危机干预与自杀预防', 
          icon: '🚨',
          instructor: '刘医生', 
          lessons: 6, 
          duration: '12小时',
          description: '专业的危机干预培训，学习如何识别和处理自杀风险，掌握危机干预的流程和技巧。',
          tags: ['危机', '干预', '预防']
        },
        { 
          id: 6, 
          title: '青少年心理问题与辅导', 
          icon: '👦',
          instructor: '李医生', 
          lessons: 9, 
          duration: '18小时',
          description: '针对青少年常见心理问题，提供专业的心理咨询和辅导技术培训。',
          tags: ['青少年', '辅导', '成长']
        }
      ]
    },
    filteredCourses() {
      if (this.activeCategory === '全部') {
        return this.courses
      }
      return this.courses.filter(c => 
        c.title.includes(this.activeCategory) || 
        c.tags.some(t => this.activeCategory.includes(t))
      )
    }
  },
  onLoad() {
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    viewCourse(course) {
      uni.showToast({ title: `进入课程: ${course.title}`, icon: 'none' })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  
  &.elder {
    font-size: 36rpx !important;
  }
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

.category-bar {
  display: flex;
  padding: 20rpx;
  background: white;
  margin-bottom: 20rpx;
  gap: 16rpx;
  overflow-x: auto;
}

.category-item {
  padding: 12rpx 32rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  white-space: nowrap;
  
  &.active {
    background: #667eea;
    color: white;
  }
}

.course-list {
  padding: 0 20rpx;
}

.course-card {
  display: flex;
  background: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.course-cover {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.cover-icon {
  font-size: 64rpx;
}

.course-info {
  flex: 1;
  min-width: 0;
}

.course-title {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 12rpx;
  line-height: 1.4;
}

.course-meta {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 12rpx;
}

.course-desc {
  display: block;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 16rpx;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.course-tags {
  display: flex;
  gap: 12rpx;
}

.tag {
  padding: 6rpx 16rpx;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 20rpx;
  font-size: 22rpx;
}
</style>
