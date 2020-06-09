package com.github.tommyt0mmy.simcard;

import com.github.tommyt0mmy.simcard.cardmanager.CardData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlaceholderapiExpansion extends PlaceholderExpansion
{
    private SimCard simCardClass;

    public PlaceholderapiExpansion(SimCard simCardClass)
    {
        this.simCardClass = simCardClass;
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public String getIdentifier()
    {
        return "simcard";
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public String getPlugin()
    {
        return null;
    }

    @Override
    public String getAuthor()
    {
        return simCardClass.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion()
    {
        return simCardClass.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if(player == null)
            return "";

        // %simcard_remaining_messages%
        if(identifier.equals("remaining_messages"))
        {
            Optional<Integer> opt_id = simCardClass.database.getOwnedSimId(player.getUniqueId());
            if (opt_id.isPresent())
            {
                Optional<CardData> opt_cd = simCardClass.database.getCardData(opt_id.get());
                if (opt_cd.isPresent())
                {
                    CardData cd = opt_cd.get();
                    return String.valueOf(cd.getRemaining_messages());
                }
            }
            return "0";
        }

        // %simcard_sim_type%
        if(identifier.equals("sim_type"))
        {
            Optional<Integer> opt_id = simCardClass.database.getOwnedSimId(player.getUniqueId());
            if (opt_id.isPresent())
            {
                Optional<CardData> opt_cd = simCardClass.database.getCardData(opt_id.get());
                if (opt_cd.isPresent())
                {
                    CardData cd = opt_cd.get();
                    return cd.getType();
                }
            }
            return "";
        }

        return null;
    }
}
