package com.swanhtetaungphyo.article_approval.contract;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface NLPServices {

    String[] SentenceProcessing(String text) throws IOException;
    String[][] TokenProcessing(String[] Sentences) throws  IOException;
    boolean DecisionMaking(String[][] tokens) throws FileNotFoundException;
}
