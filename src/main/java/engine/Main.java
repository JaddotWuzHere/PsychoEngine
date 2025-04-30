package engine;

import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("PsychoEngine bootin up yay");

        //error logging
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW ain't initiated dawg");
        }

        //creates window
        long gameWindow = glfwCreateWindow(800, 600, "PsychoEngine", 0 ,0);

        //error logging
        if (gameWindow == 0) {
            glfwTerminate();
            throw new RuntimeException("GLFW game window failed to create L");
        }

        glfwMakeContextCurrent(gameWindow);
        GL.createCapabilities();

        glClearColor(0f, 0f, 0f, 0f);

        //main loop for game
        while (!glfwWindowShouldClose(gameWindow)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(gameWindow);
        }

        //KILL IT MUAHAHA
        glfwTerminate();
    }
}
