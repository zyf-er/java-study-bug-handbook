<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  createUser,
  fetchUser,
  fetchUsers,
  getErrorMessage,
  removeUser,
  updateUser,
} from './api'
import type { User, UserAddPayload, UserUpdatePayload } from './types/user'

type FormMode = 'create' | 'edit'

const loading = ref(false)
const submitting = ref(false)
const detailLoading = ref(false)
const users = ref<User[]>([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const query = reactive<{ name: string; age: number | undefined }>({ name: '', age: undefined })
const dialogVisible = ref(false)
const detailVisible = ref(false)
const mode = ref<FormMode>('create')
const editingId = ref<number | null>(null)
const detailUser = ref<User | null>(null)
const formRef = ref<FormInstance>()
const form = reactive<UserAddPayload & { password: string }>({
  username: '',
  password: '',
  name: '',
  age: null,
  email: '',
  phone: '',
  gender: null,
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  age: [
    { type: 'number', min: 1, max: 150, message: '年龄范围为 1-150', trigger: 'blur' },
  ],
  email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
}

async function loadUsers() {
  loading.value = true
  try {
    const data = await fetchUsers({
      current: current.value,
      size: size.value,
      ...(query.name.trim() ? { name: query.name.trim() } : {}),
      ...(query.age !== undefined ? { age: query.age } : {}),
    })
    users.value = data.records
    total.value = data.total
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  } finally {
    loading.value = false
  }
}

function search() {
  current.value = 1
  void loadUsers()
}

function resetSearch() {
  query.name = ''
  query.age = undefined
  search()
}

function openCreate() {
  mode.value = 'create'
  editingId.value = null
  Object.assign(form, {
    username: '',
    password: '',
    name: '',
    age: null,
    email: '',
    phone: '',
    gender: null,
  })
  dialogVisible.value = true
}

function openEdit(user: User) {
  mode.value = 'edit'
  editingId.value = user.id
  Object.assign(form, {
    username: user.username,
    password: '',
    name: user.name,
    age: user.age,
    email: user.email ?? '',
    phone: user.phone ?? '',
    gender: user.gender,
  })
  dialogVisible.value = true
}

async function submitForm() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    if (mode.value === 'create') {
      await createUser(form)
      ElMessage.success('新增成功')
    } else if (editingId.value !== null) {
      const { password: _password, ...payload } = form
      await updateUser(editingId.value, payload as UserUpdatePayload)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    await loadUsers()
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  } finally {
    submitting.value = false
  }
}

async function confirmDelete(user: User) {
  try {
    await ElMessageBox.confirm(`确定删除用户“${user.name}”吗？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
    })
    await removeUser(user.id)
    ElMessage.success('删除成功')
    if (users.value.length === 1 && current.value > 1) current.value--
    await loadUsers()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') ElMessage.error(getErrorMessage(error))
  }
}

async function openDetail(user: User) {
  detailVisible.value = true
  detailLoading.value = true
  try {
    detailUser.value = await fetchUser(user.id)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
    detailVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

function formatGender(gender: number | null) {
  if (gender === 1) return '男'
  if (gender === 2) return '女'
  return '未设置'
}

function formatTime(value: string | null) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-'
}

onMounted(() => void loadUsers())
</script>

<template>
  <main class="page-shell">
    <header class="page-header">
      <div>
        <p class="eyebrow">USER CENTER</p>
        <h1>用户管理系统</h1>
        <p class="subtitle">集中管理用户信息，快速完成查询与维护。</p>
      </div>
      <el-button type="primary" size="large" @click="openCreate">新增用户</el-button>
    </header>

    <section class="panel">
      <div class="filter-row">
        <el-input v-model="query.name" clearable placeholder="请输入姓名" class="filter-name" @keyup.enter="search">
          <template #prefix>姓名</template>
        </el-input>
        <el-input-number v-model="query.age" :min="1" :max="150" :controls="false" placeholder="请输入年龄" class="filter-age" @keyup.enter="search" />
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </section>

    <section class="panel table-panel">
      <div class="table-heading">
        <div>
          <h2>用户列表</h2>
          <span>共 {{ total }} 位用户</span>
        </div>
      </div>
      <el-table v-loading="loading" :data="users" stripe>
        <el-table-column prop="id" label="ID" width="90" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="name" label="姓名" min-width="120" />
        <el-table-column prop="age" label="年龄" width="90" />
        <el-table-column label="性别" width="100">
          <template #default="{ row }">{{ formatGender(row.gender) }}</template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDetail(row)">查看</el-button>
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="confirmDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无用户数据" />
        </template>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="current"
          v-model:page-size="size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @current-change="loadUsers"
          @size-change="() => { current = 1; loadUsers() }"
        />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="mode === 'create' ? '新增用户' : '编辑用户'" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="82px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" /></el-form-item>
        <el-form-item v-if="mode === 'create'" label="密码" prop="password"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="年龄" prop="age"><el-input-number v-model="form.age" :min="1" :max="150" controls-position="right" /></el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" clearable placeholder="请选择性别">
            <el-option label="男" :value="1" /><el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitting" @click="submitForm">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="用户详情" width="500px">
      <el-skeleton v-if="detailLoading" :rows="5" animated />
      <el-descriptions v-else-if="detailUser" :column="1" border>
        <el-descriptions-item label="用户 ID">{{ detailUser.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailUser.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detailUser.name }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ detailUser.age ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ formatGender(detailUser.gender) }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailUser.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailUser.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(detailUser.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatTime(detailUser.updateTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </main>
</template>
