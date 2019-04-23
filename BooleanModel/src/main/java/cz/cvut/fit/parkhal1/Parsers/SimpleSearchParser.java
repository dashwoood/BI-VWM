/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Parsers;

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
    private LemmatizerAndFilter slem ;
    private ArrayList<String> query ;
    private Sources src ;
    private TreeSet<Integer> docIds ;
    private ArrayList<String> terminals = new ArrayList<>( Arrays.asList("OR", "AND", "NOT", "(", ")")) ;
    
    public SimpleSearchParser ( Sources src, Integer n ) {
        this.slem = new LemmatizerAndFilter() ;
        this.src = src ;
        this.docIds = new TreeSet<>() ;
        this.query = new ArrayList<>() ;
        IntStream.range( 1, n + 1 ).forEach(val -> this.docIds.add(val)) ;
    }
    
    public ArrayList<String> tokenize ( String input ) {
        ArrayList<String> tokens = new ArrayList<>() ;
        StringTokenizer st = new StringTokenizer( input ) ;
        this.query.clear() ;
        
        while ( st.hasMoreTokens() ) {
            tokens.add( st.nextToken() ) ;
        }
        
        return tokens ;
    }
    
    public void expect ( String what ) throws Exception {
  
       if ( this.query.get(1).equals(what) ) {
           this.query.remove(1) ;
       } else 
           throw new Exception(this.query.get(1) +" "+ what ) ;
           
    }
    
    public void expectLater ( String what ) throws Exception {
  
       if ( this.query.get(2).equals(what) ) {
           this.query.remove(2) ;
       } else 
           throw new Exception(this.query.get(2) +" "+ what ) ;
           
    }
    
    public void expectF ( String what ) throws Exception {
  
       if ( this.query.get(0).equals(what) ) {
           this.query.remove(0) ;
       } else 
           throw new Exception(this.query.get(1) +" "+ what ) ;
           
    }
    
    public TreeSet<Integer> E () throws Exception {
        if ( this.query.size() > 2 && ( this.query.get(1).equals("OR") || this.query.get(2).equals("OR") )) {
            if ( this.query.get(2).equals("OR") )
                expectLater("OR") ;
            else 
                expect( "OR" ) ;
            TreeSet<Integer> t = T() ;
            TreeSet<Integer> e = E() ;
            
            while ( true ) {
                if ( this.query.size() > 1 && this.query.get(0).equals(")") && this.query.get(1).equals(")") )
                    expectF(")") ;
                else
                    break ;
            }
            
            if ( this.query.size() > 2 && this.query.get(0).equals(")") && this.query.get(1).equals("OR") ) {
                expectF(")") ;
                expectF("OR") ;
                e.addAll( E() ) ;
                e.addAll(t) ;
            } else if ( this.query.size() > 2 && this.query.get(0).equals(")") && this.query.get(1).equals("AND") ) {
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
        
        if ( this.query.size() > 2 && ( this.query.get(1).equals("AND") || this.query.get(2).equals("AND") ) ) {
            if ( this.query.get(2).equals("AND") )
                expectLater("AND") ;
            else 
                expect( "AND" ) ;
            TreeSet<Integer> f = F() ;
            TreeSet<Integer> t = T() ;
            
            while ( true ) {
                if ( this.query.size() > 1 && this.query.get(0).equals(")") && this.query.get(1).equals(")") )
                    expectF(")") ;
                else
                    break ;
            }
            
            if ( this.query.size() > 2 && this.query.get(0).equals(")") && this.query.get(1).equals("AND") ) {
                expectF(")") ;
                expectF("AND") ;
                t.retainAll( T() ) ;
                t.retainAll(f) ;
            } else if ( this.query.size() > 2 && this.query.get(0).equals(")") && this.query.get(1).equals("OR") ) {
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
        if ( this.query.get(0).equals("(") ) {
            expectF( "(" ) ;
            TreeSet<Integer> e = E() ;
            //expectF( ")" ) ;
            return e ;
        } else if ( this.query.size() > 2 && this.query.get(1).equals("(") &&  this.query.get(0).equals("NOT") ) {
            expectF( "NOT" ) ;
            expectF( "(" ) ;
            TreeSet<Integer> e = E() ;
            TreeSet<Integer> tmp = (TreeSet<Integer>) this.docIds.clone() ;
            tmp.removeAll(e) ;
            return tmp ;
        } else if ( this.query.get(0).equals("NOT") ) {
            expectF( "NOT" ) ;    
            TreeSet<Integer> res = new TreeSet<>() ;
            for ( Integer i: this.docIds ) {
                if ( this.src.getSourceById(i).getLemmas().contains(this.slem.lemmatizeOne(this.query.get(0))) ) 
                    res.add(i) ;
            }
            TreeSet<Integer> tmp = (TreeSet<Integer>) this.docIds.clone() ;
            tmp.removeAll(res) ;
            this.query.remove(0) ;
            return tmp ;
        } else {
            TreeSet<Integer> res = new TreeSet<>() ;
            for ( Integer i: this.docIds ) {
                if ( this.src.getSourceById(i).getLemmas().contains(this.slem.lemmatizeOne(this.query.get(0))) ) 
                    res.add(i) ;
            }
            this.query.remove(0) ;
            return res ;
        }
    }
    
    public TreeSet<Integer> parse ( String inputQuery ) throws Exception { 
        this.query = tokenize( inputQuery ) ;
        if ( this.query.size() == 1 ) {
            TreeSet<Integer> res = new TreeSet<>() ;
            for ( Integer i: this.docIds ) {
                if ( this.src.getSourceById(i).getLemmas().contains(this.slem.lemmatizeOne(this.query.get(0))) ) 
                    res.add(i) ;
            }
        }
        
        return E() ;
    }
}
