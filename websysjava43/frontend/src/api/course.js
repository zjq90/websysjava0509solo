import request from '@/utils/request'

export function getCourseList() {
  return request({
    url: '/api/course/list',
    method: 'get'
  })
}

export function getCourseDetail(id) {
  return request({
    url: `/api/course/${id}`,
    method: 'get'
  })
}

export function createCourse(data) {
  return request({
    url: '/api/course',
    method: 'post',
    data
  })
}

export function updateCourse(id, data) {
  return request({
    url: `/api/course/${id}`,
    method: 'put',
    data
  })
}

export function deleteCourse(id) {
  return request({
    url: `/api/course/${id}`,
    method: 'delete'
  })
}

export function getMyCourses() {
  return request({
    url: '/api/course/my',
    method: 'get'
  })
}

export function getTeacherCourses() {
  return request({
    url: '/api/course/teacher',
    method: 'get'
  })
}

export function joinCourse(id) {
  return request({
    url: `/api/course/${id}/join`,
    method: 'post'
  })
}

export function leaveCourse(id) {
  return request({
    url: `/api/course/${id}/leave`,
    method: 'post'
  })
}

export function getCourseStudents(id) {
  return request({
    url: `/api/course/${id}/students`,
    method: 'get'
  })
}

export function getCourseMaterials(courseId) {
  return request({
    url: `/api/file/material/course/${courseId}`,
    method: 'get'
  })
}

export function uploadMaterial(data) {
  return request({
    url: '/api/file/material',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function deleteMaterial(id) {
  return request({
    url: `/api/file/material/${id}`,
    method: 'delete'
  })
}

export function getCourseVideos(courseId) {
  return request({
    url: `/api/file/video/course/${courseId}`,
    method: 'get'
  })
}

export function uploadVideo(data) {
  return request({
    url: '/api/file/video',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function deleteVideo(id) {
  return request({
    url: `/api/file/video/${id}`,
    method: 'delete'
  })
}

export function incrementViewCount(id) {
  return request({
    url: `/api/file/video/${id}/view`,
    method: 'post'
  })
}

export function getCourseAnnouncements(courseId) {
  return request({
    url: `/api/announcement/course/${courseId}`,
    method: 'get'
  })
}

export function createAnnouncement(data) {
  return request({
    url: '/api/announcement',
    method: 'post',
    params: data
  })
}

export function updateAnnouncement(id, data) {
  return request({
    url: `/api/announcement/${id}`,
    method: 'put',
    params: data
  })
}

export function deleteAnnouncement(id) {
  return request({
    url: `/api/announcement/${id}`,
    method: 'delete'
  })
}

export function getCourseComments(courseId) {
  return request({
    url: `/api/comment/course/${courseId}`,
    method: 'get'
  })
}

export function createComment(data) {
  return request({
    url: '/api/comment',
    method: 'post',
    params: data
  })
}

export function likeComment(id) {
  return request({
    url: `/api/comment/${id}/like`,
    method: 'post'
  })
}

export function deleteComment(id) {
  return request({
    url: `/api/comment/${id}`,
    method: 'delete'
  })
}
