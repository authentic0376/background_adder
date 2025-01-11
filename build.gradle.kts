plugins {
    application
}

group = "com.sprain6628"
version = "v0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
                "Main-Class" to "BackgroundAdder",            // 실행 진입점 클래스
                "Implementation-Title" to project.name,       // 프로젝트 이름
                "Implementation-Version" to project.version,  // 프로젝트 버전
                "Implementation-Vendor" to "sprain6628",       // 프로젝트 소유자/개발자 이름
                "Built-By" to System.getProperty("user.name") // 빌드한 사용자
        )
    }
}