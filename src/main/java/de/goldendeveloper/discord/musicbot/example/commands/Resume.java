package de.goldendeveloper.discord.musicbot.example.commands;

import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.discord.musicbot.example.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Resume implements CommandInterface {

    public static final String cmdResume = "resume";

    @Override
    public CommandData commandData() {
        return Commands.slash(cmdResume, "Spielt den letzten laufenden Song!");
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        Main.getMusicHelper().resumeTrack(e);
    }
}
