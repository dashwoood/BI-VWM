/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.dataStructure;

import cz.cvut.fit.parkhal1.lemmatizerAndFilter.LemmatizerAndFilter;
import java.util.ArrayList;
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
    
    Source ( String source, String filename ) {
        this.source = source ;
        this.filename = filename ;
        this.fileId = toInteger(filename.substring(21, filename.length()-4)) ;
    }
    
    public void setLemmas ( TreeSet<String> lemmas ) {
        this.lemmas = new TreeSet<>(lemmas) ;
    }
    
    public String findWord ( String input  ) {
        StringTokenizer st = new StringTokenizer( this.source ) ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter() ;
        ArrayList<String> res = new ArrayList<>() ;
        
        String result = "" ;
        
        while (st.hasMoreTokens()) {
           String tmp = st.nextToken() ;
           res.add(tmp) ;
           if ( slem.lemmatize( tmp ).equals(slem.lemmatize( input ) )) {
               result += ("..") ;
               result += res.get(res.indexOf( tmp ) - 1) ;
               result += (" ") ;
               result += res.get(res.indexOf( tmp )) ;
               result += (" ") ;
               result += res.get(res.indexOf( tmp ) + 1) ;
               result += ("..") ;
           }
        }
        
        return result ;
    }

    public String getLine ( ArrayList<ArrayList<String>> parsedQuery ) {
        String line = "" ;
        
        for ( ArrayList<String> list: parsedQuery ) {
            for ( String word : list )
                line += ( findWord( word ) ) ;
        }
        
        return line ;
        //Line with desired lemmas ...
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
