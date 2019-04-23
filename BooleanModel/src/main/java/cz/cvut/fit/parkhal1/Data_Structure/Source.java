/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Data_Structure;

import cz.cvut.fit.parkhal1.Lemmatizer_Filter.LemmatizerAndFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TreeSet;
import static jdk.nashorn.internal.runtime.JSType.toInteger;

/**
 *
 * @author parkh
 */
public class Source {
    private String source ;
    private String filename ;
    private Integer fileId ;
    private TreeSet<String> lemmas ;
    private LemmatizerAndFilter slem ;
    
    Source ( String source, String filename ) {
        this.source = source ;
        this.filename = filename ;
        this.fileId = toInteger(filename.substring(21, filename.length()-4)) ;
        this.slem = new LemmatizerAndFilter() ;
        this.lemmas = slem.lemmatize( source ) ;
    }

    public Source(String filename) {
        this.filename = filename;
    }
    
    public void setLemmas ( TreeSet<String> lemmas ) {
        this.lemmas = new TreeSet<>(lemmas) ;
    }
    
    public String findWord ( String input  ) {
        StringTokenizer st = new StringTokenizer( this.source ) ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter() ;
        ArrayList<String> res = new ArrayList<>() ;
        
        HashSet<String> output = new HashSet<>() ;
        
        while (st.hasMoreTokens()) {
           String tmp = st.nextToken() ;
           res.add(tmp) ;
           if ( slem.lemmatizeOne( tmp ).equals(slem.lemmatizeOne( input ) ) && !slem.lemmatizeOne( tmp ).equals("") ) {
               String result = "" ;
               result += ("..") ;
               
               if ( res.indexOf( tmp ) != 0 ) {
                    result += res.get(res.indexOf( tmp ) - 1) ;
                    result += (" ") ;
               }
               
               result += res.get(res.indexOf( tmp )) ;
               result += (" ") ;
               
               if ( st.hasMoreTokens() ) {
                    res.add( st.nextToken()) ;
                    result += res.get(res.indexOf( tmp ) + 1) ;
                    result += ("..  ") ;
               }
               output.add(result) ;
           }
        }
        String out = "" ;
        Integer newline = 35 ;
        for ( String s: output ) {
            if ( out.length() >= newline ) {
                out += "\n      " ;
                newline += 35 ;
            }
            out += s ;
        }
        
        return out ;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.fileId);
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
        final Source other = (Source) obj;
        if (!Objects.equals(this.fileId, other.fileId)) {
            return false;
        }
        return true;
    }

    public TreeSet<String> getLemmas() {
        return lemmas;
    }   
    
}
