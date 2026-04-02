# Life OS 安全架构与多身份模拟

## 1. 这次补齐了什么

本轮实现把原来的服务骨架继续往“可控执行平台”推进，新增了三条关键能力：

- 用户作用域隔离
  - `plans`
  - `confirmations`
  - `knowledge documents`
  - `security audit entries`
- 安全控制平面
  - 信任分层
  - 工具权限策略
  - 外呼白名单
  - 审计流水
- 多身份模拟
  - `urban-traveler`
  - `habit-builder`
  - `guest-explorer`
  - `ops-reviewer`

## 2. 为什么要这样做

结合近期公开的 Agent 产品安全事件，可以看到两个共识：

- 发布物泄漏说明，Agent 产品必须管理好产物、配置和调试信息
- Hooks / MCP / env 类攻击说明，工具和配置本身就是攻击面

所以这次没有把安全只做成“文档建议”，而是直接落到服务里：

- 让用户数据默认按身份隔离
- 让不同身份看到不同的信任状态和工具权限
- 让关键操作写入可查询的审计流水

## 3. 当前实现的安全模型

### 3.1 信任分层

`LifeOsSecurityService` 会基于 `userId prefix` 解析当前身份：

- `restricted`
- `guarded`
- `trusted`
- `operator`

并输出：

- 是否信任工作区
- 是否信任 MCP / 远程 specialist
- 是否允许外部网络
- 是否强制写入审批
- 当前外呼白名单

### 3.2 工具权限策略

当前以只读策略视图的方式输出 5 类能力：

- `knowledge.read`
- `travel.search`
- `remote.specialist`
- `external.write`
- `system.exec`

对应状态包括：

- `allowed`
- `denied`
- `approval-required`
- `seeded-only`
- `guarded-live`
- `guarded-auto`

### 3.3 审计流水

新增 `security_audit_entries`，记录这些关键事件：

- 计划生成
- 助手回复
- 恢复执行
- 审批决策
- 用户画像更新
- 知识文档写入
- 跨用户读取拦截

## 4. 多身份模拟的目标

多身份模拟不是只为演示，而是为了更快发现服务问题。

例如这次就暴露出了几个真实问题：

- 切换用户后，历史计划和确认项仍展示全局数据
- 知识文档之前是全局可见，存在串用户风险
- 恢复执行之前没有用户归属校验
- 模块探针读取时会真实写入计划和确认项，污染运营指标

现在页面内置 persona，可以快速验证：

- 旅行用户的低疲劳路径
- 学习用户的习惯保护
- 首次或低信任用户的受限模式
- 运营观察者的安全和系统状态

## 5. 新增接口

- `GET /api/v1/personas?locale=...`
- `GET /api/v1/security/overview?userId=...&limit=...`
- `GET /api/v1/security/audit?userId=...&limit=...`

并且以下接口已改成用户作用域：

- `GET /api/v1/plans?userId=...`
- `GET /api/v1/plans/{planId}?userId=...`
- `GET /api/v1/confirmations?userId=...`
- `POST /api/v1/confirmations/{id}/decision?userId=...`
- `GET /api/v1/knowledge/documents?userId=...`
- `POST /api/v1/knowledge/documents?userId=...`
- `POST /api/v1/assistant/resume`
  - 请求体新增 `userId`

## 6. 数据层变化

### 6.1 confirmation_requests

新增：

- `user_id`

### 6.2 knowledge_documents

新增：

- `user_id`

其中：

- 用户私有文档按 `userId` 隔离
- 系统种子知识使用 `system-seed`

### 6.3 security_audit_entries

新增表字段：

- `id`
- `user_id`
- `thread_id`
- `category`
- `action`
- `target`
- `outcome`
- `detail`
- `created_at`

## 7. 前端变化

### ToC

新增：

- persona 场景切换
- 用户护栏卡片
- 切换身份后自动刷新用户侧数据

### ToB

新增：

- 安全控制卡片
- 审计流水卡片
- `ops-reviewer` 身份默认查看全局审计，而不是只看自己的操作

## 8. 当前边界

这次实现的是“服务内安全控制平面第一版”，还不是完整零信任平台。

还没有做的部分包括：

- 真正的认证鉴权
- 租户级 RBAC
- 凭证代理
- MCP 注册中心
- CI 产物扫描器

但当前版本已经把最核心的三件事落了下来：

- 用户数据不再混在一起
- 风险状态可以被看见
- 关键操作可以被追踪
- 运营探针不会再制造新的业务状态
