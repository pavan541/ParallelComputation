/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcount;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author pavan
 */
public class MyThirdRunnable implements Runnable {
    
    int part = 1;
    long start,end;
    int noofthreads;
    
    String line;
    String words[]=null;
    String input = null;
    public static String path= "/Users/pavan/Desktop/WordCount/src/wordcount/example1.txt";
   
    WordCount wc = new WordCount();
    RandomAccessFile ref = new RandomAccessFile(path, "rw");
    
    HashMap<String, Integer> hm1 = new HashMap<String,Integer>();
    
    public List<String> alp = new ArrayList<String>();
    public ArrayList<String> stoplist1 = new ArrayList<String>();
     
    public MyThirdRunnable(long start,long end,int noofthreads,int part,ArrayList<String> stoplist,RandomAccessFile raf) throws FileNotFoundException
    {
        this.noofthreads = noofthreads;
        this.start = start;
        this.end = end;
        this.part = part;
//        stoplist1=stoplist;
    }
    
    public HashMap<String, Integer> getterV(){
        return hm1; 
    }
    
    @Override
    public void run() 
    {
        try {
            HashMap<String, Integer> hm =  new HashMap<String, Integer>();
            //String word = "";
            
            ref.seek(start);
            StringBuffer word = new StringBuffer(50);
            for(long i = start;i<=end;i++)
            {
                try {
                    byte ch = ref.readByte();
                    if((ch >= 65 && ch<=90)  || (ch>=97 && ch<=122) )
                        word = word.append((char)ch);
                    else
                    {
                        if(word.length() != 0) 
                            hm=processword(word.toString(),hm);
                        word = new StringBuffer(50);
                    }
                } 
                catch (EOFException ex1) {
                   break; //EOF reached.
                }
                
                catch (IOException ex) {
                     System.err.println("An IOException was caught: " + ex.getMessage());
                     ex.printStackTrace();
                }
            }
            hm1.putAll(hm);
        }
        catch (IOException ex) {
            Logger.getLogger(MyThirdRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public HashMap<String, Integer> processword(String word,HashMap<String, Integer> hm)
    {
        Integer freq = null;
        if(!hm.isEmpty())
            freq = hm.get(word.toLowerCase());
        if(!stoplist1.contains(word.toLowerCase()))
            hm.put(word.toLowerCase(), (freq == null) ? 1 : freq +1);
        return hm;
    }
  
}

