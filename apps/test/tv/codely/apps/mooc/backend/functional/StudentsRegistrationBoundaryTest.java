package tv.codely.apps.mooc.backend.functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Registro de estudiantes - clases de equivalencia y valores límite")
class StudentsRegistrationBoundaryTest {

    private static final String URL = "http://localhost:8030/students.html";
    private static final String SUCCESS = "Student registered successfully";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ---------- Helpers ----------

    private String random() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void fill(String id, String value) {
        WebElement input = driver.findElement(By.id(id));
        input.clear();
        input.sendKeys(value);
    }

    private void register(String name, String surname, String email) {
        fill("name", name);
        fill("surname", surname);
        fill("email", email);
        driver.findElement(By.id("register-button")).click();
    }

    private String waitForStatus(String expected) {
        wait.until(d -> expected.equals(
            d.findElement(By.id("message")).getAttribute("data-status")
        ));
        return driver.findElement(By.id("message")).getAttribute("data-status");
    }

    private String messageText() {
        return driver.findElement(By.id("message")).getText();
    }

    private boolean emailFieldIsValid() {
        WebElement email = driver.findElement(By.id("email"));
        return (Boolean) ((JavascriptExecutor) driver)
            .executeScript("return arguments[0].checkValidity();", email);
    }

    // ---------- Clases válidas ----------

    @Test
    @DisplayName("CP-01: Apellido vacío es aceptado")
    void cp01_shouldAcceptStudentWithoutSurname() {
        register("Ana", "", "ana-" + random() + "@test.com");

        assertEquals("201", waitForStatus("201"));
        assertEquals(SUCCESS, messageText());
    }

    @Test
    @DisplayName("CP-05: Límite válido - dominio final de 2 letras")
    void cp05_shouldAcceptEmailWithTwoLetterTld() {
        register("Ana", "Perez", "u" + random() + "@b.co");

        assertEquals("201", waitForStatus("201"));
        assertEquals(SUCCESS, messageText());
    }

    @Test
    @DisplayName("CP-10: Correo con '+' en la parte local es aceptado")
    void cp10_shouldAcceptEmailWithPlusSign() {
        register("Juan", "Perez", "juan+" + random() + "@test.com");

        assertEquals("201", waitForStatus("201"));
        assertEquals(SUCCESS, messageText());
    }

    // ---------- Clases inválidas (rechazadas por el servidor) ----------

    @Test
    @DisplayName("CP-02: Nombre con solo espacios es rechazado")
    void cp02_shouldRejectNameWithOnlySpaces() {
        register("   ", "Perez", "esp-" + random() + "@test.com");

        assertEquals("400", waitForStatus("400"));
    }

    @Test
    @DisplayName("CP-03: Todos los campos vacíos son rechazados")
    void cp03_shouldRejectAllEmptyFields() {
        register("", "", "");

        assertEquals("400", waitForStatus("400"));
    }

    @Test
    @DisplayName("CP-04: Correo vacío es rechazado")
    void cp04_shouldRejectEmptyEmail() {
        register("Ana", "Perez", "");

        assertEquals("400", waitForStatus("400"));
    }

    @Test
    @DisplayName("CP-06: Límite inválido - dominio final de 1 letra")
    void cp06_shouldRejectEmailWithOneLetterTld() {
        register("Ana", "Perez", "u" + random() + "@dominio.c");

        assertEquals("400", waitForStatus("400"));
    }

    @Test
    @DisplayName("CP-09: Correo duplicado con distinta capitalización")
    void cp09_shouldRejectDuplicateEmailIgnoringCase() {
        String local = "juan-" + random();

        register("Juan", "Perez", local + "@test.com");
        assertEquals("201", waitForStatus("201"));

        register("Pedro", "Lopez", local.toUpperCase() + "@TEST.COM");
        assertEquals("409", waitForStatus("409"));
    }

    // ---------- Clases inválidas (bloqueadas por el navegador) ----------

    @Test
    @DisplayName("CP-07: Correo sin '@' no se envía")
    void cp07_shouldNotSubmitEmailWithoutAt() {
        register("Juan", "Perez", "juanperez.com");

        assertFalse(emailFieldIsValid());
        assertNull(driver.findElement(By.id("message")).getAttribute("data-status"));
        assertEquals("", messageText());
    }

    @Test
    @DisplayName("CP-08: Correo con espacio interno no se envía")
    void cp08_shouldNotSubmitEmailWithSpace() {
        register("Juan", "Perez", "juan perez@test.com");

        assertFalse(emailFieldIsValid());
        assertNull(driver.findElement(By.id("message")).getAttribute("data-status"));
        assertEquals("", messageText());
    }
}