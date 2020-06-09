package com.github.tommyt0mmy.simcard.commands;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemovesimCommand implements CommandExecutor
{
    static SimCard simCardClass = SimCard.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player)) //check if player
        {
            sender .sendMessage(simCardClass.messages.formattedChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(Permissions.REMOVESIM.getNode())) //check permissions
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_permissions"));
            return true;
        }

        if (args.length != 1)
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("removesim_usage"));
            return true;
        }

        boolean removed = simCardClass.configs.removeCardType(args[0]);

        if (!removed)
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("removesim_invalid"));
            return true;
        }

        p.sendMessage(simCardClass.messages.formattedChatMessage("removesim_success"));
        return true;
    }
}
