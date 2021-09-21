
// Lớp vẽ các sensor trong trường hợp triển khai ngẫu nhiên

package areadeployment;

import datasensor.Sensors;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;


public class RandomView extends JComponent{
    Graphics g;
    private Point p;                                        // Tọa độ vùng triển khai
    private int width, height;                              // kích thước vùng triển khai
    private int numberSensor;
    private ArrayList<Sensors> listSensor;                  // dánh sách sensor
    private GridView grid;
    private Sensors sens;
    
    //constructor
    public RandomView(int x, int y, int widht, int height, int numberSensor, int Sradius, int radiusSensor, int age) {
        p = new Point(x, y);
        this.width = widht;
        this.height = height;
        this.numberSensor = numberSensor;
        sens = new Sensors(Sradius, radiusSensor, age);
        listSensor = new ArrayList<>();
        setDataSensor();
        grid = new GridView(x, y, widht, height, numberSensor, Sradius, radiusSensor, age);
    }

    // Tạo danh sách Sensor
    public void setDataSensor(){
        for(int i=0; i<numberSensor; i++){
            int Sx = ThreadLocalRandom.current().nextInt(p.x, p.x+width);
            int Sy = ThreadLocalRandom.current().nextInt(p.y,p.y+height);
            Sensors sens = new Sensors(i+1, Sx, Sy, this.sens.getSR(), this.sens.getRadius(), false, this.sens.getAge());
            try {
                listSensor.add(sens);
            } catch (Exception e) {
                System.out.println("Add fail.\n"+e.toString());
            }
        }

    }
    
    // Vẽ các sensor
    public String paintRandomView(Graphics g){
        String message = null;
        int num = 1;        
        for(Sensors sens: listSensor){
            grid.paintSensor(g, "black", sens);     // Lấy hàm vẽ một sensor trong lớp GridView
            num++;
        }
        if(num<numberSensor)
            message = "\n Paint Fail";
        else
            message = "\n Đã phân bố hết số lượng sensor";
        
        return message;
    }
    
    
    // getter and setter
    public GridView getGrid() {
        return grid;
    }

    public void setGrid(GridView grid) {
        this.grid = grid;
    }
    
    public Point getP() {
        return p;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNumberSensor() {
        return numberSensor;
    }

    public void setNumberSensor(int numberSensor) {
        this.numberSensor = numberSensor;
    }
    public ArrayList<Sensors> getListSensor() {
        return listSensor;
    }

    public void setListSensor(ArrayList<Sensors> listSensor) {
        this.listSensor = listSensor;
    }
}
