/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }
  
}
