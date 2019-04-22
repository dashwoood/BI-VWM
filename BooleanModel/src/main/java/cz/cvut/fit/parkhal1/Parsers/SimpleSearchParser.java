/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Parsers;

import cz.cvut.fit.parkhal1.Data_Structure.Lemma;
import cz.cvut.fit.parkhal1.Data_Structure.Sources;
import cz.cvut.fit.parkhal1.Lemmatizer_Filter.LemmatizerAndFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 *
 * @author parkh
 */
public class SimpleSearchParser {
    LemmatizerAndFilter slem ;
    ArrayList<String> query ;
    Sources src ;
    ArrayList<Integer> docIds ;
    ArrayList<String> terminals = new ArrayList<>( Arrays.asList("OR", "AND", "NOT", "(", ")")) ;
    
    public SimpleSearchParser ( Sources src, Integer n ) {
        this.slem = new LemmatizerAndFilter() ;
        this.src = src ;
        this.docIds = new ArrayList<>() ;
        IntStream.range( 1, n + 1 ).forEach(val -> docIds.add(val));
    }
    
    public ArrayList<String> tokenize ( String input ) {
        ArrayList<String> tokens = new ArrayList<>() ;
        StringTokenizer st = new StringTokenizer( input ) ;
        
        while ( st.hasMoreTokens() ) {
            tokens.add( st.nextToken() ) ;
        }
        
        return tokens ;
    }
    
    public void expect ( String what ) throws Exception {
  
       if ( query.get(1).equals(what) ) {
           query.remove(1) ;
       } else 
           throw new Exception(query.get(1) +" "+ what ) ;
           
    }
    
    public void expectF ( String what ) throws Exception {
  
       if ( query.get(0).equals(what) ) {
           query.remove(0) ;
       } else 
           throw new Exception(query.get(1) +" "+ what ) ;
           
    }
    
    public TreeSet<Integer> E () throws Exception {
        if ( query.size() != 1 && query.get(1).equals("OR") ) {
            expect( "OR" ) ;
            TreeSet<Integer> t = T() ;
            TreeSet<Integer> e = E() ;
            
            while ( true ) {
                if ( query.size() > 1 && query.get(0).equals(")") && query.get(1).equals(")") )
                    expectF(")") ;
                else
                    break ;
            }
            
            if ( query.size() > 2 && query.get(0).equals(")") && query.get(1).equals("OR") ) {
                expectF(")") ;
                expectF("OR") ;
                e.addAll( E() ) ;
                e.addAll(t) ;
            } else if ( query.size() > 2 && query.get(0).equals(")") && query.get(1).equals("AND") ) {
                expectF(")") ;
                expectF("AND") ;
                e.addAll(t) ;
                e.retainAll( T() ) ;
            }else 
                e.addAll(t) ;
            return e ;
        } else 
            return T() ;
    }
    
    public TreeSet<Integer> T () throws Exception {
        
        if ( query.size() != 1 && query.get(1).equals("AND") ) {
            expect( "AND" ) ;
            TreeSet<Integer> f = F() ;
            TreeSet<Integer> t = T() ;
            
            while ( true ) {
                if ( query.size() > 1 && query.get(0).equals(")") && query.get(1).equals(")") )
                    expectF(")") ;
                else
                    break ;
            }
            
            if ( query.size() > 2 && query.get(0).equals(")") && query.get(1).equals("AND") ) {
                expectF(")") ;
                expectF("AND") ;
                t.retainAll( T() ) ;
                t.retainAll(f) ;
            } else if ( query.size() > 2 && query.get(0).equals(")") && query.get(1).equals("OR") ) {
                expectF(")") ;
                expectF("OR") ;
                t.retainAll(f) ;
                t.addAll( E() ) ;
            } else 
                t.retainAll(f) ;
            
            return t ;
        } else 
            return F() ;
    }
    
    public TreeSet<Integer> F () throws Exception {
        if ( query.get(0).equals("(") ) {
            expectF( "(" ) ;
            TreeSet<Integer> e = E() ;
            //expectF( ")" ) ;
            return e ;
        } else {
            TreeSet<Integer> res = new TreeSet<>() ;
            for ( Integer i: docIds ) {
                if ( this.src.getSourceById(i).getLemmas().contains(slem.lemmatizeOne(query.get(0))) ) 
                    res.add(i) ;
            }
            query.remove(0) ;
            return res ;
        }
    }
    
    public TreeSet<Integer> parse ( String inputQuery ) throws Exception { 
        this.query = tokenize( inputQuery ) ;

        if ( query.size() == 1 ) {
            TreeSet<Integer> res = new TreeSet<>() ;
            for ( Integer i: docIds ) {
                if ( this.src.getSourceById(i).getLemmas().contains(slem.lemmatizeOne(query.get(0))) ) 
                    res.add(i) ;
            }
        }
        
        return E() ;
    }
}
