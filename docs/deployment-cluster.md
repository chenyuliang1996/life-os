# 分布式部署说明

## 1. 已提供的部署资产

- `Dockerfile`
- `deploy/docker-compose.cluster.yml`
- `deploy/nginx/life-os.conf`
- `deploy/postgres/init/01-enable-vector.sql`
- `deploy/k8s/namespace.yaml`
- `deploy/k8s/configmap.yaml`
- `deploy/k8s/session-pvc.yaml`
- `deploy/k8s/deployment.yaml`
- `deploy/k8s/service.yaml`
- `deploy/k8s/ingress.yaml`

## 2. Docker Compose 集群演示

```bash
docker compose -f deploy/docker-compose.cluster.yml up --build
```

启动后结构：

- `postgres`
  - 使用 `pgvector/pgvector:pg16`
- `life-os-1`
- `life-os-2`
- `gateway`
  - Nginx 负载均衡到两台应用实例

访问：

- [http://127.0.0.1:8080/](http://127.0.0.1:8080/)

## 3. Kubernetes

当前 k8s 清单是“应用级集群部署骨架”，默认假设：

- PostgreSQL 由外部托管或独立运维
- Secret `life-os-db` 由平台注入
- session 目录挂在共享 PVC 上

## 4. 为什么 session 先用共享目录

这不是最终最优解，但对 Demo 最务实：

- 复用现有 `JsonSession`
- 多副本先能跑起来
- 不引入额外 Redis 依赖

后续升级建议：

- 换成 AgentScope 的 Redis Session
- 或使用其数据库 Session 方案

参考：

- [Session 文档](https://java.agentscope.io/en/task/session.html)

## 5. 后续真正服务化的顺序

推荐顺序：

1. 先保持当前应用双副本
2. 把知识检索切到 pgvector
3. 再把 `travel-agent`、`learning-agent`、`schedule-agent` 独立成 A2A 服务
4. 最后补统一 trace、限流、熔断和权限
