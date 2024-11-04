package com.swanhtetaungphyo.article_approval.services;


import com.swanhtetaungphyo.article_approval.contract.NLPServices;
import com.swanhtetaungphyo.article_approval.utils.ResourceLoader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

@Component
public class NLProcessor implements NLPServices {
    private final Logger logger = LogManager.getLogger();

    @Override
    public String[] SentenceProcessing(String text) throws IOException {
        String[] sentences;

        try (InputStream sentenceModelStream = getClass().getClassLoader().getResourceAsStream("NLPmodels/opennlp-en-ud-ewt-sentence-1.1-2.4.0.bin")) {
            if (sentenceModelStream == null) {
                throw new IOException("Sentence model file not found.");
            }
            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

            sentences = sentenceDetector.sentDetect(text);
            logger.atInfo().log("Sentence detection is completed");

            return sentences;
        } catch (IOException e) {
            logger.atError().log("Error loading sentence model: {}", e.getMessage());
            throw new IOException("Cannot load the sentence model", e);
        }
    }

    @Override
    public String[][] TokenProcessing(String[] sentences) throws IOException {
        String[][] tokens = new String[sentences.length][];

        try (InputStream tokenModelStream = getClass().getClassLoader().getResourceAsStream("NLPmodels/opennlp-en-ud-ewt-tokens-1.1-2.4.0.bin")) {
            if (tokenModelStream == null) {
                throw new IOException("Tokenizer model file not found.");
            }
            TokenizerModel tokenizerModel = new TokenizerModel(tokenModelStream);
            TokenizerME tokenizerME = new TokenizerME(tokenizerModel);

            for (int i = 0; i < sentences.length; i++) {
                tokens[i] = tokenizerME.tokenize(sentences[i]);
            }

            logger.atInfo().log("Tokenization is completed");
        } catch (IOException e) {
            logger.atError().log("Error loading tokenizer model: {}", e.getMessage());
            throw new IOException("Cannot load the token NLPmodels", e);
        }
        return tokens;
    }

    @Override
    public boolean DecisionMaking(String[][] tokens) throws FileNotFoundException {
        HashSet<String> hashSet = ResourceLoader.LoadSwearWordFromFile();
        for(String[] tokensArray : tokens){
            for(String token : tokensArray){
                String clearToken = token.toLowerCase().replaceAll("[^a-zA-Z]", "");
                if(hashSet.contains(token)){
                    System.out.println("Swear word detected: " + clearToken);
                    logger.log(Level.DEBUG, clearToken);
                    return false;
                }
            }
        }
        return true;
    }


}
