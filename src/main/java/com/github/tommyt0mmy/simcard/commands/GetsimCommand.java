package com.github.tommyt0mmy.simcard.commands;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardType;
import com.github.tommyt0mmy.simcard.cardmanager.CardData;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class GetsimCommand implements CommandExecutor
{
    static SimCard simCardClass = SimCard.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(simCardClass.messages.formattedChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission(Permissions.GETSIM.getNode()))
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_permissions"));
            return true;
        }

        if (args.length != 1)
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("getsim_usage"));
            return true;
        }

        Optional<CardType> cardtype_optional = simCardClass.configs.getCardType(args[0]);

        if (cardtype_optional.isPresent())
        {
            CardType cardtype = cardtype_optional.get();
            Optional<CardData> carddata_optional = simCardClass.database.addCard(new CardData(-1, cardtype.getType(), cardtype.getRemaining_messages()));
            if (carddata_optional.isPresent())
            {
                CardData carddata = carddata_optional.get();
                cardtype.setId(carddata.getId());
                Optional<ItemStack> itemstack_optional = cardtype.getItemStack();
                if (itemstack_optional.isPresent())
                {
                    p.getInventory().addItem(itemstack_optional.get());
                    p.sendMessage(simCardClass.messages.formattedChatMessage("getsim_received"));
                } else { p.sendMessage(simCardClass.messages.formattedChatMessage("getsim_type_error")); }
            } else { p.sendMessage(simCardClass.messages.formattedChatMessage("getsim_type_error")); }
        } else { p.sendMessage(simCardClass.messages.formattedChatMessage("getsim_type_error")); }
        return true;
    }
}
