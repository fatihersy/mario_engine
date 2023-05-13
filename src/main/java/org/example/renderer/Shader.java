package org.example.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int handle;
    public String program_name;
    private final String vertex_path;
    private final String fragment_path;
    private String vertex_source;
    private String fragment_source;
    private boolean is_active = false;

    public Shader(String program_name, String vertex_path, String fragment_path)
    {
        this.program_name = program_name;
        this.vertex_path = vertex_path;
        this.fragment_path = fragment_path;

        try {
            vertex_source = new String(Files.readAllBytes(Paths.get(vertex_path)));
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "ERROR::COULD_NOT_OPEN_FILE_FOR_SHADER: '" + vertex_path + "'";
        }

        try {
        fragment_source = new String(Files.readAllBytes(Paths.get(fragment_path)));
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "ERROR::COULD_NOT_OPEN_FILE_FOR_SHADER: '" + fragment_path + "'";
        }

    }

    public void compile()
    {
        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, vertex_source);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:: '" + vertex_path + "'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, fragment_source);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:: '" + fragment_path + "'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        handle = glCreateProgram();
        glAttachShader(handle, vertexID);
        glAttachShader(handle, fragmentID);
        glLinkProgram(handle);

        success = glGetProgrami(handle, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(handle, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + program_name + ":'\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(handle, len));
            assert false : "";
        }
    }

    public void use()
    {
        if (!is_active)
        {
            is_active = true;
            glUseProgram(handle);
        }
    }

    public void detach()
    {
        is_active = false;
        glUseProgram(0);
    }

    public void upload_mat4f(String var_name, Matrix4f matrix)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        FloatBuffer mat_buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(mat_buffer);
        glUniformMatrix4fv(var_location, false, mat_buffer);
    }
    public void upload_mat3f(String var_name, Matrix3f matrix)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        FloatBuffer mat_buffer = BufferUtils.createFloatBuffer(9);
        matrix.get(mat_buffer);
        glUniformMatrix3fv(var_location, false, mat_buffer);
    }
    public void upload_vec4f(String var_name, Vector4f vector)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform4f(var_location, vector.x,vector.y,vector.z,vector.w);
    }
    public void upload_vec3f(String var_name, Vector3f vector)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform3f(var_location, vector.x,vector.y,vector.z);
    }
    public void upload_vec2f(String var_name, Vector2f vector)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform2f(var_location, vector.x,vector.y);
    }
    public void upload_float(String var_name, float value)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform1f(var_location, value);
    }
    public void upload_integer(String var_name, int value)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform1i(var_location, value);
    }
    public void upload_texture(String var_name, int slot)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform1i(var_location, slot);
    }

    public void upload_integer_array(String var_name, int[] value)
    {
        use();

        int var_location = glGetUniformLocation(handle, var_name);
        glUniform1iv(var_location, value);
    }
}
