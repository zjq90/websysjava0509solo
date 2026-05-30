import axios from 'axios'

const baseURL = '/api/audit'

export function getCommentList(params) {
  return axios.get(`${baseURL}/comments`, { params })
}

export function auditComment(data) {
  return axios.post(`${baseURL}/comment/audit`, data)
}

export function getSensitiveWordList(params) {
  return axios.get(`${baseURL}/sensitive-words`, { params })
}

export function addSensitiveWord(data) {
  return axios.post(`${baseURL}/sensitive-word`, data)
}

export function updateSensitiveWord(id, data) {
  return axios.put(`${baseURL}/sensitive-word/${id}`, data)
}

export function deleteSensitiveWord(id) {
  return axios.delete(`${baseURL}/sensitive-word/${id}`)
}

export function exportSensitiveWords() {
  window.open(`${baseURL}/sensitive-word/export`)
}

export function getReportList(params) {
  return axios.get(`${baseURL}/reports`, { params })
}

export function handleReport(data) {
  return axios.post(`${baseURL}/report/handle`, data)
}

export function getReportStatistics() {
  return axios.get(`${baseURL}/report/statistics`)
}
