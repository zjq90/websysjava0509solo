const request = {
    get(url, params = {}) {
        return uni.$u.http.get(url, params)
    },
    post(url, data = {}) {
        return uni.$u.http.post(url, data)
    },
    put(url, data = {}) {
        return uni.$u.http.put(url, data)
    },
    delete(url, params = {}) {
        return uni.$u.http.delete(url, params)
    },
    download(url, params = {}) {
        return new Promise((resolve, reject) => {
            uni.downloadFile({
                url: uni.$u.http.config.baseUrl + url,
                header: {
                    Authorization: 'Bearer ' + uni.getStorageSync('token')
                },
                data: params,
                success: (res) => {
                    if (res.statusCode === 200) {
                        resolve(res.tempFilePath)
                    } else {
                        reject(res)
                    }
                },
                fail: reject
            })
        })
    }
}

export const activityApi = {
    getList(params) {
        return request.get('/activities', params)
    },
    getDetail(id) {
        return request.get(`/activities/${id}`)
    },
    create(data) {
        return request.post('/activities', data)
    },
    update(id, data) {
        return request.put(`/activities/${id}`, data)
    },
    delete(id) {
        return request.delete(`/activities/${id}`)
    },
    publish(id) {
        return request.post(`/activities/${id}/publish`)
    },
    cancel(id) {
        return request.post(`/activities/${id}/cancel`)
    },
    getQrCode(id) {
        return request.get(`/activities/${id}/qr-code`)
    },
    refreshQrCode(id) {
        return request.post(`/activities/${id}/qr-code/refresh`)
    },
    archive(id) {
        return request.post(`/activities/${id}/archive`)
    },
    getArchivedList(params) {
        return request.get('/activities/archived', params)
    },
    getSignInStatistics(id) {
        return request.get(`/activities/${id}/signin-statistics`)
    }
}

export const registrationApi = {
    register(data) {
        return request.post('/registrations', data)
    },
    audit(data) {
        return request.post('/registrations/audit', data)
    },
    batchAudit(data) {
        return request.post('/registrations/audit/batch', data)
    },
    cancel(id) {
        return request.delete(`/registrations/${id}`)
    },
    getDetail(id) {
        return request.get(`/registrations/${id}`)
    },
    getActivityList(activityId, params) {
        return request.get(`/registrations/activity/${activityId}`, params)
    },
    getMyList(params) {
        return request.get('/registrations/my', params)
    },
    exportList(activityId, params) {
        return request.download(`/registrations/activity/${activityId}/export`, params)
    }
}

export const signInApi = {
    signIn(data) {
        return request.post('/signin', data)
    },
    makeUp(params) {
        return request.post('/signin/makeup', params)
    },
    getList(activityId) {
        return request.get(`/signin/activity/${activityId}`)
    },
    getStatistics(activityId) {
        return request.get(`/signin/activity/${activityId}/statistics`)
    },
    exportList(activityId, params) {
        return request.download(`/signin/activity/${activityId}/export`, params)
    },
    markAbsent(activityId) {
        return request.post(`/signin/activity/${activityId}/mark-absent`)
    }
}

export const summaryApi = {
    create(data) {
        return request.post('/activity-summaries', data)
    },
    update(id, data) {
        return request.put(`/activity-summaries/${id}`, data)
    },
    publish(id) {
        return request.post(`/activity-summaries/${id}/publish`)
    },
    delete(id) {
        return request.delete(`/activity-summaries/${id}`)
    },
    getDetail(id) {
        return request.get(`/activity-summaries/${id}`)
    },
    getByActivity(activityId) {
        return request.get(`/activity-summaries/activity/${activityId}`)
    },
    getListByClub(clubId, params) {
        return request.get(`/activity-summaries/club/${clubId}`, params)
    }
}

export const ratingApi = {
    submit(data) {
        return request.post('/activity-ratings', data)
    },
    getList(activityId, params) {
        return request.get(`/activity-ratings/activity/${activityId}`, params)
    },
    getMyRating(activityId) {
        return request.get(`/activity-ratings/activity/${activityId}/my`)
    },
    delete(id) {
        return request.delete(`/activity-ratings/${id}`)
    }
}

export default {
    activityApi,
    registrationApi,
    signInApi,
    summaryApi,
    ratingApi
}
