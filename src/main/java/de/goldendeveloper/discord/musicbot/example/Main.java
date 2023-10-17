package de.goldendeveloper.discord.musicbot.example;

import de.goldendeveloper.dcbcore.DCBotBuilder;
import de.goldendeveloper.discord.musicbot.example.commands.*;
import de.goldendeveloper.discord.musicbot.example.helper.MusicHelper;

public class Main {

    private static MusicHelper musicHelper;

    public static void main(String[] args) {
        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, false);
        dcBotBuilder.registerCommands(
                new List(),
                new Pause(),
                new Play(),
                new Resume(),
                new Skip(),
                new Stop()
        );
        dcBotBuilder.build();
        musicHelper = new MusicHelper();
    }

    public static MusicHelper getMusicHelper() {
        return musicHelper;
    }
}
