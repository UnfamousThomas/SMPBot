package us.unfamousthomas.bot.commands.team;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.unfamousthomas.bot.api.commands.Command;
import us.unfamousthomas.bot.api.commands.CustomPermission;
import us.unfamousthomas.bot.api.objects.teams.TeamManager;
import us.unfamousthomas.bot.api.objects.teams.TeamObject;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class TeamRecolorCommand extends Command {
    public TeamRecolorCommand() {
        super("recolor");
        minArgs = 1;
        maxArgs = 1;
        permission = CustomPermission.MEMBER;
    }

    @Override
    public void run(Member m, List<String> args, MessageReceivedEvent event) {
        String color = args.get(0);
        Color realColor;
        if (color.startsWith("#")) {
            realColor = hexToColor(color);
        } else {
            realColor = getColor(color);
        }

        TeamObject team = TeamManager.getInstance().getTeamLeaderOf(m);

        if(team == null) {
            event.getTextChannel().sendMessage("Could not find you as a leader of a team.").queue();
            return;
        }
         TeamManager.getInstance().recolorTeam(realColor, team, m);

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

}
