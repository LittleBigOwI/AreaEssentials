package net.philocraft.commands.subcommands.area;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.errors.NoAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaShowSubcommand extends Subcommand {

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
        if(args.length != 1) {
            return new InvalidArgumentsException().sendCause(player);
        }

        int i = 0;
        while(i < AreaUtil.getAreas().size() && !AreaUtil.getAreas().get(i).contains(player)) {
            i++;
        }

        if(i == AreaUtil.getAreas().size()) {
            return new NoAreaException().sendCause(player);
        }

        Area area = AreaUtil.getAreas().get(i);

        area.show(player);
        return true;
    }

}
