import request from '@/utils/request.js'

export const authApi = {
	login: (data) => request({ url: '/api/auth/login', method: 'POST', data }),
	register: (data) => request({ url: '/api/auth/register', method: 'POST', data }),
	getCurrentUser: () => request({ url: '/api/auth/me', method: 'GET' })
}

export const clubApi = {
	getCategories: () => request({ url: '/api/clubs/categories', method: 'GET' }),
	getList: (params) => request({ url: '/api/clubs', method: 'GET', data: params }),
	getDetail: (id) => request({ url: `/api/clubs/${id}`, method: 'GET' }),
	create: (data) => request({ url: '/api/clubs', method: 'POST', data }),
	update: (id, data) => request({ url: `/api/clubs/${id}`, method: 'PUT', data }),
	getHot: (params) => request({ url: '/api/clubs/hot', method: 'GET', data: params }),
	getMyManaged: () => request({ url: '/api/clubs/my-managed', method: 'GET' })
}

export const followApi = {
	follow: (clubId) => request({ url: `/api/clubs/${clubId}/follow`, method: 'POST' }),
	unfollow: (clubId) => request({ url: `/api/clubs/${clubId}/follow`, method: 'DELETE' }),
	getMyFollowed: (params) => request({ url: '/api/clubs/my-followed', method: 'GET', data: params }),
	checkStatus: (clubId) => request({ url: `/api/clubs/${clubId}/follow/check`, method: 'GET' }),
	getCount: (clubId) => request({ url: `/api/clubs/${clubId}/follow/count`, method: 'GET' })
}

export const recruitApi = {
	create: (clubId, data) => request({ url: `/api/clubs/${clubId}/recruit`, method: 'POST', data }),
	update: (clubId, recruitId, data) => request({ url: `/api/clubs/${clubId}/recruit/${recruitId}`, method: 'PUT', data }),
	close: (clubId, recruitId) => request({ url: `/api/clubs/${clubId}/recruit/${recruitId}/close`, method: 'PUT' }),
	getList: (clubId, params) => request({ url: `/api/clubs/${clubId}/recruit`, method: 'GET', data: params }),
	getDetail: (clubId, recruitId) => request({ url: `/api/clubs/${clubId}/recruit/${recruitId}`, method: 'GET' }),
	submitApply: (clubId, recruitId, data) => request({ url: `/api/clubs/${clubId}/recruit/${recruitId}/apply`, method: 'POST', data }),
	reviewApply: (clubId, applyId, data) => request({ url: `/api/clubs/${clubId}/recruit/apply/${applyId}/review`, method: 'PUT', data }),
	getApplyList: (clubId, params) => request({ url: `/api/clubs/${clubId}/recruit/apply/list`, method: 'GET', data: params }),
	getMyApplyList: (clubId, params) => request({ url: `/api/clubs/${clubId}/recruit/apply/my`, method: 'GET', data: params }),
	exportApplyList: (clubId, params) => request({ url: `/api/clubs/${clubId}/recruit/apply/export`, method: 'GET', data: params })
}

export const memberApi = {
	getList: (clubId, params) => request({ url: `/api/clubs/${clubId}/members`, method: 'GET', data: params }),
	add: (clubId, data) => request({ url: `/api/clubs/${clubId}/members`, method: 'POST', data }),
	update: (clubId, memberId, data) => request({ url: `/api/clubs/${clubId}/members/${memberId}`, method: 'PUT', data }),
	remove: (clubId, memberId) => request({ url: `/api/clubs/${clubId}/members/${memberId}`, method: 'DELETE' }),
	transferPresident: (clubId, data) => request({ url: `/api/clubs/${clubId}/members/transfer-president`, method: 'PUT', data }),
	quit: (clubId) => request({ url: `/api/clubs/${clubId}/members/quit`, method: 'POST' }),
	getActivityRecords: (clubId, memberId) => request({ url: `/api/clubs/${clubId}/members/${memberId}/activities`, method: 'GET' }),
	markActive: (clubId, memberId, data) => request({ url: `/api/clubs/${clubId}/members/${memberId}/active`, method: 'PUT', data }),
	exportList: (clubId) => request({ url: `/api/clubs/${clubId}/members/export`, method: 'GET' }),
	getDepartments: (clubId) => request({ url: `/api/clubs/${clubId}/members/departments`, method: 'GET' }),
	createDepartment: (clubId, data) => request({ url: `/api/clubs/${clubId}/members/departments`, method: 'POST', data }),
	updateDepartment: (clubId, deptId, data) => request({ url: `/api/clubs/${clubId}/members/departments/${deptId}`, method: 'PUT', data }),
	deleteDepartment: (clubId, deptId) => request({ url: `/api/clubs/${clubId}/members/departments/${deptId}`, method: 'DELETE' })
}

export const activityApi = {
	create: (data) => request({ url: '/api/activities', method: 'POST', data }),
	update: (id, data) => request({ url: `/api/activities/${id}`, method: 'PUT', data }),
	delete: (id) => request({ url: `/api/activities/${id}`, method: 'DELETE' }),
	getList: (params) => request({ url: '/api/activities', method: 'GET', data: params }),
	getDetail: (id) => request({ url: `/api/activities/${id}`, method: 'GET' }),
	signup: (id) => request({ url: `/api/activities/${id}/signup`, method: 'POST' }),
	cancelSignup: (id) => request({ url: `/api/activities/${id}/signup`, method: 'DELETE' }),
	checkin: (id) => request({ url: `/api/activities/${id}/checkin`, method: 'POST' }),
	getParticipants: (id, params) => request({ url: `/api/activities/${id}/participants`, method: 'GET', data: params }),
	getMy: (params) => request({ url: '/api/activities/my', method: 'GET', data: params })
}

export const postApi = {
	create: (clubId, data) => request({ url: `/api/clubs/${clubId}/posts`, method: 'POST', data }),
	getList: (clubId, params) => request({ url: `/api/clubs/${clubId}/posts`, method: 'GET', data: params }),
	getDetail: (clubId, postId) => request({ url: `/api/clubs/${clubId}/posts/${postId}`, method: 'GET' }),
	like: (clubId, postId) => request({ url: `/api/clubs/${clubId}/posts/${postId}/like`, method: 'POST' }),
	delete: (clubId, postId) => request({ url: `/api/clubs/${clubId}/posts/${postId}`, method: 'DELETE' }),
	top: (clubId, postId, data) => request({ url: `/api/clubs/${clubId}/posts/${postId}/top`, method: 'PUT', data }),
	createComment: (clubId, postId, data) => request({ url: `/api/clubs/${clubId}/posts/${postId}/comments`, method: 'POST', data }),
	getComments: (clubId, postId, params) => request({ url: `/api/clubs/${clubId}/posts/${postId}/comments`, method: 'GET', data: params }),
	deleteComment: (clubId, commentId) => request({ url: `/api/clubs/${clubId}/posts/comments/${commentId}`, method: 'DELETE' })
}

export const fileApi = {
	upload: (clubId, formData) => request({ url: `/api/clubs/${clubId}/files/upload`, method: 'POST', data: formData }),
	getList: (clubId, params) => request({ url: `/api/clubs/${clubId}/files`, method: 'GET', data: params }),
	download: (clubId, fileId) => request({ url: `/api/clubs/${clubId}/files/${fileId}/download`, method: 'GET' }),
	delete: (clubId, fileId) => request({ url: `/api/clubs/${clubId}/files/${fileId}`, method: 'DELETE' })
}

export const milestoneApi = {
	getMilestoneList: (clubId) => request({ url: `/api/clubs/${clubId}/milestones`, method: 'GET' }),
	createMilestone: (clubId, data) => request({ url: `/api/clubs/${clubId}/milestones`, method: 'POST', data }),
	updateMilestone: (clubId, id, data) => request({ url: `/api/clubs/${clubId}/milestones/${id}`, method: 'PUT', data }),
	deleteMilestone: (clubId, id) => request({ url: `/api/clubs/${clubId}/milestones/${id}`, method: 'DELETE' }),
	getPresidentList: (clubId) => request({ url: `/api/clubs/${clubId}/milestones/presidents`, method: 'GET' }),
	createPresident: (clubId, data) => request({ url: `/api/clubs/${clubId}/milestones/presidents`, method: 'POST', data }),
	updatePresident: (clubId, id, data) => request({ url: `/api/clubs/${clubId}/milestones/presidents/${id}`, method: 'PUT', data }),
	deletePresident: (clubId, id) => request({ url: `/api/clubs/${clubId}/milestones/presidents/${id}`, method: 'DELETE' })
}

export const statisticsApi = {
	getBase: (clubId) => request({ url: `/api/clubs/${clubId}/statistics`, method: 'GET' }),
	getMemberGrowth: (clubId) => request({ url: `/api/clubs/${clubId}/statistics/member-growth`, method: 'GET' }),
	getActivityParticipation: (clubId) => request({ url: `/api/clubs/${clubId}/statistics/activity-participation`, method: 'GET' }),
	getDepartmentDistribution: (clubId) => request({ url: `/api/clubs/${clubId}/statistics/department-distribution`, method: 'GET' }),
	getMonthlyReport: (clubId, params) => request({ url: `/api/clubs/${clubId}/statistics/monthly-report`, method: 'GET', data: params })
}

export const notificationApi = {
	getList: (params) => request({ url: '/api/notifications', method: 'GET', data: params }),
	markAsRead: (id) => request({ url: `/api/notifications/${id}/read`, method: 'PUT' }),
	markAllAsRead: () => request({ url: '/api/notifications/read-all', method: 'PUT' }),
	delete: (id) => request({ url: `/api/notifications/${id}`, method: 'DELETE' }),
	getUnreadCount: () => request({ url: '/api/notifications/unread-count', method: 'GET' })
}
