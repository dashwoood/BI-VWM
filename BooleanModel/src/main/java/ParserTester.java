
import cz.cvut.fit.parkhal1.Data_Structure.LemmaStorage;
import cz.cvut.fit.parkhal1.Data_Structure.Source;
import cz.cvut.fit.parkhal1.Data_Structure.Sources;
import cz.cvut.fit.parkhal1.Parsers.BooleanQueryParser;
import cz.cvut.fit.parkhal1.Lemmatizer_Filter.LemmatizerAndFilter;
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
public class ParserTester {
    
     public static void main(String[] args) throws IOException {
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            lemmaStorage.addLemmas( slem.lemmatize(source.getSource()), source.getFileId() ) ;
        
        lemmaStorage.printStorage() ;
        
        BooleanQueryParser parser = new BooleanQueryParser( lemmaStorage, src.getSources().size() ) ;
       
        try {
            TreeSet<Integer> res = parser.parse("count OR ( new AND something AND world ) OR hello") ;
            System.out.println( res.toString() ) ;
        } catch ( Exception ex ) {
            System.out.println( ex ) ;
        }
       
        /*
        TreeSet<Integer> res = lemmaStorage.getQueryResult(parser.parseSimple("heard OR edits OR forgot")) ;
        
        System.out.print("Query result = ") ;
        for ( Integer i: res )
            System.out.print( "ex"+i+".txt  ") ; 
        
        */
    }
}
