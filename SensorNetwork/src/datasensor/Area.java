/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasensor;

import java.awt.Point;
import java.io.Serializable;
import main.GiaoTiep;


public class Area implements Serializable,GiaoTiep{
    private Point point;                    // Toạ độ vùng triển khai
    private int Awidth, Aheight;            // kích thước của vùng triển khai: chiều dài x chiều rộng
    private int numberSensor;               // Số lượng sensor
    private int numberObject;               // số lượng đối tượng

    public Area(Point point, int Awidth, int Aheight, int numberSensor) {
        this.point = new Point(point);
        this.Awidth = Awidth;
        this.Aheight = Aheight;
        this.numberSensor = numberSensor;
    }
    public Area(int Ax, int Ay, int Awidth, int Aheight, int numObject) {
        Point p = new Point(Ax, Ay);
        this.point = p;
        this.Awidth = Awidth;
        this.Aheight = Aheight;
        this.numberObject = numObject;
    }
     public Area(Point point, int Awidth, int Aheight, int numberSensor, int numObject) {
        this.point = new Point(point);
        this.Awidth = Awidth;
        this.Aheight = Aheight;
        this.numberSensor = numberSensor;
        this.numberObject = numObject;
    }
    public Area(){}

    public Area(Point point, int Awidth, int Aheight) {
        this.point = point;
        this.Awidth = Awidth;
        this.Aheight = Aheight;
    }

    public Point getPoint() {
        return point;
    }
    public String getAPoint(){
        Point p = getPoint();
        return "("+String.valueOf(p.x)+","+ String.valueOf(p.y)+")";
    }
    public void setPoint(Point p) {
        this.point = p;
    }
    public String getDimention(){
        int width = getAwidth();
        int height = getAheight();
        return "("+String.valueOf(width)+"x"+String.valueOf(height)+")";
    }
    public int getAwidth() {
        return Awidth;
    }

    public void setAwidth(int Awidth) {
        this.Awidth = Awidth;
    }

    public int getAheight() {
        return Aheight;
    }

    public void setAheight(int Aheight) {
        this.Aheight = Aheight;
    }
    
    public int getNumberSensor() {
        return numberSensor;
    }

    public void setNumberSensor(int numberSensor) {
        this.numberSensor = numberSensor;
    }

    public int getNumberObject() {
        return numberObject;
    }

    public void setNumberObject(int numberObject) {
        this.numberObject = numberObject;
    }



    @Override
    public Object[] toObject() {
        return new Object[]{
          this.getAPoint(), this.getDimention(), this.getNumberSensor(), this.getNumberObject()
        };
        
    }
    
    
    
}
