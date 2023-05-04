package org.example.jade;

public abstract class Component
{
    public GameObject game_object = null;

    public void start() {}
    public abstract void update(float dt);
}
