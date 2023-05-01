package org.example.renderer;

import java.io.IOException;
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
            System.out.println("ERROR:: '" + vertex_path + ".vert'\n\tVertex shader compilation failed");
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
            System.out.println("ERROR:: '" + fragment_path + ".frag'\n\tFragment shader compilation failed");
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
        glUseProgram(handle);
    }

    public void detach()
    {
        glUseProgram(0);
    }
}
