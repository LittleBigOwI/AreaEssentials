package net.philocraft.components;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.constants.Colors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ShopOpenComponent {

    private static ArrayList<String> possibleCommands = new ArrayList<>();
    
    private Player player;
    private String[][] message;

    private String[] commands;

    public ShopOpenComponent(Player player, String[][] message, String[] commands) {
        this.player = player;
        this.message = message;

        this.commands = commands;

        for(String command : commands) {
            if(!possibleCommands.contains(command)) {
                possibleCommands.add(command);
            }
        }
    }

    private BaseComponent[] build() {
        
        TextComponent[][] message = new TextComponent[this.message.length][this.message[0].length];
        
        for(int i = 0; i < this.message.length; i++) {
            for(int j = 0; j < this.message[i].length; j++) {
                message[i][j] = new TextComponent(this.message[i][j]);
                if(j%2 == 0) {
                    message[i][j].setColor(Colors.WARNING.getChatColor());
                } else {
                    message[i][j].setColor(Colors.WARNING_DARK.getChatColor());
                }
            }
        }
        
        ComponentBuilder finalMessage = new ComponentBuilder();
        for(int i = 0; i < this.message.length; i++) {
            for(int j = 0; j < this.message[i].length; j++) {
                if(j == message[i].length-1) {
                    finalMessage.append(message[i][j])
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to buy!")))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.commands[i]));
                
                } else {
                    finalMessage.append(message[i][j])
                        .event((HoverEvent)null)
                        .event((ClickEvent)null);
                }
            }
        }

        return finalMessage.create();
    }

    public static ArrayList<String> getPossibleCommands() {
        return ShopOpenComponent.possibleCommands;
    }

    public void send() {
        this.player.spigot().sendMessage(this.build());
    }
}
