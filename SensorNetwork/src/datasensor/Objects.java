/*
   Lớp các đối tượng cần theo dõi
 */
package datasensor;

import java.awt.Point; 
import java.io.Serializable;
import main.GiaoTiep;

public class Objects implements Serializable,GiaoTiep{
    private int ID;             // Định danh vật
    private Point oPoint;       // Tọa độ  của đối tượng
    private int radius;         // bán kính xác định kích thước đối tượng
    private String color;       // màu sắc đối tượng
    
    // constructor
    public Objects(){}
    public Objects(int ID, int x, int y, int radius, String color){
        this.ID = ID;
        oPoint = new Point(x,y);
        this.radius=radius;
        this.color = color;
    }
    public Objects(int radius, String color){
        this.radius=radius;
        this.color = color;
    }
    //getter and setter
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }   
    public Point getOpoint() {
        return oPoint;
    }
    public void setOpoint(Point p) {
        this.oPoint = p;
    }
    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
 
    public String getPoint(){
        Point p = getOpoint(); 
        return "("+String.valueOf(p.x)+","+ String.valueOf(p.y)+")";
    }
    @Override
    public Object[] toObject() {
        return new Object[]{
          this.getID(), this.getPoint(), this.getRadius(), this.getColor()
        };
    }
}
