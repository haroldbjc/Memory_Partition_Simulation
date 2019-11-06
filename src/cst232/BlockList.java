package cst232;

import javafx.scene.shape.Rectangle;
import jdk.nashorn.internal.ir.Block;

public class BlockList {
    public double start;
    public double end;
    public int jobNo;
    public Rectangle rect;

    public int getJobNo() {
        return jobNo;
    }

    public void setJobNo(int jobNo) {
        this.jobNo = jobNo;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void setRect(int x) {
        this.rect = null;
    }

    public double getSize(){
        return  (end - start);
    }

    BlockList(){
        start = 0;
        end = Main.Data.getDynamicMemory();
        rect = null;
        jobNo = 0;
    }
}
