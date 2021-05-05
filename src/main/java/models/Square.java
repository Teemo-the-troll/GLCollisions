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
    private final int vaoId;
    private final int vboId;
    private final int eboId;
    private final float sideSize;
    private float[] verticies;
    private float x;
    private float y;

    public Square(float x, float y, float sideSize) {
        this.vaoId = GL33.glGenVertexArrays();
        this.vboId = GL33.glGenBuffers();
        this.eboId = GL33.glGenBuffers();
        this.sideSize = sideSize;
        this.x = x;
        this.y = y;
        this.verticies = vertsFromCoor(x, y, sideSize);
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

    public void render(boolean shader) {
        if (shader)
            GL33.glUseProgram(Shaders.shaderProgramId);

        // Draw using the glDrawElements function
        GL33.glBindVertexArray(this.vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public float[] vertsFromCoor(float x, float y, float sideSize) {
        //TODO remove the - at Y to revert changes
        return new float[]{
                x + sideSize, -y, 0.0f, // 0 -> Top right
                x + sideSize, -y -sideSize, 0.0f, // 1 -> Bottom right
                x, -y - sideSize, 0.0f, // 2 -> Bottom left
                x, -y , 0.0f, // 3 -> Top left
        };

        /*
        * return new float[]{
                x + sideSize, y + sideSize, 0.0f, // 0 -> Top right
                x + sideSize, y, 0.0f, // 1 -> Bottom right
                x, y, 0.0f, // 2 -> Bottom left
                x, y + sideSize, 0.0f, // 3 -> Top left
        };
*/

    }

    public void update() {
        float[] newVerts = vertsFromCoor(this.x, this.y, this.sideSize);
        this.verticies = newVerts;
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);
        GL33.glBufferSubData(GL33.GL_ARRAY_BUFFER, 0, newVerts);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0); // 0 idea what this does but ok
    }

    private boolean isBetween(float toCompare, float smaller, float bigger) {
        return ((smaller <= toCompare) && (toCompare <= bigger));
    }

    public boolean colides(Square square) {
        float maxAcceptX = square.x + square.sideSize;
        float maxAcceptY = square.y + square.sideSize;
        float minAcceptX = square.x;
        float minAcceptY = square.y;

        return isBetween(this.x, minAcceptX, maxAcceptX) && isBetween(this.y, minAcceptY, maxAcceptY) ||
                isBetween(this.x + this.sideSize, minAcceptX, maxAcceptX) && isBetween(this.y, minAcceptY, maxAcceptY) ||
                isBetween(this.x, minAcceptX, maxAcceptX) && isBetween(this.y + this.sideSize, minAcceptY, maxAcceptY) ||
                isBetween(this.x + this.sideSize, minAcceptX, maxAcceptX) && isBetween(this.y + this.sideSize, minAcceptY, maxAcceptY);
    }

    public void rawInit() {
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

    }

    public void init() {
        Shaders.initShaders();
        int colorLoc = GL33.glGetUniformLocation(Shaders.shaderProgramId, "inColor");

        rawInit();
        // Change to Color...
        // Tell OpenGL we are currently writing to this buffer (colorsId)
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glUniform3f(colorLoc, 0.1f, 0.5f, 0.6f);
    }


}

