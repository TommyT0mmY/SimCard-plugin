package com.github.tommyt0mmy.simcard.configuration;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Configs
{
    static SimCard simCardClass = SimCard.getInstance();

    private FileConfiguration configsConfiguration;
    private File configsFile;
    private final String fileName = "config.yml";

    private List<CardType> cardTypes = new ArrayList<>();
    private boolean feedback_consumed_message = true;

    public Configs()
    {
        loadConfigsFile();
    }

    private void loadConfigsFile()
    {
        configsFile = new File(simCardClass.getDataFolder(), fileName);
        if (!configsFile.exists())
        {
            configsFile.getParentFile().mkdirs();
            simCardClass.saveResource(fileName, false);
            simCardClass.console.info("Created config.yml");
        }

        configsConfiguration = new YamlConfiguration();

        try
        {
            configsConfiguration.load(configsFile);
            loadConfigs();
            configsConfiguration.save(configsFile);
        } catch (Exception e) { logError(e); }
    }

    public void loadConfigs()
    {
        try
        { //LOADING
            configsConfiguration.load(configsFile);
        } catch (Exception e) { logError(e); }

        //read types
        cardTypes.clear();
        Set<String> types = configsConfiguration.getConfigurationSection("card-types").getKeys(false);
        for (String curr_type : types)
        {
            ConfigurationSection cSection = configsConfiguration.getConfigurationSection("card-types." + curr_type);
            String display_name = cSection.getString("display_name");
            ItemStack itemstack = cSection.getItemStack("itemstack");
            int remaining_messages = cSection.getInt("remaining_messages");

            cardTypes.add(new CardType(-1, curr_type, display_name, itemstack, remaining_messages));
        }

        //read feedback-consumed-message

        try
        { //SAVING
            configsConfiguration.save(configsFile);
        } catch (IOException e) { logError(e); }
    }

    public Optional<CardType> getCardType(String type)
    {
        loadConfigs();
        for (CardType curr_cardtype : cardTypes)
        {
            if (curr_cardtype.getType().equals(type))
            {
                return Optional.of(curr_cardtype);
            }
        }
        return Optional.empty();
    }

    public boolean removeCardType(String type)
    {
        try
        { //LOADING
            configsConfiguration.load(configsFile);
        } catch (Exception e) { logError(e); }

        ConfigurationSection type_cs = configsConfiguration.getConfigurationSection("card-types." + type);

        if (type_cs == null)
            return false;

        configsConfiguration.set("card-types." + type, null);

        try
        { //SAVING
            configsConfiguration.save(configsFile);
        } catch (IOException e) { logError(e); }
        loadConfigs(); //Reloading
        return true;
    }

    public void addCardType(CardType cardType)
    {
        try
        { //LOADING
            configsConfiguration.load(configsFile);
        } catch (Exception e) { logError(e); }

        String node = "card-types." + cardType.getType();
        try
        {
            configsConfiguration.set(node + ".display_name", cardType.getDisplay_name());
            configsConfiguration.set(node + ".itemstack", cardType.getItemStack().get());
            configsConfiguration.set(node + ".remaining_messages", cardType.getRemaining_messages());
        }catch (Exception e) { logError(e); }

        try
        { //SAVING
            configsConfiguration.save(configsFile);
        } catch (IOException e) { logError(e); }
    }

    public List<CardType> getCardTypes()
    {
        loadConfigs();
        return cardTypes;
    }

    public boolean isFeedback_consumed_message()
    {
        return feedback_consumed_message;
    }

    private void logError(Exception e)
    {
        simCardClass.console.severe("Couldn't load config.yml properly!");
        simCardClass.console.severe(Arrays.toString(e.getStackTrace()));
    }

}
