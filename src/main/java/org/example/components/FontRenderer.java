package org.example.components;

import org.example.jade.Component;

public class FontRenderer extends Component
{
    public void start()
    {
        if (game_object.get_component(SpriteRenderer.class) != null)
        {
            System.out.println("Found Font Renderer!");
        }
    }
    @Override
    public void update(float dt)
    {

    }
}
