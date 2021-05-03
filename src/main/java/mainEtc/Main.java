package mainEtc;

import models.Maze;
import models.Square;
import models.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

public class Main {

    public static boolean isKeyPressed(long window, int key)
    {
        return (GLFW.glfwGetKey(window, key) == GLFW.GLFW_TRUE);
    }

    public static void main(String[] args) throws Exception {

        long window = Window.windowCreate();
        String source = """
                -0.19999999;-1.0;0.4
                0.20000005;-1.0;0.4
                0.6;-1.0;0.4
                0.6;-0.6;0.4
                -1.0;-0.19999999;0.4
                -0.6;-0.19999999;0.4
                -0.19999999;-0.19999999;0.4
                0.6;-0.19999999;0.4
                -1.0;0.20000005;0.4
                -1.0;0.6;0.4
                -0.6;0.6;0.4
                -0.19999999;0.6;0.4""";
        Maze maze = new Maze(source);
        for (Square square : maze.getBody()) {
            square.init();
        }
        Square player = new Square(0,0,0.4f);

        player.init();
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            if (isKeyPressed(window, GLFW.GLFW_KEY_W)){
                player.setY(player.getY() + 0.0001f);
                System.out.println(player.getX() +", " + player.getY());
                player.update();
            }


            player.render();
            for (Square square : maze.getBody()) {
                square.render();
            }


            // Swap the color buffer -> screen tearing solution
            GLFW.glfwSwapBuffers(window);
            // Listen to input
            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }
}
