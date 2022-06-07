package de.goldendeveloper.discord.musicbot.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Discord {

    private final JDA bot;
    public static final String cmdPlay = "play";
    public static final String cmdPlayOptionTrackUrl = "track-url";
    public static final String cmdPause = "pause";
    public static final String cmdStop = "stop";
    public static final String cmdSkip = "skip";
    public static final String cmdResume = "resume";
    public static final String cmdList = "list";

    public Discord(String token) {
        try {
            bot = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_TYPING)
                    .addEventListeners(new Events())
                    .setAutoReconnect(true)
                    .build().awaitReady();
            registerCommands();
        } catch (InterruptedException | LoginException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        this.bot.upsertCommand(cmdPlay, "Spielt einen Song ab!").addOption(OptionType.STRING, cmdPlayOptionTrackUrl, "YouTube Tack Url", true).queue();
        this.bot.upsertCommand(cmdPause, "Pausiert den laufenden Song!").queue();
        this.bot.upsertCommand(cmdStop, "Stoppt den laufenden Song!").queue();
        this.bot.upsertCommand(cmdSkip, "Überspringt den laufenden Song!").queue();
        this.bot.upsertCommand(cmdResume, "Spielt den letzten laufenden Song!").queue();
        this.bot.upsertCommand(cmdList, "Zeigt die Liste der kommenden Songs!").queue();
    }

    public JDA getBot() {
        return bot;
    }
}
