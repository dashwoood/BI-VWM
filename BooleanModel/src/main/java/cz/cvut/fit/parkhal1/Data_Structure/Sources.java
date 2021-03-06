/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Data_Structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parkh
 */
public class Sources {
    private ArrayList<Source> sources ; 
    
    public Sources ( String dirPath ) throws IOException {
       sources = new ArrayList<>() ;
        
       Files.walk(Paths.get( dirPath ))
                .filter(Files::isRegularFile)
                .forEach( filePath -> { try {
                                            readFile( filePath.toString() ) ;
                                        } catch (IOException ex) {
                                            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                       } );
    }
    
    private void readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            sources.add( new Source( stringBuilder.toString(), file ) ) ;
        } finally {
            reader.close();
        }
    }
    
    public Source getSourceById ( Integer id ) {
        for ( Source s: sources ) {
            if ( s.getFileId().equals(id) )
                return s ;
        }  
        return new Source("NULL") ;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }
    
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //only for testing purposes 
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    
    public TreeSet<Integer> getQueryResult( ArrayList<ArrayList<String>> parsedQuery ) {
        TreeSet<Integer> result = new TreeSet<>() ;
                
        for ( ArrayList<String> list: parsedQuery ) {
            TreeSet<Integer> mergedIndexes = new TreeSet<>() ;
            for ( int i = 1; i < list.size() ; i++ ) {
                TreeSet<Integer> current = new TreeSet<>() ;
                for ( Source src : this.sources ) {
                    if ( src.getLemmas().contains( list.get(i))) 
                        current.add( src.getFileId() ) ;
                }
                if ( mergedIndexes.isEmpty() )
                    mergedIndexes.addAll( current ) ;
                else 
                    mergedIndexes.retainAll( current ) ;
            }
            result.addAll( mergedIndexes ) ;
        }
        return result ;
    }
}
