package ru.Praktikim;

import com.github.javafaker.Faker;

public class GenerateUser {

    public static User generateUser() {

        Faker faker = new Faker();

        String email = faker.name().username().toLowerCase() + "@mail.ru";
        String password = faker.name().name();
        String name = faker.name().username();

        return new User(email, password, name);
    }
}
