package com.charizardbot.four.commands;

import com.charizardbot.four.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.requests.Route;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.nio.channels.Channel;
import java.util.List;

import static com.charizardbot.four.Main.*;

public class Clears extends ListenerAdapter {
    private Object User;

    //TODO: Write Clear commands for MM, AS, ES, and Mods+, Reading from a file where the roles can be stored
    // Include required channels for each of the roles except Mods+,
    // only Admins can add to these lists?
    // useable by checking the role. Roles will determine if can be used
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        try {
            String prefix = Main.config.getProperty(event.getGuild().getId().toString());
            if (prefix == null)
                prefix = "!";
            String args[] = event.getMessage().getContentRaw().split(" ");
            if (args[0].equalsIgnoreCase(prefix + "addMod") && (event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
                String[] roles = new String[10];

                for (int i = 1; args[i] != null; i++) {
                      String role = args[i];
//                    roles[i-1] = role;
                    CLEAR_MODS += args[i] + ",";
                    event.getChannel().sendMessage(args[i]).queue();
                }
            }
            if (args[0].equalsIgnoreCase(prefix + "auctionAddChannel") && (event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
                String[] roles = new String[10];

                for (int i = 1; args[i] != null; i++) {
                    String role = args[i];
//                    roles[i-1] = role;
                    AUCTION_CHANNELS += args[i] + ",";
                    event.getChannel().sendMessage(args[i]).queue();
                }
            }



            if (args[0].equalsIgnoreCase(prefix + "clear") && (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
                TextChannel channel = event.getChannel();
                MessageHistory history = new MessageHistory(channel);
                List<Message> msg;
                int num = Integer.parseInt(args[1]);
                msg = history.retrievePast(num).complete();
                channel.deleteMessages(msg).queue();
                event.getChannel().sendMessage("Cleared " + num + " messages").queue();
            }
            if (args[0].equalsIgnoreCase(prefix + "asclear")) {
                String userID = event.getAuthor().getId();
                Role[] allowedRoles = new Role[10];
                String[] allowedChannels = new String [200];
                String[] ch = AUCTION_CHANNELS.split(",");
                String[] roles = CLEAR_MODS.split(",");
                String ch2 = "";
                for(int h = 2; h < 20; h++)
                {
                    ch2 += ch[0].charAt(h);
                }
                TextChannel ch1 = event.getGuild().getTextChannelById(ch2);
                TextChannel channel = event.getChannel();
                Member member = event.getGuild().getMemberById(userID);
                List<Role> memberRoles = event.getMember().getRoles();
                String temp = "";
                Role temp2;
                for (int k = 0; k < roles.length; k++) {
                    int j = 3;
                    while (j < 21) {
                        {
                            temp += roles[k].charAt(j);
                            j++;
                        }
                        temp2 = event.getGuild().getRoleById(temp);
                        allowedRoles[k] = temp2;
                    }
                }
                    for (int i = 0; i < roles.length; i++) {
                        for(int q = 0; q < 1; q++)
                        {
                            if ((memberRoles.contains(allowedRoles[i]) || event.getMember().hasPermission(Permission.ADMINISTRATOR)) && ch1.equals(channel)) {
                                MessageHistory history = new MessageHistory(channel);
                                List<Message> msg;
                                int num = Integer.parseInt(args[1]);
                                msg = history.retrievePast(num).complete();
                                channel.deleteMessages(msg).queue();
                                event.getChannel().sendMessage("Cleared " + num + " messages").queue();
                                break;
                            }
                        }
                    }
                }
        }

        catch(Exception e){
                    Main.logger.info("Exception in Clear: Invalid clear?");
                }
            }

    }
    