FROM eclipse-temurin:23-jdk
ADD target/RecipeDemo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]