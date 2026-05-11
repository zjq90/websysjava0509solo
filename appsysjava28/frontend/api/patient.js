import request from '@/utils/request'

export const getPatientList = () => {
    return request.get('/patients')
}

export const getPatientDetail = (id) => {
    return request.get(`/patients/${id}`)
}

export const addPatient = (data) => {
    return request.post('/patients', data)
}

export const updatePatient = (id, data) => {
    return request.put(`/patients/${id}`, data)
}

export const deletePatient = (id) => {
    return request.delete(`/patients/${id}`)
}

export const verifyIdentity = (id, idCardNumber, name) => {
    return request.post(`/patients/${id}/verify`, { idCardNumber, name })
}

export const setPrimaryPatient = (id) => {
    return request.post(`/patients/${id}/primary`)
}
