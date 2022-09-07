package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @org.junit.Test
    void shouldPresent() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldPresentWithDoubleName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Мария-Луиза");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.className("paragraph_theme_alfa-on-white")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldPresentWhenNameIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldPresentWhenNoValidName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Perry");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldPresentWhenPhoneNumberIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] span.input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldPresentWhenPhoneNumberNoValid() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+790");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'] span.input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldPresentWhenNotSelectedCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+790");
        driver.findElement(By.cssSelector("[type='button']")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid")).getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", text.trim());
    }
}
