package de.goldendeveloper.discord.musicbot.example;

public class Main {

    public static final String token = "";

    private static Discord discord;

    public static void main(String[] args) {
        discord = new Discord(token);
    }

    public static Discord getDiscord() {
        return discord;
    }
}
