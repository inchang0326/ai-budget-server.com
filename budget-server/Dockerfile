# ---------- Build stage ----------
FROM gradle:8.7-jdk17-alpine AS builder
WORKDIR /workspace

# 멀티모듈 전체를 컨텍스트로 포함해야 하므로 루트 전체 복사
COPY . .

# budget-server 모듈만 부트 JAR 빌드
RUN gradle :budget-server:bootJar --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre

# Render는 동적 PORT를 제공. 기본값 8080은 문서용 표기
ENV PORT=8080
WORKDIR /app

# 빌드 산출물 복사
COPY --from=builder /workspace/budget-server/build/libs/*.jar app.jar

# 문서용 노출 (실제 바인딩은 $PORT)
EXPOSE 8080

# Spring이 $PORT로 바인딩되도록 JVM 옵션으로 강제
# management.server.port도 동일 포트로 맞춰 헬스체크 불일치 방지
CMD ["sh", "-c", "java -Dserver.port=$PORT -Dmanagement.server.port=$PORT -jar app.jar"]