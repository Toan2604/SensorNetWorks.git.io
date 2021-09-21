
// Lớp vẽ các sensor trong trường hợp phân bố đồng đều

package areadeployment;


import datasensor.Area;
import datasensor.Sensors;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Shape;
import java.lang.reflect.Field;

public class GridView extends JComponent{
    Graphics g;
    private Sensors sens;
    private int numberSensor;
    private ArrayList<Sensors> listSensor;
    private Area area;
    
    public GridView(int x, int y, int width, int height, int numberSensor, int Sradius, int radiusSensor,int age) {
        Point p = new Point(x,y);
        area = new Area(p, width, height, numberSensor);
        this.numberSensor = numberSensor;
        sens = new Sensors(Sradius, radiusSensor, age);
        listSensor = new ArrayList<>();
        setDataSensor();
    }

    // Tạo danh sách Sensor
    public void setDataSensor(){
        int dX = (int) (area.getAwidth()/Math.sqrt(area.getNumberSensor()));
        int dY = (int) (area.getAheight()/Math.sqrt(area.getNumberSensor()));
        //  tinh toa do dau tien cua sensor
        int x = (int)(dX%area.getAwidth());
        int y = (int)(dY%area.getAheight());
        int numSensor = 0;  // dem so luong sensor dc ve
        for (int i = x/2; i <= area.getAwidth(); i+=x)
            for(int j = y/2; j <= area.getAheight(); j+=y){
            int Sx = i+area.getPoint().x;
            int Sy = j+area.getPoint().y;
            
            Sensors sens = new Sensors(numSensor+1, Sx, Sy, this.sens.getSR(), this.sens.getRadius(), false, this.sens.getAge());
            try {
                listSensor.add(sens);
            } catch (Exception e) {
                System.out.println("Add Sensor fail.\\n\"+e.toString()+\"\\nPlease try again!");
            }
            if(numSensor==numberSensor-1){
                break;
            }
            else
                numSensor++;                
        }
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public void paintSensor(Graphics g, String c, Sensors sens){
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setFont(new Font("TimeSRoman", Font.PLAIN,12));
        g2d.setStroke(new BasicStroke(1));
        int id = sens.getID();
        int Sx = sens.getSPoint().x;
        int Sy = sens.getSPoint().y;
        int Sr = sens.getSR();
        int radius = sens.getRadius();
        // lấy màu 
        Color color = null;
        try {
            Field f = Color.class.getField(c);
            color = (Color)f.get(null);
        } catch (Exception e) {
            color = Color.BLUE;
        }
        g.setColor(color);
        // Vẽ ID sensor
        g2d.drawString(String.valueOf(id), Sx-Sr/2, Sy-Sr/2);
        // Ve tam
        Shape Oval = new Ellipse2D.Double(Sx-Sr/2, Sy-Sr/2, Sr, Sr);
        g2d.fill(Oval);
        g2d.drawOval(Sx-radius/2, Sy-radius/2, radius, radius);
    }
    // Vẽ các sensor
    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    public String paintGridView(Graphics g){   
        String message = "";
        int num = 1;
        for(Sensors sens: listSensor){
            paintSensor(g, "black", sens);
            num++;
        }
        if(num<=numberSensor){
            message = "Đã phân bố đủ diện tích, còn thừa "+(numberSensor-num+1)+" sensors";
            setNumberSensor(numberSensor - (numberSensor-num+1));
        }   
        else
            message = "Đã phân bố hết số lượng sensor đã cho";
        
        return message;
    }

    // getter and setter
    
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

