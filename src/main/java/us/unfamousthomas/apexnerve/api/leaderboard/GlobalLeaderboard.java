package us.unfamousthomas.apexnerve.api.leaderboard;

import org.apache.commons.math3.util.Precision;
import us.unfamousthomas.apexnerve.api.Text;
import us.unfamousthomas.apexnerve.api.objects.user.User;
import us.unfamousthomas.apexnerve.api.objects.user.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalLeaderboard {

    List<User> users = new ArrayList<>();

    public GlobalLeaderboard() {
        users = new ArrayList<>(UserManager.getInstance().getUsers().values());
        users.sort(Collections.reverseOrder());
    }

    public String getLeaderBoards(int amount) {
        StringBuilder builder = new StringBuilder();

        users.sort(Collections.reverseOrder());
        int i = 1;

        if (amount > 100) {
            builder.append(Text.LEADERBOARD_TOO_BIG.getMessage());
            return builder.toString();
        }
        if (users.size() < amount) {
            amount = users.size();
        }

        builder.append(Text.GLOBAL_PREFIX.getMessage());
        while (amount >= i) {
            if(users.get(i - 1).getTotalExperience() > 0) {
                builder.append(i);
                builder.append(".");
                builder.append(users.get(i - 1).getName());
                builder.append(" - ");
                builder.append(users.get(i - 1).getLevel());
                builder.append(" (");
                builder.append(Precision.round(users.get(i - 1).getTotalExperience(), 2));
                builder.append(")");
                builder.append("\n");
            }
            i = i + 1;
        }

        return builder.toString();
    }
}
