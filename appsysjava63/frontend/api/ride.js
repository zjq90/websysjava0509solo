import { get, post } from '../utils/request'

export const unlockBike = (data) => {
  return post('/ride/unlock', data)
}

export const lockBike = (data) => {
  return post('/ride/lock', data)
}

export const getOngoingRide = () => {
  return get('/ride/ongoing')
}

export const getCurrentCost = () => {
  return get('/ride/current-cost')
}

export const getRideRecords = (pageNum, pageSize) => {
  return get('/ride/records', { pageNum, pageSize })
}

export const getRideRecordDetail = (recordId) => {
  return get(`/ride/records/${recordId}`)
}
