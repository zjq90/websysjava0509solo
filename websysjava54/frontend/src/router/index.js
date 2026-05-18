import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import HeritageAudit from '../views/heritage/HeritageAudit.vue'
import HeritageList from '../views/heritage/HeritageList.vue'
import CategoryTags from '../views/heritage/CategoryTags.vue'
import UserList from '../views/user/UserList.vue'
import RealnameAudit from '../views/user/RealnameAudit.vue'
import ExpertAudit from '../views/user/ExpertAudit.vue'
import SuspiciousUsers from '../views/user/SuspiciousUsers.vue'
import TransactionList from '../views/transaction/TransactionList.vue'
import AbnormalTransactions from '../views/transaction/AbnormalTransactions.vue'
import FrozenTransactions from '../views/transaction/FrozenTransactions.vue'
import ReportList from '../views/transaction/ReportList.vue'
import Dashboard from '../views/dashboard/Dashboard.vue'
import Heatmap from '../views/dashboard/Heatmap.vue'
import UserGrowth from '../views/dashboard/UserGrowth.vue'
import DesensitizationRules from '../views/settings/DesensitizationRules.vue'
import MonitoringRules from '../views/settings/MonitoringRules.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/heritage-audit',
      name: 'HeritageAudit',
      component: HeritageAudit
    },
    {
      path: '/heritage-list',
      name: 'HeritageList',
      component: HeritageList
    },
    {
      path: '/category-tags',
      name: 'CategoryTags',
      component: CategoryTags
    },
    {
      path: '/user-list',
      name: 'UserList',
      component: UserList
    },
    {
      path: '/realname-audit',
      name: 'RealnameAudit',
      component: RealnameAudit
    },
    {
      path: '/expert-audit',
      name: 'ExpertAudit',
      component: ExpertAudit
    },
    {
      path: '/suspicious-users',
      name: 'SuspiciousUsers',
      component: SuspiciousUsers
    },
    {
      path: '/transaction-list',
      name: 'TransactionList',
      component: TransactionList
    },
    {
      path: '/abnormal-transactions',
      name: 'AbnormalTransactions',
      component: AbnormalTransactions
    },
    {
      path: '/frozen-transactions',
      name: 'FrozenTransactions',
      component: FrozenTransactions
    },
    {
      path: '/report-list',
      name: 'ReportList',
      component: ReportList
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard
    },
    {
      path: '/heatmap',
      name: 'Heatmap',
      component: Heatmap
    },
    {
      path: '/user-growth',
      name: 'UserGrowth',
      component: UserGrowth
    },
    {
      path: '/desensitization-rules',
      name: 'DesensitizationRules',
      component: DesensitizationRules
    },
    {
      path: '/monitoring-rules',
      name: 'MonitoringRules',
      component: MonitoringRules
    }
  ]
})
