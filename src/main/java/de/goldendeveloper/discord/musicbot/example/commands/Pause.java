package de.goldendeveloper.discord.musicbot.example.commands;

import de.goldendeveloper.dcbcore.DCBot;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.discord.musicbot.example.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Pause implements CommandInterface {

    public static final String cmdPause = "pause";

    @Override
    public CommandData commandData() {
        return Commands.slash(cmdPause, "Pausiert den laufenden Song!");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Main.getMusicHelper().pauseTrack(e);
    }
}
