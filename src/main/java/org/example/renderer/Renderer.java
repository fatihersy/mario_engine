package org.example.renderer;

import org.example.components.SpriteRenderer;
import org.example.jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final List<RenderBatch> batchList;

    public Renderer() {
        this.batchList = new ArrayList<>();
    }

    public void add(GameObject gameObject)
    {
        SpriteRenderer spriteRenderer = gameObject.get_component(SpriteRenderer.class);

        if (spriteRenderer != null)
        {
            add(spriteRenderer);
        }
    }

    public void add(SpriteRenderer spriteRenderer)
    {
        boolean added = false;
        Texture texture = spriteRenderer.get_texture();

        for (RenderBatch rb: batchList)
        {
            if (!rb.has_sprite_room()) continue;
            if (texture == null || (rb.has_texture_room() || rb.has_texture(texture)))
            {
                rb.add_sprite(spriteRenderer);
                added = true;
                break;
            }
        }

        if(!added)
        {
            int MAX_BATCH_SIZE = 1000;
            RenderBatch new_batch = new RenderBatch(MAX_BATCH_SIZE);
            new_batch.start();
            batchList.add(new_batch);
            new_batch.add_sprite(spriteRenderer);

        }
    }

    public void render()
    {
        for (RenderBatch batch : batchList)
        {
            batch.render();
        }
    }




}
