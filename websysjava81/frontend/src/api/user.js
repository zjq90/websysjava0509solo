import axios from 'axios'

const baseURL = '/api/user'

export function getUserList(params) {
  return axios.post(`${baseURL}/list`, params)
}

export function getUserDetail(userId) {
  return axios.get(`${baseURL}/detail/${userId}`)
}

export function banUser(data) {
  return axios.post(`${baseURL}/ban`, data)
}

export function unbanUser(banRecordId) {
  return axios.post(`${baseURL}/unban/${banRecordId}`)
}

export function batchBanUsers(data) {
  return axios.post(`${baseURL}/batch-ban`, data)
}

export function getBanRecords(userId) {
  return axios.get(`${baseURL}/ban-records/${userId}`)
}
