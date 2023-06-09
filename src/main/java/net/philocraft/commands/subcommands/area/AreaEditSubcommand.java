package net.philocraft.commands.subcommands.area;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.errors.InvalidArgumentsException;
import net.philocraft.commands.subcommands.area.edit.AreaEditColorSubcommand;
import net.philocraft.commands.subcommands.area.edit.AreaEditEnterMessageSubcommand;
import net.philocraft.commands.subcommands.area.edit.AreaEditLeaveMessageSubcommand;
import net.philocraft.commands.subcommands.area.edit.AreaEditNameSubcommand;
import net.philocraft.commands.subcommands.area.edit.AreaEditPermissionsSubcommand;
import net.philocraft.errors.BadAreaException;
import net.philocraft.models.Area;
import net.philocraft.models.Subcommand;
import net.philocraft.utils.AreaUtil;

public class AreaEditSubcommand extends Subcommand {

    private ArrayList<Subcommand> subcommands = new ArrayList<>();
    private static Area area;

    public AreaEditSubcommand() {
        this.subcommands.add(new AreaEditNameSubcommand());
        this.subcommands.add(new AreaEditColorSubcommand());

        this.subcommands.add(new AreaEditEnterMessageSubcommand());
        this.subcommands.add(new AreaEditLeaveMessageSubcommand());
        this.subcommands.add(new AreaEditPermissionsSubcommand());
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Allows areas to be edited";
    }

    @Override
    public String getSyntax() {
        return "/area edit";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        area = AreaUtil.getArea(player.getLocation());

        if(area == null) {
            return new BadAreaException().sendCause(player);
        }

        if(!area.getUUID().equals(player.getUniqueId())) {
            return new BadAreaException("You are not the owner of this area.").sendCause(player);
        }

        boolean performedCommand = false;

        if(args.length > 1) {
            for(int i = 0; i < this.subcommands.size(); i++) {
                if(args[1].equalsIgnoreCase(this.subcommands.get(i).getName())) {
                    this.subcommands.get(i).perform(player, args);
                    performedCommand = true;
                }
            }
        }

        if(!performedCommand) {
            return new InvalidArgumentsException().sendCause(player);
        }

        return true;
    }

    public static Area getArea() {
        return AreaEditSubcommand.area;
    }
    
}
