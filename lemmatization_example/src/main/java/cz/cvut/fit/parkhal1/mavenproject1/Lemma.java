/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import java.util.Objects;
import java.util.TreeSet;

/**
 *
 * @author parkh
 */
public class Lemma {
    private String lemma ;
    private TreeSet<Integer> documentIds ;

    public Lemma(String lemma) {
        this.lemma = lemma ;
        this.documentIds = new TreeSet<Integer>() ;
    }
    
    public Lemma( String lemma, Integer docId ) {
        this.lemma = lemma ;
        this.documentIds = new TreeSet<>() ;
        this.documentIds.add(docId) ;
    }
  
    public void addDoc ( Integer docId ) {
        this.documentIds.add(docId) ;
    }
    
    public String getLemma() {
        return lemma ;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma ;
    }

    public TreeSet<Integer> getDocumentIds() {
        return documentIds ;
    }

    public void setDocumentIds(TreeSet<Integer> documentIds) {
        this.documentIds = documentIds ;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.lemma);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lemma other = (Lemma) obj;
        if (!Objects.equals(this.lemma, other.lemma)) {
            return false;
        }
        return true;
    }
}
