package users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import users.model.User;
import users.service.UserService;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"users.controller", "users.service", "users.repository", "users.model", "users.config"})
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        UserService userService = context.getBean(UserService.class);

        System.out.println("Создаем новых пользователей:");
        User u1 = userService.create("Mariam", LocalDate.of(1995, 5, 15));
        User u2 = userService.create("Diniya", LocalDate.of(1998, 8, 22));
        User u3 = userService.create("Raihana", LocalDate.of(1996, 3, 10));
        System.out.println("   " + u1);
        System.out.println("   " + u2);
        System.out.println("   " + u3);

        System.out.println("\nВсе пользователи в базе:");
        List<User> all = userService.getAll();
        for (User u : all) {
            System.out.println("   " + u);
        }

        System.out.println("\nПолучаем пользователя с id=1:");
        try {
            User found = userService.getById(1L);
            System.out.println("   " + found);
        } catch (RuntimeException e) {
            System.out.println("   Пользователь не найден");
        }

        System.out.println("\nОбновляем пользователя с id=1:");
        try {
            User updated = userService.update(1L, "Mariam Borina", LocalDate.of(1995, 5, 15), null);
            System.out.println("   " + updated);
        } catch (RuntimeException e) {
            System.out.println("   Пользователь не найден");
        }

        System.out.println("\nВерифицируем пользователя с id=2:");
        try {
            User verified = userService.verifyUser(2L);
            System.out.println("   " + verified);
        } catch (RuntimeException e) {
            System.out.println("   Пользователь не найден");
        }

        System.out.println("\nУдаляем пользователя с id=3:");
        boolean deleted = userService.delete(3L);
        System.out.println("   Результат удаления: " + deleted);

        System.out.println("\nИтоговый список пользователей:");
        List<User> finalList = userService.getAll();
        for (User u : finalList) {
            System.out.println("   " + u);
        }

    }
}