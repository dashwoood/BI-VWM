/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import static jdk.nashorn.internal.runtime.JSType.toInteger;

/**
 *
 * @author parkh
 */
public class Source {
    private String source ;
    private String filename ;
    private Integer fileId ;
    
    Source ( String source, String filename ) {
        this.source = source ;
        this.filename = filename ;
        this.fileId = toInteger(filename.substring(21, filename.length()-4)) ;
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
    
    
    
}
