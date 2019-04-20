/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/**
 *
 * @author parkh
 */
public class LemmaStorage {

    private HashSet<Lemma> storage ;

    public LemmaStorage() {
        this.storage = new HashSet<Lemma>() ;
    }

    public void addLemmas ( HashSet<String> lemmas, Integer docId ) {
        for ( String lemma : lemmas ) {
            if ( storage.contains( new Lemma(lemma) ) ) {
               for ( Lemma l: storage ) {
                   if ( l.getLemma().equals(lemma) ) {
                       l.addDoc(docId) ;
                       l.getDocumentIds() ;
                       break ;
                   }
               }  
            } else {        
                storage.add( new Lemma( lemma, docId ) ) ;
            }

        }
    }
    
    public void addLemma ( Lemma lemma ) {
        this.storage.add( lemma ) ;
    }

    public HashSet<Lemma> getStorage() {
        return storage;
    }

    public void setStorage(HashSet<Lemma> storage) {
        this.storage = storage;
    }

    public void printStorage() {
        for ( Lemma l : this.storage ) {
            System.out.print( l.getLemma() ) ;
            System.out.print(" ") ;
            for ( Integer doc : l.getDocumentIds() ) {
                System.out.print("-->") ;
                System.out.print(doc) ; 
            }
            System.out.println() ;
        }
    }
    
    public Lemma getLemma ( String lemma ) {
        for ( Lemma l : this.storage ) {
            if ( l.getLemma().equals( lemma ) )
                return l ;
        }
        
        return new Lemma("") ;
    }
    
    public TreeSet<Integer> getQueryResult( ArrayList<ArrayList<String>> parsedQuery ) {
        TreeSet<Integer> result = new TreeSet<>() ;
                
        for ( ArrayList<String> list: parsedQuery ) {
            Lemma lemma = getLemma(list.get(0)) ;
            TreeSet<Integer> mergedIndexes = new TreeSet<>( lemma.getDocumentIds() ) ;
            for ( int i = 1; i < list.size() ; i++ ) {
                lemma = getLemma( list.get(i) ) ;
                mergedIndexes.retainAll( lemma.getDocumentIds() ) ;
            }
            result.addAll( mergedIndexes ) ;
        }
        return result ;
    }
}
