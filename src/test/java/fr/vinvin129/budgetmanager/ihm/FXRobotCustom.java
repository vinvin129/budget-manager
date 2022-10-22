package fr.vinvin129.budgetmanager.ihm;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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

    public void removeWindow(String name) {
        this.windows.remove(name);
    }

    public void type(KeyCode... keysCode) {
        this.robot.type(keysCode);
    }

    public <T> boolean selectItem(String query, T item) {
        ChoiceBox<T> choiceBox = getNode(query);
        int index = choiceBox.getItems().indexOf(item);
        if (index > -1) {
            int indexChosen = choiceBox.getItems().indexOf(choiceBox.getValue());
            this.clickOn(query);
            if (indexChosen < index) {
                for (int i = 0; i < index - indexChosen; i++) {
                    this.type(KeyCode.DOWN);
                }
            } else if (indexChosen > index) {
                for (int i = 0; i < indexChosen-index; i++) {
                    this.type(KeyCode.UP);
                }
            }
            this.type(KeyCode.ENTER);
            return true;
        } else {
            return false;
        }
    }

    public <T extends Node> T getNode(String query) {
        return this.robotContext.getNodeFinder().from(actualNode).lookup(query).query();
    }

    public void clickOnChartDataNode(String chartId, Predicate<? super PieChart.Data> predicate) {
        PieChart chart = getNode(chartId);
        Node dataNode = chart.getData().stream().filter(predicate).findFirst().orElseThrow().getNode();
        this.robot.clickOn(dataNode);
    }
}
