
package main;

import datasensor.Area;
import datasensor.Objects;
import datasensor.Sensors;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.table.DefaultTableModel;


public class ShowData extends javax.swing.JFrame {
    private ArrayList<Sensors> listSensorGrid;  // Danh sách các sensor theo Grid View
    public ArrayList<Sensors> listSensorRan;
    public ArrayList<Objects> listObjects;
    private ArrayList<Area> listArea;
    DefaultTableModel model1, model2, model3;

    /**
     * Creates new form ShowData
     */
    public ShowData() {
        initComponents();
        
        
        model1 = (DefaultTableModel) tableArea.getModel();
        model2= (DefaultTableModel) tableSensors.getModel();
        model3= (DefaultTableModel) tableObject.getModel();
        if(comboView.getSelectedItem().toString().equalsIgnoreCase("Random View"))
            RAND();
        else
            GRID();
        AREA();
        OBJECT();
    }
    public void RAND(){
        listSensorRan = readDataRandomView();
        model2.setRowCount(0);
        for(Sensors s:listSensorRan)
            model2.addRow(s.toObject());
    }
    public void GRID(){
        listSensorGrid = readDataGridView();
        model2.setRowCount(0);
        for(Sensors s:listSensorGrid)
            model2.addRow(s.toObject());
    }
    public void AREA(){
        listArea = readDataArea();
        model1.setRowCount(0);
        for(Area a: listArea)
            model1.addRow(a.toObject());
    }
    void OBJECT(){
        listObjects = readDataObject();
        model3.setRowCount(0);
        for(Objects o: listObjects)
            model3.addRow(o.toObject());
    }
    public ArrayList readDataArea(){
        ArrayList<Area> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("ListAreaDeployment.DAT"))){
            while(sc.hasNext()){
                Area a = new Area();
                int x = Integer.valueOf(sc.nextLine());
                int y = Integer.valueOf(sc.nextLine());
                Point p = new Point(x, y);
                a.setPoint(p);
                a.setAwidth(Integer.valueOf(sc.nextLine()));
                a.setAheight(Integer.valueOf(sc.nextLine()));
                a.setNumberSensor(Integer.valueOf(sc.nextLine()));
                a.setNumberObject(Integer.valueOf(sc.nextLine()));
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println("Fail to read Data\n"+e.toString());
        }
        
        return list;
    }
    
    public ArrayList readDataGridView(){
        ArrayList<Sensors> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("ListGridSensor.DAT"))){
            while(sc.hasNext()){
                Sensors s = new Sensors();
                s.setID(Integer.parseInt(sc.nextLine()));
                int x = Integer.valueOf(sc.nextLine());
                int y = Integer.valueOf(sc.nextLine());
                Point p = new Point(x, y);
                s.setSPoint(p);
                s.setSR(Integer.parseInt(sc.nextLine()));
                s.setRadius(Integer.parseInt(sc.nextLine()));
                s.setStatus(sc.nextLine());
                s.setAge(Integer.valueOf(sc.nextLine()));
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("Fail to read Data\n"+e.toString());
        }
        
        return list;
    }
    
    public ArrayList readDataObject(){
        ArrayList<Objects> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("ListObject.DAT"))){
            while(sc.hasNext()){
                Objects s = new Objects();
                s.setID(Integer.parseInt(sc.nextLine()));
                int x = Integer.valueOf(sc.nextLine());
                int y = Integer.valueOf(sc.nextLine());
                Point p = new Point(x, y);
                s.setOpoint(p);
                s.setRadius(Integer.parseInt(sc.nextLine()));
                s.setColor(sc.nextLine());
                list.add(s);
            }
            

        } catch (Exception e) {
            System.out.println("Fail to read Data\n"+e.toString());
        }
        
        return list;
    }
    public ArrayList readDataRandomView(){
        ArrayList<Sensors> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("ListRandomSensor.DAT"))){
            while(sc.hasNext()){
                Sensors s = new Sensors();
                s.setID(Integer.parseInt(sc.nextLine()));
                int x = Integer.valueOf(sc.nextLine());
                int y = Integer.valueOf(sc.nextLine());
                Point p = new Point(x, y);
                s.setSPoint(p);
                s.setSR(Integer.parseInt(sc.nextLine()));
                s.setRadius(Integer.parseInt(sc.nextLine()));
                s.setStatus(sc.nextLine());
                s.setAge(Integer.valueOf(sc.nextLine()));
                list.add(s);
            }
            
//
        } catch (Exception e) {
            System.out.println("Fail to read Data\n"+e.toString());
        }
        
        return list;
    }
    
    
    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        RunMain run = new RunMain();
        run.setVisible(true);
        dispose();
    }

    private void comboViewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if(comboView.getSelectedItem().toString().equalsIgnoreCase("Random View"))
            RAND();
        else
            GRID();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        table = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableArea = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSensors = new javax.swing.JTable();
        comboView = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableObject = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableArea.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Point", "Dimension", "NumberSensor", "NumberObject"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableArea.setShowGrid(true);
        jScrollPane3.setViewportView(tableArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        table.addTab("Area Deployment", jPanel1);

        tableSensors.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tableSensors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Point", "Sr", "RadiusSensor ", "Status ", "Age"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableSensors.setShowGrid(true);
        jScrollPane1.setViewportView(tableSensors);

        comboView.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboView.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Grid View", "Random View" }));
        comboView.setSelectedIndex(1);
        comboView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(comboView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.addTab("Sensors", jPanel2);

        tableObject.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tableObject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Point", "RadiusObject", "Color"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableObject.setShowGrid(true);
        jScrollPane2.setViewportView(tableObject);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );

        table.addTab("Objects", jPanel3);

        btnOK.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table)
            .addGroup(layout.createSequentialGroup()
                .addGap(283, 283, 283)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnOK)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

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
            java.util.logging.Logger.getLogger(ShowData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShowData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> comboView;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane table;
    private javax.swing.JTable tableArea;
    private javax.swing.JTable tableObject;
    private javax.swing.JTable tableSensors;
    // End of variables declaration//GEN-END:variables
}
