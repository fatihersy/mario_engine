package org.example.components;

import org.example.jade.Component;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color;
    private Vector2f[] tex_coords;
    private Texture texture;

    public SpriteRenderer(Texture _texture){
        this.texture = _texture;
        this.color = new Vector4f(1,1,1,1);
    }

    public SpriteRenderer(Vector4f color)
    {
        this.color = color;
        this.texture = null;
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

    public Texture get_texture() { return this.texture; }

    public Vector2f[] get_tex_coords()
    {
        Vector2f[] vector =
        {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        return vector;
    }
}
