/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parkh
 */
import cz.cvut.fit.parkhal1.Data_Structure.LemmaStorage;
import cz.cvut.fit.parkhal1.Data_Structure.Source;
import cz.cvut.fit.parkhal1.Data_Structure.Sources;
import cz.cvut.fit.parkhal1.Parsers.BooleanQueryParser;
import cz.cvut.fit.parkhal1.Lemmatizer_Filter.LemmatizerAndFilter;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.swing.*;
 
public class GUIBooleanModel {
 
    public static void main(String s[]) throws IOException {
        
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            lemmaStorage.addLemmas( slem.lemmatize(source.getSource()), source.getFileId() ) ;
        
        lemmaStorage.printStorage() ;
        
        BooleanQueryParser parser = new BooleanQueryParser( lemmaStorage ) ;
        
        JFrame frame = new JFrame("Boolean Model") ;
 
        JPanel panel = new JPanel() ;
        panel.setLayout(new FlowLayout()) ;
        
        JTextArea result = new JTextArea( 20, 30 ) ;
        result.setFont(new Font("Courier", Font.BOLD, 25)) ;
 
        JLabel label = new JLabel("Enter boolean query: ") ;
        label.setFont(new Font("Courier", Font.BOLD, 25)) ;
        JTextField textfield = new JTextField( "", 30 ) ;
 
        JButton button = new JButton() ;
        button.setText("Submit") ;
        button.addActionListener( new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeSet<Integer> res = new TreeSet<>() ;
                try {
                    res = parser.parse(textfield.getText());
                } catch (Exception ex) {
                    Logger.getLogger(GUIBooleanModel.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                String output = "Query results: " ;
                
                if ( res.isEmpty() )
                    output += "Not found." ;
                
                for ( Integer i: res ) 
                    output += ( "\n"+"ex"+i+".txt ") ;
                
                result.setText(output) ;
            }

        });
        
        panel.add( label ) ;
        panel.add( textfield ) ;
        panel.add( button ) ;
        panel.add( result ) ;
 
        frame.add(panel) ;
        frame.setSize(800, 800) ;
        frame.setLocationRelativeTo(null) ;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        frame.setVisible(true) ;
 
    }
 
}
