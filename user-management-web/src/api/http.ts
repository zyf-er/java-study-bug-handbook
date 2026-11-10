import axios from 'axios'
import type { AxiosError } from 'axios'
import type { ApiResult } from '@/types/user'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

export function getErrorMessage(error: unknown): string {
  const axiosError = error as AxiosError<ApiResult<null>>
  return axiosError.response?.data?.message ?? '请求失败，请稍后重试'
}

export default http
