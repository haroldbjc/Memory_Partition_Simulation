package cst232;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.LinkedList;

public class DynamicSimulation {

        public JFXButton runButton;
        public StackPane DynamicSimulateStack;
        public AnchorPane DynamicSimulate;
        public Label timeTotal;
        public double[] jobSize = new double[Integer.parseInt(Main.Data.getTotal())];
        public Rectangle[] jobRectangle = new Rectangle[40];
        public int jobMemory[][] = Main.Data.Integer.clone(); //job details
        public LinkedList<BlockList> list = new LinkedList<>();
        public LinkedList<Integer> queue = new LinkedList<Integer>();
        public boolean runSimulation = true;

        public int timeCount = -1;
        public double po = 0;
        public double[] partitionWidth = new double[Main.Data.getBlockTotal()];

        public boolean runAdd = true;


        public void initialize(){

            for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
                Rectangle rect = new Rectangle();
                rect.setHeight(81);
                rect.setWidth((870* jobMemory[i][3])/ Main.Data.getDynamicMemory());
                rect.setFill(Color.BLUE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(3);
                rect.setStrokeType(StrokeType.INSIDE);
                jobRectangle[i] = rect;
                jobSize[i]= rect.getWidth();

            }
            BlockList obj = new BlockList();
            list.add(obj);

        }

    public void addJob(ActionEvent event) {
        runSimulation = true;
        if (runAdd == false) return;
        while(runSimulation) {
            for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
                if (jobMemory[i][2] == 0) {
                    for (int j = 0; j < list.size(); j++) {
                        if (jobMemory[i][0] == list.get(j).getJobNo()) {
                            deleteJob(list.get(j).getRect());
                            jobMemory[i][2]--;
                            list.get(j).setRect(0);
                            list.get(j).setJobNo(0);
                        }
                    }
                } else if ((jobMemory[i][2] > 0) && (jobMemory[i][1] == -2)) {
                    jobMemory[i][2]--;
                }
            }
            if(list.size() > 1) {
                for (int i = 0; i < list.size()-1; i++) {
                    if((list.get(i).getRect() == null) && (list.get(i+1).getRect() == null)){
                        list.get(i).setEnd(list.get(i+1).getEnd());
                        list.remove(i+1);
                        i--;
                    }
                }
            }
            po = 0;
            if (!queue.isEmpty()) {
                for (int i = 0; i < queue.size(); i++) {
                    int b = queue.get(i);
                    int a = fit(b - 1);
                    if (a != -1) {
                        b--;

                        jobMemory[b][1] = -2;
                        jobMemory[b][2]--;
                        list.get(a).setRect(jobRectangle[b]);
                        list.get(a).setJobNo(jobMemory[b][0]);
                        BlockList obj = new BlockList();
                        obj.setEnd(list.get(a).getEnd());
                        list.get(a).setEnd(list.get(a).getStart()+jobMemory[b][3]);
                        obj.setStart(list.get(a).getEnd()+1);
                        list.add(a+1, obj);
                        po = ((870*list.get(a).getStart()/Main.Data.getDynamicMemory())+(list.get(a).getRect().getWidth()/2));
                        move(po, jobRectangle[b]);
                        queue.remove(i);
                        i--;
                    }
                }
            }
            if(list.size() > 1) {
                for (int i = 0; i < list.size()-1; i++) {
                    if((list.get(i).getRect() == null) && (list.get(i+1).getRect() == null)){
                        list.get(i).setEnd(list.get(i+1).getEnd());
                        list.remove(i+1);
                        i--;
                    }
                }
            }
            for (int i = 0; i < Integer.parseInt(Main.Data.getTotal()); i++) {
                po = 0;
                if (jobMemory[i][1] == 0) {
                    int a = fit(i);
                    if (a != -1) {
                        jobMemory[i][1] = -2;
                        jobMemory[i][2]--;
                        list.get(a).setRect(jobRectangle[i]);
                        list.get(a).setJobNo(jobMemory[i][0]);

                        if(list.get(a).getEnd() < Main.Data.getDynamicMemory()){
                            BlockList obj = new BlockList();
                            list.get(a).setEnd(list.get(a).getStart()+jobMemory[i][3]);
                            obj.setStart(list.get(a).getEnd()+1);
                            obj.setEnd(list.get(a+1).getStart()-1);
                            list.add(a+1, obj);
                        }
                        else {
                            list.get(a).setEnd(list.get(a).getStart()+jobMemory[i][3]);
                            BlockList obj = new BlockList();
                            obj.setStart(list.get(a).getEnd()+1);
                            obj.setEnd(Main.Data.getDynamicMemory());
                            list.addLast(obj);
                        }
                        po = ((870*list.get(a).getStart()/Main.Data.getDynamicMemory())+(list.get(a).getRect().getWidth()/2));
                        move(po, jobRectangle[i]);
                    } else {
                        jobMemory[i][1] = -1;
                        queue.addLast(jobMemory[i][0]);
                    }
                } else if (jobMemory[i][1] > 0) jobMemory[i][1]--;
            }
            timeCount++;
            if(list.get(0).getSize() == Main.Data.getDynamicMemory())runAdd = false;
            timeTotal.setText(Integer.toString(timeCount));
            if(runAdd == false) break;
        }
    }

    public void deleteJob(Rectangle root) {

        root.translateYProperty().set(-203);
        Timeline timeline = new Timeline();
        KeyValue kv2 = new KeyValue(root.translateYProperty(), -300, Interpolator.EASE_IN);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        timeline.getKeyFrames().add(kf2);
        timeline.setOnFinished(event -> {
            DynamicSimulateStack.getChildren().remove(root);
        });
        timeline.play();
        runSimulation = false;
    }

    public int fit(int job_index) {
        String a = Main.Data.getStyle();
        if (a.equals("First-Fit")) {
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i).getRect() == null) && (jobMemory[job_index][3] <= (list.get(i).getSize()))) {return i;}
            }
            return -1;
        }
        else if(a.equals("Best-Fit")){
            double p = Main.Data.getDynamicMemory();
            int y = -1;
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i).getRect() == null) && (jobMemory[job_index][3] <= (list.get(i).getSize()))){
                    if(((list.get(i).getSize())-jobMemory[job_index][3])<p){
                        p=(((list.get(i).getSize())-jobMemory[job_index][3]));
                        y = i;
                    }
                }
            }
            return y;

        }
        else if(a.equals("Worst-Fit")){
            double p = -Main.Data.getDynamicMemory();
            int y = -1;
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i).getRect() == null) && (jobMemory[job_index][3] <= (list.get(i).getSize()))){
                    if(((list.get(i).getSize())-jobMemory[job_index][3])>p){
                        p=(((list.get(i).getSize())-jobMemory[job_index][3]));
                        y = i;
                    }
                }
            }
            return y;

        }
        return 10;
    }

    public void move(double x1, Rectangle root) {
            try {
                DynamicSimulateStack.getChildren().add(root);
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
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                System.out.println(root);
            }
    }
    public void exitProgram() {
        System.exit(0);
    }
}
