package org.example.jade;
import org.example.components.SpriteRenderer;
import org.example.components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;


public class LevelEditorScene extends Scene
{

    public LevelEditorScene()
    {

    }

    @Override
    public void init()
    {
        load_resources();

        this.camera = new Camera(new Vector2f());

        Spritesheet spritesheet = AssetPool.get_spritesheet("spritesheet");
        assert spritesheet != null : "'spritesheet.png' return null";

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));

        obj1.add_component(new SpriteRenderer(spritesheet.get_sprite(0)));
        this.add_game_object(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.add_component(new SpriteRenderer(spritesheet.get_sprite(15)));
        this.add_game_object(obj2);

    }

    private void load_resources()
    {
        AssetPool.get_shader("default");

        AssetPool.add_spritesheet(
                "spritesheet",
                new Spritesheet(AssetPool.get_texture("spritesheet.png"), 16, 16, 26, 0)
        );
    }

    @Override
    public void update(float dt)
    {
        for (GameObject obj : this.game_objects)
            obj.update(dt);

        this.renderer.render();
    }
}
