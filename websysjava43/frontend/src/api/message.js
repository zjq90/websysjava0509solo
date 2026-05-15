import request from '@/utils/request'

export function sendMessage(data) {
  return request({
    url: '/api/message',
    method: 'post',
    params: data
  })
}

export function getMyMessages() {
  return request({
    url: '/api/message/list',
    method: 'get'
  })
}

export function getUnreadMessages() {
  return request({
    url: '/api/message/unread',
    method: 'get'
  })
}

export function getUnreadCount() {
  return request({
    url: '/api/message/unread/count',
    method: 'get'
  })
}

export function getMessageDetail(id) {
  return request({
    url: `/api/message/${id}`,
    method: 'get'
  })
}

export function markAsRead(id) {
  return request({
    url: `/api/message/${id}/read`,
    method: 'post'
  })
}

export function markAllAsRead() {
  return request({
    url: '/api/message/read/all',
    method: 'post'
  })
}

export function deleteMessage(id) {
  return request({
    url: `/api/message/${id}`,
    method: 'delete'
  })
}
