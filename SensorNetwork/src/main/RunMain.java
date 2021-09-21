/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import areadeployment.GridView;
import areadeployment.RandomView;
import areadeployment.paintListObject;
import datasensor.Area;
import datasensor.Objects;
import datasensor.Sensors;
import java.awt.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class RunMain extends javax.swing.JFrame{
    Graphics g;
    private RandomView random;
    private GridView grid;
    private paintListObject object;
    private Area area;
    
    private ArrayList<Area> listArea = new ArrayList<Area>();;
    private ArrayList<Sensors> listSensorGrid;  // Danh sách các sensor theo Grid View
    public ArrayList<Sensors> listSensorRan;    // Danh sách các sensor theo Random View
    private ArrayList<Objects> listObject;      // Danh sách Object
    
    private int numberSensor;               // Số lượng sensor
    private int numberObject;               // số lượng đối tượng
    private int cluster;                    // số lượng sensor của vùng phủ sóng k
    
    private String typeView;                // loại triển khai: lưói hoặc random
    private int numSensor_Cluster;          // Kết quả đưa ra
    public Map<Integer, ArrayList<Integer>> mapSensor;  // key: số sensor được lấy, value: danh sách các ID sensor
    public ArrayList<Integer> countSensors;             // danh sách các ID sensor
    
    /**
     * Creates new form RunMain
     */
    public RunMain() {
        initComponents();
        g = drawingPane.getGraphics();
        drawingPane.paint(g);
        drawingPane.paintComponents(g);
        drawingPane.paintAll(g);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g); 
    }
    
// ===================================== Lấy dữ liệu từ bàn phím ==========================
    // Lấy dữ liệu cho vùng triển khai
    @SuppressWarnings("UseSpecificCatch")   // Use vs Exception
    void getArea(){
        try {
            int Ax = Integer.valueOf(txtAx.getText());
            int Ay = Integer.valueOf(txtAy.getText());
            Point point = new Point(Ax, Ay); 
            int Awidth = Integer.valueOf(txtAwidth.getText());
            int Aheight = Integer.valueOf(txtAheight.getText());
            area = new Area(point, Awidth, Aheight);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Lỗi\n"+e.toString(), "Cảnh báo", JOptionPane.ERROR_MESSAGE);
        }        
    }
    // Lấy dữ liệu cho Sensor, Object
    @SuppressWarnings("UseSpecificCatch")
    void getSensorObject(){
        try{
            numberSensor = Integer.valueOf(txtNumberSensor.getText());
            int Sr = Integer.valueOf(txtSradius.getText()); 
            int radiusS = Integer.valueOf(txtRadiusSensor.getText());
            int age = Integer.valueOf(txtAgeOfPin.getText()); 
            
            numberObject = Integer.valueOf(txtNumberObject.getText());
            int radiusO = Integer.valueOf(txtRadiusObject.getText());
            String color = comboColorObject.getSelectedItem().toString();
            typeView = comboViewTpye.getSelectedItem().toString();
            
            random = new RandomView(area.getPoint().x, area.getPoint().y, area.getAwidth(), area.getAheight(), numberSensor, Sr, radiusS ,age);
            grid = new GridView(area.getPoint().x, area.getPoint().y, area.getAwidth(), area.getAheight(), numberSensor, Sr, radiusS, age);
            object = new paintListObject(area.getPoint().x, area.getPoint().y, area.getAwidth(), area.getAheight(), numberObject, radiusO, color);

        }catch(Exception e){}
    }
    

    // Đưa ra danh sách các sensor đã quét được object
    void sensorChosen(String type){
        
        if(type.equalsIgnoreCase("Random View")){               // TH ngẫu nhiên (Random View)
            mapSensor = new HashMap<Integer, ArrayList<Integer>>();
            ArrayList <Integer> listInteger;
            for (Objects o: listObject){
                //lay toa do object
                Point objectPoint = o.getOpoint();
                // lay ban kinh object
                int radiusObj = o.getRadius();
                // duyet sensors
                int index = 0;
                listInteger  = new ArrayList<>();
                for(Sensors sens: listSensorRan){
                    int IDSensor = sens.getID();
                    int radiusSen = sens.getRadius();
                    // lấy tọa độ sensor
                    Point sensorPoint = sens.getSPoint();
                    // tính khoảng cách từ sensor đến object
                    double dis = sensorPoint.distance(objectPoint);
                    int distance = radiusObj+radiusSen;
                    if((int)dis<=distance){
                        try{
                            listInteger.add(IDSensor);
                            index++;
                        }catch(Exception e){
                            System.out.println("Fail to add "+e.toString());
                        }  
                    }
                }
                if(index==0)
                    mapSensor.put(0, new ArrayList<Integer>(Arrays.asList(-1)));
                else{
                    mapSensor.put(index, listInteger);
                }
            }
            
        }
        else{                                               // TH đồng đều (Grid View)
            mapSensor = new HashMap<Integer, ArrayList<Integer>>();
            ArrayList <Integer> listInteger;
            for (Objects o: listObject){
                //lay toa do object
                Point objectPoint = o.getOpoint();
                // lay ban kinh object
                int radiusObj = o.getRadius();
                // duyet sensors
                int index = 0;
                listInteger = new ArrayList<>();
                for(Sensors s: listSensorGrid){
                    int ID = s.getID();
                    int radiusSen = s.getRadius();//getRadiusSensor();//
                    // lấy tọa độ sensor
                    Point sensorPoint = s.getSPoint();
                    // tính khoảng cách từ sensor đến object
                    double dis = sensorPoint.distance(objectPoint);
                    int distance = radiusObj+radiusSen;
                    
                    if((int)dis<=distance){
                        try{
                            listInteger.add(ID);
                            index++;
                        }catch(Exception e){
                            System.out.println("Fail to add "+e.toString());
                        }
                    }
                        
                }
                if(index==0)
                    mapSensor.put(0, new ArrayList<Integer>(Arrays.asList(-1)));
                else{
                    mapSensor.put(index, listInteger);
                }
            }
        }
    }
    // đếm số lượng sensor quét được các sensor
    @SuppressWarnings({"BoxedValueEquality", "NumberEquality"})
    int clusterNumber(){
        sensorChosen(comboViewTpye.getSelectedItem().toString());
        cluster = Integer.valueOf(txtCluster.getText());
        ArrayList<Integer> keys = new ArrayList<Integer>(mapSensor.keySet());
        for(Integer kt : keys){
            if(kt<=0)
            {
                JOptionPane.showMessageDialog(rootPane, "Sensors không quét được Object. Stop. "
                        + "Không thể thỏa mãn yêu cầu.\nResult = 0", "Warning",JOptionPane.ERROR_MESSAGE);
                return 0;
            }
            if(kt<=cluster){
                JOptionPane.showMessageDialog(rootPane, "Số sensor yêu cầu trong một cụm quá lớn. "
                        + "Không thể thỏa mãn yêu cầu. \nResult = 0", "Warning",JOptionPane.ERROR_MESSAGE);
                return 0;
            }     
        }
        ArrayList<Integer> count = new ArrayList<Integer>();
        
        countSensors = new ArrayList<Integer>();
        for(ArrayList<Integer> list: mapSensor.values())
            count.addAll(list);

        for(Integer integer: count)
            if(!countSensors.contains(integer))
                countSensors.add(integer);

        return countSensors.size();
    }
    
//=================================== PAINT =========================================================
    // Ghi đè để thực thi vẽ vùng triển khai
    @Override
    public void paint(Graphics g) {
        super.paint(g);        
        paintArea(); 
    }

    // Vẽ vùng triển khai
    final int BOLD = 3; // duong vien vung trien khai
    void paintArea(){
        
        try {
            getArea();
            Graphics2D g2d = (Graphics2D)g;
            // to dam duong vien
            g2d.setStroke(new BasicStroke(BOLD));
            g2d.setColor(Color.red);
            g2d.drawRect(area.getPoint().x, area.getPoint().y, area.getAwidth(), area.getAheight()); // ve vung trien khai
        } catch (Exception e) {
            drawingPane.repaint();
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập dữ liệu vào");   
        }   
    }
    // xác định vẽ sensor theo các trường hợp
    String paintViewType(){
        String message = "";
        if (typeView.equalsIgnoreCase("Grid View")){
            message = grid.paintGridView(g);
            listSensorGrid = new ArrayList<Sensors>();
            for(Sensors sens: grid.getListSensor())
                listSensorGrid.add(sens);
        }   
        else {
            message = random.paintRandomView(g);
            listSensorRan = new ArrayList<Sensors>();
            for(Sensors sen: random.getListSensor())
                listSensorRan.add(sen);
            
        }   
        return message;
    }
    
    // Vẽ các sensor hoạt động khi quét được Object
    void paintActionSensors(){
        Graphics2D g2d = (Graphics2D)g;
        
        numSensor_Cluster = clusterNumber();
        g2d.setFont(new Font("TimeSRoman", Font.PLAIN,12));
     
        if(numSensor_Cluster>0){
        
            g2d.setFont(new Font("TimeSRoman", Font.PLAIN,12));
            if(typeView.equalsIgnoreCase("Grid View")){
                for(Sensors sens: listSensorGrid)
                    if(countSensors.contains(sens.getID()))
                        grid.paintSensor(g, "red", sens);
            } 
            else{
                for(Sensors sens: listSensorRan)
                    if(countSensors.contains(sens.getID()))
                            random.getGrid().paintSensor(g, "red", sens);
                }   
        }
        else{
            if(typeView.equalsIgnoreCase("Random View")){
                for(Sensors sens: listSensorRan)
                    random.getGrid().paintSensor(g, "black", sens);
            }

            else{
                for(Sensors sens: listSensorGrid)
                    grid.paintSensor(g, "black", sens); 
            }
        }
            
    }
    
    
    
// ++++++++++++++++++++++++++++++ Lưu dữ liệu vào file +++++++++++++++++++++++++++++++++++++++++++
    
    
    public ArrayList doc( String file){
        ArrayList list = new ArrayList();
        try(FileInputStream out = new FileInputStream(new File(file))) {
            ObjectInputStream oos = new ObjectInputStream(out);
            list = (ArrayList) oos.readObject();
        } catch (Exception e) {
            System.out.println("Got an exception!");
        }
        return list;
    }
    public void ghi(ArrayList list, String file){
        try(FileOutputStream out = new FileOutputStream(new File(file))) {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(list);
        } catch (Exception e) {
            System.out.println("Got an exception!");
        }
    }
    // Ghi vào file kiểu Grid View
    public String writeDataGridView(){
        String message = null;
        try (PrintWriter pw = new PrintWriter(new File("ListGridSensor.DAT"))){
            for(Sensors s: listSensorGrid){
                pw.println(s.getID());
                pw.println(s.getSPoint().x);
                pw.println(s.getSPoint().y);
                pw.println(s.getSR());
                pw.println(s.getRadius());
                pw.println(s.getStatus());
                pw.println(s.getAge());
            }
            message = "Succeed to write Data on ListGridSensor.DAT\n";
        } catch (Exception e) {
            message = "Fail to write Data on ListGridSensor.DAT\n"+e.toString()+"\n";
        }
        return message;
    }
    
    // Ghi vào file kiểu Random View
    public String writeDataRandomView(){
        String message = null;
        try (PrintWriter pw = new PrintWriter(new File("ListRandomSensor.DAT"))){
            for(Sensors s: listSensorRan){
                pw.println(s.getID());
                pw.println(s.getSPoint().x);
                pw.println(s.getSPoint().y);
                pw.println(s.getSR());
                pw.println(s.getRadius());
                pw.println(s.getStatus());
                pw.println(s.getAge());
            }
            message = "Succeed to write Data on ListRandomSensor.DAT\n";
        } catch (Exception e) {
            message = "Fail to write Data on ListRandomSensor.DAT\n"+e.toString()+"\n";
        }
        return message;
    }
    //Ghi vào file của AreaDeployment
    public String writeDataArea(){
        String message = null;
        try (PrintWriter pw = new PrintWriter(new File("ListAreaDeployment.DAT"))){
            for(Area a: listArea){
                pw.println(a.getPoint().x);
                pw.println(a.getPoint().y);
                pw.println(a.getAwidth());
                pw.println(a.getAheight());
                pw.println(a.getNumberSensor());
                pw.println(a.getNumberObject());
            }
            message = "Succeed to write Data on ListAreaDeployment.DAT\n";
            
        } catch (Exception e) {
            message = "Fail to write Data on ListAreaDeployment.DAT\n"+e.toString()+"\n";
        }
        return message;
    }
    // Ghi vào file của Object
    public String writeDataObject(){
        String message = null;
        try (PrintWriter pw = new PrintWriter(new File("ListObject.DAT"))){
            for(Objects o : object.getListObject()){
                pw.println(o.getID());
                pw.println(o.getOpoint().x);
                pw.println(o.getOpoint().y);
                pw.println(o.getRadius());
                pw.println(o.getColor());
            }
            message = "Succeed to write Data on ListObject.DAT\n";
        } catch (Exception e) {
            message = "Fail to write Data on ListObject.DAT\n"+e.toString()+"\n";
        }
        return message;
    }


//++++++++++++++++++++++++++++++++++++++Xử lý các sự kiện xảy ra +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
//=================================== AREA DEPLOYMENT ===============================================
    
    // Xử lý sự kiện nhập tọa độ vùng triển khai
    private void txtAxFocusGained(java.awt.event.FocusEvent evt) {
        if(txtAx.getText().equals("0"))
            txtAx.setText("");
    }
    private void txtAxFocusLost(java.awt.event.FocusEvent evt) {
        if(txtAx.getText().equals(""))
            txtAx.setText("0");
        if(Integer.valueOf(txtAx.getText())>drawingPane.getWidth())
        {
            JOptionPane.showMessageDialog(rootPane, "Tọa độ vùng triển khai nằm ngoài vùng cho phép."+
                     "\nYêu cầu nhập lại toạ độ.","Cảnh báo",JOptionPane.ERROR_MESSAGE);
            txtAx.setText("0");
            txtAx.requestFocus();
        }
    }
    private void txtAxKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }

    private void txtAyFocusGained(java.awt.event.FocusEvent evt) {
        if(txtAy.getText().equals("0"))
            txtAy.setText("");
    }
    private void txtAyFocusLost(java.awt.event.FocusEvent evt) {
        if(txtAy.getText().equals(""))
            txtAy.setText("0");
        if(Integer.valueOf(txtAy.getText())>drawingPane.getWidth())
        {
            JOptionPane.showMessageDialog(rootPane, "Tọa độ vùng triển khai ngoài vùng cho phép, yêu cầu nhập lại toạ độ.");
            txtAy.setText("0");
            txtAy.requestFocus();
        }
    }
    
    private void txtAyKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
 
    // Xử lý sự kiện nhập kích thước vùng triển khai
    private void txtAwidthFocusGained(java.awt.event.FocusEvent evt) {
        if(txtAwidth.getText().equals("0"))
            txtAwidth.setText("");
    }
    private void txtAwidthFocusLost(java.awt.event.FocusEvent evt) {
        if(txtAwidth.getText().equals(""))
            txtAwidth.setText("0");
        if((Integer.valueOf(txtAwidth.getText())+Integer.valueOf(txtAx.getText()))>drawingPane.getWidth()){
            JOptionPane.showMessageDialog(rootPane, "Kích thước vùng triển khai lớn hơn cho phép, yêu cầu nhập lại toạ độ.");
            txtAwidth.setText("0");
            txtAwidth.requestFocus();
        }
    }
    private void txtAwidthKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }

    private void txtAheightFocusGained(java.awt.event.FocusEvent evt) {
        if(txtAheight.getText().equals("0"))
            txtAheight.setText("");
    }
    private void txtAheightFocusLost(java.awt.event.FocusEvent evt) {
        if(txtAheight.getText().equals(""))
            txtAheight.setText("0");
        if ((Integer.valueOf(txtAheight.getText())+Integer.valueOf(txtAy.getText()))>drawingPane.getHeight()){
            JOptionPane.showMessageDialog(rootPane, "Kích thước vùng triển khai lớn hơn cho phép, yêu cầu nhập lại toạ độ.");
            txtAheight.setText("0");
            txtAheight.requestFocus();
        }
    }

    private void txtAheightKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // Xử lý sự kiện nhập số lượng sensor
    private void txtNumberSensorFocusGained(java.awt.event.FocusEvent evt) {
        if(txtNumberSensor.getText().equals("0"))
            txtNumberSensor.setText("");
    }
    private void txtNumberSensorFocusLost(java.awt.event.FocusEvent evt) {
        if(txtNumberSensor.getText().equals(""))
            txtNumberSensor.setText("0");
    }

    private void txtNumberSensorKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // Xử lý sự kiện nhập số lượng Object
    private void txtNumberObjectFocusGained(java.awt.event.FocusEvent evt) {
        if(txtNumberObject.getText().equals("0"))
            txtNumberObject.setText("");
        
    }
    private void txtNumberObjectFocusLost(java.awt.event.FocusEvent evt) {
        
        if(txtNumberObject.getText().equals(""))
            txtNumberObject.setText("0");
        
    }
    private void txtNumberObjectKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }

    // +++++++++++++++++++++++++++Xử lý sự kiện vẽ vùng triển khai +++++++++++++++++++++
    @SuppressWarnings("UseSpecificCatch")
    private void btnRunAreaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if(txtAwidth.getText().equals("0"))
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn phải nhập kích thước.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtAwidth.requestFocus();
            }
            else if(txtAheight.getText().equals("0"))
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn phải nhập kích thước.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtAheight.requestFocus();
            }
            else{
                repaint();
                
            }
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Paint Area fail\n"+e.toString()+"\nPlease check data again");
        }
        
    }
    // +++++++++++++++++++++++Xử lý sự kiện  xóa vùng triển khai +++++++++++++++++++++++++
    private void btnDeleteAreaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            drawingPane.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Fail.\n"+e.toString()+
                    "\nPlease check again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
//=================================== SENSORS ===============================================

    // Xử lý sự kiện  bán kính sensor
    private void txtSradiusFocusGained(java.awt.event.FocusEvent evt) {
        if(txtSradius.getText().equals("0"))
            txtSradius.setText("");
    }
    private void txtSradiusFocusLost(java.awt.event.FocusEvent evt) {
        if(txtSradius.getText().equals(""))
            txtSradius.setText("0");
    }

    private void txtSradiusKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // Xử lý sự kiện nhập bán kính cảm biến
    private void txtRadiusSensorFocusGained(java.awt.event.FocusEvent evt) {
        if(txtRadiusSensor.getText().equals("0"))
            txtRadiusSensor.setText("");
    }
    private void txtRadiusSensorFocusLost(java.awt.event.FocusEvent evt) {
        if(txtRadiusSensor.getText().equals(""))
            txtRadiusSensor.setText("0");
    }

    private void txtRadiusSensorKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // Xử lý sự kiện nhập tuổi thọ pin
    private void txtAgeOfPinFocusGained(java.awt.event.FocusEvent evt) {
        if(txtAgeOfPin.getText().equals("0"))
            txtAgeOfPin.setText("");
    }
    private void txtAgeOfPinFocusLost(java.awt.event.FocusEvent evt) {
        if(txtAgeOfPin.getText().equals(""))
            txtAgeOfPin.setText("0");
    }

    private void txtAgeOfPinKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // +++++++++++++++++++++++++Xử lý sự kiện Vẽ sensor +++++++++++++++++++++++
    @SuppressWarnings("UseSpecificCatch")
    private void btnRunSensorActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if(Integer.valueOf(txtNumberSensor.getText())==0)
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập số lượng sensor", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtNumberSensor.requestFocus();
            }
            else if(Integer.valueOf(txtSradius.getText())==0)
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập bán kính của sensor", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSradius.requestFocus();
            }
            else if(Integer.valueOf(txtRadiusSensor.getText())==0)
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập bán kính cảm biến", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtRadiusSensor.requestFocus();
            }
            else if(Integer.valueOf(txtAgeOfPin.getText())==0)
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tuổi thọ của pin", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtAgeOfPin.requestFocus();
            }
            else{
                getSensorObject();
                String mes = "";
                mes = paintViewType();   
                JOptionPane.showMessageDialog(rootPane, mes);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Paint Sensor Fail.\n"+e.toString()+"\nPlease "
                    + "check data again!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ++++++++++++++++++++ Xử lý sự kiện xóa sensor ++++++++++++++++++++++++++++++++++++++++++++
    private void btnDeleteSensorActionPerformed(java.awt.event.ActionEvent evt) {
        repaint();
    }
//=================================== OBJECTS ===============================================

    // Xử lý sự kiện nhập bán kính Object
    private void txtRadiusObjectFocusGained(java.awt.event.FocusEvent evt) {
        if(txtRadiusObject.getText().equals("0"))
            txtRadiusObject.setText("");
    }
    private void txtRadiusObjectFocusLost(java.awt.event.FocusEvent evt) {
        if(txtRadiusObject.getText().equals(""))
            txtRadiusObject.setText("0");
    }

    private void txtRadiusObjectKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    
    // Xử lý sự kiện  RUN OBJECT
    @SuppressWarnings("UseSpecificCatch")
    private void btnRunobjectActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            
            if(Integer.valueOf(txtNumberObject.getText())==0)
            {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập số lượng object", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtNumberObject.requestFocus();
            }
            else{
                if(Integer.valueOf(txtRadiusObject.getText())==0)
                {
                    JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập bán kính object", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    txtRadiusObject.requestFocus();
                }
                else{
                    getSensorObject();
                    object.paintListObject(g);
                    listObject = new ArrayList<Objects>();
                    for(Objects o: object.getListObject())
                        listObject.add(o);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Paint Object Fail.\n"+e.toString()+
                    "\nPlease check data again!","Errỏr",JOptionPane.ERROR_MESSAGE);
        }
    }
    // Xử lý sự kiện  DELETE OBJECT
     private void btnDeleteObjectActionPerformed(java.awt.event.ActionEvent evt) {
        repaint();
    }
//=================================== RUN PROGRAM ===============================================
    // Xử lý sự kiện nhập số sensor trong một cluster
    private void txtClusterFocusGained(java.awt.event.FocusEvent evt) {
        if(txtCluster.getText().equals("0"))
            txtCluster.setText("");
        
    }
    private void txtClusterFocusLost(java.awt.event.FocusEvent evt) {
        if(txtCluster.getText().equals(""))
            txtCluster.setText("0");
    }

    private void txtClusterKeyTyped(java.awt.event.KeyEvent evt) {
        char testChar = evt.getKeyChar();
        if(!(Character.isDigit(testChar)))
            evt.consume();
    }
    // Xử lý sự kiện  RUN PROGRAM
    @SuppressWarnings("UseSpecificCatch")
    private void btnRUNProgramActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            /**/
            if(btnRUNProgram.getText().equalsIgnoreCase("Run"))
            {
                if(txtCluster.getText().equals("0")){
                    String input = JOptionPane.showInputDialog(rootPane, "Bạn chưa nhập cluster.\nHãy nhập vào.");
                    txtCluster.setText(input);
                    
                    txtCluster.requestFocus();
                }
                else{
                    paintActionSensors();
                    btnRUNProgram.setText("Next");
                    Point p = new Point(Integer.valueOf(txtAx.getText()),Integer.valueOf(txtAy.getText()));
                    int Awidth = Integer.valueOf(txtAwidth.getText());
                    int Aheight = Integer.valueOf(txtAheight.getText());
                    int numS = Integer.valueOf(txtNumberSensor.getText());
                    int numO = Integer.valueOf(txtNumberObject.getText());
                    area = new Area(p, Awidth, Aheight, numS, numO);
                    try {
                        listArea.add(area);
                    } catch (Exception e) {
                    }
                }   
            }
            else{
                btnRUNProgram.setText("Run");
                JOptionPane.showMessageDialog(rootPane, "Result = "+numSensor_Cluster);
                txtResult.setText(String.valueOf(numSensor_Cluster));
                txtCluster.setText("0");
                txtResult.requestFocus();
            }
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Paint Fail.\n"+e.toString()+
                    "\nPlease check data again!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Xử lý sự kiện SAVE DATA
    private void btnSAVEActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try{ String mes = null;
            mes = writeDataArea();
            if(typeView.equalsIgnoreCase("Random View"))
                mes += writeDataRandomView();
            else 
                mes+= writeDataGridView();
            mes += writeDataObject();

            JOptionPane.showMessageDialog(rootPane, mes);
            int i = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn xem dữ liệu không", "Thông báo", JOptionPane.YES_NO_OPTION);
            if(i == 0){
                ShowData show = new ShowData();
                show.setVisible(true);
                dispose();
            }
                
            
        } catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Fail to save data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Xử lý sự kiện  DELETE PROGRAM
    private void btnDELETEProgramActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            drawingPane.repaint();
            txtResult.setText("0");
            txtCluster.setText("0");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Fail!\n"+e.toString());
        }
    }

    // Xử lý sự kiện  EXIT PROGRAM
    private void btnEXITProgramActionPerformed(java.awt.event.ActionEvent evt) {
        int opt = JOptionPane.showConfirmDialog(btnEXITProgram, "Bạn có chắc chắn muốn thoát?", "Warning",
            JOptionPane.WARNING_MESSAGE);
        if (opt == 0)
        System.exit(0);
    }
    
    // Xử lý sự kiện REFRESH DATA
    private void btnREFRESHDataActionPerformed(java.awt.event.ActionEvent evt) {
        drawingPane.repaint();
        
        txtAx.setText("0");
        txtAy.setText("0");
        txtAwidth.setText("0");
        txtAheight.setText("0");
        txtNumberSensor.setText("0");
        txtNumberObject.setText("0");
        txtCluster.setText("0");
        txtSradius.setText("0");
        txtRadiusSensor.setText("0");
        txtAgeOfPin.setText("0");
        txtRadiusObject.setText("0");
        txtAx.requestFocus();
        txtResult.setText("");
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RunMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RunMain().setVisible(true);
            }
        });
    }

    
    // getter and setter
//    public Point getPoint() {
//        return point;
//    }
//
//    public void setPoint(Point point) {
//        this.point = point;
//    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }
  
    
/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawingPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnREFRESHData = new javax.swing.JButton();
        txtAwidth = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtAy = new javax.swing.JTextField();
        txtAx = new javax.swing.JTextField();
        txtAheight = new javax.swing.JTextField();
        txtCluster = new javax.swing.JTextField();
        txtNumberSensor = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNumberObject = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSradius = new javax.swing.JTextField();
        txtAgeOfPin = new javax.swing.JTextField();
        txtRadiusSensor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        btnRunSensor = new javax.swing.JButton();
        btnDeleteSensor = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtRadiusObject = new javax.swing.JTextField();
        comboColorObject = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnRunobject = new javax.swing.JButton();
        btnDeleteObject = new javax.swing.JButton();
        comboViewTpye = new javax.swing.JComboBox<>();
        btnRUNProgram = new javax.swing.JButton();
        btnDELETEProgram = new javax.swing.JButton();
        btnRunArea = new javax.swing.JButton();
        btnDeleteArea = new javax.swing.JButton();
        btnEXITProgram = new javax.swing.JButton();
        txtResult = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        btnSAVE = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        drawingPane.setBackground(new java.awt.Color(255, 255, 255));
        drawingPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout drawingPaneLayout = new javax.swing.GroupLayout(drawingPane);
        drawingPane.setLayout(drawingPaneLayout);
        drawingPaneLayout.setHorizontalGroup(
            drawingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 996, Short.MAX_VALUE)
        );
        drawingPaneLayout.setVerticalGroup(
            drawingPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Property");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Area Deployment");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Sensors");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Objects");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Coordinate:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Width:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Height:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Number of Sensors:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Number of Object:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("k: ");

        btnREFRESHData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/refresh.png"))); // NOI18N
        btnREFRESHData.setBorderPainted(false);
        btnREFRESHData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnREFRESHDataActionPerformed(evt);
            }
        });

        txtAwidth.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAwidth.setText("0");
        txtAwidth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAwidthFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAwidthFocusLost(evt);
            }
        });
        txtAwidth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAwidthKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Ax:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Ay:");

        txtAy.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAy.setText("0");
        txtAy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAyFocusLost(evt);
            }
        });
        txtAy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAyKeyTyped(evt);
            }
        });

        txtAx.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAx.setText("0");
        txtAx.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAxFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAxFocusLost(evt);
            }
        });
        txtAx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAxKeyTyped(evt);
            }
        });

        txtAheight.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAheight.setText("0");
        txtAheight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAheightFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAheightFocusLost(evt);
            }
        });
        txtAheight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAheightKeyTyped(evt);
            }
        });

        txtCluster.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCluster.setText("0");
        txtCluster.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClusterFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClusterFocusLost(evt);
            }
        });
        txtCluster.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtClusterKeyTyped(evt);
            }
        });

        txtNumberSensor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumberSensor.setText("0");
        txtNumberSensor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumberSensorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberSensorFocusLost(evt);
            }
        });
        txtNumberSensor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumberSensorKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("m");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("m");

        txtNumberObject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNumberObject.setText("0");
        txtNumberObject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumberObjectFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberObjectFocusLost(evt);
            }
        });
        txtNumberObject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumberObjectKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Sr:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Radius:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Age of pin:");

        txtSradius.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSradius.setText("0");
        txtSradius.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSradiusFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSradiusFocusLost(evt);
            }
        });
        txtSradius.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSradiusKeyTyped(evt);
            }
        });

        txtAgeOfPin.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAgeOfPin.setText("0");
        txtAgeOfPin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAgeOfPinFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAgeOfPinFocusLost(evt);
            }
        });
        txtAgeOfPin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAgeOfPinKeyTyped(evt);
            }
        });

        txtRadiusSensor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRadiusSensor.setText("0");
        txtRadiusSensor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRadiusSensorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRadiusSensorFocusLost(evt);
            }
        });
        txtRadiusSensor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadiusSensorKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("m");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("m");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("hours");

        btnRunSensor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRunSensor.setText("Run");
        btnRunSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunSensorActionPerformed(evt);
            }
        });

        btnDeleteSensor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDeleteSensor.setText("Delete");
        btnDeleteSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSensorActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Radius sensor:");

        txtRadiusObject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtRadiusObject.setText("0");
        txtRadiusObject.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRadiusObjectFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRadiusObjectFocusLost(evt);
            }
        });
        txtRadiusObject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadiusObjectKeyTyped(evt);
            }
        });

        comboColorObject.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboColorObject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "red", "gray", "black", "pink", "orange", "yellow", "green", "magenta", "cyan", "blue", "darkGray", "lightGray", " " }));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("m");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Color:");

        btnRunobject.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRunobject.setText("Run");
        btnRunobject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunobjectActionPerformed(evt);
            }
        });

        btnDeleteObject.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDeleteObject.setText("Delete");
        btnDeleteObject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteObjectActionPerformed(evt);
            }
        });

        comboViewTpye.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        comboViewTpye.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Grid View", "Random View" }));
        comboViewTpye.setSelectedIndex(1);

        btnRUNProgram.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRUNProgram.setText("Run");
        btnRUNProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRUNProgramActionPerformed(evt);
            }
        });

        btnDELETEProgram.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDELETEProgram.setText("Delete");
        btnDELETEProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDELETEProgramActionPerformed(evt);
            }
        });

        btnRunArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRunArea.setText("Run");
        btnRunArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunAreaActionPerformed(evt);
            }
        });

        btnDeleteArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDeleteArea.setText("Delete");
        btnDeleteArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAreaActionPerformed(evt);
            }
        });

        btnEXITProgram.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnEXITProgram.setText("Exit");
        btnEXITProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEXITProgramActionPerformed(evt);
            }
        });

        txtResult.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Result of program:");
        jLabel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSAVE.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSAVE.setText("Save");
        btnSAVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSAVEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(comboViewTpye, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCluster, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnRUNProgram, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtResult, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(btnSAVE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDELETEProgram)
                        .addGap(18, 18, 18)
                        .addComponent(btnEXITProgram, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(drawingPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(btnREFRESHData, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtNumberSensor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtNumberObject, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(btnRunArea, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDeleteArea))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtAwidth)
                                                    .addComponent(txtAheight, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jLabel14)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(9, 9, 9)
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAx, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtAy, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel22)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtRadiusSensor, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel20))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtAgeOfPin, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel21))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtSradius, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel19))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRunSensor, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDeleteSensor))))
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(comboColorObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(txtRadiusObject, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jLabel24))))
                                            .addComponent(jLabel25)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRunobject, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDeleteObject)))))))
                        .addGap(34, 42, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jLabel10))
                        .addComponent(txtCluster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(btnREFRESHData, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel11)
                                    .addComponent(txtAy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtAx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtAwidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtAheight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtNumberSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtNumberObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRunArea)
                                    .addComponent(btnDeleteArea))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(txtSradius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtRadiusSensor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(txtAgeOfPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRunSensor)
                                    .addComponent(btnDeleteSensor))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(txtRadiusObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel25)
                                    .addComponent(comboColorObject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnRunobject)
                                    .addComponent(btnDeleteObject)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(drawingPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnRUNProgram)
                                .addComponent(jLabel23)
                                .addComponent(txtResult))
                            .addComponent(comboViewTpye, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnEXITProgram)
                                    .addComponent(btnDELETEProgram)
                                    .addComponent(btnSAVE))
                                .addGap(2, 2, 2)))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDELETEProgram;
    private javax.swing.JButton btnDeleteArea;
    private javax.swing.JButton btnDeleteObject;
    private javax.swing.JButton btnDeleteSensor;
    private javax.swing.JButton btnEXITProgram;
    private javax.swing.JButton btnREFRESHData;
    private javax.swing.JButton btnRUNProgram;
    private javax.swing.JButton btnRunArea;
    private javax.swing.JButton btnRunSensor;
    private javax.swing.JButton btnRunobject;
    private javax.swing.JButton btnSAVE;
    private javax.swing.JComboBox<String> comboColorObject;
    private javax.swing.JComboBox<String> comboViewTpye;
    private javax.swing.JPanel drawingPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtAgeOfPin;
    private javax.swing.JTextField txtAheight;
    private javax.swing.JTextField txtAwidth;
    private javax.swing.JTextField txtAx;
    private javax.swing.JTextField txtAy;
    private javax.swing.JTextField txtCluster;
    private javax.swing.JTextField txtNumberObject;
    private javax.swing.JTextField txtNumberSensor;
    private javax.swing.JTextField txtRadiusObject;
    private javax.swing.JTextField txtRadiusSensor;
    private javax.swing.JTextField txtResult;
    private javax.swing.JTextField txtSradius;
    // End of variables declaration//GEN-END:variables
}
