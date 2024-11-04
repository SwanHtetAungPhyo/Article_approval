package com.swanhtetaungphyo.article_approval.contract;

import com.swanhtetaungphyo.article_approval.utils.InternalResponse;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface NLPServices {

    String[] SentenceProcessing(String text) throws IOException;
    String[][] TokenProcessing(String[] Sentences) throws  IOException;
    InternalResponse DecisionMaking(String[][] tokens) throws FileNotFoundException;
}
