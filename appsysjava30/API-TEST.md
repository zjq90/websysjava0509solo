# 医疗挂号系统 API 测试脚本

## 1. 获取测试Token
GET http://localhost:8080/api/test/token

### 响应示例：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userId": 1,
    "username": "zhangshan",
    "realName": "张三",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

## 2. 登录（手动获取Token）
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "zhangshan",
  "password": "123456"
}

## 3. 获取科室列表（需携带Token）
GET http://localhost:8080/api/schedules/departments
Authorization: Bearer {token}

## 4. 获取某科室的号源（如内科 deptCode=001）
GET http://localhost:8080/api/schedules/departments/001/date/2026-05-11
Authorization: Bearer {token}

## 5. 创建预约
POST http://localhost:8080/api/registrations
Content-Type: application/json
Authorization: Bearer {token}

{
  "scheduleId": 1,
  "symptoms": "头痛、发热",
  "remark": "无"
}

## 6. 支付挂号费
POST http://localhost:8080/api/registrations/pay
Content-Type: application/json
Authorization: Bearer {token}

{
  "registrationNo": "REG1234567890",
  "paymentMethod": "WECHAT"
}

## 7. 查看预约列表
GET http://localhost:8080/api/registrations?page=0&size=10
Authorization: Bearer {token}

## 8. 查看预约详情
GET http://localhost:8080/api/registrations/{registrationNo}
Authorization: Bearer {token}

## 9. 取消预约
POST http://localhost:8080/api/registrations/{registrationNo}/cancel
Authorization: Bearer {token}

## 10. 模拟退费完成（测试用）
POST http://localhost:8080/api/test/refund/{registrationNo}

## 11. 模拟就诊完成（测试用）
POST http://localhost:8080/api/test/visit-complete/{registrationNo}

## 12. 健康检查
GET http://localhost:8080/api/test/health
