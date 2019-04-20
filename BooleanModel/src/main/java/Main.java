
import cz.cvut.fit.parkhal1.dataStructure.LemmaStorage;
import cz.cvut.fit.parkhal1.dataStructure.Source;
import cz.cvut.fit.parkhal1.dataStructure.Sources;
import cz.cvut.fit.parkhal1.BooleanParser.BooleanQueryParser;
import cz.cvut.fit.parkhal1.lemmatizerAndFilter.LemmatizerAndFilter;
import java.io.IOException;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parkh
 */
public class Main {
    
     public static void main(String[] args) throws IOException {
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            lemmaStorage.addLemmas( slem.lemmatize(source.getSource()), source.getFileId() ) ;
        
        //lemmaStorage.printStorage() ;
        
        BooleanQueryParser parser = new BooleanQueryParser() ;
        parser.parse("( heard OR ( edits OR ( forgot AND edits ) ) ) ", "") ;
        /*
        TreeSet<Integer> res = lemmaStorage.getQueryResult(parser.parseSimple("heard OR edits OR forgot")) ;
        
        System.out.print("Query result = ") ;
        for ( Integer i: res )
            System.out.print( "ex"+i+".txt  ") ; 
        
        */
    }
}
