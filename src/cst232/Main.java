package cst232;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Memory Partition Simulation");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    public static class Data{
        public static int Integer [][];
        public static String total;
        public static String total2;
        public static int Block [];
        public static int BlockSize;
        public static String fit;
        public static String style;
        public static int dynamicMemory;

        public static int getDynamicMemory() {
            return dynamicMemory;
        }

        public static void setDynamicMemory(int dynamicMemory) {
            Data.dynamicMemory = dynamicMemory;
        }

        public static String getStyle() {
            return style;
        }

        public static void setStyle(String style) {
            Data.style = style;
        }

        public static String getFit() {
            return fit;
        }

        public static void setFit(String fit) {
            Data.fit = fit;
        }

        public static int getBlockSize() {
            return BlockSize;
        }

        public static void setBlockSize(int blockSize) {
            BlockSize = blockSize;
        }
        public static int getBlockTotal(){
            int a = 0;
            for(int i = 0; i < BlockSize;i++){

                a+=Block[i];
            }
            return a;
        }
        public static void setInteger(int[][] integer) { Integer = integer; }

        public static void setTotal(String total) {
            Data.total = total;
        }

        public static String getTotal() {
            return total;
        }

        public static int[][] getInteger() {
            return Integer;
        }

        public static void setBlock(int[] block) {
            Block = block;
        }

        public static int[] getBlock() {
            return Block;
        }

        public static String getTotal2() {
            return total2;
        }

        public static void setTotal2(String total2) {
            Data.total2 = total2;
        }
    }

    public static void cout(String a){
        System.out.println(a);
    }
}
