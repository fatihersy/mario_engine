package org.example.renderer;

import org.example.components.SpriteRenderer;
import org.example.jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch
{
    // Vertex
    // ----------------
    // Pos              Color                           TexCoords       TexID
    // float, float,    float, float, float, float      float, float    float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int num_sprites;
    private boolean has_room;
    private float[] vertices;
    private int[] tex_slots = { 0, 1, 2, 3, 4, 5, 6, 7 };

    private List<Texture> texture_list;
    private int vaoID, vboID;
    private int max_batch_size;
    private Shader shader;

    public RenderBatch(int max_batch_size)
    {
        shader = AssetPool.get_shader("default");
        shader.compile();
        this.sprites = new SpriteRenderer[max_batch_size];
        this.max_batch_size = max_batch_size;

        vertices = new float[max_batch_size * 4 * VERTEX_SIZE];

        this.num_sprites = 0;
        this.has_room = true;
        this.texture_list = new ArrayList<>();
    }

    public void start() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generate_indices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void add_sprite(SpriteRenderer sprite)
    {
        int index = this.num_sprites;
        this.sprites[index] = sprite;
        this.num_sprites++;

        if (sprite.get_texture() != null)
        {
            if (!texture_list.contains(sprite.get_texture()))
            {
                texture_list.add(sprite.get_texture());
            }
        }

        load_vertex_properties(index);

        if (num_sprites >= this.max_batch_size)
        {
            this.has_room = false;
        }
    }

    public void render(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.upload_mat4f("uProjection", Window.get_scene().get_camera().get_projection_matrix());
        shader.upload_mat4f("uView", Window.get_scene().get_camera().get_view_matrix());
        for (int i=0; i < texture_list.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            texture_list.get(i).bind();
        }
        shader.upload_integer_array("uTextures", tex_slots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.num_sprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < texture_list.size(); i++)
        {
            texture_list.get(i).unbind();
        }

        shader.detach();
    }

    private void load_vertex_properties(int index)
    {
        SpriteRenderer sprite_renderer = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite_renderer.get_color();
        Vector2f[] tex_coords = sprite_renderer.get_tex_coords();

        int texID = 0;
        if (sprite_renderer.get_texture() != null)
            for (int i=0; i < texture_list.size(); i++)
            {
                if (texture_list.get(i) == sprite_renderer.get_texture()) {
                    texID = i + 1;
                    break;
                }
            }

        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for (int i = 0; i < 4; i++)
        {
            if (i == 1)
                yAdd = 0.0f;
            else if (i == 2)
                xAdd = 0.0f;
            else if (i == 3)
                yAdd = 1.0f;

            vertices[offset] = sprite_renderer.game_object.transform.position.x + (xAdd * sprite_renderer.game_object.transform.scale.x);
            vertices[offset + 1] = sprite_renderer.game_object.transform.position.y + (yAdd * sprite_renderer.game_object.transform.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            vertices[offset + 6] = tex_coords[i].x;
            vertices[offset + 7] = tex_coords[i].y;

            vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }



    private int[] generate_indices()
    {
        int[] elements = new int[6 * max_batch_size];

        for (int i=0; i < max_batch_size; i++)
        {
            load_element_indices(elements, i);
        }

        return elements;
    }

    private void load_element_indices(int[] elements, int index)
    {
        int offset_array_index = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1     7, 6, 4, 4, 6, 5
        // TRIANGLE 1
        elements[offset_array_index] = offset + 3;
        elements[offset_array_index + 1] = offset + 2;
        elements[offset_array_index + 2] = offset + 0;

        // TRIANGLE 2
        elements[offset_array_index + 3] = offset + 0;
        elements[offset_array_index + 4] = offset + 2;
        elements[offset_array_index + 5] = offset + 1;
    }

    public boolean has_room(){
        return this.has_room;
    }
}























