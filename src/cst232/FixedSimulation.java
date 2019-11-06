package cst232;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import java.util.LinkedList;



public class FixedSimulation {

    public AnchorPane FixedSimulate;
    public StackPane FixedSimulateStack;
    public JFXButton runButton;
    public boolean runSimulation = true;

    public int blockArray[] = Main.Data.getBlock().clone(); //partition memory size
    public int jobMemory[][] = Main.Data.Integer.clone(); //job details
    public Rectangle[] blocks = new Rectangle[Main.Data.getBlockSize()];
    public double[][] segmentX = new double[Main.Data.getBlockSize()][2];
    public double[] jobSize = new double[Integer.parseInt(Main.Data.getTotal())];
    public Rectangle[] jobRectangle = new Rectangle[40];
    public int timeCount = -1;
    public int po = 0;
    public double[] partitionWidth = new double[Main.Data.getBlockTotal()];
    public LinkedList<BlockList> queue = new LinkedList<>();
    public LinkedList<BlockList> wait = new LinkedList<>();
    public boolean runAdd = true;
    public Label timeTotal, timeTotal1;
    public int internalFragmentation = 0;
    public int jobDone = 0;


    public void initialize() {

        double x2 = 15;
        for (int i = 0; i < Main.Data.getBlockSize(); i++) {
            Rectangle rect = new Rectangle();
            rect.setHeight(81);
            rect.setWidth(870 * blockArray[i] / Main.Data.getBlockTotal());
            rect.setFill(Color.RED);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(3);
            rect.setStrokeType(StrokeType.INSIDE);
            x2 += (rect.getWidth() / 2);
            addInQueue(0, 0, x2, 0, rect);
            x2 += (rect.getWidth() / 2);
            segmentX[i][0] = x2;
            partitionWidth[i] = x2;
        }

        for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
            Rectangle rect = new Rectangle();
            rect.setHeight(81);
            rect.setWidth(870 * jobMemory[i][3] / Main.Data.getBlockTotal());
            rect.setFill(Color.BLUE);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(3);
            rect.setStrokeType(StrokeType.INSIDE);
            jobRectangle[i] = rect;
            jobSize[i] = rect.getWidth();

        }
        for (int o = 0; o < Main.Data.getBlockSize(); o++) segmentX[o][1] = 0;
        timeTotal.setText(Integer.toString(0));
        timeTotal1.setText(Integer.toString(0));
    }

    //    public void addJob(ActionEvent event){
//
//    }
    public void addJob(ActionEvent event) {
        runSimulation = true;
        if (runAdd == false) return;
        while(runSimulation) {
            for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
                if (jobMemory[i][2] == 0) {
                    for (int j = 0; j < Main.Data.getBlockSize(); j++) {
                        if (jobMemory[i][0] == segmentX[j][1]) {
                            deleteJob(blocks[j]);
                            jobMemory[i][2]--;
                            blocks[j] = null;
                            segmentX[j][1] = 0;
                            internalFragmentation+=(blockArray[j]- jobMemory[i][3]);
                            jobDone++;
                            timeTotal1.setText(Integer.toString(internalFragmentation/jobDone));
                            //System.out.println(internalFragmentation);
                            //System.out.println(jobDone);
                            //System.out.println(internalFragmentation/jobDone);
                        }
                    }
                } else if ((jobMemory[i][2] > 0) && (jobMemory[i][1] == -2)) {
                    jobMemory[i][2]--;
                }
            }
            po = 0;
            if (!queue.isEmpty()) {
                for (int i = 0; i < queue.size(); i++) {
                    int b = queue.get(i).getJobNo();
                    int a = fit(b - 1);
                    if (a != -1) {
                        b--;
                        if (a == 0) {
                            po += (jobSize[b] / 2);
                        } else po += (partitionWidth[a - 1] + (jobSize[b] / 2) - 15);
                        move(po, jobRectangle[b]);
                        jobMemory[b][1] = -2;
                        jobMemory[b][2]--;
                        segmentX[a][1] = jobMemory[b][0];
                        blocks[a] = jobRectangle[b];
                        queue.remove(i);
                        FixedSimulate.getChildren().remove(wait.get(i).getRect());
                        wait.remove(i);
                        i--;
                        po = 0;
                        reArrage();
                    }
                }
            }
            for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
                po = 0;
                if (jobMemory[i][1] == 0) {
                    int a = fit(i);
                    if (a != -1) {
                        if (a == 0) {
                            po += (jobSize[i] / 2);
                        } else po += (partitionWidth[a - 1] + (jobSize[i] / 2) - 15);
                        move(po, jobRectangle[i]);
                        jobMemory[i][1] = -2;
                        jobMemory[i][2]--;
                        segmentX[a][1] = jobMemory[i][0];
                        blocks[a] = jobRectangle[i];
                    } else {
                        jobMemory[i][1] = -1;
                        BlockList obj = new BlockList();
                        obj.setRect(jobRectangle[i]);
                        obj.setEnd(jobSize[i]);
                        obj.setJobNo(i+1);
                        queue.addLast(obj);
                        waitingList();
                    }
                } else if (jobMemory[i][1] > 0) jobMemory[i][1]--;
            }

            timeCount++;
            for (int i = 0; i < Main.Data.getBlockSize(); i++) {
                if (blocks[i] != null || (timeCount < 20)) {
                    runAdd = true;
                    break;
                } else runAdd = false;
            }
            timeTotal.setText(Integer.toString(timeCount));
            if(runAdd == false) break;
        }
    }

    public void addInQueue(double x1, double y1, double x2, double y2, Rectangle root) {
        FixedSimulateStack.getChildren().add(root);
        root.translateXProperty().set(x1);
        root.translateYProperty().set(y1);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), -450 + x2, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(root.translateYProperty(), -203, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        timeline.getKeyFrames().add(kf);
        timeline.getKeyFrames().add(kf2);
        timeline.play();
    }

    public void deleteJob(Rectangle root) {

        root.translateYProperty().set(-203);
        Timeline timeline = new Timeline();
        KeyValue kv2 = new KeyValue(root.translateYProperty(), -300, Interpolator.EASE_IN);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        timeline.getKeyFrames().add(kf2);
        timeline.setOnFinished(event -> {
            FixedSimulateStack.getChildren().remove(root);
        });
        timeline.play();
        runSimulation = false;
    }

    public int fit(int job_index) {
        String a = Main.Data.getStyle();
        if (a.equals("First-Fit")) {
            for (int i = 0; i < Main.Data.getBlockSize(); i++) {

                if ((segmentX[i][1] == 0) && (jobMemory[job_index][3] <= blockArray[i])) return i;
            }
            return -1;
        }
        else if(a.equals("Best-Fit")){
            double p = 14000;
            int y = -1;
            for (int i = 0; i < Main.Data.getBlockSize(); i++) {
                if ((segmentX[i][1] == 0) && (jobMemory[job_index][3] <= blockArray[i])){
                    if((blockArray[i]-jobMemory[job_index][3])<p){
                        p=((blockArray[i]-jobMemory[job_index][3]));
                        y = i;
                    }
                }
            }
            return y;

        }
        else if(a.equals("Worst-Fit")){
            double p = -14000;
            int y = -1;
            for (int i = 0; i < Main.Data.getBlockSize(); i++) {
                if ((segmentX[i][1] == 0) && (jobMemory[job_index][3] <= blockArray[i])){
                    if((blockArray[i]-jobMemory[job_index][3])>p){
                        p=((blockArray[i]-jobMemory[job_index][3]));
                        y = i;
                    }
                }
            }
            return y;

        }
        return 10;
    }

    public void move(double x1, Rectangle root) {
        FixedSimulateStack.getChildren().add(root);
        root.translateXProperty().set(0);
        root.translateYProperty().set(300);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), -435 + x1, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(root.translateYProperty(), -203, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        timeline.getKeyFrames().add(kf);
        timeline.getKeyFrames().add(kf2);
        timeline.play();
        runSimulation = false;
    }

    public void waitingList() {
        Rectangle root = new Rectangle();
        BlockList obj = new BlockList();
        root.setHeight(80);
        root.setWidth(30);
        root.setFill(Color.BLUE);
        root.setStroke(Color.BLACK);
        root.setStrokeWidth(3);
        root.setStrokeType(StrokeType.INSIDE);
        obj.setRect(root);
        double x2 = 15;
        wait.addLast(obj);
        for(int i = 0; i < wait.size()-1; i++){
            x2+=30;
        }
        root.setLayoutX(x2);
        root.setLayoutY(400);
        FixedSimulate.getChildren().add(root);
    }

    public void reArrage(){
        Rectangle root;
        double x1;
        for(int i = 0; i < wait.size();i++){
            root = wait.get(i).getRect();
            x1 = 15;
            for(int j = 0; j < i; j++){
                x1+=30;
            }
            root.setLayoutX(x1);
            root.setLayoutY(400);
        }

    }


    public void exitProgram() {
        System.exit(0);
    }


}
