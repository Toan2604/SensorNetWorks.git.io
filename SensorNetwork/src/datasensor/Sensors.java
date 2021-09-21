/*
    Lớp các sensor
    Ở đây ta chỉ mặc định các sensor như chấm điểm nhỏ trog vùng triển khai, nên không cần kích thước các sensor
 */
package datasensor;
import java.awt.Point;
import java.io.Serializable;
import main.GiaoTiep;


public class Sensors implements Serializable,GiaoTiep{
    private int ID;             // Định danh sensor
    private Point sPoint;       // Tọa độ các sensor
    private int sR;             // Bán kính của sensor xác định kích thước sensors
    private int radius;         // Bán kính cảm biến của các sensor
    private boolean status;     // Tình trạng hoạt động của sensor: True: Hoạt động, False: Ngủ
    private int age;            // Tuổi thọ pin sensor
    
    // constructor
    public Sensors() {
    }
    
    public Sensors(int id, int Sx, int Sy, int Sr, int radius, boolean sts, int age) {
        this.ID = id;
        sPoint = new Point(Sx, Sy);
        this.sR = Sr;
        this.radius = radius;
        this.status = sts;
        this.age = age;
    }
    public Sensors(int id, Point p, int Sr, int radius, boolean sts, int age) {
        this.ID = id;
        sPoint = new Point(p);
        this.sR = Sr;
        this.radius = radius;
        this.status = sts;
        this.age = age;
    }
    public Sensors(int Sradius, int radiusSensor,int age){
        this.sR = Sradius;
        this.radius = radiusSensor;
        this.age = age;
    }
    //getter and setter
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public Point getSPoint() {
        return sPoint;
    }
    public void setSPoint(Point p) {
        this.sPoint = p;
    }
    public int getSR() {
        return sR;
    }
    public void setSR(int sR) {
        this.sR = sR;
    }
    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(String status) {
        if(status.equalsIgnoreCase("false"))
            this.status = false;
        else
            this.status = true;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }   

    public String getPoint(){
        Point p = getSPoint();
        return "("+String.valueOf(p.x)+","+ String.valueOf(p.y)+")";
    }
    @Override
    public Object[] toObject() {
        return new Object[]{
          this.getID(), this.getPoint(), this.getSR(), this.getRadius(), this.getStatus(), this.getAge()
        };
    }
}

