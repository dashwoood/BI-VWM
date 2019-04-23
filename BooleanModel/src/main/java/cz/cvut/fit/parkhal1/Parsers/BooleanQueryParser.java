/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Parsers;

import cz.cvut.fit.parkhal1.Data_Structure.LemmaStorage;
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
public class BooleanQueryParser {
    
    private LemmatizerAndFilter slem ;
    private ArrayList<String> query ;
    private LemmaStorage lemmaStorage ;
    private ArrayList<String> terms ;
    private TreeSet<Integer> docIds ;
    private ArrayList<String> terminals = new ArrayList<>( Arrays.asList("OR", "AND", "NOT", "(", ")")) ;
    
    public BooleanQueryParser ( LemmaStorage lemmaStorage, Integer n ) {
        this.slem = new LemmatizerAndFilter() ;
        this.lemmaStorage = lemmaStorage ;
        this.terms = new ArrayList<>() ;
        this.docIds = new TreeSet<>() ;
        IntStream.range( 1, n + 1 ).forEach(val -> this.docIds.add(val)) ;
    }
    
    public ArrayList<String> tokenize ( String input ) {
        ArrayList<String> tokens = new ArrayList<>() ;
        StringTokenizer st = new StringTokenizer( input ) ;
        this.terms.clear() ;
        
        while ( st.hasMoreTokens() ) {
            tokens.add( st.nextToken() ) ;
            if ( !this.terminals.contains(tokens.get( tokens.size() - 1)) )
                this.terms.add(tokens.get( tokens.size() - 1)) ;
        }
        
        return tokens ;
    }

    public ArrayList<String> getTerms() {
        return this.terms;
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
        } else if ( this.query.size() > 2 && this.query.get(1).equals("(") && this.query.get(0).equals("NOT") ) {
            expectF( "NOT" ) ;
            expectF( "(" ) ;
            TreeSet<Integer> e = E() ;
            TreeSet<Integer> tmp = (TreeSet<Integer>) this.docIds.clone() ;
            tmp.removeAll(e) ;
            return tmp ;
        } else if ( this.query.get(0).equals("NOT") ) {
            expectF( "NOT" ) ;    
            TreeSet<Integer> f = (TreeSet<Integer>) this.lemmaStorage.getLemma( this.slem.lemmatizeOne(this.query.get(0))).getDocumentIds().clone() ;
            TreeSet<Integer> tmp = (TreeSet<Integer>) this.docIds.clone() ;
            tmp.removeAll(f) ;
            this.query.remove(0) ;
            return tmp ;
        } else {
            TreeSet<Integer> f = (TreeSet<Integer>) this.lemmaStorage.getLemma( this.slem.lemmatizeOne(this.query.get(0))).getDocumentIds().clone() ;
            this.query.remove(0) ;
            return f ;
        }
    }
    
    public TreeSet<Integer> parse ( String inputQuery ) throws Exception { 
        this.query = tokenize( inputQuery ) ;
        
        if ( query.size() == 1 )
            return (TreeSet<Integer>) this.lemmaStorage.getLemma( this.slem.lemmatizeOne(this.query.get(0))).getDocumentIds().clone() ;
        
        return E() ;
    }
    
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //only for testing purposes 
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    
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