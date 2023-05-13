package org.example.jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final int width, height;
    private long glfwWindow;
    private final String title;
    private static Window window = null;
    public static Scene currentScene;
    public float r,g,b;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title ="Mario";
        r = 0.0f;
        g = 0.0f;
        b = 0.0f;
    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0 -> {
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
            }
            case 1 -> {
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
            }
            default -> {
                assert false : "Unknown scene '" + newScene + "'";
            }
        }
    }

    public static Window get() {
        if (Window.window == null)
        {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene get_scene()
    {
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();

        // FREE THE MEMORY
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwFreeCallbacks(glfwWindow);
        glfwTerminate();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // CREATE THE WINDOW
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mouse_pos_callback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouse_button_callback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouse_scroll_callback);
        glfwSetKeyCallback(glfwWindow, KeyListener::key_callback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable V-Sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }
    public void loop()
    {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow))
        {
            // POLL EVENTS
            glfwPollEvents();

            glClearColor(r, g, b, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0)
                currentScene.update(dt);

            if (KeyListener.is_key_pressed(GLFW_KEY_ESCAPE))
                glfwSetWindowShouldClose(glfwWindow, true);

            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
