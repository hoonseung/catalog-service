FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR workspace
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} catalog-service.jar
RUN java -Djarmode=layertools -jar catalog-service.jar extract


FROM eclipse-temurin:17-jdk-alpine
RUN adduser catalog
USER catalog
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]