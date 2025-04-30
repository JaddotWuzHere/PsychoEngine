package engine.render;

import engine.shader.ShaderStuff;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.joml.Matrix4f;


import java.nio.FloatBuffer;

public class Renderer {
    private Cube cube;
    private int superShader;

    public Renderer() {
        cube = new Cube();
        superShader = ShaderStuff.makeShader("/shaders/vertex.glsl", "/shaders/fragment.glsl");
    }

    //camera stuff
    public void render(Matrix4f matrix4f) {
        //TODO: figure out wtf this does cause i copied it
        //actually activates the shader
        GL20.glUseProgram(superShader);

        int loc = GL20.glGetUniformLocation(superShader, "uMatrix");
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix4f.get(buffer);
        GL20.glUniformMatrix4fv(loc, false, buffer);

        cube.draw();

        //unbinds shader
        GL20.glUseProgram(0);
    }
}
