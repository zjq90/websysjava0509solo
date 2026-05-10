# 种子库存管理系统 API 测试脚本
# 使用方法：在PowerShell中运行 .\test-api.ps1

$BASE_URL = "http://localhost:8080"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  种子库存管理系统 API 测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 测试1：获取仓库列表
Write-Host "[1/10] 测试获取仓库列表..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/warehouses" -Method Get -TimeoutSec 10
    Write-Host "✓ 成功获取 $($response.data.Count) 个仓库" -ForegroundColor Green
    foreach ($wh in $response.data) {
        Write-Host "   - $($wh.warehouseName) ($($wh.warehouseCode))"
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试2：获取种子列表
Write-Host "[2/10] 测试获取种子列表..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/seeds" -Method Get -TimeoutSec 10
    Write-Host "✓ 成功获取 $($response.data.Count) 个种子" -ForegroundColor Green
    foreach ($seed in $response.data) {
        Write-Host "   - $($seed.seedName) ($($seed.seedCode))"
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试3：获取库存列表
Write-Host "[3/10] 测试获取库存列表..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/inventory/list" -Method Get -TimeoutSec 10
    Write-Host "✓ 成功获取 $($response.data.Count) 个库存批次" -ForegroundColor Green
    foreach ($inv in $response.data) {
        Write-Host "   - 批次: $($inv.batchNo), 数量: $($inv.quantity)$($inv.unit), 状态: $($inv.status)"
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试4：扫码查询库存
Write-Host "[4/10] 测试扫码查询库存 (批次: WH01AB1234)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/inventory/scan/WH01AB1234" -Method Get -TimeoutSec 10
    if ($response.code -eq 200) {
        $inv = $response.data.inventory
        Write-Host "✓ 查询成功" -ForegroundColor Green
        Write-Host "   批次号: $($inv.batchNo)"
        Write-Host "   数量: $($inv.quantity)$($inv.unit)"
        Write-Host "   保质期: $($inv.expiryDate)"
        Write-Host "   发芽率: $($inv.germinationRate)%"
    } else {
        Write-Host "✗ 查询失败: $($response.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试5：获取客户列表
Write-Host "[5/10] 测试获取客户列表..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/customers" -Method Get -TimeoutSec 10
    Write-Host "✓ 成功获取 $($response.data.Count) 个客户" -ForegroundColor Green
    foreach ($cust in $response.data) {
        Write-Host "   - $($cust.customerName), 联系人: $($cust.contactName)"
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试6：获取通知列表
Write-Host "[6/10] 测试获取通知列表..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/notifications" -Method Get -TimeoutSec 10
    Write-Host "✓ 成功获取 $($response.data.Count) 条通知" -ForegroundColor Green
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试7：检测近效期
Write-Host "[7/10] 测试检测近效期..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/inventory/check-expiry" -Method Post -TimeoutSec 10
    if ($response.code -eq 200) {
        Write-Host "✓ 检测完成，发现 $($response.data) 条预警记录" -ForegroundColor Green
    } else {
        Write-Host "✗ 检测失败: $($response.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ 失败: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# 测试8：测试数据校验 - 无效批次号
Write-Host "[8/10] 测试数据校验 - 无效批次号..." -ForegroundColor Yellow
try {
    $body = @{
        batchNo = "INVALID"  # 不符合8位数字字母格式
        warehouseId = 1
        seedId = 1
        quantity = 100
        unit = "kg"
        expiryDate = (Get-Date).AddMonths(12).ToString("yyyy-MM-dd")
        germinationRate = 95.5
        origin = "测试产地"
        storageLocation = "A-01"
    }
    $json = $body | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/inventory/inbound?operator=测试员" -Method Post -Body $json -ContentType "application/json" -TimeoutSec 10
    if ($response.code -ne 200) {
        Write-Host "✓ 校验成功，拒绝了无效批次号: $($response.message)" -ForegroundColor Green
    } else {
        Write-Host "! 注意: 批次号可能被接受了" -ForegroundColor Yellow
    }
} catch {
    $errorMsg = $_.Exception.Message
    if ($errorMsg -match "批次号" -or $errorMsg -match "400" -or $errorMsg -match "500") {
        Write-Host "✓ 校验成功，拒绝了无效批次号" -ForegroundColor Green
    } else {
        Write-Host "! 响应: $errorMsg" -ForegroundColor Yellow
    }
}
Write-Host ""

# 测试9：Swagger文档检查
Write-Host "[9/10] 检查Swagger文档..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$BASE_URL/swagger-ui.html" -Method Get -TimeoutSec 10 -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ Swagger文档可用: $BASE_URL/swagger-ui.html" -ForegroundColor Green
    }
} catch {
    Write-Host "! Swagger检查: $($_.Exception.Message)" -ForegroundColor Yellow
}
Write-Host ""

# 测试10：H2控制台检查
Write-Host "[10/10] 检查H2控制台..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$BASE_URL/h2-console" -Method Get -TimeoutSec 10 -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ H2控制台可用: $BASE_URL/h2-console" -ForegroundColor Green
    }
} catch {
    Write-Host "! H2控制台检查: $($_.Exception.Message)" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  测试完成!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Swagger文档: $BASE_URL/swagger-ui.html"
Write-Host "H2控制台: $BASE_URL/h2-console"
Write-Host "  - JDBC URL: jdbc:h2:mem:seedinventorydb"
Write-Host "  - 用户名: sa"
Write-Host "  - 密码: (空)"
