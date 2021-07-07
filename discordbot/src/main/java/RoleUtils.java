import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class RoleUtils extends ListenerAdapter {

    //Checks for Added Roles
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        //Test Update Channel, can be removed or ID can be changed
        if (DiscordMain.canCon) {
            DiscordMain.canCon = false;
            DiscordMain.refreashConnection();
        }
        TextChannel t = e.getJDA().getTextChannelById("842942420009746482");

        try {

                //Check to see if the bot is in an accepted discord
                Connection c = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/pbeV735jNH?autoReconnect=true", "pbeV735jNH", "");
                PreparedStatement check = c.prepareStatement("SELECT RoleID,OrgC FROM MasterList WHERE DiscordID=?");
                check.setString(1, e.getGuild().getId());
                ResultSet rs = check.executeQuery();
                if(rs.next()) {
                    t.sendMessage("Valid Guild Found > Attempting Role Update\n").queue();
                    if (e.getRoles().get(0).getId().equals(rs.getString(1))) {
                        t.sendMessage("Role located in database. Attempting to push role to user\n").queue();
                        Thread.sleep(1000);


                        e.getJDA().getGuildById("689597555872628874").addRoleToMember(e.getJDA().getGuildById("689597555872628874").getMember(e.getUser()), e.getJDA().getRoleById(rs.getString(2))).queue();
                        t.sendMessage("Role successfully added to user!\n").queue();

                    }
                }
                else {
                    c.close();
                    return;
                }
            c.close();

        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }


    }
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {

        if (DiscordMain.canCon) {
            DiscordMain.canCon = false;
            DiscordMain.refreashConnection();
        }
        TextChannel t = e.getJDA().getTextChannelById("842942420009746482");
        try {
            Connection c = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/pbeV735jNH?autoReconnect=true", "pbeV735jNH", "");

            PreparedStatement check = c.prepareStatement("SELECT RoleID,OrgC FROM MasterList WHERE DiscordID=?");
            System.out.println(e.getRoles().get(0).getId());
            System.out.println(e.getRoles().get(0).getName());

            check.setString(1, e.getGuild().getId());
            ResultSet rs = check.executeQuery();
            if(rs.next()) {
                t.sendMessage("Role Removal Detected in Valid Discord, checking for valid roles\n").queue();

                if (e.getRoles().get(0).getId().equals(rs.getString(1))) {
                    t.sendMessage("Role located in database. Attempting to remove role from user\n").queue();

                    e.getJDA().getGuildById("689597555872628874").removeRoleFromMember(e.getJDA().getGuildById("689597555872628874").getMember(e.getUser()), e.getJDA().getRoleById(rs.getString(2))).queue();
                    t.sendMessage("Role successfully removed from user!\n").queue();

                }
            }
            else {
                c.close();

                return;
            }
            c.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (DiscordMain.canCon) {
            DiscordMain.canCon = false;
            DiscordMain.refreashConnection();
        }
        TextChannel t = e.getJDA().getTextChannelById("842942420009746482");

        if (e.getGuild().getId().equalsIgnoreCase("689597555872628874")) {
            t.sendMessage("Authorized Guild Join Detected").queue();


            try {
                PreparedStatement c = DiscordMain.connection().prepareStatement("SELECT DiscordID,RoleID,OrgC FROM MasterList");
                ResultSet r = c.executeQuery();
                    while (r.next())  {
                        System.out.print("got here 1");
                        Guild g = e.getJDA().getGuildById(r.getString("DiscordID"));
                        if (g.getMember(e.getUser())!=null) {
                            t.sendMessage("Newly Joined User is in Affilated Discord").queue();

                            if (g.getMemberById(e.getMember().getId()).getRoles().contains(e.getJDA().getRoleById(r.getString("RoleID")))) {
                                t.sendMessage("Newly Joined User has Active Season Role, Updating OrgC Roles.").queue();

                                e.getJDA().getGuildById("689597555872628874").addRoleToMember(e.getMember(), e.getJDA().getRoleById(r.getString("OrgC"))).queue();
                                t.sendMessage("Role Updated!").queue();

                            }
                            else {
                                t.sendMessage("User does not have acceptable role").queue();

                            }
                        }
                        else {
                            //r.getString("DiscordID"
                            t.sendMessage("User not a Member").queue();
                            t.sendMessage(r.getString("DiscordID")).queue();


                        }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }

    }
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        EmbedBuilder eb = new EmbedBuilder();

        ArrayList<String> upd = new ArrayList<>();
        DiscordMain.refreashConnection();
        TextChannel t = e.getChannel();

        PreparedStatement r = null;

        PreparedStatement c = null;

        if ((Objects.requireNonNull(e.getMember()).getId().equals("183951503726346240")) || (Objects.requireNonNull(e.getMember()).getId().equals("3975002467559014iuh51"))) {


            String[] mes = e.getMessage().getContentRaw().split(" ");

            if (mes[0].equalsIgnoreCase("-randomizecolors")) {
                
                t.sendMessage("Lmfao eat my nuts spoon boy").queue();
                eb.setColor(Color.ORANGE);
                eb.setTitle("Randomizing Color Palette");
                eb.setImage("https://media.discordapp.net/attachments/689605682411995146/861794231600939068/pngbase64iVBORw0KGgoAAAANSUhEUgAAAOQAAAChCAMAAADgHONgAAAAD1BMVEVjbBpe3MXFeTfAp2pqamNGm1dAAAAsUlEQVR4.png");
                eb.addField("Changing Colors", "11/25", false);
                t.sendMessage(eb.build()).queue();
                eb.clear();
            }

            if (mes[0].equalsIgnoreCase("-status")) {
                for (Guild g : e.getJDA().getGuilds()) {
                    eb.addField(g.getName(), g.getOwner().getEffectiveName(), true);
                }
                eb.setColor(Color.ORANGE);
                eb.setTitle("Currently Active Discords");
                t.sendMessage(eb.build()).queue();
                eb.clear();
            }


            if (mes[0].equalsIgnoreCase("-remove")) {
                String s = "";
                try {
                    c = DiscordMain.connection().prepareStatement("SELECT DiscordID,OrgC FROM MasterList WHERE DiscordID=?");
                    c.setString(1, mes[1]);
                    ResultSet cr = c.executeQuery();

                    if (cr.next()) {
                        for (Member m : e.getJDA().getGuildById("689597555872628874").getMembers()) {
                            if (m.getRoles().contains(e.getJDA().getRoleById(cr.getString(2)))) {
                                e.getJDA().getGuildById("689597555872628874").removeRoleFromMember(m, e.getJDA().getRoleById(cr.getString(2))).queue();
                                s = s + m.getEffectiveName() + ", ";
                            }
                        }


                                PreparedStatement k = DiscordMain.connection().prepareStatement("DELETE FROM MasterList WHERE DiscordID=?");
                        eb.setTitle("REMOVED " + e.getJDA().getGuildById(mes[1]).getName());
                        eb.setImage(e.getJDA().getGuildById(mes[1]).getIconUrl());
                        eb.setColor(Color.RED);
                        eb.addField("Players with Removed Roles:", s, false);


                        t.sendMessage(eb.build()).queue();
                        eb.clear();
                        k.setString(1, mes[1]);
                        k.executeUpdate();
                    }



                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return;
                }
                }

            if (mes[0].equalsIgnoreCase("-update") && mes.length == 1) {
                try {
                    c = DiscordMain.connection().prepareStatement("SELECT DiscordID,RoleID,OrgC FROM MasterList");
                    ResultSet cp = c.executeQuery();

                    while (cp.next()) {
                        String dis = cp.getString(1);
                        if (e.getJDA().getGuildById(dis) == null) continue;

                        for (Member m : e.getJDA().getGuildById(dis).getMembers()) {
                            if (e.getJDA().getRoleById(cp.getString(2)) == null) {
                                t.sendMessage("Role in "+ e.getJDA().getGuildById(dis).getName() + " was null! skipping update!");
                                continue;
                            }

                            if (m.getRoles().contains(e.getJDA().getRoleById(cp.getString(2)))) {
                                System.out.println("Hey look  " + m.getEffectiveName() + " Is playing soup lmao");

                                if (e.getJDA().getGuildById("689597555872628874").getMembers().contains(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()))) {
                                    e.getJDA().getGuildById("689597555872628874").addRoleToMember(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()), e.getJDA().getRoleById(cp.getString(3))).queue();
upd.add(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()).getEffectiveName())    ;                            }
                            }
                        }
                        String sc = "";
                        for (String s : upd) {
                            sc = sc + s + ", ";
                        }
                        eb.setTitle(e.getJDA().getGuildById(dis).getName());
                        eb.setImage(e.getJDA().getGuildById(dis).getIconUrl());
                        eb.addField("Detected Users:", sc, false);
                        eb.setColor(Color.orange);


                        t.sendMessage(eb.build()).queue();
                        eb.clear();
                        upd.clear();

                    }
                    return;

                } catch (Exception ep) {
                    ep.printStackTrace();

                }

                return;
            }
            if (mes[0].contains("-update")) {
                try {
                    c = DiscordMain.connection().prepareStatement("SELECT DiscordID,RoleID,OrgC FROM MasterList WHERE DiscordID=?");
                    c.setString(1, mes[1]);
                    ResultSet cp = c.executeQuery();

                    while (cp.next()) {
                        String dis = cp.getString(1);
                        for (Member m : e.getJDA().getGuildById(dis).getMembers()) {
                            if (m.getRoles().contains(e.getJDA().getRoleById(cp.getString(2)))) {

                                if (e.getJDA().getGuildById("689597555872628874").getMembers().contains(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()))) {
                                    e.getJDA().getGuildById("689597555872628874").addRoleToMember(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()), e.getJDA().getRoleById(cp.getString(3))).queue();
                                    upd.add(m.getEffectiveName());
                                }
                            }
                        }
                        String sc = "";
                        for (String s : upd) {
                            sc = sc + s + ", ";
                        }
                        eb.setTitle(e.getJDA().getGuildById(mes[1]).getName() + " Successfully Updated!");
                        eb.setImage(e.getJDA().getGuildById(mes[1]).getIconUrl());
                        eb.addField("Detected Users:", sc, false);
                        eb.setColor(Color.ORANGE);


                        t.sendMessage(eb.build()).queue();
                        eb.clear();
                        upd.clear();
                    }
                    return;

                } catch (Exception ep) {
                    ep.printStackTrace();

                }

                return;
            }

            if (!mes[0].contains("-connect")) return;
            if (mes[0].equalsIgnoreCase("-connect help") || mes.length != 4) {
                t.sendMessage("Proper Command Format:").queue();
                t.sendMessage("-connect (Affiliated Discord ID) (ID of Player Role in Affiliated Discord) (ID of Respective Player Role in ORGC)").queue();
                t.sendMessage("1").queue();
                return;

            }
            try {
                e.getJDA().getGuildById(mes[1]);
                e.getJDA().getRoleById(mes[2]);
                e.getJDA().getGuildById("689597555872628874").getRoleById(mes[3]);

            } catch (Exception e1) {
                t.sendMessage("Invalid arugments!").queue();
                t.sendMessage("Proper Command Format:").queue();
                t.sendMessage("-connect (Affiliated Discord ID) (ID of Player Role in Affiliated Discord) (ID of Respective Player Role in ORGC)").queue();
                t.sendMessage("3").queue();

                e1.printStackTrace();

                return;
            }
            try {
                t.sendMessage("Authorized Command Recieved, Starting Database Update").queue();
                c = DiscordMain.connection().prepareStatement("SELECT DiscordID FROM MasterList WHERE DiscordID=?");
                c.setString(1, mes[1]);
                ResultSet cr = c.executeQuery();

                if (cr.next()) {
                    //t.sendMessage("Existing values detected. Removing them now.").queue();

                    // PreparedStatement k = DiscordMain.connection().prepareStatement("DELETE FROM MasterList WHERE DiscordID=?");
                    // k.setString(1, mes[1]);
                    // k.executeUpdate();
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return;
            }

            try {


                r = DiscordMain.connection().prepareStatement("INSERT INTO MasterList (DiscordID,RoleID,OrgC) VALUES (?,?,?)");
                r.setString(1, mes[1]);
                r.setString(2, mes[2]);
                r.setString(3, mes[3]);
                r.executeUpdate();

                    c = DiscordMain.connection().prepareStatement("SELECT DiscordID,RoleID,OrgC FROM MasterList WHERE DiscordID=?");
                    c.setString(1, mes[1]);
                    ResultSet cp = c.executeQuery();

                    while (cp.next()) {
                        String dis = cp.getString(1);
                        t.sendMessage(e.getJDA().getGuildById(dis).getName()).queue();

                        for (Member m : e.getJDA().getGuildById(dis).getMembers()) {
                            System.out.println("Checking " + m.getEffectiveName());

                            if (m.getRoles().contains(e.getJDA().getRoleById(cp.getString(2)))) {

                                if (e.getJDA().getGuildById("689597555872628874").getMembers().contains(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()))) {
                                    e.getJDA().getGuildById("689597555872628874").addRoleToMember(e.getJDA().getGuildById("689597555872628874").getMember(m.getUser()), e.getJDA().getRoleById(cp.getString(3))).queue();
                                    upd.add(m.getEffectiveName());
                                }
                            }
                        }
                        String sc = "";
                        for (String s : upd) {
                            sc = sc + s + ", ";
                        }
                        eb.setTitle(e.getJDA().getGuildById(mes[1]).getName());
                        eb.setImage(e.getJDA().getGuildById(mes[1]).getIconUrl());
                        eb.addField("Detected Users:", sc, false);
                        eb.setColor(Color.GREEN);


                        t.sendMessage(eb.build()).queue();
                        eb.clear();
                        upd.clear();
                    }



            } catch (SQLException throwables) {
                t.sendMessage("Proper Command Format:").queue();
                t.sendMessage("-connect (Affiliated Discord ID) (ID of Player Role in Affiliated Discord) (ID of Respective Player Role in ORGC)").queue();
                t.sendMessage("4").queue();

                throwables.printStackTrace();
                return;

            }
        }

    }
    }

