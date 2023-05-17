package util;

import org.example.components.Spritesheet;
import org.example.renderer.Shader;
import org.example.renderer.Texture;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private final static String shader_path = "assets/shaders/";
    private final static String texture_path = "assets/images/";
    private static final Map<String, Shader> shader_map = new HashMap<>();
    private static final Map<String, Texture> texture_map = new HashMap<>();
    private static final Map<String, Spritesheet> spritesheet_map = new HashMap<>();

    public static Shader get_shader(String resource_name)
    {
        if (!AssetPool.shader_map.containsKey(resource_name))
        {
            Shader shader = new Shader(
                    resource_name,
                    shader_path + resource_name + "_vertex.vert",
                    shader_path + resource_name + "_fragment.frag"
            );
            shader.compile();
            AssetPool.shader_map.put(resource_name, shader);
            System.out.println("Shader::get_shader(): Shader '" + resource_name + "' created!");
        }

        return AssetPool.shader_map.get(resource_name);
    }

    public static Texture get_texture(String resource_name)
    {
        if (!AssetPool.texture_map.containsKey(resource_name))
        {
            Texture texture = new Texture(texture_path + resource_name);
            AssetPool.texture_map.put(resource_name, texture);
        }

        return AssetPool.texture_map.get(resource_name);
    }

    public static void add_spritesheet(String resource_name, Spritesheet spritesheet)
    {
        if (!AssetPool.spritesheet_map.containsKey(resource_name))
            AssetPool.spritesheet_map.put(resource_name, spritesheet);
    }
    public static Spritesheet get_spritesheet(String resource_name)
    {
        if (AssetPool.spritesheet_map.containsKey(resource_name))
            return AssetPool.spritesheet_map.get(resource_name);

        return null;
    }
}
