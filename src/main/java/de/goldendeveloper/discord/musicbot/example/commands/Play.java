package de.goldendeveloper.discord.musicbot.example.commands;

import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.discord.musicbot.example.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Objects;

public class Play implements CommandInterface {

    public static final String cmdPlay = "play";
    public static final String cmdPlayOptionTrackUrl = "track-url";

    @Override
    public CommandData commandData() {
        return Commands.slash(cmdPlay, "Spielt einen Song ab!").addOption(OptionType.STRING, cmdPlayOptionTrackUrl, "YouTube Tack Url", true);
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        String TrackUrl = Objects.requireNonNull(e.getOption(cmdPlayOptionTrackUrl)).getAsString();
        Main.getMusicHelper().loadAndPlay(e.getChannel().asTextChannel(), TrackUrl);
    }
}
