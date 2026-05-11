import request from '@/utils/request'

export const getPopularDoctors = () => {
    return request.get('/doctors/popular')
}

export const getAllDoctors = () => {
    return request.get('/doctors')
}

export const getDoctorDetail = (id) => {
    return request.get(`/doctors/${id}`)
}

export const searchDoctors = (keyword) => {
    return request.get('/doctors/search', { keyword })
}
