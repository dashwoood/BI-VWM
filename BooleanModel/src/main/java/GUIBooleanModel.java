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
import cz.cvut.fit.parkhal1.Parsers.SimpleSearchParser;
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
        
        /* Preprocessing */
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            lemmaStorage.addLemmas( slem.lemmatize(source.getSource()), source.getFileId() ) ;
        
        lemmaStorage.printStorage() ;
        
        /* GUI */
        JFrame frame = new JFrame("Boolean Model") ;
        JPanel panel = new JPanel() ;
        panel.setLayout(new FlowLayout()) ;
        JTextArea result = new JTextArea( 20, 20 ) ;
        result.setFont(new Font("Courier", Font.BOLD, 25)) ;
        JLabel label = new JLabel("Enter boolean query: ") ;
        label.setFont(new Font("Courier", Font.BOLD, 25)) ;
        JTextField textfield = new JTextField( "", 45 ) ;
 
        JButton button = new JButton() ;
        button.setText("Submit") ;
        button.addActionListener( new ActionListener() { 
            
            @Override
            public void actionPerformed(ActionEvent e) {
                BooleanQueryParser parser = new BooleanQueryParser( lemmaStorage, src.getSources().size() ) ;
                SimpleSearchParser parserSimple = new SimpleSearchParser( src, src.getSources().size() ) ;
                
                TreeSet<Integer> res = new TreeSet<>() ;
                String query = textfield.getText() ;
                
                /* Simple Search */
                long startSimple = System.nanoTime();  
                try {  
                    parserSimple.parse( query ) ;
                } catch (Exception ex) {
                    Logger.getLogger(GUIBooleanModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                long elapsedTimeSimple = System.nanoTime()- startSimple ;
                
                /* Inverted index */
                long start = System.nanoTime();  
                try {  
                    res = parser.parse( query ) ;
                } catch (Exception ex) {
                    Logger.getLogger(GUIBooleanModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                long elapsedTime = System.nanoTime()- start ;
                
                String output = ( "Time elapsed using inverted index : " + elapsedTime + " ns\n" ) ;
                output += ( "Time elapsed using sequential search : " + elapsedTimeSimple + " ns\n" ) ;
                output += ( "Benefit : " + elapsedTimeSimple/elapsedTime + "x faster\n\n" ) ;
                output += "Query results: " ;
                
                if ( res.isEmpty() )
                    output += "Not found.\n" ;
                else 
                    output += ( res.size() + " document(s) found.\n" ) ;
                
                /* Query result */
                for ( Integer i: res ) {
                    output += ( "\n"+"ex"+i+".txt :\n      ") ;
                    Integer newline = 45 + output.length() ;
                    for ( String term : parser.getTerms() ) {
                        if ( output.length() >= newline && !src.getSourceById(i).findWord( term ).isEmpty()) {
                            output += "\n      " ;
                            newline += 50  ;
                        }
                        output += src.getSourceById(i).findWord( term ) ;
                    }
                }
                result.setText(output) ;
            }

        });
        
        panel.add( label ) ;
        panel.add( textfield ) ;
        panel.add( button ) ;
        panel.add( result ) ;
 
        frame.add(panel) ;
        frame.setSize(1000, 1000) ;
        frame.setLocationRelativeTo(null) ;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        frame.setVisible(true) ;
 
    }
 
}
