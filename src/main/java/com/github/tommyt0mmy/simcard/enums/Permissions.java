package com.github.tommyt0mmy.simcard.enums;

public enum Permissions
{
    GETSIM("getsim"),
    MSG("msg"),
    ACTIVATESIM("activatesim"),
    NEWSIM("newsim"),
    REMOVESIM("removesim");

    private String node;

    Permissions(String node)
    {
        this.node = node;
    }

    public String getNode() { return "simcard." + node; }
}
