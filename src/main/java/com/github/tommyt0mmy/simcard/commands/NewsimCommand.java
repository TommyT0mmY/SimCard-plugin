package com.github.tommyt0mmy.simcard.commands;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardType;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
    STEP 1: Getting held item in main hand, SIM type (name), remaining sim messages
    STEP 2: Getting SIM's display name through chat

    to undo the command / get out of step 2, type again /newsim
 */

public class NewsimCommand implements CommandExecutor, Listener
{
    static SimCard simCardClass = SimCard.getInstance();

    static Map<UUID, CardType> cardTypeMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player)) //check if player
        {
            sender .sendMessage(simCardClass.messages.formattedChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(Permissions.NEWSIM.getNode())) //check permissions
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_permissions"));
            return true;
        }

        if (cardTypeMap.containsKey(p.getUniqueId())) //undo command
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_undo"));
            cardTypeMap.remove(p.getUniqueId());
            return true;
        }

        if (args.length == 2)
        {
            //check if args[1] is a valid number
            if (!args[1].matches("\\d+"))
            {
                p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_invalid_remaining_messages_value"));
                return true;
            }
            ItemStack held_item = p.getInventory().getItemInMainHand().clone();
            //check if held item in main hand
            if (held_item.getType().equals(Material.AIR))
            {
                p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_usage_2"));
                return true;
            }
            held_item.setAmount(1);
            CardType newct = new CardType(-1, args[0], "", held_item, Integer.parseInt(args[1]));
            cardTypeMap.put(p.getUniqueId(), newct);
            p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_usage_3"));
        }
        else
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_usage_1"));
            return true;
        }


        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        simCardClass.console.info(e.getMessage() + " " + e.getPlayer().getName());
        Player p = e.getPlayer();

        if (cardTypeMap.containsKey(p.getUniqueId())) //undo command
        {
            CardType newCardType = cardTypeMap.get(p.getUniqueId());
            cardTypeMap.remove(p.getUniqueId());

            newCardType.setDisplay_name(ChatColor.translateAlternateColorCodes('&', e.getMessage()));

            simCardClass.configs.addCardType(newCardType);

            p.sendMessage(simCardClass.messages.formattedChatMessage("newsim_success"));

            e.setCancelled(true);
        }
    }
}
