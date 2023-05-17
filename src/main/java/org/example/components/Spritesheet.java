package org.example.components;

import org.example.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet
{
    private final Texture texture;
    private final List<Sprite> sprite_list;

    public Spritesheet(Texture _texture, int cell_width, int cell_height, int cell_count, int spacing)
    {
        this.sprite_list = new ArrayList<>();

        this.texture = _texture;
        int currentX = 0;
        int currentY = texture.get_height() - cell_height;
        for (int i = 0; i < cell_count; i++)
        {
            float topY    = (currentY + cell_height) / (float) texture.get_height();
            float rightX  = (currentX + cell_width) / (float) texture.get_width();
            float leftX   = currentX / (float) texture.get_width();
            float bottomY = currentY / (float) texture.get_height();

            Vector2f[] tex_coords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.texture, tex_coords);
            this.sprite_list.add(sprite);

            currentX += cell_width + spacing;
            if (currentX >= texture.get_width())
            {
                currentX = 0;
                currentY -= cell_height + spacing;
            }
        }
    }

    public Sprite get_sprite(int index)
    {
        return this.sprite_list.get(index);
    }
}
