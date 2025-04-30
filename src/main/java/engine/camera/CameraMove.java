package engine.camera;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class CameraMove {
    private final Camera camera;
    private final long gameWindow;

    public CameraMove(Camera camera, long gameWindow) {
        this.camera = camera;
        this.gameWindow = gameWindow;
    }

    //actually "move" the camera
    public void cameraMove () {
        Vector3f movementVector = new Vector3f(0, 0, 0);
        float cameraSpeed = 0.03f;

        if (glfwGetKey(gameWindow, GLFW_KEY_W) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(0, 0, -cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_S) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(0, 0, cameraSpeed));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_A) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(-cameraSpeed, 0, 0));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_D) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(cameraSpeed, 0, 0));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_SPACE) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(0, cameraSpeed, 0));
        }
        if (glfwGetKey(gameWindow, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            movementVector = movementVector.add(new Vector3f(0, -cameraSpeed, 0));
        }

        //kill extra speed when it's moving too fast
        if (movementVector.lengthSquared() != 0) {
            movementVector.normalize().mul(cameraSpeed);
            camera.move(movementVector);
        }
    }
}
