package com.smartexpense;

public class Main {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        String email = "test@gmail.com";
        String password = "12345";

        User user = userDAO.loginUser(email, password);

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getName());
            System.out.println("User ID: " + user.getId());
        } else {
            System.out.println("Invalid email or password!");
        }
    }
}