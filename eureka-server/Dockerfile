# ---------- Build stage ----------
# Gradle 8.7, JDK 17이 설치된 Alpine 리눅스를 import하고 이를 builder로 지칭
# Alpine 리눅스는 불필요한 기능(화려한 텍스트 편집기나 그래픽 툴)을 제외한 경량 버전임
FROM gradle:8.7-jdk17-alpine AS builder
# builder의 workdspace 공간에서 build 작업
WORKDIR /workspace
# Host의 모든 파일을 builder의 workspace로 복사
# 필요한 것만 copy하는게 아니라, 멀티모듈 프로젝트이기 때문에 Root의 build.gradle.kts 등 전체 파일 복사 필요함
COPY . .
# eureka-server/Dockerfile이기 때문에, eureka-server 모듈만 실행 가능 Jar 파일로 build
RUN gradle :eureka-server:bootJar --no-daemon

# ---------- Run stage ----------
# Eclipse Temurin 버전의 JRE(Java Executor) import
# 실행만할 땐 JDK(컴파일러 javac, 디버거 등 포함) 보단 JRE가 가벼움
FROM eclipse-temurin:17-jre
# Default Port 값으로, Render에서 PORT를 제공 시 Render의 PORT를 따름
ENV PORT=8761
# 해당 Docker Container의 app 공간에서 execution 작업
WORKDIR /app
# 앞서 builder가 build한 eureka-server의 bootJar 파일 복사 후 app.jar 이름으로 저장
COPY --from=builder /workspace/eureka-server/build/libs/*.jar app.jar
# 문서용 노출 (실제 바인딩은 $PORT 사용)
EXPOSE 8761
# Render의 해당 Docker Imgage의 docker run 시, sh 통해 Render의 PORT로 app.jar 파일 실행 명령
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]

# Dockerfile은 타겟 프로젝트에 대한 Docker Image Build 용 파일임
# Render에서 Dockerfile에 의해 build 된 Docker Image를 Container로 실행
# vs docker-compose.yml(IaC): N개의 Docker Image를 Container로 실행 및 구성하여 Host를 지원하는 목적