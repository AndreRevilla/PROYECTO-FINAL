
package tv.codely.apps.mooc.backend.functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentsRegistrationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8030/students.html");
    }

    // CP-01: Registro exitoso
    @Test
    void shouldRegisterStudentSuccessfully() {
        String email = "student-" + UUID.randomUUID() + "@test.com";

        driver.findElement(By.id("name")).sendKeys("Juan");
        driver.findElement(By.id("surname")).sendKeys("Perez");
        driver.findElement(By.id("email")).sendKeys(email);

        driver.findElement(By.id("register-button")).click();

        wait.until(driver ->
            "201".equals(
                driver.findElement(By.id("message"))
                      .getAttribute("data-status")
            )
        );

        assertEquals(
            "Student registered successfully",
            driver.findElement(By.id("message")).getText()
        );
    }

    // CP-02: Nombre vacío
    @Test
    void shouldRejectStudentWithoutName() {
        String email = "student-" + UUID.randomUUID() + "@test.com";

        driver.findElement(By.id("surname")).sendKeys("Perez");
        driver.findElement(By.id("email")).sendKeys(email);

        driver.findElement(By.id("register-button")).click();

        wait.until(driver ->
            "400".equals(
                driver.findElement(By.id("message"))
                      .getAttribute("data-status")
            )
        );

        assertEquals(
            "400",
            driver.findElement(By.id("message"))
                  .getAttribute("data-status")
        );
    }

    // CP-03: Correo inválido
    @Test
    void shouldRejectStudentWithInvalidEmail() {
        driver.findElement(By.id("name")).sendKeys("Juan");
        driver.findElement(By.id("surname")).sendKeys("Perez");
        driver.findElement(By.id("email")).sendKeys("juan@correo");

        driver.findElement(By.id("register-button")).click();

        wait.until(driver ->
            "400".equals(
                driver.findElement(By.id("message"))
                      .getAttribute("data-status")
            )
        );

        assertEquals(
            "400",
            driver.findElement(By.id("message"))
                  .getAttribute("data-status")
        );
    }

    // CP-04: Correo duplicado
    @Test
    void shouldRejectDuplicateStudentEmail() {
        String email = "student-" + UUID.randomUUID() + "@test.com";

        // Primer registro
        driver.findElement(By.id("name")).sendKeys("Juan");
        driver.findElement(By.id("surname")).sendKeys("Perez");
        driver.findElement(By.id("email")).sendKeys(email);

        driver.findElement(By.id("register-button")).click();

        wait.until(driver ->
            "201".equals(
                driver.findElement(By.id("message"))
                      .getAttribute("data-status")
            )
        );

        // Segundo registro con el mismo correo
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Pedro");

        driver.findElement(By.id("surname")).clear();
        driver.findElement(By.id("surname")).sendKeys("Lopez");

        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);

        driver.findElement(By.id("register-button")).click();

        wait.until(driver ->
            "409".equals(
                driver.findElement(By.id("message"))
                      .getAttribute("data-status")
            )
        );

        assertEquals(
            "409",
            driver.findElement(By.id("message"))
                  .getAttribute("data-status")
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
