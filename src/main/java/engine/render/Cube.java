package engine.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Cube {
   private final int vao;
   private final int vbo;
   private final int ebo;

    private final float[] vertices = {
            // x, y, z,    r, g, b
            -0.5f, -0.5f, -0.5f, 0f, 0f, 1f,
            0.5f, -0.5f, -0.5f, 0f, 0f, 1f,
            0.5f,  0.5f, -0.5f, 0f, 0f, 1f,
            -0.5f,  0.5f, -0.5f, 0f, 0f, 1f,
            -0.5f, -0.5f,  0.5f, 0f, 0f, 1f,
            0.5f, -0.5f,  0.5f, 0f, 0f, 1f,
            0.5f,  0.5f,  0.5f, 0f, 0f, 1f,
            -0.5f,  0.5f,  0.5f, 0f, 0f, 1f
    };

    private final int[] indices = {
            0, 1, 2, 2, 3, 0,
            4, 5, 6, 6, 7, 4,
            4, 0, 3, 3, 7, 4,
            1, 5, 6, 6, 2, 1,
            4, 5, 1, 1, 0, 4,
            3, 2, 6, 6, 7, 3
    };

    public Cube() {
        vao = GL30.glGenVertexArrays();
        vbo = GL30.glGenBuffers();
        ebo = GL30.glGenBuffers();

        GL30.glBindVertexArray(vao);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);

        //copy over
        vertexBuffer.put(vertices).flip();
        indexBuffer.put(indices).flip();

        GL15.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL15.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ebo);

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        //enable gpu info reading for specific info types
        //0: location | 1: color
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        //unbind
        GL30.glBindVertexArray(0);
    }

    //draws triangles to form the cube
    public void draw() {
        GL30.glBindVertexArray(vao);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }
}
