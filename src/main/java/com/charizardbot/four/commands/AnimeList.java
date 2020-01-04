package com.charizardbot.four.commands;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.charizardbot.four.Main;
import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.Studios;
import com.github.doomsdayrs.jikan4java.types.support.basic.meta.Genre;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AnimeList extends ListenerAdapter {
    /**
     * CharizardBot's Anime Search command. Searches MyAnimeList based on the query
     * from the user and returns some information about the anime.
     */
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        try {
            String miscToggle = "1";
            if (Main.config.getProperty("miscCmds" + event.getGuild().getId()) != null) {
            miscToggle = Main.config.getProperty("miscCmds" + event.getGuild().getId());
            }
            String prefix = Main.config.getProperty(event.getGuild().getId());
            
            if (prefix == null)
                prefix = "!";
            if (event.getMessage().getContentRaw().toLowerCase().startsWith(prefix + "anime") && miscToggle.equals("1")) {
                String query = event.getMessage().getContentRaw().substring(7, event.getMessage().getContentRaw().length());
                AnimeSearch search = new AnimeSearch();
                search.setQuery(query).setLimit(2);
                CompletableFuture<Anime> completableFuture = search.getFirst();
                int a = 0;
                while (!completableFuture.isDone()) {
                    a++;
                }
                Random rand = new Random();
                EmbedBuilder embed = new EmbedBuilder();
                String desc = completableFuture.get().synopsis;
                if (desc.length() > 500) {
                    desc = desc.substring(0, 500);
                    desc += "...";
                }
                String genre = "";
                for (Genre genres : completableFuture.get().genres) {
                    genre += genres.name + ", ";
                }
                genre = genre.substring(0, genre.length() - 2);
                String studiolist = "";
                for (Studios studio : completableFuture.get().studios) {
                    studiolist += studio.name + ", ";
                }
                studiolist = studiolist.substring(0, studiolist.length() - 2);
                embed.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                embed.setTitle("Results for your Anime search");
                embed.setThumbnail(completableFuture.get().imageURL);
                embed.addField("Title:", completableFuture.get().title_english, false);
                embed.addField("Description:", desc, false);
                embed.addField("Number of episodes:", "" + completableFuture.get().episodes, true);
                try {embed.addField("Premiered:", completableFuture.get().premiered, true);}catch (Exception e){embed.addField("Year:", "Unknown", true);}
                embed.addField("Genres:", genre, false);
                embed.addField("Studios", studiolist, false);
                embed.addField("Source:", completableFuture.get().url, false);
                embed.setFooter("CharizardBot Team", "https://cdn.discordapp.com/attachments/382377954908569600/463038441547104256/angery_cherizord.png");
                event.getChannel().sendMessage(embed.build()).queue();
        }
    } catch (Exception e) {
        Main.logger.info("WARN: Exception in AnimeList command." + e);
    }
    }
    
}