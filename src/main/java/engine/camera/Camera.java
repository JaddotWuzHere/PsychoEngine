package engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f pos;
    private final float fov;
    private final float aspr;
    private final float near;
    private final float far;

    private float yaw = -90.0f;  //default
    private float pitch = 0.0f; //default
    private final float sensitivity = 0.1f;

    private Vector3f front = new Vector3f(0, 0, -1);
    private Vector3f right = new Vector3f(1, 0, 0);
    private final Vector3f up;


    public Camera(Vector3f pos, float aspr) {
        this.pos = pos;
        this.up = new Vector3f(0, 1, 0);
        this.fov = (float) Math.toRadians(90f);
        this.aspr = aspr;
        this.near = 0.1f;
        this.far = 100f;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), up);
    }

    public Matrix4f getSuperMatrix(Matrix4f model) {
        return new Matrix4f()
                .perspective(fov, aspr, near, far)
                .mul(getViewMatrix())
                .mul(model);
    }


    //how the camera should move if called upon
    public void move(Vector3f offset) {
        pos.add(offset);
    }

    private void updateCameraDirections() {
        float yawRadians = (float) Math.toRadians(yaw);
        float pitchRadians = (float) Math.toRadians(pitch);

        //get direction after moving camera
        float x = (float) (Math.cos(pitchRadians) * Math.cos(yawRadians));
        float y = (float) (Math.sin(pitchRadians));
        float z = (float) (Math.cos(pitchRadians) * Math.sin(yawRadians));

        front.set(x, y, z).normalize();
        right.set(front).cross(up).normalize();
    }

    public void rotate(float changeYaw, float changePitch) {
        yaw += changeYaw * sensitivity;
        pitch += changePitch * sensitivity;
        //clamp pitch so camera doesn't experience dumb issues when looking straight up/down
        pitch = Math.max(-89f, Math.min(89f, pitch));
        updateCameraDirections();
    }

    public Vector3f getFront() {
        return new Vector3f(front);
    }

    public Vector3f getRight() {
        return new Vector3f(right);
    }

    public Vector3f getUp() {
        return new Vector3f(up);
    }
}
