package ru.netology;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;


public class LoginTest {

    @BeforeEach
    public void setUpAll() {
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    @Test
    void shouldSuccessLoginIfRegisteredActiveUser() {
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    void shouldFailLoginIfNotRegisteredUser() {
        val notRegisteredUser = getUser("blocked");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Ошибка")).shouldBe(visible, Duration.ofSeconds(5));
        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldFailLoginIfBlockedUser() {
        val blockedUser = getUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldFailLoginIfWrongLogin() {
        val registeredUser = getRegisteredUser("active");
        val wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }


    @Test
    void shouldFailLoginIfWrongPassword() {
        val registeredUser = getRegisteredUser("active");
        val wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldHave(text("Неверно указан логин или пароль"));
    }

}
