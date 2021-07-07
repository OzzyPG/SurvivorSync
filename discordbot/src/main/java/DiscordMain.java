import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordMain {
    public static Connection con;
    public static boolean canCon = true;

    public static void main(String[] args) throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/pbeV735jNH?autoReconnect=true", "pbeV735jNH", "");


        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
       commandClientBuilder.setPrefix("$!");
       commandClientBuilder.useDefaultGame();
       commandClientBuilder.setOwnerId("183951503726346240");
        JDA jda = JDABuilder.createDefault("").enableIntents(GatewayIntent.GUILD_MEMBERS).setChunkingFilter(ChunkingFilter.ALL).setMemberCachePolicy(MemberCachePolicy.ALL).build();
            try {
                Thread.sleep(5000);
            } catch (Exception ex) {
            }
        jda.getPresence().setActivity(Activity.playing("Bot made by Ozzy"));
            jda.addEventListener(new RoleUtils());



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            canCon = true;
                DiscordMain.refreashConnection();

                System.out.println("hi");
            }
        }, 0, 1000 * 60 * 10);
        }


        public static Connection connection() throws SQLException {
    return con;
            }
            public static void refreashConnection() {
        try {
            con.close();
            con = null;
            con = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/pbeV735jNH?autoReconnect=true", "pbeV735jNH", "");
        }
        catch (Exception e) {
        e.printStackTrace();
        }

            }

    }

