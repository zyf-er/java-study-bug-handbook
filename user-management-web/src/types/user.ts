export interface User {
  id: number
  username: string
  name: string
  age: number | null
  email: string | null
  phone: string | null
  gender: number | null
  createTime: string | null
  updateTime: string | null
}

export interface UserAddPayload {
  username: string
  password: string
  name: string
  age: number | null
  email: string
  phone: string
  gender: number | null
}

export interface UserUpdatePayload {
  username: string
  name: string
  age: number | null
  email: string
  phone: string
  gender: number | null
}

export interface PageData<T> {
  records: T[]
  total: number
  current: number
  size: number
  pages: number
}

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}
