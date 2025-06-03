package engine;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL33.*;

public class FpsCounter {
    private long lastTime;
    private int frameCount = 0;
    private float timeCount = 0;
    private int currentFPS = 0;

    public FpsCounter() {
        lastTime = System.nanoTime();
    }

    //records how many frames were rendered every second
    public float getFPS() {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
        lastTime = currentTime;
        timeCount += deltaTime;
        frameCount++;
        //if 1 second passed, record fps and then reset time
        if (timeCount >= 1f) {
            currentFPS = frameCount;
            frameCount = 0;
            timeCount -= 1f;
        }
        return currentFPS;
    }

    private static final int BITMAP_W = 512;
    private static final int BITMAP_H = 512;
    private static final int FONT_HEIGHT = 24;

    private int fontTextureID;
    private STBTTBakedChar.Buffer charData;

    //TODO: figure out wtf everything below does
    //initialize the text
    public void initText() throws IOException {
        ByteBuffer ttf;
        try (FileChannel fc = FileChannel.open(Path.of("src/main/resources/OpenSans-Regular.ttf"), StandardOpenOption.READ)) {
            ttf = ByteBuffer.allocateDirect((int) fc.size());
            fc.read(ttf);
            ttf.flip();
        }

        charData = STBTTBakedChar.malloc(96);
        ByteBuffer bitmap = ByteBuffer.allocateDirect(BITMAP_W * BITMAP_H);

        STBTruetype.stbtt_BakeFontBitmap(ttf, FONT_HEIGHT, bitmap, BITMAP_W, BITMAP_H, 32, charData);

        fontTextureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTextureID);

        glTexImage2D(GL_TEXTURE_2D,
                0,
                GL_ALPHA,
                BITMAP_W,
                BITMAP_H,
                0,
                GL_RED,
                GL_UNSIGNED_BYTE,
                bitmap);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }

    //displays fps on screen
    public void renderText(String text, float startX, float startY) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, fontTextureID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xBuf = stack.floats(startX);
            FloatBuffer yBuf = stack.floats(startY);
            STBTTAlignedQuad quad = STBTTAlignedQuad.malloc(stack);

            glBegin(GL_QUADS);

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c < 32 || c >= 128) continue;

                STBTruetype.stbtt_GetBakedQuad(charData, BITMAP_W, BITMAP_H, c - 32, xBuf, yBuf, quad, true);

                glTexCoord2f(quad.s0(), quad.t0()); glVertex2f(quad.x0(), quad.y0());
                glTexCoord2f(quad.s1(), quad.t0()); glVertex2f(quad.x1(), quad.y0());
                glTexCoord2f(quad.s1(), quad.t1()); glVertex2f(quad.x1(), quad.y1());
                glTexCoord2f(quad.s0(), quad.t1()); glVertex2f(quad.x0(), quad.y1());
            }

            glEnd();
        }
    }

}
