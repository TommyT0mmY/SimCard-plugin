package com.github.tommyt0mmy.simcard.tabcompleters;

import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MsgTabCompleter implements TabCompleter
{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String paramString, String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        if ((!(sender instanceof Player)) || (!(sender.hasPermission( Permissions.MSG.getNode() ))))
        {
            return suggestions;
        }

        if (args.length == 1)
        {
            for (Player curr_player : Bukkit.getOnlinePlayers())
            {
                if (curr_player.getName().toLowerCase().startsWith(args[0].toLowerCase()) && !curr_player.getName().equals(sender.getName()))
                {
                    suggestions.add(curr_player.getName());
                }
            }
        }

        return suggestions;
    }
}
