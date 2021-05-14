package com.github.tommyt0mmy.simcard.commands;

import com.github.tommyt0mmy.simcard.SimCard;
import com.github.tommyt0mmy.simcard.cardmanager.CardData;
import com.github.tommyt0mmy.simcard.cardmanager.CardManager;
import com.github.tommyt0mmy.simcard.enums.Permissions;
import litebans.api.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

import static com.google.gson.internal.bind.TypeAdapters.UUID;

public class MsgCommand implements CommandExecutor
{
    static SimCard simCardClass = SimCard.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        /// how to consume messages
        /// CardManager.consumeMessage(p, cardData, simCardClass.configs.isFeedback_consumed_message());
        ///
        if (!(sender instanceof Player)) //check if player
        {
            sender .sendMessage(simCardClass.messages.formattedChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(Permissions.MSG.getNode())) //check permissions
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_permissions"));
            return true;
        }
        if (Database.get().isPlayerMuted(p.getUniqueId(), "0.0.0.0")) {
            p.sendMessage(simCardClass.messages.getChatMessage("messages.muted_player_error"));
            return true;
        }

        if (args.length < 2) //check correct usage
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("msg_usage"));
            return true;
        }

        Optional<Integer> id_opt = simCardClass.database.getOwnedSimId(p.getUniqueId());
        if (!id_opt.isPresent()) //check if player has a sim activated (check 1)
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("no_activated_sim"));
            return true;
        }
        Optional<CardData> cd_opt = simCardClass.database.getCardData(id_opt.get());
        if (!cd_opt.isPresent()) //check if player has a sim activated (check 2)
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("no_activated_sim"));
            return true;
        }

        Player receiver = simCardClass.getServer().getPlayer(args[0]);
        if (receiver == null) //check if player exists
        {
            p.sendMessage(simCardClass.messages.formattedChatMessage("invalid_player"));
            return true;
        }

        //sending message
        String message = "";
        for (int i = 1; i < args.length; ++i)
        {
           String curr_arg = args[i];
           message += " " + curr_arg;
        }
        message = message.trim();

        receiver.sendMessage(simCardClass.messages.getReceivedMessageFormat(p, message));
        sender.sendMessage(simCardClass.messages.getSentMessageFormat(receiver, message));

        String finalMessage = message;
        simCardClass.getServer().getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(Permissions.SPY.getNode())) {
                player.sendMessage(simCardClass.messages.getSpyMessageFormat(player, finalMessage));
            }
        });


        CardManager.consumeMessage(p, cd_opt.get(), simCardClass.configs.isFeedback_consumed_message());

        return true;
    }
}
