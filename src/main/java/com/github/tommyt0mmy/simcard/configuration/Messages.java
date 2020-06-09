package com.github.tommyt0mmy.simcard.configuration;

import com.github.tommyt0mmy.simcard.SimCard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Messages
{

    public Messages()
    {
        loadMessagesFile();
    }

    private SimCard simCardClass = SimCard.getInstance();

    private FileConfiguration messagesConfig;
    private File messagesConfigFile;
    private final String fileName = "messages.yml";



    private HashMap<String, String> messagesMap = new HashMap<String, String>()
    {
        {
            put("ingame_prefix", "[SimCard]");
            put("received_message_format", "<sender_display_name> to you: <message>");
            put("sent_message_format", "you to <receiver_display_name>: <message>");

            //CHAT MESSAGES
            put("messages.only_players_command.text", "Only players can execute this command");
            put("messages.only_players_command.prefix_color", "");

            put("messages.invalid_permissions.text", "&cInvalid Permissions!");
            put("messages.invalid_permissions.prefix_color", "&c");

            put("messages.getsim_usage.text", "&cCorrect usage: /getsim <sim type>");
            put("messages.getsim_usage.prefix_color", "&c");

            put("messages.msg_usage.text", "&cCorrect usage: /msg <player name> <message>");
            put("messages.msg_usage.prefix_color", "&c");

            put("messages.getsim_type_error.text", "&cSIM type doesn't exist");
            put("messages.getsim_type_error.prefix_color", "&c");

            put("messages.getsim_received.text", "&aSIM received");
            put("messages.getsim_received.prefix_color", "&a");

            put("messages.expired_sim.text", "&cYou finished all the messages of your SIM plan");
            put("messages.expired_sim.prefix_color", "&c");

            put("messages.consumed_message.text", "&aYou got &6<remaining_messages> &aremaining messages");
            put("messages.consumed_message.prefix_color", "&a");

            put("messages.activated_sim.text", "&aYou activated your SIM");
            put("messages.activated_sim.prefix_color", "&a");

            put("messages.already_activated_sim.text", "&cYou already activated a SIM");
            put("messages.already_activated_sim.prefix_color", "&c");

            put("messages.no_activated_sim.text", "&cYou haven't got any activated SIM yet!");
            put("messages.no_activated_sim.prefix_color", "&c");

            put("messages.invalid_player.text", "&cInvalid player name");
            put("messages.invalid_player.prefix_color", "&c");

            put("messages.newsim_usage_1.text", "&cCorrect usage: /newsim <SIM type> <remaining messages>");
            put("messages.newsim_usage_1.prefix_color", "&c");

            put("messages.newsim_usage_2.text", "&cYou need to hold an item in the main hand!");
            put("messages.newsim_usage_2.prefix_color", "&c");

            put("messages.newsim_usage_3.text", "&aNow write the SIM's display name in chat, use '&' for color codes, to undo the command type /newsim again");
            put("messages.newsim_usage_3.prefix_color", "&a");

            put("messages.newsim_undo.text", "&aCommand undone");
            put("messages.newsim_undo.prefix_color", "&a");

            put("messages.newsim_success.text", "&aSIM type successfully added");
            put("messages.newsim_success.prefix_color", "&a");

            put("messages.newsim_invalid_remaining_messages_value.text", "&cInvalid remaining messages value");
            put("messages.newsim_invalid_remaining_messages_value.prefix_color", "&c");


            put("messages.removesim_usage.text", "&cCorrrect usage: /removesim <SIM type>");
            put("messages.removesim_usage.prefix_color", "&c");

            put("messages.removesim_invalid.text", "&cInvalid SIM type");
            put("messages.removesim_invalid.prefix_color", "&c");

            put("messages.removesim_success.text", "&aSIM type removed");
            put("messages.removesim_success.prefix_color", "&a");


            //KEYWORDS
            put("keywords.remaining_messages", "remaining_messages");
            put("keywords.expired", "expired");

            //GUI TITLES
            //put("gui.titles.main_page", "&eRides");
            //put("gui.titles.select_horse", "&eSelect An Horse");

            //GUI BUTTONS
            // titles
            //put("gui.buttons.select_horse.title", "&eSelect An Horse");
            //put("gui.buttons.send_back_horse.title", "&cSend Back");
            //put("gui.buttons.help.title", "&eHelp");

            // lores
            //put("gui.buttons.select_horse.lore", "&aA list of every horse possessed by you");
            //put("gui.buttons.send_back_horse.lore", "&aSend <HORSE_NAME> to the stable");
            //put("gui.buttons.help.lore", "&aFor help digit /rideshelp");
        }
    };



    private void loadMessagesFile()
    { //loading messages.yml
        messagesConfigFile = new File(simCardClass.getDataFolder(), fileName);
        if (!messagesConfigFile.exists())
        {
            messagesConfigFile.getParentFile().mkdirs();
            simCardClass.saveResource(fileName, false);
            simCardClass.console.info("Created messages.yml");
            simCardClass.console.info("To modify ingame messages edit messages.yml and reload the plugin");
        }

        messagesConfig = new YamlConfiguration();
        try
        {
            messagesConfig.load(messagesConfigFile);
            loadMessages();
        } catch (Exception e) {
            simCardClass.console.severe("Couldn't load messages.yml file properly!");
        }
    }

    private void loadMessages()
    {
        boolean needsRewrite = false; //A rewrite is needed when loaded on the server there is a older version of messages.yml, without newer messages

        for (String messageKey : messagesMap.keySet())
        {
            boolean result = loadMessage(messageKey);
            needsRewrite = needsRewrite || result;
        }

        //Once every message is loaded on the messagesMap, if needsRewrite is true, messages.yml gets closed, deleted, and rewritten with every message
        if (needsRewrite)
        {
            try
            {
                if (messagesConfigFile.delete())
                { //deleting file
                    messagesConfigFile.getParentFile().mkdirs(); //creating file
                    messagesConfigFile.createNewFile();
                    messagesConfig.load(messagesConfigFile);
                    for (String messageKey : messagesMap.keySet())
                    { //writing file
                        messagesConfig.set(messageKey, messagesMap.get(messageKey));
                    }
                    messagesConfig.save(messagesConfigFile);
                }
                else
                {
                    simCardClass.console.severe("Couldn't load messages.yml file properly!");
                }
            }
            catch (Exception e)
            {
                simCardClass.console.severe("Couldn't load messages.yml file properly!");
            }
        }

        simCardClass.console.info("Loaded custom messages");
    }

    private boolean loadMessage (String messageName)
    { //returns true if the message is not found, letting loadMessages() know if a rewrite of the file is needed or not
        boolean returnValue = false;

        String path = messageName;
        if (messagesConfig.getString(path, null) == null)
        { //message not found, returns true
            returnValue = true;
        }

        if (messagesConfig.getString(path) == null)
        {
            return true;
        }

        messagesMap.put(messageName, messagesConfig.getString(messageName)); //loading messages into messagesMap
        return returnValue;
    }

    public String getKeyword(String keywordName)
    {
        return messagesMap.get("keywords." + keywordName);
    }

    public String getChatMessage(String messageName)
    {
        return ChatColor.translateAlternateColorCodes('&', messagesMap.get("messages." + messageName + ".text"));
    }

    public String formattedChatMessage(String messageName)
    { //Automatically puts the prefix and the color to the message
        String prefix_color = ChatColor.translateAlternateColorCodes('&', messagesMap.get("messages." + messageName + ".prefix_color"));
        return ChatColor.translateAlternateColorCodes('&', String.format("%s%s %s", prefix_color, messagesMap.get("ingame_prefix"), getChatMessage(messageName)));
    }

    public String getSentMessageFormat(Player receiver, String message) //me to player
    {
        String result = messagesMap.get("sent_message_format");
        result = result.replaceAll("<receiver_display_name>", receiver.getDisplayName());
        result = result.replaceAll("<receiver_custom_name>", receiver.getCustomName());
        result = result.replaceAll("<receiver_name>", receiver.getName());
        result = result.replaceAll("<message>", message);

        return ChatColor.translateAlternateColorCodes('&', result);
    }

    public String getReceivedMessageFormat(Player sender, String message) //player to me
    {
        String result = messagesMap.get("received_message_format");
        result = result.replaceAll("<sender_display_name>", sender.getDisplayName());
        result = result.replaceAll("<sender_custom_name>", sender.getCustomName());
        result = result.replaceAll("<sender_name>", sender.getName());
        result = result.replaceAll("<message>", message);

        return ChatColor.translateAlternateColorCodes('&', result);
    }

    public String getGuiButtonName(String buttonName)
    {
        return ChatColor.translateAlternateColorCodes('&', messagesMap.get("gui.buttons." + buttonName + ".title"));
    }

    public ArrayList<String> getGuiButtonLore(String buttonName)
    {
        String completeLore = ChatColor.translateAlternateColorCodes('&', messagesMap.get("gui.buttons." + buttonName + ".lore"));
        String[] words = completeLore.split("\\s+");
        ArrayList<String> lore = new ArrayList<>();
        StringBuilder currLine = new StringBuilder();
        int count = 0;
        for (String word : words)
        {
            if (currLine.length() + word.length() > 20)
            {
                lore.add(currLine.toString());
                currLine.setLength(0);
                currLine.append(word + " ");
                continue;
            }
            currLine.append(word + " ");
        }
        lore.add(currLine.toString());
        //coloring
        String lastColor = ChatColor.getLastColors(lore.get(0));
        count = 0;
        for (String line : lore)
        {
            lore.set(count, lastColor + line);
            lastColor = ChatColor.getLastColors(lore.get(count));
            count++;
        }

        return lore;
    }

    public String getGuiTitle(String pageName)
    {
        return ChatColor.translateAlternateColorCodes('&', messagesMap.get("gui.titles." + pageName));
    }
}
