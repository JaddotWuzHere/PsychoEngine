#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;

uniform mat4 uMatrix;

out vec3 vColor;

void main() {
    gl_Position = uMatrix * vec4(aPos, 1.0);
    vColor = aColor;
}