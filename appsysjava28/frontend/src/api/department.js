import request from '@/utils/request'

export const getDepartmentTree = () => {
    return request.get('/departments/tree')
}

export const getDepartmentDetail = (id) => {
    return request.get(`/departments/${id}`)
}

export const getDoctorsByDepartment = (id) => {
    return request.get(`/departments/${id}/doctors`)
}

export const searchDepartments = (keyword) => {
    return request.get('/departments/search', { keyword })
}
