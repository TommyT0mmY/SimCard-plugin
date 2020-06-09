package com.github.tommyt0mmy.simcard.cardmanager;

import com.github.tommyt0mmy.simcard.SimCard;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardType
{
    static SimCard simCardClass = SimCard.getInstance();

    private int id;
    private String type;
    private String display_name;
    private ItemStack itemstack;
    private int remaining_messages;

    public CardType(int id, String type, String display_name, ItemStack itemstack, int remaining_messages)
    {
        this.id = id;
        this.type = type;
        this.display_name = display_name;
        this.itemstack = itemstack;
        this.remaining_messages = remaining_messages;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDisplay_name()
    {
        return display_name;
    }

    public void setDisplay_name(String display_name)
    {
        this.display_name = display_name;
    }

    public void setItemstack(ItemStack itemstack)
    {
        this.itemstack = itemstack;
    }

    public Optional<ItemStack> getItemStack()
    {
        if (itemstack != null)
        {
            ItemStack result = itemstack;
            ItemMeta resultMeta = result.getItemMeta();
            resultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
            List<String> lore = new ArrayList<>();
            //LINE 1: "xxxx SIM"
            lore.add(ChatColor.YELLOW + type + " SIM");
            //LINE 2: "#xxxx" (sim id)
            lore.add(ChatColor.YELLOW + "#" + id);
            //LINE 3: "xxxx remaining messages"
            lore.add(ChatColor.GREEN.toString() + remaining_messages + ChatColor.YELLOW.toString() + " " + simCardClass.messages.getKeyword("remaining_messages"));
            resultMeta.setLore(lore);
            result.setItemMeta(resultMeta);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    public int getRemaining_messages()
    {
        return remaining_messages;
    }

    public void setRemaining_messages(int remaining_messages)
    {
        this.remaining_messages = remaining_messages;
    }

}
