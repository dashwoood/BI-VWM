/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.BooleanParser;

import cz.cvut.fit.parkhal1.lemmatizerAndFilter.LemmatizerAndFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author parkh
 */
public class BooleanQueryParser {
    
    LemmatizerAndFilter slem ;
    
    public BooleanQueryParser () {
        slem = new LemmatizerAndFilter();
    }
    
    public ArrayList<ArrayList<String>> parse ( String inputQuery, String previous ) {
        ArrayList<ArrayList<String>> result = new ArrayList<>() ;
        StringTokenizer st = new StringTokenizer( inputQuery );
        ArrayList<String> currentPart = new ArrayList<>() ;
        while (st.hasMoreTokens()) {
            currentPart.add( st.nextToken() ) ;
        }
        
        for ( int i = 0 ; i < currentPart.size() ; i++ )
        {   
            if ( currentPart.get(i).equals("(") ) {
                String tmp = "" ;
                int skip = 0 ;
                
                while ( true ) {
                    i++ ;
                    if ( currentPart.get(i).equals(")") && skip != 0 )
                        skip -- ;
                    else if ( currentPart.get(i).equals("(") )
                        skip ++ ;
                    else if ( currentPart.get(i).equals(")") && skip == 0 )
                        break ;
              
                    tmp += currentPart.get(i) ;
                    tmp += " " ;
                }
                System.out.println( tmp );
                
                if ( currentPart.get(1).equals("OR") || currentPart.get(2).equals("OR") )
                    parse( tmp, "OR" ) ;
                else if ( currentPart.get(1).equals("AND") || currentPart.get(2).equals("AND") )
                    parse( tmp, "AND" ) ;
            }
        }

        return result ;
    }
    
    
    public ArrayList<ArrayList<String>> parseSimple ( String inputQuery ) {
        ArrayList<ArrayList<String>> result = new ArrayList<>() ;
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
        return result ;
    }

    public void printResult ( ArrayList<ArrayList<String>> result ) {
        for ( ArrayList l: result ) {
           System.out.print("[") ;
           for ( Object s: l ) {
                System.out.print(s) ;
                System.out.print(" ") ;
           }
           System.out.print("]") ;
        }
        System.out.println(); 
    }

}