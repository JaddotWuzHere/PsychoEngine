package engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f pos;
    private final Vector3f target;
    private final Vector3f up;
    private final float fov;
    private final float aspr;
    private final float near;
    private final float far;

    public Camera(Vector3f pos, Vector3f target, float aspr) {
        this.pos = pos;
        this.target = target;
        this.up = new Vector3f(0, 1, 0);
        this.fov = (float) Math.toRadians(90f);
        this.aspr = aspr;
        this.near = 0.1f;
        this.far = 100f;
    }

    //position: where you are
    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(pos, target, up);
    }

    //projection: how your nearsighted eyeballs see
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov, aspr, near, far);
    }

    public Matrix4f getSuperMatrix(Matrix4f model) {
        return getProjectionMatrix().mul(getViewMatrix().mul(model));
    }

    //move camera
    //TODO: make wasd for camera
    public void move(Vector3f offset) {
        pos.add(offset);
        target.add(offset);
    }

    //camera position getter
    public Vector3f getPosition() {
        return new Vector3f(pos);
    }
}
