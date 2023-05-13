package org.example.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener
{
    private static KeyListener instance;
    private final boolean[] key_pressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get()
    {
        if (KeyListener.instance == null) KeyListener.instance = new KeyListener();

        return  KeyListener.instance;
    }

    public static void key_callback(long window, int key, int scancode, int action, int mods)
    {
        if (action == GLFW_PRESS)
        {
            get().key_pressed[key] = true;
        }
        else if (action == GLFW_RELEASE)
        {
            get().key_pressed[key] = false;
        }
    }

    public static boolean is_key_pressed(int key_code)
    {
        if (key_code < get().key_pressed.length)
            return get().key_pressed[key_code];
        else
            return false;
    }
}
