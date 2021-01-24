/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting.application;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static voting.application.Loaded.sessionID;

/**
 *
 * @author Admin
 */
public class VoteQuestions extends javax.swing.JFrame {
    
    public static String sessionID, studentID;
    //ArrayList to store Question and its type_table
    ArrayList<ArrayList<String>> quesAndType = new ArrayList<>();
    //POINTER TO KEEP TRACK OF ARRAYLIST INDEX
    int index = 0;
    /**
     * Creates new form VoteQuestions
     */
    public VoteQuestions() {
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
        submit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        quesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Question", "Type", "Option A", "Option B", "Option C", "Option D", "Votes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(quesTable);
        if (quesTable.getColumnModel().getColumnCount() > 0) {
            quesTable.getColumnModel().getColumn(0).setPreferredWidth(300);
            quesTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            quesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(5).setPreferredWidth(100);
            quesTable.getColumnModel().getColumn(6).setPreferredWidth(10);
        }

        submit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        submit.setText("Submit your Votes");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(462, 462, 462)
                .addComponent(submit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit)
                .addGap(0, 13, Short.MAX_VALUE))
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
        
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        quesTable.setShowGrid(true);          // show Horizontal and Vertical
        quesTable.setGridColor(Color.yellow);
        
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
                    model.addRow(new Object[]{question,"Descriptive",null,null,null,null});
                    quesAndType.add(index, new ArrayList<>(Arrays.asList(descriptiveSet.getString("Question"),"type_descriptive")));
                    index++;
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
                                    mcqSet.getString("Op_D")});
                    quesAndType.add(index, new ArrayList<>(Arrays.asList(mcqSet.getString("Question"),"type_mcq")));
                    index++;
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
                    model.addRow(new Object[] {question, "True/False", null, null, null, null});
                    quesAndType.add(index, new ArrayList<>(Arrays.asList(truefalseSet.getString("Question"),"type_truefalse")));
                    index++;
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

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        DefaultTableModel model = (DefaultTableModel)quesTable.getModel();
        int n = model.getRowCount();
        //ARRAY TO STORE 0 or 1 FOR WHICH QUESTIONS STUDENT VOTED
        int[] myVotes = new int[n];
        for(int i = 0; i < n; i++){
            Boolean b = (Boolean) model.getValueAt(i,6);        //THE CHECKBOXES RETURN Boolean type SO UNCHECKED BOXES WILL RETURN null INSTEAD OF false.
            //null NEEDS TO BE CHECKED BEFORE true TO AVOID NULL POINTER EXCEPTION
            if(b == null)    
               myVotes[i] = 0;
            else if(b == true) 
               myVotes[i] = 1;
        }
       
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/VoteApp","root","root");
            Statement st = con.createStatement();
            //UPDATE VOTE COUNT OF EACH QUESTION BY ADDING 0 OR 1 
            for(int i = 0; i < n; i++){
                String update = "UPDATE " + quesAndType.get(i).get(1) + " SET Votes = Votes + " + myVotes[i] + " WHERE Question = '" + quesAndType.get(i).get(0) +
                        "' AND SessionID = " + sessionID + ";";
                int rs1 = st.executeUpdate(update);
            }
            //UPDATE THE ATTEMPTED TABLE 
            String attemptID = "S" + sessionID;
            String setAttempt = "UPDATE Attempted SET " + attemptID + " = 'Yes' WHERE StudentID = " + studentID + ";";
            int res = st.executeUpdate(setAttempt);
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        this.dispose();
    }//GEN-LAST:event_submitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        sessionID = args[0];
        studentID = args[1];
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
            java.util.logging.Logger.getLogger(VoteQuestions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VoteQuestions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VoteQuestions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VoteQuestions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VoteQuestions().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable quesTable;
    private javax.swing.JButton submit;
    // End of variables declaration//GEN-END:variables
}