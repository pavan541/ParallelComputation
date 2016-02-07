/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.lucene.analysis.PorterStemmer;

/**
 *
 * @author pavan
 */
public class MySecondRunnable implements Runnable {

    int part = 1;
    
    String line;
    String input = null;
    String words[]=null;
    
    public HashMap<String, Integer> hm1 = new HashMap<>();
    public List<String> alp = new ArrayList<>();
    public ArrayList<String> stoplist1 = new ArrayList<>();
    
    WordCount wc = new WordCount();
    
    public MySecondRunnable(List<String> alps,int part,ArrayList<String> stoplist)
    {
        this.alp = alps;
        this.part = part;
        stoplist1=stoplist;
    }
    
    public HashMap<String, Integer> getterV(){
        
               return hm1; 
    }
    
    @Override
    public void run() 
    {
//        System.out.println("I'm running "+ Thread.currentThread().getName()+" ");
        HashMap<String, Integer> hm =  new HashMap<String, Integer>();
        for(int i=0;i<alp.size();i++)
        {
            String line = null;
            String words[] = null;
            // LineReader --> it skips to the line by calling setLine()
            // getline will return the current line number
            // while (getline() != finalLine) {
            //    ReadAWord(); // overlapping these
            //    ProcessWord(); // two operations
            // }
            // readUntil lineNumber
            if((line = alp.get(i)) != null)
            {
                
                line = line.replaceAll("\\W", " "); 
                line = line.replaceAll("[^A-Za-z]"," ");
                words = line.split("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]");
                         
                for(String read : words)
                {
                    Integer freq = null;
                    if(!hm.isEmpty())
                        freq = hm.get(read.toLowerCase());
                    
                    if(!read.isEmpty())
                    {
                        if(!stoplist1.contains(read.toLowerCase()))
                        {
                            hm.put(read.toLowerCase(), (freq == null) ? 1 : freq +1);   
//                            System.out.println(hm);
                        }
                    }
                }
            }
      }
        hm1.putAll(hm);
    }
}
