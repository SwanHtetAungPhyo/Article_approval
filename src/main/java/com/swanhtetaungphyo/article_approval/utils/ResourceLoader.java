package com.swanhtetaungphyo.article_approval.utils;


import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;


@Component
public class ResourceLoader {


    private static final String FilePath  = "/Users/swanhtetaungphyo/IdeaProjects/Article_approval/src/main/resources/swear_eng.txt";
    //private static final Logger log = LoggerFactory.getLogger(ResourceLoader.class);

    public static HashSet<String> LoadSwearWordFromFile() throws FileNotFoundException {
        HashSet<String> hashSet = new HashSet<>();
        try{
            Files.lines(Path.of(FilePath))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(line -> !line.isEmpty())
                    .forEach(hashSet::add);
        }catch (IOException ex){
            throw  new FileNotFoundException(ex.getLocalizedMessage());
        }
        return hashSet;
    }
}
