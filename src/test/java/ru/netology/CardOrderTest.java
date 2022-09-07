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
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldPresent() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWithDoubleName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Мария-Луиза");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWhenNameIsEmpty() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWhenNoValidName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Perry");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='name'] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWhenPhoneNumberIsEmpty() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='phone'] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWhenPhoneNumberNoValid() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+790");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span.input_invalid[data-test-id='phone'] .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    void shouldPresentWhenNotSelectedCheckbox() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79000000000");
        driver.findElement(By.cssSelector("[type='button']")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).getText();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expected, actualText);
    }
}
