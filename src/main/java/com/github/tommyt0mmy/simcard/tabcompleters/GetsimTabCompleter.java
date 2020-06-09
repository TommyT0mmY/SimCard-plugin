package com.github.tommyt0mmy.simcard.tabcompleters;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardType;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetsimTabCompleter implements TabCompleter
{
    static SimCard simCardClass = SimCard.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String paramString, String[] args)
    {
        List <String> suggestions = new ArrayList<>();

        if ((!(sender instanceof Player)) || (!(sender.hasPermission( Permissions.GETSIM.getNode() ))))
        {
            return suggestions;
        }

        List<CardType> cardtypes = simCardClass.configs.getCardTypes();

        if (args.length == 1)
        {
            for(CardType cardType : cardtypes)
            {
                if (cardType.getType().toLowerCase().startsWith(args[0].toLowerCase()))
                {
                    suggestions.add(cardType.getType());
                }
            }
        }

        return suggestions;
    }
}
