package org.example.components;

import org.example.jade.Component;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private final Vector4f color;
    private final Sprite sprite;

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite _sprite){
        this.sprite = _sprite;
        this.color = new Vector4f(1,1,1,1);
    }


    @Override
    public void start()
    {

    }

    @Override
    public void update(float dt)
    {

    }

    public Vector4f get_color() {
        return this.color;
    }

    public Texture get_texture() { return this.sprite.get_texture(); }

    public Vector2f[] get_tex_coords()
    {
        return this.sprite.get_texture_coords();
    }
}
