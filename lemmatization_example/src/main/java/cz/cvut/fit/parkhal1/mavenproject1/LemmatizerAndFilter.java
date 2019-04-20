/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.mavenproject1;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 *
 * @author parkh
 */
public class LemmatizerAndFilter {
    protected StanfordCoreNLP pipeline;
    
    //spojky a predlozky 
    private final ArrayList<String> UselessWords ; 

    public LemmatizerAndFilter() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        UselessWords = new ArrayList<>( Arrays.asList("and","a","the","but","to","in","at","as","from","yet","so","by","on","else",
                "no","for","nor","or","both","either","neither","although","because","if","before",
                "after","of","with","not","'s","oh","ah","ha","then","that","these","those",
                "about","what","who","where","whom","there","here","be","it","itself","I","they","he","she","we",
                "more","less","myself","you","have","just","than","my","do","much","how","why","without",
                "would","make","little","this","like","unlike","can","ok","each","under","between","above","very",
                "z","while","when","off","all","none","must","may","any","already","almost","too","-rrb-","its","--",
                "-lsb-")) ;
    
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    public HashSet<String> lemmatize(String documentText)
    {
        HashSet<String> lemmas = new LinkedHashSet<>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                String curr = token.get(LemmaAnnotation.class) ;
                
                if ( !UselessWords.contains(curr) && Pattern.matches("[a-zA-Z-]{3,}", curr) )
                    lemmas.add( curr.toLowerCase() );
            }
        }

        return lemmas;
    } 
    
    public static void main(String[] args) throws IOException {
        Sources src = new Sources("src/main/resources") ;
        LemmaStorage lemmaStorage = new LemmaStorage() ;
        LemmatizerAndFilter slem = new LemmatizerAndFilter();
        
        for ( Source source : src.getSources() ) 
            lemmaStorage.addLemmas( slem.lemmatize(source.getSource()), source.getFileId() ) ;
        
        lemmaStorage.printStorage() ;
        
        BooleanQueryParser parser = new BooleanQueryParser() ;
        parser.parse("heard OR edits OR forgot") ;
        
        TreeSet<Integer> res = lemmaStorage.getQueryResult(parser.getResult()) ;
        
        System.out.print("Query result = ") ;
        for ( Integer i: res )
            System.out.print(i+" ") ; 
        
        
    }
}