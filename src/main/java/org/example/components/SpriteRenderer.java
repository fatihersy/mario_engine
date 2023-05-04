package org.example.components;

import org.example.jade.Component;

public class SpriteRenderer extends Component
{

    @Override
    public void start()
    {
        System.out.println("LOG::SpriteRenderer::start");
    }

    @Override
    public void update(float dt)
    {
        System.out.println("LOG::SpriteRenderer::update");
    }
}
