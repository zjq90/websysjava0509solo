import request from '@/utils/request'

export const analyzeSymptoms = (symptoms) => {
    return request.post('/diagnosis/analyze', { symptoms })
}

export const getHotSymptoms = () => {
    return request.get('/diagnosis/hot-symptoms')
}
