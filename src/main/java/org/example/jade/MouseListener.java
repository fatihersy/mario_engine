package org.example.jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean[] mouse_button_pressed = new boolean[3];
    private boolean is_dragging;

    private MouseListener() {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    public static MouseListener get() {
        if(MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mouse_pos_callback(long window, double xpos, double ypos)
    {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().is_dragging =
                get().mouse_button_pressed[0] || get().mouse_button_pressed[1] || get().mouse_button_pressed[2];
    }

    public static void mouse_button_callback(long window, int button, int action, int mods)
    {
        if (action == GLFW_PRESS)
        {
            if (button < get().mouse_button_pressed.length)
            {
                get().mouse_button_pressed[button] = true;
            }
        }
        else if (action == GLFW_RELEASE)
        {
            if (button < get().mouse_button_pressed.length)
            {
                get().mouse_button_pressed[button] = false;
                get().is_dragging = false;
            }
        }
    }

    public static void mouse_scroll_callback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void end_frame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }
    public static float getY() {
        return (float) get().yPos;
    }
    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }
    public static float getDy() {
        return  (float) (get().lastY - get().yPos);
    }
    public static float getScrollX() {
        return (float) get().scrollX;
    }
    public static float getScrollY() {
        return (float) get().scrollY;
    }
    public static boolean is_dragging() {
        return get().is_dragging;
    }
    public static boolean mouse_button_down(int button)
    {
        if (button < get().mouse_button_pressed.length)
        {
            return  get().mouse_button_pressed[button];

        }
        else
        {
            return false;
        }

    }

}
