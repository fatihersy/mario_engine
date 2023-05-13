package org.example.jade;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;
    public Transform transform;

    public GameObject(String name)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends Component> T get_component(Class<T> component_class)
    {
        for(Component c: components) {
            if (component_class.isAssignableFrom(c.getClass())) {
                try {
                    return component_class.cast(c);
                }
                catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error::Casting_Component!";
                }
            }
        }
        return null;
    }

    public <T extends Component> void remove_component(Class<T> component_class)
    {
        for(int i=0; i < components.size(); i++)
        {
            Component c = components.get(i);
            if (component_class.isAssignableFrom(c.getClass()))
            {
                components.remove(i);
                return;
            }
        }
    }

    public void add_component(Component c)
    {
        this.components.add(c);
        c.game_object = this;
    }

    public void update(float dt)
    {
        for (Component component: components) component.update(dt);
    }

    public void start()
    {
        for (Component component: components) component.start();
    }
}
