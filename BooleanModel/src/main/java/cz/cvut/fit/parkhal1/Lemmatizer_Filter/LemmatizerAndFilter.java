/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fit.parkhal1.Lemmatizer_Filter;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 *
 * @author parkh
 */

// Using extern library Stanford Lemmatizer 
public class LemmatizerAndFilter {
    protected StanfordCoreNLP pipeline;
    
    private final ArrayList<String> UselessWords ; 

    public LemmatizerAndFilter() {

        UselessWords = new ArrayList<>( Arrays.asList("and","a","the","but","to","in","at","as","from","yet","so","by","on","else",
                "no","for","nor","or","both","either","neither","although","because","if","before",
                "after","of","with","not","'s","oh","ah","ha","then","that","these","those",
                "about","what","who","where","whom","there","here","be","it","itself","I","they","he","she","we",
                "more","less","myself","you","have","just","than","my","do","much","how","why","without",
                "would","make","little","this","like","unlike","can","ok","each","under","between","above","very",
                "z","while","when","off","all","none","must","may","any","already","almost","too","-rrb-","its","--",
                "-lsb-","some","many","rather","another","other","again","over","moreover","however","-lrb-","-rsb-","ago",
                "along","also","into","now","one","two","which","since","say","never","ever","yes")) ;
    
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        this.pipeline = new StanfordCoreNLP(props);
    }

    public TreeSet<String> lemmatize(String documentText)
    {
        TreeSet<String> lemmas = new TreeSet<>();

        Annotation document = new Annotation(documentText);

        this.pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String curr = token.get(LemmaAnnotation.class) ;
                
                if ( !UselessWords.contains(curr) && Pattern.matches("[a-zA-Z-]{3,}", curr) )
                    lemmas.add( curr.toLowerCase() );
            }
        }
        return lemmas;
    } 
    
    public String lemmatizeOne(String documentText)
    {
        String result =  "";

        Annotation document = new Annotation(documentText);

        this.pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                String curr = token.get(LemmaAnnotation.class) ;
                
                if ( !UselessWords.contains(curr) && Pattern.matches( "[a-zA-Z-]{3,}", curr ) )
                    result = curr.toLowerCase() ;
            }
        }
        return result ;
    } 
    
   
}