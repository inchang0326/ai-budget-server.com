# ---------- Build stage ----------
FROM gradle:8.7-jdk17-alpine AS builder
WORKDIR /workspace

# 멀티모듈 전체를 컨텍스트에 포함해야 하므로 루트 기준으로 복사
COPY . .

# gateway-server 모듈만 부트 JAR 빌드
RUN gradle :gateway-server:bootJar --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre

# Render는 동적 PORT를 제공. 기본값은 8000으로 문서적 표기
ENV PORT=8000
WORKDIR /app

# 빌드 산출물 복사
COPY --from=builder /workspace/gateway-server/build/libs/*.jar app.jar

# 문서용 노출 (실제 바인딩은 $PORT 사용)
EXPOSE 8000

# Spring이 $PORT로 바인딩되도록 JVM 옵션 전달
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]