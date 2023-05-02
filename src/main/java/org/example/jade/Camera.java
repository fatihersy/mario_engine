package org.example.jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private Matrix4f projection_matrix, view_matrix;
    public Vector2f position;

    public Camera(Vector2f position)
    {
        this.position = position;
        this.projection_matrix = new Matrix4f();
        this.view_matrix = new Matrix4f();
        adjust_projection();
    }

    public void adjust_projection()
    {
        projection_matrix.identity();
        projection_matrix.ortho(0.0f, 120.0f * 16.0f, 0.0f, 120.0f * 9.0f, 0.0f, 100.0f);
    }

    public Matrix4f get_view_matrix()
    {
        Vector3f camera_front = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f camera_up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.view_matrix.identity();
        this.view_matrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),
                camera_front.add(position.x, position.y, 0.0f),
                camera_up
        );

        return this.view_matrix;
    }

    public Matrix4f get_projection_matrix() {
        return this.projection_matrix;
    }
}
