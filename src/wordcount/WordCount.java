/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcount;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import static jdk.nashorn.internal.objects.NativeArray.map;
import static jdk.nashorn.internal.objects.NativeDebug.map;

/**
 *
 * @author pavan
 */
public class WordCount {

    /**
     * @param args the command line arguments
     */
    public static String path= "/Users/pavan/Desktop/WordCount/src/wordcount/example1.txt";
 
    public int splitValue;
    public int nooflines;
    public int noofthreads=3;
    
    public HashMap<String, Integer> finalMap = new HashMap<>();
    public HashMap<String, Integer>[] hashmap = new HashMap[noofthreads];
    
    public ArrayList<String> fileArrayList = new ArrayList<>();
    public ArrayList<String> stopArrayList = new ArrayList<>();
     
    public List<HashMap<String , Integer>> myMap  = new ArrayList<HashMap<String,Integer>>();
    
    public WordCount() {
    
    }
   
    public static void main(String[] args) throws FileNotFoundException, IOException
        , InterruptedException {
        
        long startTime = System.currentTimeMillis();
        WordCount wc1 = new WordCount();
        wc1.execute();
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long startTime1 = System.currentTimeMillis();
        wc1.execute_seek();
        long endTime1   = System.currentTimeMillis();
        long totalTime1 = endTime1 - startTime1;
        System.out.println(" Time is "+totalTime);
        System.out.println(" Time is "+totalTime1);
//        wc1.printmapping();
    }
    

    

public void execute() throws FileNotFoundException, IOException, InterruptedException
{
//    stopArrayList = readStopWords();
    
    File file= new File(path);
   
    BufferedReader brFile1 = new BufferedReader(new FileReader(file));
    fileArrayList = readFile(brFile1);
    brFile1.close();

    nooflines = fileArrayList.size();
    splitValue = nooflines/noofthreads;
    
    if(file.isFile())
        if(file.getName().endsWith(".txt") || file.getName().endsWith(".TXT"))
           initthreads();
}

public void execute_seek() throws FileNotFoundException, IOException, InterruptedException
{
//    stopArrayList = readStopWords();
    File file= new File(path);
    
    if(file.isFile())
    {
        if(file.getName().endsWith(".txt") || file.getName().endsWith(".TXT"))
        {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            //RandomAccessFile rat = raf;
            List threadList = new ArrayList();
            long len = file.length();
            long start = 0;
            long end;
            for(int i=1;i<=noofthreads;i++)
            {
                if(i == noofthreads)
                    end = len-1;
                else
                    end = ((long)(len/noofthreads) * i) - 1;
                raf.seek(end);
                byte ch = raf.readByte();
                while(((ch >= 65 && ch<=90)  || (ch>=97 && ch<=122) ))
                {
                    if(end == len -1)
                       break;
                    end++;
                    ch = raf.readByte();
                }
                MyThirdRunnable mtr = new MyThirdRunnable(start,end,noofthreads,i,stopArrayList,raf);
                Thread thread = new Thread(mtr);
                thread.setName("Thread "+i);
                thread.start();
                threadList.add(thread);
                myMap.add(mtr.getterV());
                start = end+1;
            }

            for (int i = 0; i < threadList.size(); i++)
                ((Thread) threadList.get(i)).join();
                
        }
    }
}

public void initthreads() throws InterruptedException{
    List threadList = new ArrayList();
    int start,end;
    for(int i=1;i<=noofthreads;i++)
    {
        start = (i-1)*splitValue;
        end = i*splitValue;
        if(i ==  noofthreads)
            end = nooflines;
        List<String> sal =  fileArrayList.subList(start,end);
        MySecondRunnable msr = new MySecondRunnable(sal,i,stopArrayList);
        Thread thread = new Thread(msr);
        thread.setName("Thread "+i);
        thread.start();
        threadList.add(thread);
        myMap.add(msr.getterV());
    }

    for (int i = 0; i < threadList.size(); i++)
        ((Thread) threadList.get(i)).join();
}

public void createFile(BufferedReader br) throws IOException
{
    BufferedWriter output = null;
    try {
        File file1 = new File("/Users/pavan/Desktop/WordCount/src/wordcount/example1.txt");
        output = new BufferedWriter(new FileWriter(file1));
        output.write("hello");
        String  line;
        while((line = br.readLine()) != null)
        {
            if(!line.isEmpty())
            {
                output.write(line);
                output.newLine();
            }

        }
    } 
    catch ( IOException e ) {
    }
    finally {
        if ( output != null ) output.close();
    }
}

public ArrayList<String> readFile(BufferedReader br ) 
{
    ArrayList<String> tempArrayList = new ArrayList<String>();
    
    try {
        String  line;
        while((line = br.readLine()) != null)
            if(!line.isEmpty())
                tempArrayList.add(line);
    } 
    catch ( IOException e ) {
    }
    return tempArrayList;
}

public ArrayList<String> readStopWords() throws FileNotFoundException, IOException 
{
    ArrayList<String> tempArrayList = new ArrayList<String>();
    
    String path1 = "/Users/pavan/Desktop/WordCount/src/wordcount/stopwords.txt";
    BufferedReader brStopFile = new BufferedReader(new FileReader(path1));
    tempArrayList = readFile(brStopFile);
    brStopFile.close();
    return tempArrayList;
}
   


public void printmapping()
{

    for(Map<String, Integer> map : myMap){
         map.forEach((k, v) -> finalMap.merge(k, v, (v1, v2) -> v1+ v2));
    } 

    Map<String, Integer> treeMap = sortByComparator(finalMap);

    System.out.println("-------------------------------");
    for(Entry entry : treeMap.entrySet()){
             System.out.println(entry.getValue()+ " : " + entry.getKey());

    }
}
    
private  Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) 
{

    // Convert Map to List
    List<Map.Entry<String, Integer>> list = 
            new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

    // Sort list with comparator, to compare the Map values
    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
            }
    });

    // Convert sorted map back to a Map
    Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
    for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
    }
            return sortedMap;
}

}
