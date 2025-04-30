package engine;

import engine.camera.Camera;
import engine.render.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        final int windowWidth = 1600;
        final int windowHeight = 900;
        final String windowTitle = "PsychoEngine";

        System.out.println("PsychoEngine bootin up yay");

        //error logging
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW ain't initiated dawg");
        }

        //creates window
        long gameWindow = glfwCreateWindow(windowWidth, windowHeight, windowTitle, 0 ,0);

        //error logging
        if (gameWindow == 0) {
            glfwTerminate();
            throw new RuntimeException("GLFW game window failed to create L");
        }

        glfwMakeContextCurrent(gameWindow);
        glfwSwapInterval(1); //vsync
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        //camera
        Camera camera = new Camera(
                new Vector3f(0, 0, 3),
                new Vector3f(0, 0, 0),
                (float) windowWidth / windowHeight
        );
        Renderer renderer = new Renderer();

        glClearColor(0f, 0f, 0f, 0f);

        //main loop for game
        while (!glfwWindowShouldClose(gameWindow)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float time = (float) glfwGetTime();

            //rotate camera
            Matrix4f model = new Matrix4f()
                    .rotateY(time * 0.5f)
                    .rotateX(time * 0.3f);

            Matrix4f superMatrix = camera.getSuperMatrix(model);
            renderer.render(superMatrix);

            glfwSwapBuffers(gameWindow);
        }

        //KILL IT MUAHAHA
        glfwTerminate();
    }
}
