export function formatDate(date, format = 'yyyy-MM-dd') {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('yyyy', year)
    .replace('MM', month)
    .replace('dd', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

export function getToday() {
  return formatDate(new Date())
}

export function getTomorrow() {
  const d = new Date()
  d.setDate(d.getDate() + 1)
  return formatDate(d)
}

export function getDayAfterTomorrow() {
  const d = new Date()
  d.setDate(d.getDate() + 2)
  return formatDate(d)
}

export function getWeekDays() {
  const days = []
  const weekNames = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  for (let i = 0; i < 7; i++) {
    const d = new Date()
    d.setDate(d.getDate() + i)
    days.push({
      date: formatDate(d),
      day: d.getDate(),
      week: i === 0 ? '今天' : (i === 1 ? '明天' : weekNames[d.getDay()]),
      timestamp: d.getTime()
    })
  }
  return days
}

export function getStatusTag(status) {
  const map = {
    'BOOKED': { text: '预约成功', class: 'tag-success' },
    'VISITING': { text: '就诊中', class: 'tag-info' },
    'VISITED': { text: '已就诊', class: 'tag-info' },
    'CANCELLED': { text: '已取消', class: 'tag-default' },
    'REFUNDING': { text: '退费中', class: 'tag-warning' }
  }
  return map[status] || { text: status, class: 'tag-default' }
}

export function getPaymentStatusText(status) {
  const map = {
    'UNPAID': '待支付',
    'PAID': '已支付',
    'REFUNDING': '退费中',
    'REFUNDED': '已退款'
  }
  return map[status] || status
}

export function maskPhone(phone) {
  if (!phone || phone.length < 11) return phone
  return phone.substring(0, 3) + '****' + phone.substring(7)
}

export function maskIdCard(idCard) {
  if (!idCard) return idCard
  if (idCard.length === 15) {
    return idCard.substring(0, 6) + '****' + idCard.substring(12)
  }
  return idCard.substring(0, 6) + '********' + idCard.substring(14)
}
