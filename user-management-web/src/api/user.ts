import http from './http'
import type { ApiResult, PageData, User, UserAddPayload, UserUpdatePayload } from '@/types/user'

export async function fetchUsers(params: {
  current: number
  size: number
  name?: string
  age?: number
}) {
  const response = await http.get<ApiResult<PageData<User>>>('/users/page', { params })
  return response.data.data
}

export async function fetchUser(id: number) {
  const response = await http.get<ApiResult<User>>(`/users/${id}`)
  return response.data.data
}

export async function createUser(payload: UserAddPayload) {
  const response = await http.post<ApiResult<User>>('/users', payload)
  return response.data.data
}

export async function updateUser(id: number, payload: UserUpdatePayload) {
  const response = await http.put<ApiResult<boolean>>(`/users/${id}`, payload)
  return response.data.data
}

export async function removeUser(id: number) {
  const response = await http.delete<ApiResult<boolean>>(`/users/${id}`)
  return response.data.data
}
