import request from './request'

const api = {
  // 认证相关
  login: (data) => request({
    url: '/auth/login',
    method: 'POST',
    data
  }),
  
  register: (data) => request({
    url: '/auth/register',
    method: 'POST',
    data
  }),
  
  sendCode: (phone) => request({
    url: '/auth/send-code',
    method: 'POST',
    data: { phone }
  }),
  
  logout: () => request({
    url: '/auth/logout',
    method: 'POST'
  }),
  
  // 用户相关
  getUserInfo: () => request({
    url: '/user/info',
    method: 'GET'
  }),
  
  getUserById: (id) => request({
    url: '/user/' + id,
    method: 'GET'
  }),
  
  updateUser: (data) => request({
    url: '/user/update',
    method: 'PUT',
    data
  }),
  
  // 社团相关
  getCategories: () => request({
    url: '/club/categories',
    method: 'GET'
  }),
  
  getClubPage: (params) => request({
    url: '/club/page',
    method: 'GET',
    data: params
  }),
  
  getClubDetail: (id) => request({
    url: '/club/' + id,
    method: 'GET'
  }),
  
  submitApplication: (data) => request({
    url: '/club/apply',
    method: 'POST',
    data
  }),
  
  getMyApplications: () => request({
    url: '/club/my-applications',
    method: 'GET'
  }),
  
  getApplicationDetail: (id) => request({
    url: '/club/application/' + id,
    method: 'GET'
  }),
  
  getMyClubs: () => request({
    url: '/club/my-clubs',
    method: 'GET'
  }),
  
  isJoinedClub: (clubId) => request({
    url: '/club/check-join/' + clubId,
    method: 'GET'
  }),
  
  quitClub: (clubId) => request({
    url: '/club/quit/' + clubId,
    method: 'POST'
  }),
  
  // 活动相关
  getActivityPage: (params) => request({
    url: '/activity/page',
    method: 'GET',
    data: params
  }),
  
  getActivityDetail: (id) => request({
    url: '/activity/' + id,
    method: 'GET'
  }),
  
  signUpActivity: (activityId, remark) => request({
    url: '/activity/signup/' + activityId,
    method: 'POST',
    data: { remark }
  }),
  
  cancelSignUp: (activityId) => request({
    url: '/activity/cancel-signup/' + activityId,
    method: 'POST'
  }),
  
  isSignedUp: (activityId) => request({
    url: '/activity/check-signup/' + activityId,
    method: 'GET'
  }),
  
  signInActivity: (activityId, signInType, location, isOffline) => request({
    url: '/activity/signin/' + activityId,
    method: 'POST',
    data: { signInType, location, isOffline }
  }),
  
  syncOfflineSignIn: (data) => request({
    url: '/activity/sync-offline-signin',
    method: 'POST',
    data
  }),
  
  isSignedIn: (activityId) => request({
    url: '/activity/check-signin/' + activityId,
    method: 'GET'
  }),
  
  getMyActivities: () => request({
    url: '/activity/my-activities',
    method: 'GET'
  }),
  
  getMySignInRecords: (activityId) => request({
    url: '/activity/my-signin-records',
    method: 'GET',
    data: { activityId }
  }),
  
  // 动态相关
  getFeedPage: (params) => request({
    url: '/feed/page',
    method: 'GET',
    data: params
  }),
  
  getFeedDetail: (id) => request({
    url: '/feed/' + id,
    method: 'GET'
  }),
  
  publishFeed: (clubId, content, images) => request({
    url: '/feed/publish',
    method: 'POST',
    data: { clubId, content, images }
  }),
  
  toggleLike: (feedId) => request({
    url: '/feed/toggle-like/' + feedId,
    method: 'POST'
  }),
  
  isLiked: (feedId) => request({
    url: '/feed/check-like/' + feedId,
    method: 'GET'
  }),
  
  addComment: (feedId, content, parentId, replyToId) => request({
    url: '/feed/comment',
    method: 'POST',
    data: { feedId, content, parentId, replyToId }
  }),
  
  getComments: (feedId) => request({
    url: '/feed/comments/' + feedId,
    method: 'GET'
  }),
  
  deleteComment: (commentId) => request({
    url: '/feed/comment/' + commentId,
    method: 'DELETE'
  }),
  
  deleteFeed: (feedId) => request({
    url: '/feed/' + feedId,
    method: 'DELETE'
  }),
  
  // 消息相关
  getMessagePage: (params) => request({
    url: '/message/page',
    method: 'GET',
    data: params
  }),
  
  getMessageDetail: (id) => request({
    url: '/message/' + id,
    method: 'GET'
  }),
  
  markAsRead: (id) => request({
    url: '/message/read/' + id,
    method: 'POST'
  }),
  
  markAllAsRead: (type) => request({
    url: '/message/read-all',
    method: 'POST',
    data: { type }
  }),
  
  deleteMessage: (id) => request({
    url: '/message/' + id,
    method: 'DELETE'
  }),
  
  getUnreadCount: (type) => request({
    url: '/message/unread-count',
    method: 'GET',
    data: { type }
  }),
  
  getNotificationSetting: () => request({
    url: '/message/notification-setting',
    method: 'GET'
  }),
  
  updateNotificationSetting: (data) => request({
    url: '/message/notification-setting',
    method: 'PUT',
    data
  }),
  
  // 聊天相关
  sendMessage: (clubId, messageType, content, mediaUrl, duration) => request({
    url: '/chat/send',
    method: 'POST',
    data: { clubId, messageType, content, mediaUrl, duration }
  }),
  
  getChatMessages: (params) => request({
    url: '/chat/messages',
    method: 'GET',
    data: params
  }),
  
  markChatAsRead: (clubId) => request({
    url: '/chat/read/' + clubId,
    method: 'POST'
  }),
  
  getChatUnreadCount: (clubId) => request({
    url: '/chat/unread-count/' + clubId,
    method: 'GET'
  })
}

export default api
