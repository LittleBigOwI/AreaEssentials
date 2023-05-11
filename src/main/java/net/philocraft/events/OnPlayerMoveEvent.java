package net.philocraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.philocraft.models.Area;
import net.philocraft.utils.AreaUtil;

public class OnPlayerMoveEvent implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        
        Player player = event.getPlayer();

        Area fromArea = AreaUtil.getArea(event.getFrom());
        Area toArea = AreaUtil.getArea(event.getTo());

        if(fromArea == null && toArea != null) {
            if(toArea.getEnterMessage() != null){ player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', "&b" + toArea.getEnterMessage()))); }
            
        } else if(toArea == null && fromArea != null){
            if(fromArea.getLeaveMessage() != null){ player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', "&b" + fromArea.getLeaveMessage()))); }

        }

        
    }

}
