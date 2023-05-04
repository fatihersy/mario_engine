package org.example.jade;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Camera camera;
    private boolean is_running;
    protected List<GameObject> game_objects;


    public Scene()
    {
        this.is_running = false;
        game_objects = new ArrayList<>();
    }

    public void init() {
    }

    public void start()
    {
        for (GameObject obj : game_objects) obj.start();

        is_running = true;
    }

    public void add_game_object(GameObject obj)
    {
        if (!is_running)
            game_objects.add(obj);
        else {
            game_objects.add(obj);
            obj.start();
        }
    }

    public abstract void update(float dt);
}
