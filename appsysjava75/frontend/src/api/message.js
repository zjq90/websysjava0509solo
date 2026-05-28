import request from './request'

export const getSystemMessageList = (params) => {
  return request({
    url: '/message/system/list',
    method: 'GET',
    data: params
  })
}

export const sendSystemMessage = (data) => {
  return request({
    url: '/message/system/send',
    method: 'POST',
    data
  })
}

export const markSystemMessageAsRead = (messageId, userId) => {
  return request({
    url: `/message/system/read/${messageId}`,
    method: 'POST',
    data: { userId }
  })
}

export const markAllSystemMessageAsRead = (userId) => {
  return request({
    url: '/message/system/read-all',
    method: 'POST',
    data: { userId }
  })
}

export const getUnreadMessageCount = (userId) => {
  return request({
    url: '/message/system/unread-count',
    method: 'GET',
    data: { userId }
  })
}

export const getChatGroupList = (params) => {
  return request({
    url: '/message/group/list',
    method: 'GET',
    data: params
  })
}

export const createChatGroup = (data) => {
  return request({
    url: '/message/group/create',
    method: 'POST',
    data
  })
}

export const getGroupMemberList = (groupId) => {
  return request({
    url: `/message/group/members/${groupId}`,
    method: 'GET'
  })
}

export const addGroupMember = (data) => {
  return request({
    url: '/message/group/add-member',
    method: 'POST',
    data
  })
}

export const sendGroupMessage = (data) => {
  return request({
    url: '/message/group/send',
    method: 'POST',
    data
  })
}

export const getGroupMessageList = (groupId, params) => {
  return request({
    url: `/message/group/messages/${groupId}`,
    method: 'GET',
    data: params
  })
}

export const markGroupMessageAsRead = (groupId, userId) => {
  return request({
    url: `/message/group/read/${groupId}`,
    method: 'POST',
    data: { userId }
  })
}

export const sendPrivateMessage = (data) => {
  return request({
    url: '/message/private/send',
    method: 'POST',
    data
  })
}

export const getPrivateMessageList = (params) => {
  return request({
    url: '/message/private/messages',
    method: 'GET',
    data: params
  })
}

export const markPrivateMessageAsRead = (senderId, receiverId) => {
  return request({
    url: '/message/private/read',
    method: 'POST',
    data: { senderId, receiverId }
  })
}

export const getPrivateUnreadCount = (userId) => {
  return request({
    url: '/message/private/unread-count',
    method: 'GET',
    data: { userId }
  })
}

export const getMessageReadStatus = (messageId, messageType) => {
  return request({
    url: '/message/read-status',
    method: 'GET',
    data: { messageId, messageType }
  })
}

export const remindUnreadMember = (messageId, messageType, userId) => {
  return request({
    url: '/message/remind',
    method: 'POST',
    data: { messageId, messageType, userId }
  })
}

export const createMessageReadStatus = (data) => {
  return request({
    url: '/message/read-status/batch',
    method: 'POST',
    data
  })
}
