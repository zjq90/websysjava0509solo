import request from '../utils/request'

export const getActivityPostList = (params) => {
  return request({
    url: '/square/activity/list',
    method: 'GET',
    data: params
  })
}

export const getActivityPostListByUser = (userId, params) => {
  return request({
    url: `/square/activity/user/${userId}`,
    method: 'GET',
    data: params
  })
}

export const publishActivityPost = (data) => {
  return request({
    url: '/square/activity/publish',
    method: 'POST',
    data
  })
}

export const getActivityPostDetail = (postId) => {
  return request({
    url: `/square/activity/${postId}`,
    method: 'GET'
  })
}

export const deleteActivityPost = (postId, userId) => {
  return request({
    url: `/square/activity/${postId}`,
    method: 'DELETE',
    data: { userId }
  })
}

export const getTopicList = () => {
  return request({
    url: '/square/topic/list',
    method: 'GET'
  })
}

export const getHotTopicList = () => {
  return request({
    url: '/square/topic/hot',
    method: 'GET'
  })
}

export const createTopic = (data) => {
  return request({
    url: '/square/topic/create',
    method: 'POST',
    data
  })
}

export const getTopicDetail = (topicId) => {
  return request({
    url: `/square/topic/${topicId}`,
    method: 'GET'
  })
}

export const getTopicPostList = (topicId, params) => {
  return request({
    url: `/square/topic/posts/${topicId}`,
    method: 'GET',
    data: params
  })
}

export const publishTopicPost = (data) => {
  return request({
    url: '/square/topic/post/publish',
    method: 'POST',
    data
  })
}

export const getTopicPostDetail = (postId) => {
  return request({
    url: `/square/topic/post/${postId}`,
    method: 'GET'
  })
}

export const getSeniorShareList = (params) => {
  return request({
    url: '/square/senior/list',
    method: 'GET',
    data: params
  })
}

export const publishSeniorShare = (data) => {
  return request({
    url: '/square/senior/publish',
    method: 'POST',
    data
  })
}

export const getSeniorShareDetail = (shareId) => {
  return request({
    url: `/square/senior/${shareId}`,
    method: 'GET'
  })
}

export const getCommentList = (params) => {
  return request({
    url: '/square/comment/list',
    method: 'GET',
    data: params
  })
}

export const getReplyCommentList = (parentId) => {
  return request({
    url: `/square/comment/reply/${parentId}`,
    method: 'GET'
  })
}

export const publishComment = (data) => {
  return request({
    url: '/square/comment/publish',
    method: 'POST',
    data
  })
}

export const toggleLike = (data) => {
  return request({
    url: '/square/like/toggle',
    method: 'POST',
    data
  })
}

export const isLiked = (params) => {
  return request({
    url: '/square/like/check',
    method: 'GET',
    data: params
  })
}
