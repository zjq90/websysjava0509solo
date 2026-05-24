import { get } from '../utils/request'

export const getMyCoupons = (status) => {
  return get('/coupon/my-coupons', { status })
}

export const getAvailableCouponCount = () => {
  return get('/coupon/available-count')
}

export const getCreditRecords = (pageNum, pageSize) => {
  return get('/coupon/credit-records', { pageNum, pageSize })
}
