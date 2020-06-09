package com.github.tommyt0mmy.simcard.events;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardData;
import com.github.tommyt0mmy.simcard.cardmanager.CardManager;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ActivateSim implements Listener
{
    static SimCard simCardClass = SimCard.getInstance();

    @EventHandler
    public void onSimActivated(PlayerInteractEvent e)
    {
        if (e.hasItem())
        {
            Player p = e.getPlayer();
            ItemStack in_hand = e.getItem();
            if(CardManager.getCardData(in_hand).isPresent())
            {
                e.setCancelled(true);
                if (!p.hasPermission(Permissions.ACTIVATESIM.getNode()))
                {
                    p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_permissions"));
                    return;
                }

                Optional<Integer> id_opt = simCardClass.database.getOwnedSimId(p.getUniqueId());
                if (id_opt.isPresent())
                {
                    p.sendMessage(simCardClass.messages.formattedChatMessage("already_activated_sim"));
                    return;
                }

                CardData cardData = CardManager.getCardData(in_hand).get();
                simCardClass.database.addSimOwner(cardData.getId(), p.getUniqueId());
                p.sendMessage(simCardClass.messages.formattedChatMessage("activated_sim"));
                if (e.getItem().equals(p.getInventory().getItemInMainHand()))
                { //sim is in main hand
                    p.getInventory().setItemInMainHand(null);
                }
                else
                { //sim is in off hand
                    p.getInventory().setItemInOffHand(null);
                }
            }
        }
    }
}
