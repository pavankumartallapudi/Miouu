## Explanation of Docker File

## 🐳 Dockerfile (for reference)

```dockerfile
# ===== Build stage =====
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 9696
ENV PORT=9696

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# 🏗️ Build Stage — Why It Exists

The build stage exists because:

* **Local machine builds are not reliable**
  Developers may use different Java versions, different Maven versions, etc.
  Docker may build using a different environment.

* **We want the project to be compiled in a controlled, consistent environment.**
  Using the official Maven image ensures that the build is always reproducible and uses the exact versions of Maven + JDK we expect.

---

## 🔹 Line-by-Line Explanation (Build Stage)

### **1. FROM maven:3.8.3-openjdk-17 AS build**

We are instructing Docker to use:

* Maven **3.8.3** (official)
* JDK **17**

This ensures:

* The build happens in a fully predictable environment
* No dependency on the developer’s local Maven/Java versions
* A JAR built **inside Docker**, not your local JAR

---

### **2. WORKDIR /app**

Sets the working directory to **/app**.
All subsequent commands run in this folder.

---

### **3. COPY pom.xml .**

You copy only the `pom.xml` first for a very important reason:

* Docker will **cache** this layer
* So if no dependency changes → no re-download
* If a new dependency is added → **only the delta** is downloaded

This dramatically speeds up builds.

---

### **4. RUN mvn dependency:go-offline**

This downloads all Maven dependencies upfront so that:

* The next steps run faster
* Cached dependencies persist across builds
* Only changed dependencies are fetched later

---

### **5. COPY src ./src**

After dependencies are cached, you copy your actual source code.

If this were copied earlier, any small source code change would invalidate the cache and force Maven to re-download everything — exactly what we avoid with the previous step.

---

### **6. RUN mvn clean package -DskipTests**

This is the **actual build step**.

* Cleans any previous build
* Compiles the project
* Packages it into a JAR
* Skips tests for faster container builds

At this point, Docker has produced a fresh, clean, and consistent JAR file.

---

# 🚀 Runtime Stage — Why It Exists

The runtime stage keeps the final image:

* **Small**
* **Efficient**
* **Secure**
* **Free from Maven / JDK / source code**

We only need:

* Java runtime
* The compiled JAR

---

## 🔹 Line-By-Line Explanation (Runtime Stage)

### **1. FROM eclipse-temurin:17-jre**

Uses the lightweight **JRE-only image**, not the full JDK.
This is:

* Smaller
* Faster
* Suitable for production

---

### **2. WORKDIR /app**

Sets the working directory where the application will run.

---

### **3. COPY --from=build /app/target/*.jar app.jar**

Copies the JAR created in the build stage into this runtime environment and renames it to `app.jar`.

This ensures:

* The runtime container only contains what is needed
* No source code or build tools are included
* The JAR inside the container is always the one built using Docker’s Maven environment

---

### **4. EXPOSE 9696**

Provides a hint to Docker that the application listens on port **9696**.

This does **not** open the port automatically — but communicates it to tools like:

* Docker
* Kubernetes
* Compose

---

### **5. ENV PORT=9696**

Sets an environment variable `PORT=9696`.
This is useful for:

* Cloud providers
* Container orchestrators
* Runtime overrides

---

### **6. ENTRYPOINT ["java", "-jar", "app.jar"]**

Defines the command that runs when the container starts.

Since we renamed the jar to `app.jar`, this ensures:

```
java -jar app.jar
```

runs every time.

---

