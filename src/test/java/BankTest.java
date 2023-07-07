
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static DataGenerator.*;
import static DataGenerator.Registration.getRegisteredUser;
import static DataGenerator.Registration.getUser;
import static sun.security.jgss.GSSUtil.login;


class BankTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void successfulRegistered() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void errorNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void blockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void wrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = login();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    void wrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = password();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(10));
    }
}