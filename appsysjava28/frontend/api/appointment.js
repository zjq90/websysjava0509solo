import request from '@/utils/request'

export const createAppointment = (data) => {
    return request.post('/appointments', data)
}

export const getAppointmentList = (status) => {
    return request.get('/appointments', { status })
}

export const getAppointmentDetail = (id) => {
    return request.get(`/appointments/${id}`)
}

export const payAppointment = (id) => {
    return request.post(`/appointments/${id}/pay`)
}

export const cancelAppointment = (id, reason) => {
    return request.post(`/appointments/${id}/cancel`, { reason })
}

export const createReview = (id, rating, comment, tags, isAnonymous) => {
    return request.post(`/appointments/${id}/review`, {
        rating,
        comment,
        tags,
        isAnonymous
    })
}
