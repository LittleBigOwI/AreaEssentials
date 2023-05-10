package net.philocraft.commands.subcommands;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.DatabaseUtil;

public class ShowSubcommand extends Subcommand {

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Displays an areas boundaries";
    }

    @Override
    public String getSyntax() {
        return "/area show";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        if(args.length > 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        int i = 0;
        while(i < DatabaseUtil.getAreas().size() && !DatabaseUtil.getAreas().get(i).contains(player)) {
            i++;
        }

        if(i == DatabaseUtil.getAreas().size()) {
            return new NoAreaException().sendCause(player);
        }

        Area area = DatabaseUtil.getAreas().get(i);

        area.show(player);
        return true;
    }

}
