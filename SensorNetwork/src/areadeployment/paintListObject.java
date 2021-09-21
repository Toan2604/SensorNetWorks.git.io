
// Lớp vẽ các Objects

package areadeployment;

import datasensor.Area;
import datasensor.Objects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;



public class paintListObject extends JComponent{
    private Area area;
    private Objects obj;
    private int numberObject;  // kích thước
    private ArrayList<Objects> listObject;                  // danh sách đối tượng

    // Constructor
    public paintListObject(int Ax, int Ay, int width, int height, int numberObject, int radiusObject, String color) {
        area = new Area(Ax, Ay, width, height, numberObject);
        this.numberObject = numberObject;
        obj = new Objects(radiusObject, color);
        listObject = new ArrayList<>();
        setDataObject();
    }
    
    // Hàm tạo danh sách đối tượng
    public void setDataObject(){
        
        for(int i=0; i<numberObject; i++){
            int x = ThreadLocalRandom.current().nextInt(area.getPoint().x,area.getPoint().y+area.getAwidth());
            int y = ThreadLocalRandom.current().nextInt(area.getPoint().x,area.getPoint().y+area.getAheight());
            
            Objects obj = new Objects(i+1, x, y, this.obj.getRadius(), this.obj.getColor());
            try {
                listObject.add(obj);
            } catch (Exception e) {
                System.out.println("Add Object fail.\n"+e.toString()+"\nPlease try again!");
            }
        }
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public void paintObject(Graphics g, String s, Objects o){
        Graphics2D g2d = (Graphics2D)g;
        // Lấy dữ liệu từ danh sách đã tạo
        int ID = o.getID();
        int Ox = o.getOpoint().x;
        int Oy = o.getOpoint().y;
        int Or = o.getRadius();

        // lấy màu 
        Color color = null;
        try {
            Field f = Color.class.getField(s);
            color = (Color)f.get(null);
        } catch (Exception e) {
            color = Color.BLUE;
        }

        // Ve ban kinh
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        g2d.fillOval(Ox-Or/2, Oy-Or/2, Or, Or);

        // Ve ID Object
        g2d.setColor(Color.black);// default
        g2d.setFont(new Font("TimeSRoman", Font.BOLD,12));
        g2d.drawString(String.valueOf(ID), Ox-Or/2,Oy-Or/2);
    }
    
    // Hàm vẽ đối tượng
    @SuppressWarnings("UseSpecificCatch")
    public void paintListObject(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        for(Objects o: listObject)
            paintObject(g, o.getColor(), o);

    }

    // getter and setter

    public int getNumberObject() {
        return numberObject;
    }

    public void setNumberObject(int numberObject) {
        this.numberObject = numberObject;
    }

    public ArrayList<Objects> getListObject() {
        return listObject;
    }

    public void setListObject(ArrayList<Objects> listObject) {
        this.listObject = listObject;
    }

}
