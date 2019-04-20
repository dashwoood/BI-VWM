/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author parkh
 */
public class BooleanQueryParser {
    
    private ArrayList<ArrayList<String>> result ;
    LemmatizerAndFilter slem ;
    
    BooleanQueryParser () {
        result = new ArrayList<>() ;
        slem = new LemmatizerAndFilter();
    }
    
    public void parse ( String inputQuery ) {
        StringTokenizer st = new StringTokenizer( inputQuery );
        ArrayList<String> currentBlock = new ArrayList<>() ;
        currentBlock.add(st.nextToken()) ;
        while (st.hasMoreTokens()) {
            String tmp = st.nextToken() ;
            
            if ( tmp.equals("AND") ) {
                currentBlock.addAll( slem.lemmatize(st.nextToken()) ) ;
            }
            else if ( tmp.equals("OR") ) {
                result.add( currentBlock ) ;
                currentBlock = new ArrayList<>() ;
                currentBlock.addAll( slem.lemmatize(st.nextToken()) ) ;
            }

        }
        result.add(currentBlock) ;
        
    }

    public ArrayList<ArrayList<String>> getResult() {
        return result;
    }

    public void printResult () {
        for ( ArrayList l: result ) {
           for ( Object s: l ) {
                System.out.print(s) ;
                System.out.print(" ") ;
           }
           System.out.println(); 
        }
    }
    
    public void setResult(ArrayList<ArrayList<String>> result) {
        this.result = result;
    }
}
