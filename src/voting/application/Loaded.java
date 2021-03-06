/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Admin
 */

public class Loaded extends javax.swing.JFrame {
    
    /**
     * Creates new form Loaded
     */
    public static String sessionID;
    public Loaded() {
        initComponents();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        quesTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        quesTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        quesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Question", "Type", "Option A", "Option B", "Option C", "Option D", "Votes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(quesTable);
        if (quesTable.getColumnModel().getColumnCount() > 0) {
            quesTable.getColumnModel().getColumn(0).setResizable(false);
            quesTable.getColumnModel().getColumn(0).setPreferredWidth(330);
            quesTable.getColumnModel().getColumn(1).setResizable(false);
            quesTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            quesTable.getColumnModel().getColumn(2).setResizable(false);
            quesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(3).setResizable(false);
            quesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(4).setResizable(false);
            quesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(5).setResizable(false);
            quesTable.getColumnModel().getColumn(5).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(6).setResizable(false);
            quesTable.getColumnModel().getColumn(6).setPreferredWidth(25);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
//FUNCTION TO ADJUST ROW HEIGHT IN CASE OF A STRING THAT EXCEEDS CURRENT CELL LENGTH
private void updateRowHeights()
{
    for (int row = 0; row < quesTable.getRowCount(); row++)
    {
        int rowHeight = quesTable.getRowHeight();

        for (int column = 0; column < quesTable.getColumnCount(); column++)
        {
            Component comp = quesTable.prepareRenderer(quesTable.getCellRenderer(row, column), row, column);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
        }

        quesTable.setRowHeight(row, rowHeight);
    }
}
//FUNCTION TO BREAK QUESTION INTO VARIOUS STRINGS TO ADJUST A LONG STRING IN NEXT LINE (because /n does not work in tables)
private String splitString(String s){
    StringBuilder sb = new StringBuilder();
    int j = 0, left;
    sb.append("<html>");
    //SPLITTING AT EVERY 50 CHARACTER LENGTH
    for(int i = 51; i <= s.length();){
        left = i;
        //IF SPLIT HAPPENS AT A WHOLE WORD, THEN TRACE BACK TO A NON ALPHABET CHARACTER SO THAT WORD DOES NOT BREAKUP
        if(Character.isLetter(s.charAt(i))){
            while(Character.isLetter(s.charAt(left))) left--;
        }
        String sub = s.substring(j,left);
        sb.append(sub + "<br>");        //USING HTML LINE BREAKS 
        j += left;
        i += 51;
        if(i >= s.length()){
            while(i >= s.length()) i--;
            sb.append(s.substring(j,i));
            break;
        }
    }
    sb.append("</html>");
    return sb.toString();
}
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        quesTable.setShowGrid(true);          // show Horizontal and Vertical
        quesTable.setGridColor(Color.yellow);
        //CREATING A TABLE MODEL
        DefaultTableModel model = (DefaultTableModel)quesTable.getModel();
        
        String[] typeCheck = new String[3];
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VoteApp","root","root");
            Statement st = con.createStatement();
            String sql = "SELECT Descriptive, mcq, truefalse FROM Session_details where sessionID = " + sessionID + ";";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            //CHECKING WHICH CATEGORIES OF QUESTIONS DOES THE SESSION HAS
            String isDescriptive = rs.getString("Descriptive") == null ? "" : rs.getString("Descriptive");
            String isMcq = rs.getString("mcq") == null ? "" : rs.getString("mcq");
            String isTrueFalse = rs.getString("truefalse") == null ? "" : rs.getString("truefalse");
            //IF IT HAS DESCRIPTIVE TYPE QUESTIONS THEN DO THE FOLLOWING
            if(!isDescriptive.equals("")){
                String descriptiveQuery = "SELECT Question, Votes FROM type_descriptive WHERE SessionId = " + sessionID + " ORDER BY Votes DESC;";
                ResultSet descriptiveSet = st.executeQuery(descriptiveQuery);
            //LOAD ALL THE DESCRIPTIVE TYPE QUESTIONS ORDER BY HIGHEST OF VOTES
            while(descriptiveSet.next())
                {
                    String question = descriptiveSet.getString("Question");
                    //IF QUESTION LENGTH IS LONGER THAN THE CELL'S LENGTH, THEN SPLIT TO ADJUST IT IN BELOW LINES 
                    if(question.length() > 50)
                        question = splitString(question);
                    model.addRow(new Object[]{question,"Descriptive",null,null,null,null,descriptiveSet.getString("Votes")});
                }
            
            }
            //IF IT HAS MCQ TYPE QUESTIONS THEN DO THE FOLLOWING
            if(!isMcq.equals("")){
                String mcqQuery = "SELECT Question, Op_A, Op_B, Op_C, Op_D, Votes FROM type_mcq WHERE SessionId = " + sessionID + " ORDER BY Votes DESC;";
                ResultSet mcqSet = st.executeQuery(mcqQuery);
            //LOAD ALL THE MCQ TYPE QUESTIONS ORDER BY HIGHEST OF VOTES    
                while(mcqSet.next())
                {
                    String question = mcqSet.getString("Question");
                    //IF QUESTION LENGTH IS LONGER THAN THE CELL'S LENGTH, THEN SPLIT TO ADJUST IT IN BELOW LINES
                    if(question.length() > 50)
                        question = splitString(question);
                    model.addRow(new Object[] {question,"MCQ", mcqSet.getString("Op_A"),mcqSet.getString("Op_B"),mcqSet.getString("Op_C"),
                                    mcqSet.getString("Op_D"), mcqSet.getString("Votes")});
                }
            }
            //IF IT HAS TRUE/FALSE TYPE QUESTIONS THEN DO THE FOLLOWING
            if(!isTrueFalse.equals("")){
                String truefalseQuery = "SELECT Question, Votes FROM type_truefalse WHERE SessionId = " + sessionID + " ORDER BY Votes DESC;";
                ResultSet truefalseSet = st.executeQuery(truefalseQuery);
            //LOAD ALL THE TRUE/FALSE TYPE QUESTIONS ORDER BY HIGHEST OF VOTES
            while(truefalseSet.next())
                {
                    String question = truefalseSet.getString("Question");
                    //IF QUESTION LENGTH IS LONGER THAN THE CELL'S LENGTH, THEN SPLIT TO ADJUST IT IN BELOW LINES
                    if(question.length() > 50)
                        question = splitString(question);
                    model.addRow(new Object[] {question, "True/False", null, null, null, null, truefalseSet.getString("Votes")});
                }
            }
	}
        catch(Exception e)
        {
               JOptionPane.showMessageDialog(null, e);
        }
        //EXTEND THE ROW HEIGHT SO THAT SPLIT QUESTIONS ARE VISIBLE
        updateRowHeights();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Loaded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Loaded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Loaded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loaded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        sessionID = args[0];
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loaded().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable quesTable;
    // End of variables declaration//GEN-END:variables
}
