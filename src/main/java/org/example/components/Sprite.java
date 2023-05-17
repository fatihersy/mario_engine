package org.example.components;

import org.example.renderer.Texture;
import org.joml.Vector2f;

public class Sprite
{
    private final Texture texture;
    private final Vector2f[] texture_coords;

    public Sprite(Texture _texture)
    {
        this.texture = _texture;
        texture_coords = new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }

    public Sprite(Texture _texture, Vector2f[] _texture_coords)
    {
        this.texture = _texture;
        texture_coords = _texture_coords;
    }

    public Texture get_texture() { return this.texture; }
    public Vector2f[] get_texture_coords() { return this.texture_coords; }
}
