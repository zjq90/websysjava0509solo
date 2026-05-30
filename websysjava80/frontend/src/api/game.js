import request from '@/utils/request'

export function getGameList(params) {
  return request({
    url: '/games',
    method: 'get',
    params
  })
}

export function getGameDetail(id) {
  return request({
    url: `/games/${id}`,
    method: 'get'
  })
}

export function saveGame(data) {
  return request({
    url: '/games',
    method: 'post',
    data
  })
}

export function updateGame(id, data) {
  return request({
    url: `/games/${id}`,
    method: 'put',
    data
  })
}

export function deleteGame(id) {
  return request({
    url: `/games/${id}`,
    method: 'delete'
  })
}

export function batchUpdateStatus(ids, status) {
  return request({
    url: '/games/batch/status',
    method: 'put',
    params: { ids: ids.join(','), status }
  })
}

export function batchUpdateRecommend(ids, recommend) {
  return request({
    url: '/games/batch/recommend',
    method: 'put',
    params: { ids: ids.join(','), recommend }
  })
}

export function exportGames(params) {
  return request({
    url: '/games/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function getPendingGames(params) {
  return request({
    url: '/games/pending',
    method: 'get',
    params
  })
}
