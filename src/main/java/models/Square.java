package models;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {
    private final int[] indices = {
            0, 1, 3,
            1, 2, 3
    };
    private final float[] verticies;

    private float x;
    private float y;
    private int vaoId;
    private int vboId;
    private int eboId;
    private float sideSize;

    public Square(float x, float y, float sideSize) {
        this.vaoId = GL33.glGenVertexArrays();
        this.vboId = GL33.glGenBuffers();
        this.eboId = GL33.glGenBuffers();
        this.sideSize = sideSize;
        this.x = x;
        this.y = y;
        /*
        this.verticies = new float[]{
                x + sideSize, y + sideSize  , 0.0f, // 0 -> Top right
                x + sideSize, y             , 0.0f, // 1 -> Bottom right
                x           , y             , 0.0f, // 2 -> Bottom left
                x           , y + sideSize  , 0.0f, // 3 -> Top left
        };*/

        this.verticies = new float[]{
                x + sideSize, - y - sideSize  , 0.0f, // 0 -> Top right
                x + sideSize, - y             , 0.0f, // 1 -> Bottom right
                x           , - y             , 0.0f, // 2 -> Bottom left
                x           , - y - sideSize  , 0.0f, // 3 -> Top left
        };
    }

    public float getSideSize() {
        return sideSize;
    }

    public void setSideSize(float sideSize) {
        this.sideSize = sideSize;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getVaoId() {
        return vaoId;
    }

    public void setVaoId(int vaoId) {
        this.vaoId = vaoId;
    }

    public int getVboId() {
        return vboId;
    }

    public void setVboId(int vboId) {
        this.vboId = vboId;
    }

    public int getEboId() {
        return eboId;
    }

    public void setEboId(int eboId) {
        this.eboId = eboId;
    }

    public void render() {
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(this.vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public void update() {
        FloatBuffer fb = BufferUtils.createFloatBuffer(this.verticies.length)
                .put(verticies)
                .flip();

        MemoryUtil.memFree(fb);
    }

    public void init() {
        //Shaders.initShaders();
        //int colorLoc = GL33.glGetUniformLocation(Shaders.shaderProgramId, "outColor");


        GL33.glBindVertexArray(this.vaoId);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, this.eboId);
        IntBuffer ib = BufferUtils.createIntBuffer(this.indices.length)
                .put(this.indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, this.vboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(this.verticies.length)
                .put(verticies)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);

        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        MemoryUtil.memFree(fb);
        // Change to Color...
        // Tell OpenGL we are currently writing to this buffer (colorsId)
       // GL33.glUseProgram(Shaders.shaderProgramId);
       // GL33.glUniform3f(colorLoc, 1.0f, 1.0f, 0.0f);
    }


}

