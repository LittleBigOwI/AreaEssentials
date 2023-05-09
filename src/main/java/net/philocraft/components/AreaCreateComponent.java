package net.philocraft.components;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class AreaCreateComponent {
    
    private Player player;
    private String message;

    public AreaCreateComponent(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    private BaseComponent[] build() {
        TextComponent message = new TextComponent(this.message);
        message.setColor(Colors.SUCCESS.getChatColor());

        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Create area!")));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/area create"));

        ComponentBuilder finalMessage = new ComponentBuilder();
        finalMessage.append(message);

        return finalMessage.create();
    }

    public void send() {
        this.player.spigot().sendMessage(this.build());
    }

}
