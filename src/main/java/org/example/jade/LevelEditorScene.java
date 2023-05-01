package org.example.jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene
{
    private final String vertexShaderSrc =
            """
            #version 330 core
            layout (location=0) in vec3 aPos;
            layout (location=1) in vec4 aColor;

            out vec4 fColor;

            void main()
            {
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0f);
            }
            """;
    private final String fragmentShaderSrc =
            """
            #version 330 core
            in vec4 aColor;

            out vec4 color;

            void main()
            {
                color = fColor;
            }
            """;
    private int vertexID, fragmentID, shaderProgram;


    public LevelEditorScene()
    {

    }

    @Override
    public void update(float dt)
    {

        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            System.out.println("LevelEditor::TRIGGERED::SPACE_BAR");
        }

    }
}
