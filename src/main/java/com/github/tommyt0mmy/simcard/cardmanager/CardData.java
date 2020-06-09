package com.github.tommyt0mmy.simcard.cardmanager;

public class CardData
{
    private int id;
    private String type;
    private int remaining_messages;

    public CardData(int id, String type, int remainingMessages)
    {
        this.id = id;
        this.type = type;
        this.remaining_messages = remainingMessages;
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

    public int getRemaining_messages()
    {
        return remaining_messages;
    }

    public void setRemaining_messages(int remaining_messages)
    {
        this.remaining_messages = remaining_messages;
    }
}
