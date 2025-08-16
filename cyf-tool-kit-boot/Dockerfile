# 使用官方 JDK 8 基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将JAR文件复制到镜像中
COPY target/cyf-tool-kit-boot.jar app.jar

# 设置时区（可选）
RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 暴露应用端口（根据你的应用端口修改）
EXPOSE 18888

# 启动Java应用
ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]