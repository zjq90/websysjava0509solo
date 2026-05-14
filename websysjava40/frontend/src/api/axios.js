import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

instance.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error)
    throw error
  }
)

export default instance
