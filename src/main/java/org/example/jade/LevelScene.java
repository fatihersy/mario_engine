package org.example.jade;

public class LevelScene extends Scene
{
    public LevelScene() {
        System.out.println("Inside level scene");
        Window.get().r = 1.f;
        Window.get().g = 1.f;
        Window.get().b = 1.f;
    }

    @Override
    public void update(float dt)
    {

    }
}
