package engine.camera;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class CameraControl {
    private final Camera camera;
    private final long gameWindow;

    private double lastX;
    private double lastY;
    private boolean mouseFirstMove = true;


    public CameraControl(Camera camera, long gameWindow) {
        this.camera = camera;
        this.gameWindow = gameWindow;
    }

    //actually "move" the camera
    public void update() {
        cameraWASD();
        cameraLook();
    }

    public void cameraWASD() {
        Vector3f movementVector = new Vector3f(0, 0, 0);
        float cameraSpeed = 0.03f;

        if (glfwGetKey(gameWindow, GLFW_KEY_W) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getFront()).mul(cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_S) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getFront()).mul(-cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_A) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getRight()).mul(-cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_D) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getRight()).mul(cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_SPACE) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getUp()).mul(cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            movementVector.add(new Vector3f(camera.getUp()).mul(-cameraSpeed));
        }

        //kill extra speed when it's moving too fast
        if (movementVector.lengthSquared() != 0) {
            movementVector.normalize().mul(cameraSpeed);
            camera.move(movementVector);
        }
    }

    public void cameraLook() {
        DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(gameWindow, mouseX, mouseY);

        double currentX = mouseX.get(0);
        double currentY = mouseY.get(0);

        if(mouseFirstMove) {
            lastX = currentX;
            lastY = currentY;
            mouseFirstMove = false;
        }

        double changeX = currentX - lastX;
        double changeY = currentY - lastY;

        lastX = currentX;
        lastY = currentY;

        camera.rotate((float) changeX, (float) changeY);
    }
}
