
import cz.cvut.fit.parkhal1.dataStructure.LemmaStorage;
import cz.cvut.fit.parkhal1.dataStructure.Source;
import cz.cvut.fit.parkhal1.dataStructure.Sources;
import cz.cvut.fit.parkhal1.BooleanParser.BooleanQueryParser;
import cz.cvut.fit.parkhal1.lemmatizerAndFilter.LemmatizerAndFilter;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parkh
 */
public class GUISimpleSearch {
    public static void main(String s[]) throws IOException {
        
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            source.setLemmas( slem.lemmatize(source.getSource()) ) ;
        
        BooleanQueryParser parser = new BooleanQueryParser() ;
        
        JFrame frame = new JFrame("Boolean Model") ;
 
        JPanel panel = new JPanel() ;
        panel.setLayout(new FlowLayout()) ;
        
        JLabel result = new JLabel() ;
 
        JLabel label = new JLabel("Enter boolean query: ") ;
        label.setFont(new Font("Courier", Font.BOLD, 25)) ;
        JTextField textfield = new JTextField( "", 30 ) ;
 
        JButton button = new JButton() ;
        button.setText("Submit") ;
        button.addActionListener( new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                TreeSet<Integer> res = src.getQueryResult(parser.parseSimple(textfield.getText())) ;
        
                String output = "Query results: " ;
                for ( Integer i: res )
                    output += ( "ex"+i+".txt  ") ;
                
                result.setText(output) ;
            }

        });
        
        panel.add( label ) ;
        panel.add( textfield ) ;
        panel.add( button ) ;
        panel.add( result ) ;
 
        frame.add(panel) ;
        frame.setSize(1000, 200) ;
        frame.setLocationRelativeTo(null) ;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        frame.setVisible(true) ;
 
    }
}
