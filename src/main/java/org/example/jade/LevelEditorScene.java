package org.example.jade;

import org.example.renderer.Shader;
import org.example.renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene
{
    private int vaoID;
    private final Shader default_program;
    private Texture texture;

    private final float[] vertexArray =
    {
             // Position                    // Color                   // UV Coord
              50.0f*3.f,   50.0f*3.f,  0.0f,        1.0f, 0.0f, 0.0f, 1.0f,    1, 1, // Top Right
              50.0f*3.f,  -50.0f*3.f,  0.0f,        0.0f, 1.0f, 0.0f, 1.0f,    1, 0, // Bottom Right
             -50.0f*3.f,  -50.0f*3.f,  0.0f,        0.0f, 0.0f, 1.0f, 1.0f,    0, 0, // Bottom Left
             -50.0f*3.f,   50.0f*3.f,  0.0f,        1.0f, 1.0f, 0.0f, 1.0f,    0, 1  // Top Left
    };

    // Counter-Clockwise
    private final int[] elementArray = {
            0,1,3,
            1,2,3
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
        this.camera = new Camera(new Vector2f());
        this.texture = new Texture("D:\\Documents\\OneDrive\\Pictures\\oc.png");

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

        int positions_size = 3;
        int color_size = 4;
        int uv_size = 2;
        int vertex_size_bytes = (positions_size + color_size + uv_size) * Float.BYTES;

        glVertexAttribPointer(0, positions_size, GL_FLOAT, false, vertex_size_bytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, color_size, GL_FLOAT, false, vertex_size_bytes, positions_size * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uv_size, GL_FLOAT, false, vertex_size_bytes, (color_size + positions_size) * Float.BYTES);
        glEnableVertexAttribArray(2);

    }

    @Override
    public void update(float dt)
    {
        this.camera.position.x -= dt * 50.f*3.5f;
        this.camera.position.y -= dt * 25.f*3.5f;

        default_program.use();

        default_program.upload_texture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        default_program.upload_mat4f("uProjection", camera.get_projection_matrix());
        default_program.upload_mat4f("uView", camera.get_view_matrix());
        default_program.upload_float("uTime", (float)glfwGetTime());

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
