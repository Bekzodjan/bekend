# 1. Baza tasviri (image) ni tanlash
FROM openjdk:21-jdk

# 2. Ishchi katalogni yaratish
WORKDIR /app

# 3. Loyiha fayllarini konteynerga nusxalash
COPY target/*.jar app.jar

# 4. Konteyner ichida foydalaniladigan portni ko‘rsatish
EXPOSE 8080

# 5. Konteyner ishga tushganda bajariladigan buyruq
CMD ["java", "-jar", "app.jar"]
