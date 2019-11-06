package cst232;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public AnchorPane PartitionPane, FixedSchemePane, DynamicSchemePane, MenuMainPane;
    public StackPane PartitionStack, parentContainer1;
    public JFXComboBox<String> fixedMemoryBox, fixedStyle, dynamicStyle;
    public JFXComboBox<String> dynamicMemoryBox;
    public List<String> StyleList= Arrays.asList("First-Fit", "Best-Fit", "Worst-Fit");
    public List<String> MemoryList= Arrays.asList("50000", "40000", "30000", "20000");
    public List<String> BlockList= Arrays.asList("10", "7", "5", "3");
    public Label fixedJobField, fixedJobField1;
    public JFXButton nextButton;
    public Label jobFilePath, memoryFilePath;

    public File memoryList, jobList;
    public int arrayjob[][], arrayblocksize[];

    public void initialize(){

    }

    public boolean importData() {
        try (Scanner scanner = new Scanner(new File(jobFilePath.getText()))) {
            Integer total = scanner.nextInt();
            arrayjob = new int[total][4];
            Main.Data.setTotal(total.toString());
            Integer i = 0;
            while (scanner.hasNext()) {
                arrayjob[i][0] = scanner.nextInt();
                arrayjob[i][1] = scanner.nextInt();
                arrayjob[i][2] = scanner.nextInt();
                arrayjob[i][3] = scanner.nextInt();
                i++;
            }
            Main.Data.setInteger(arrayjob);
        } catch (FileNotFoundException a) { a.printStackTrace(); }

        try (Scanner scanner = new Scanner(new File(memoryFilePath.getText()))) {
            Integer total2 = scanner.nextInt();
            arrayblocksize = new int[total2];
            Integer i = 0;
            while (scanner.hasNext()) {
                arrayblocksize[i] = scanner.nextInt(); i++;
            }
            Main.Data.setTotal2(total2.toString());
            Main.Data.setBlock(arrayblocksize);
        } catch (FileNotFoundException a) {
            a.printStackTrace();
        }
        return false;
    }

    public void chooseMemoryTxt() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            memoryList = jfc.getSelectedFile();
            memoryFilePath.setText(memoryList.getAbsolutePath());
        }
    }

    public void chooseJobTxt() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            jobList = jfc.getSelectedFile();
            jobFilePath.setText(jobList.getAbsolutePath());
        }
    }

    public void nextMenu(ActionEvent event) {
        if(importData()) return;
        new changeScene(MenuMainPane, parentContainer1, "MenuSelection.fxml", 1, 1);

    }
    public void exitProgram(){
        System.exit(0);
    }

    public void initializeArray(){
        fixedMemoryBox.getItems().addAll(BlockList);
        fixedMemoryBox.setValue("10");
        dynamicMemoryBox.getItems().addAll(MemoryList);
        dynamicMemoryBox.setValue("50000");
        fixedStyle.getItems().addAll(StyleList);
        fixedStyle.setValue("First-Fit");
        dynamicStyle.getItems().addAll(StyleList);
        dynamicStyle.setValue("First-Fit");
    }

    public void runFixedSimulate(ActionEvent event)  {
        Main.Data.setBlockSize(Integer.parseInt(fixedMemoryBox.getValue()));
        Main.Data.setStyle(fixedStyle.getValue());
        new changeScene(PartitionPane, PartitionStack, "FixedSimulation.fxml", 1, 2 );
    }

    public void runDynamicSimulate(ActionEvent event){
        Main.Data.setDynamicMemory(Integer.parseInt(dynamicMemoryBox.getValue()));
        Main.Data.setStyle(dynamicStyle.getValue());
        new changeScene(PartitionPane, PartitionStack, "DynamicSimulation.fxml", 1, 3 );
    }



    public void backMenu(MouseEvent event){
        new changeScene(PartitionPane, PartitionStack, "Menu.fxml", 0);
    }

    public void fixedSchemeButton(ActionEvent event) {
        fixedJobField.setText(Main.Data.getTotal());
        FixedSchemePane.setVisible(true);
    }
    public void dynamicSchemeButton(ActionEvent event) {
        fixedJobField1.setText(Main.Data.getTotal());
        DynamicSchemePane.setVisible(true);
    }
    public void fixedBack(MouseEvent event){ FixedSchemePane.setVisible(false); }
    public void dynamicBack(MouseEvent event){
        DynamicSchemePane.setVisible(false);
    }


}
