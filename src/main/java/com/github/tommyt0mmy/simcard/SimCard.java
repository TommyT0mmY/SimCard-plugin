package com.github.tommyt0mmy.simcard;

import com.github.tommyt0mmy.simcard.commands.GetsimCommand;
import com.github.tommyt0mmy.simcard.commands.MsgCommand;
import com.github.tommyt0mmy.simcard.commands.NewsimCommand;
import com.github.tommyt0mmy.simcard.commands.RemovesimCommand;
import com.github.tommyt0mmy.simcard.configuration.Configs;
import com.github.tommyt0mmy.simcard.configuration.Messages;
import com.github.tommyt0mmy.simcard.database.CartaSimDatabase;
import com.github.tommyt0mmy.simcard.events.ActivateSim;
import com.github.tommyt0mmy.simcard.tabcompleters.GetsimTabCompleter;
import com.github.tommyt0mmy.simcard.tabcompleters.MsgTabCompleter;
import com.github.tommyt0mmy.simcard.tabcompleters.RemovesimTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SimCard extends JavaPlugin
{
    //INSTANCE
    private static SimCard instance;
    public static SimCard getInstance()
    {
        return instance;
    }
    private void setInstance(SimCard instance)
    {
        SimCard.instance = instance;
    }
    //

    public CartaSimDatabase database;
    public Configs configs;
    public Messages messages;
    public Logger console = getLogger();

    public void onEnable()
    {
        //priority 1
        setInstance(this);
        getDataFolder().mkdir();

        //priority 2
        configs = new Configs();
        messages = new Messages();
        database = new CartaSimDatabase();

        /* DEBUG
        configs.addCardType(new CardType(20, "test1", "&4test_display_name", new ItemStack(Material.STONE), 200));

        database.addSimOwner(200, UUID.fromString("124b086a-fe6f-4e12-82ae-a05dc1b9dcd1"));
        */

        //priority 3
        loadCommands();
        loadEvents();

        //loading PAPI
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderapiExpansion(this).register();
        }
    }

    public void onDisable()
    {
        database.closeConnection();
    }

    private void loadCommands()
    {
        getCommand("getsim").setExecutor(new GetsimCommand());
        getCommand("getsim").setTabCompleter(new GetsimTabCompleter());

        getCommand("phone").setExecutor(new MsgCommand());
        getCommand("phone").setTabCompleter(new MsgTabCompleter());

        getCommand("newsim").setExecutor(new NewsimCommand());

        getCommand("removesim").setExecutor(new RemovesimCommand());
        getCommand("removesim").setTabCompleter(new RemovesimTabCompleter());
    }

    private void loadEvents()
    {
        this.getServer().getPluginManager().registerEvents(new ActivateSim(), this);
        this.getServer().getPluginManager().registerEvents(new NewsimCommand(), this);
    }
}
