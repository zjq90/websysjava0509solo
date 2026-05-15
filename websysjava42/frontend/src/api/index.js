import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => response.data,
  error => Promise.reject(error)
)

export default {
  login(data) {
    return request.post('/auth/login', data)
  },
  getUsers() {
    return request.get('/auth/users')
  },
  resetPassword(userId, newPassword) {
    return request.post(`/auth/reset-password/${userId}?newPassword=${newPassword}`)
  },
  getReferees() {
    return request.get('/referee')
  },
  getChiefReferees() {
    return request.get('/referee/chief')
  },
  addReferee(data) {
    return request.post('/referee', data)
  },
  updateReferee(id, data) {
    return request.put(`/referee/${id}`, data)
  },
  deleteReferee(id) {
    return request.delete(`/referee/${id}`)
  },
  getAthletes() {
    return request.get('/athlete')
  },
  addAthlete(data) {
    return request.post('/athlete', data)
  },
  updateAthlete(id, data) {
    return request.put(`/athlete/${id}`, data)
  },
  deleteAthlete(id) {
    return request.delete(`/athlete/${id}`)
  },
  evaluateAthlete(id, evaluation) {
    return request.post(`/athlete/${id}/evaluate?evaluation=${evaluation}`)
  },
  getCompetitions() {
    return request.get('/competition')
  },
  addCompetition(data) {
    return request.post('/competition', data)
  },
  updateCompetition(id, data) {
    return request.put(`/competition/${id}`, data)
  },
  deleteCompetition(id) {
    return request.delete(`/competition/${id}`)
  },
  getCompetitionAthletes(competitionId) {
    return request.get(`/competition/${competitionId}/athletes`)
  },
  addAthleteToCompetition(competitionId, athleteId) {
    return request.post(`/competition/${competitionId}/athlete/${athleteId}`)
  },
  removeAthleteFromCompetition(competitionId, athleteId) {
    return request.delete(`/competition/${competitionId}/athlete/${athleteId}`)
  },
  checkIn(competitionId, athleteId) {
    return request.post(`/competition/${competitionId}/athlete/${athleteId}/checkin`)
  },
  getAthleteCompetitions(athleteId) {
    return request.get(`/competition/athlete/${athleteId}/competitions`)
  },
  getScores() {
    return request.get('/score')
  },
  getScoresByCompetition(competitionId) {
    return request.get(`/score/competition/${competitionId}`)
  },
  getScoresByAthlete(athleteId) {
    return request.get(`/score/athlete/${athleteId}`)
  },
  submitScore(data) {
    return request.post('/score', data)
  },
  updateScore(id, data) {
    return request.put(`/score/${id}`, data)
  },
  auditScore(id, auditStatus, auditorId, auditComment) {
    return request.post(`/score/${id}/audit?auditStatus=${auditStatus}&auditorId=${auditorId}&auditComment=${auditComment || ''}`)
  },
  getPendingAuditScores() {
    return request.get('/score/pending')
  },
  getFinalScore(competitionId, athleteId) {
    return request.get(`/score/final-score?competitionId=${competitionId}&athleteId=${athleteId}`)
  }
}
