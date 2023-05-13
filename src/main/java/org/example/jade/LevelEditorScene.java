package org.example.jade;
import org.example.components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;


public class LevelEditorScene extends Scene
{

    public LevelEditorScene()
    {

    }

    @Override
    public void init()
    {
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float total_width = (float) (600 - xOffset * 2);
        float total_height = (float) (300 - yOffset * 2);

        float sizeX = total_width / 100.0f;
        float sizeY = total_height / 100.0f;

        for(int x=0; x < 100; x++)
        {
            for (int y=0; y < 100; y++)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject obj = new GameObject("obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                obj.add_component(new SpriteRenderer(new Vector4f( xPos / total_width, yPos / total_height, 1, 1)));
                this.add_game_object(obj);
            }
        }
    }

    private void load_resources()
    {
        AssetPool.get_shader("default");
    }

    @Override
    public void update(float dt)
    {
        for (GameObject obj : this.game_objects)
            obj.update(dt);

        this.renderer.render();
    }
}
