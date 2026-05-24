import { get, post } from '../utils/request'

export const getNearbyBikes = (lat, lng, bikeType, minBattery, limit) => {
  return get('/bike/nearby', { lat, lng, bikeType, minBattery, limit })
}

export const getBikeDetail = (bikeId) => {
  return get(`/bike/${bikeId}`)
}

export const getBikeByQrCode = (qrCode) => {
  return get('/bike/qrcode', { qrCode })
}

export const reserveBike = (bikeId) => {
  return post('/bike/reserve', null, { params: { bikeId } })
}

export const cancelReservation = (reservationId) => {
  return post('/bike/reserve/cancel', null, { params: { reservationId } })
}

export const getCurrentReservation = () => {
  return get('/bike/reserve/current')
}

export const generateDynamicQrCode = (bikeId) => {
  return get(`/bike/${bikeId}/dynamic-qrcode`)
}
