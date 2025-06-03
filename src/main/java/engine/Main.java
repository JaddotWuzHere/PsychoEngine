package engine;

import engine.camera.Camera;
import engine.camera.CameraControl;
import engine.render.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        final int windowWidth = 1600;
        final int windowHeight = 900;
        final String windowTitle = "PsychoEngine";

        System.out.println("[STARTUP] PsychoEngine bootin up yay");

        //error logging
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW ain't initiated dawg");
        }

        //creates window
        long gameWindow = glfwCreateWindow(windowWidth, windowHeight, windowTitle, 0 ,0);

        //hides mouse, infinite camera movement essentially
        glfwSetInputMode(gameWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

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
                (float) windowWidth / windowHeight
        );

        //enables the camera to have WASD LET'S GOOOOO
        CameraControl cameraControl = new CameraControl(camera, gameWindow);

        //fps counter
        FpsCounter fpsCounter = new FpsCounter();
        try {
            fpsCounter.initText();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Renderer renderer = new Renderer();

        glClearColor(0f, 0f, 0f, 1f);

        //get last delta time (used for fps counting)
        long lastTime = System.nanoTime();

        //main loop for game
        while (!glfwWindowShouldClose(gameWindow)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //fps counter stuff
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastTime) / 1_000_000_000f;
            lastTime = currentTime;

            float currentFPS = fpsCounter.getFPS();

            Matrix4f ortho = new Matrix4f().ortho(
                    0, windowWidth,       //left to right
                    0, windowHeight,      //bottom to top
                    -1, 1                 //near/far planes
            );

            //checks conditions to move camera
            cameraControl.update(deltaTime);

            //camera matrix for perspective stuff
            Matrix4f model = new Matrix4f();
            Matrix4f superMatrix = camera.getSuperMatrix(model);
            renderer.render(superMatrix);

            //display fps on screen
            float x = 10f;
            float y = windowHeight - 30f;

            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            glOrtho(0, windowWidth, windowHeight, 0, -1, 1);

            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            glLoadIdentity();

            glDisable(GL_DEPTH_TEST);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glColor4f(1f, 1f, 1f, 1f);

            fpsCounter.renderText("FPS: " + currentFPS, 10, 30);

            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);

            glPopMatrix();
            glMatrixMode(GL_PROJECTION);
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);


            glfwSwapBuffers(gameWindow);

            //press escape to terminate
            if (glfwGetKey(gameWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                glfwSetWindowShouldClose(gameWindow, true);
            }

        }

        //KILL IT MUAHAHA
        glfwTerminate();
    }
}
