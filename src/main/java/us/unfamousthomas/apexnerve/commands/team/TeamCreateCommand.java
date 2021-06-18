package us.unfamousthomas.apexnerve.commands.team;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.commands.Command;
import us.unfamousthomas.apexnerve.api.commands.CustomPermission;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamManager;
import us.unfamousthomas.apexnerve.api.objects.teams.TeamObject;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class TeamCreateCommand extends Command {
    public TeamCreateCommand() {
        super("create");
        aliases = alias("c");
        permission = CustomPermission.MEMBER;
        usage = "!team create [COLOR] [NAME]";
        minArgs = 2;
        description = "Used to create a team";
    }
    TeamManager manager = TeamManager.getInstance();

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        boolean isInTeam = false;

        TeamObject teamObject = TeamManager.getInstance().getTeamMemberOf(m);
        if(teamObject != null) {
            isInTeam = true;
        }

        create(isInTeam, args.get(0), getName(args), event.getMember(), event.getGuild(), event.getTextChannel());


    }

    private void create(boolean inTeam, String color, String name, Member m, Guild guild, TextChannel channel) {
        if (!inTeam) {
            Color realColor;
            if (color.startsWith("#")) {
                realColor = hexToColor(color);
            } else {
                realColor = getColor(color);
            }
            TeamManager.getInstance().createTeam(m, realColor, name, guild);

            channel.sendMessage(Text.TEAM_CREATED.getReplaced("%name", name)).queue();
        } else {
            channel.sendMessage(Text.ALREADY_IN_TEAM.getMessage()).queue();
        }
    }

    private Color getColor(String possibleColor) {
        Color color;
        try {
            Field field = Color.class.getField(possibleColor);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null;
        }
        return color;
    }

    private Color hexToColor(String value) {
        String digits;
        if (value.startsWith("#")) {
            digits = value.substring(1, Math.min(value.length(), 7));
        } else {
            digits = value;
        }
        String hstr = "0x" + digits;
        Color c;
        try {
            c = Color.decode(hstr);
        } catch (NumberFormatException nfe) {
            c = null;
        }
        return c;
    }

    private String getName(List<String> argumentsOfCommand) {
        argumentsOfCommand.remove(0);
        return String.join(" ", argumentsOfCommand);
    }

}
