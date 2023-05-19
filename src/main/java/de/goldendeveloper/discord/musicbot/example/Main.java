package de.goldendeveloper.discord.musicbot.example;

import de.goldendeveloper.dcbcore.DCBotBuilder;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.discord.musicbot.example.commands.*;
import de.goldendeveloper.discord.musicbot.example.helper.MusicHelper;

import java.util.LinkedList;

public class Main {

    private static MusicHelper musicHelper;

    public static void main(String[] args) {
        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, false);
        dcBotBuilder.registerCommands(registerCommands());
        dcBotBuilder.build();
        musicHelper = new MusicHelper();
    }

    public static LinkedList<CommandInterface> registerCommands() {
        LinkedList<CommandInterface> commands = new LinkedList<>();
        commands.add(new List());
        commands.add(new Pause());
        commands.add(new Play());
        commands.add(new Resume());
        commands.add(new Skip());
        commands.add(new Stop());
        return commands;
    }

    public static MusicHelper getMusicHelper() {
        return musicHelper;
    }
}
