package fr.vinvin129.budgetmanager.ihm;

import javafx.scene.Node;
import javafx.stage.Window;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotContext;

import java.util.HashMap;
import java.util.Map;

public class FXRobotCustom {
    private final FxRobot robot;
    private final FxRobotContext robotContext;
    private final Map<String, Window> windows = new HashMap<>();
    private Node actualNode;

    public FXRobotCustom(FxRobot robot) {
        this.robot = robot;
        this.robotContext = robot.robotContext();
        Window w = this.robotContext.getWindowFinder().targetWindow();
        this.actualNode = this.robotContext.getNodeFinder().rootNode(w);
        windows.put("root", w);
    }

    public void clickOn(String query) {
        this.robot.clickOn(actualNode.lookup(query));
    }

    public void write(String text) {
        this.robot.write(text);
    }

    public void newWindowCreated(String name) {
        Window newWindow = robotContext.getWindowFinder().listTargetWindows().stream().filter(window -> !windows.containsValue(window)).findFirst().orElseThrow();
        this.windows.put(name, newWindow);
    }

    public void changeWindow(String name) {
        this.actualNode = this.robotContext.getNodeFinder().rootNode(this.windows.get(name));

    }

    public void changeToNewWindowCreated(String name) {
        newWindowCreated(name);
        changeWindow(name);
    }
}
