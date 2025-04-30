package engine.shader;

import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStream;

public class ShaderStuff {
    public static int makeShader(String vertexShaderPath, String fragmentShaderPath) {

        //puts the shader code from the files into these strings
        String vertexShaderSource = readFile(vertexShaderPath);
        String fragmentShaderSource = readFile(fragmentShaderPath);

        //create blank shader objects
        int vertex = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragment = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        //shove code into shader objects
        GL20.glShaderSource(vertex, vertexShaderSource);
        GL20.glShaderSource(fragment, fragmentShaderSource);

        //compile them
        GL20.glCompileShader(vertex);
        GL20.glCompileShader(fragment);

        //check for errors
        checkShaderCompileError(vertex, "vertex");
        checkShaderCompileError(fragment, "fragment");

        //combines them into usable super shader
        int superShader = GL20.glCreateProgram();
        GL20.glAttachShader(superShader, vertex);
        GL20.glAttachShader(superShader, fragment);
        GL20.glLinkProgram(superShader);

        //nukes the mini shaders
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);

        return superShader;
    }

    private static String readFile(String path) {
        try (InputStream in = ShaderStuff.class.getResourceAsStream(path)) {
            if (in == null)
                throw new IOException("Shader file not found lmao: " + path);
            return new String(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }
    }

    private static void checkShaderCompileError(int shader, String shaderType) {
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("Shader compilation error rip: " + shaderType + " " + GL20.glGetShaderInfoLog(shader));
        }
    }
}
