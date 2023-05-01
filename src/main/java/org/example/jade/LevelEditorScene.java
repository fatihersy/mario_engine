package org.example.jade;

import org.example.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene
{
    private int vaoID;
    private final Shader default_program;

    private final float[] vertexArray =
    {
             // Position                // Color
             0.5f, -0.5f,  0.0f,        1.0f, 0.0f, 0.0f, 1.0f, // Bottom Right
            -0.5f,  0.5f,  0.0f,        0.0f, 1.0f, 0.0f, 1.0f, // Top Left
             0.5f,  0.5f,  0.0f,        0.0f, 0.0f, 1.0f, 1.0f, // Top Right
            -0.5f, -0.5f,  0.0f,        1.0f, 1.0f, 0.0f, 1.0f, // Bottom Left
    };

    // Counter-Clockwise
    private final int[] elementArray = {
            2,1,0,
            0,1,3
    };


    public LevelEditorScene()
    {
        default_program = new Shader(
                "default",
                "assets/shaders/default_vertex.vert",
                "assets/shaders/default_fragment.frag");

        default_program.compile();
    }

    @Override
    public void init()
    {


        // ============================================
        //  Generate VAO, VBO, EBO and send -> GPU
        // ============================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt)
    {
        default_program.use();

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            System.out.println("LevelEditor::TRIGGERED::SPACE_BAR");
        }

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        default_program.detach();
    }
}
