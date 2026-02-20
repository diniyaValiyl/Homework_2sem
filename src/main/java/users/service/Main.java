package users.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import users.config.AppConfig;
import users.model.User;
import users.service.UserService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);

        System.out.println("пользователей:");
        User u1 = userService.create("Mariam");
        User u2 = userService.create("Diniya");
        User u3 = userService.create("Raihana");
        System.out.println("   " + u1);
        System.out.println("   " + u2);
        System.out.println("   " + u3);

        System.out.println("\nВсе");
        List<User> all = userService.getAll();
        for (User u : all) {
            System.out.println("   " + u);
        }

        System.out.println("\n Получаем пользователя с id=3:");
        User found = userService.getById(3L);
        System.out.println("   " + found);

        System.out.println("\nОбновляем пользователя с id=1:");
        User updated = userService.update(1L, "Mariam Borina");
        System.out.println("   " + updated);

        System.out.println("\nУдаляем пользователя с id=2:");
        boolean deleted = userService.delete(2L);
        System.out.println("Кого удалили" + deleted);

        System.out.println("\nИтог");
        List<User> finalList = userService.getAll();
        for (User u : finalList) {
            System.out.println("   " + u);
        }
    }
}