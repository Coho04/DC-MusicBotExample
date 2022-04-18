package de.goldendeveloper.discord.musicbot.example;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.goldendeveloper.discord.musicbot.example.music.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class Events extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public Events() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            String cmd = e.getName();
            if (cmd.equalsIgnoreCase(Discord.cmdPlay)) {
                String TrackUrl = e.getOption(Discord.cmdPlayOptionTrackUrl).getAsString();
                loadAndPlay(e.getTextChannel(), TrackUrl);
            } else if (cmd.equalsIgnoreCase(Discord.cmdSkip)) {
                skipTrack(e);
            } else if (cmd.equalsIgnoreCase(Discord.cmdPause)) {
                pauseTrack(e);
            } else if (cmd.equalsIgnoreCase(Discord.cmdResume)) {
//                resumeTrack(e);
            } else if (cmd.equalsIgnoreCase(Discord.cmdStop)) {
                stopTrack(e);
            }
        }
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());
        musicManager.scheduler.queue(track);
    }

    private void skipTrack(SlashCommandInteractionEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        musicManager.scheduler.nextTrack();
        e.reply("Skipped to next track.").queue();
    }

    private void stopTrack(SlashCommandInteractionEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        if (musicManager.getPlayer().getPlayingTrack() != null) {
            musicManager.getPlayer().stopTrack();
            if (e.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                e.getGuild().getAudioManager().closeAudioConnection();
                e.reply("Ich beende die Vorstellung!").queue();
            }
        } else {
            e.reply("Es wird momentan nichts abgespielt!").queue();
        }
    }

    private void pauseTrack(SlashCommandInteractionEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        if (!musicManager.getPlayer().isPaused()) {
            musicManager.getPlayer().setPaused(true);
        } else {
            e.reply("Es wird momentan nichts abgespielt!").queue();
        }
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }
}
