package com.github.tommyt0mmy.simcard.cardmanager;

import com.github.tommyt0mmy.simcard.SimCard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class CardManager
{
    static SimCard simCardClass = SimCard.getInstance();

    public static Optional<CardData> getCardData(ItemStack sim)
    {
        simCardClass.configs.loadConfigs();

        if (!sim.hasItemMeta())
            return Optional.empty();

        ItemMeta sim_IM = sim.getItemMeta();

        if (!sim_IM.hasLore())
            return Optional.empty();

        if (sim_IM.getLore().size() != 3)
            return Optional.empty();


        for (CardType curr_CardType : simCardClass.configs.getCardTypes())
        {
            Optional<ItemStack> curr_is_optional = curr_CardType.getItemStack();
            if (curr_is_optional.isPresent())
            {
                ItemStack curr_is = curr_is_optional.get();
                ItemMeta curr_is_IM = curr_is.getItemMeta();
                if (!curr_is_IM.getLore().get(0).equals(sim_IM.getLore().get(0)))
                    continue;
                //check sim number
                String simId_string = sim_IM.getLore().get(1).replaceAll("§e#","");
                int simId = Integer.parseInt(simId_string);
                return simCardClass.database.getCardData(simId);
            }
        }
        return Optional.empty();
    }

    public static boolean consumeMessage(Player p, CardData cardData, boolean feedback)
    {
        int remaining_messages = cardData.getRemaining_messages();
        if (remaining_messages <= 0)
            return false;

        remaining_messages--;
        simCardClass.database.updateRemainingMessages(cardData.getId(), remaining_messages);
        if (remaining_messages <= 0) //expired
        {
            simCardClass.database.removeCard(cardData.getId());
            simCardClass.database.removeSimOwner(p.getUniqueId());
            p.sendMessage(simCardClass.messages.formattedChatMessage("expired_sim"));
        }
        else if (feedback)
        {
            p.sendMessage((simCardClass.messages.formattedChatMessage("consumed_message")).replaceAll("<remaining_messages>", String.valueOf(remaining_messages)));
        }
        return true;
    }
}
