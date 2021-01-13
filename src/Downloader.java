/*
 * Copyright (c) 2020, 2021 Daylam Tayari <daylam@tayari.gg>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License version 3as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  @author Daylam Tayari daylam@tayari.gg https://github.com/daylamtayari
 *  @version 1.0
 *  Github project home page: https://github.com/daylamtayari/M3U8-Downloader
 */

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class contains all of the downloading methods
 */
public class Downloader {
    private static final int MAX_TRIES=5;       //Constant variable which represents the maximum amount of tries for the downloading of an individual file.
    private static final int TIMEOUT=60000;     //Constant variable representing the duration in milliseconds that the program has to try downloading a file before timing out.
    private static final int THREAD_COUNT=100;  //Constant variable representing the amount of threads used for the downloading of individual files.
    private static String TEMP_FOLDER_PATH;     //String value representing the absolute file path of the temporary folder.

    /**
     * Method which creates and downloads a temporary file.
     * @param url       String value which represents the URL of the contents to download to the file.
     * @return File     File object equal to the file which was created and downloaded to.
     * @throws IOException
     */
    protected static File tempDownload(String url) throws IOException {
        URL dURL=new URL(url);
        String prefix=FilenameUtils.getBaseName(dURL.getPath());
        //Edit the prefix length:
        /**
         * This has to be implemented since the prefix value, which is used to created the temporary files,
         * needs to have a minimum length of 3. So here we add either one or two zeros when the prefix value
         * has a length of 1 or 2 respectively.
         */
        if(prefix.length()<2){
            prefix="00"+prefix;
        }
        else if(prefix.length()<3){
            prefix="0"+prefix;
        }
        //Create the file:
        File downloadFile;
        prefix+="-";
        String suffix="."+FilenameUtils.getExtension(dURL.getPath());
        if(TEMP_FOLDER_PATH==null){     //Creates the temporary file if the temp folder has not been created yet.
            downloadFile=File.createTempFile(prefix, suffix);
        }
        else{
            downloadFile=File.createTempFile(prefix, suffix, new File(TEMP_FOLDER_PATH+File.separator));
        }
        downloadFile.deleteOnExit();    //Delete the file when the program exits.
        FileUtils.copyURLToFile(dURL, downloadFile, TIMEOUT, TIMEOUT);      //Downloads the contents of the URL to the file we just made.
        return downloadFile;
    }

    /**
     * This method processes the downloading of a series of
     * TS file links, which are the file type of videos contained
     * in M3U8 playlists.
     * @param links     String arraylist which contains all of the TS links to download.
     * @return NavigableMap<Integer, File>      Navigable map which contains an integer value, representing the order of each feed,
     * and a file object which represents the corresponding file.
     */
    protected static NavigableMap<Integer, File> TSDownload(ArrayList<String> links){
        NavigableMap<Integer, File> segmentMap=new TreeMap<>();
        Queue<String> downloadQueue=new ConcurrentLinkedQueue<>();
        for(String link: links){    //Add all of the links that are in the inputted links arraylist to the download queue.
            downloadQueue.offer(link);
        }
        int index=0;
        ThreadPoolExecutor downloadTPE=(ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        while(!downloadQueue.isEmpty()){
            index++;
            String item=downloadQueue.poll();
            final int finalIndex=index;
            downloadTPE.execute(new Runnable() {
                int currentTries=1;
                @Override
                public void run() {
                    while(currentTries<=MAX_TRIES){     //Keeps trying until download is successful or it has tried it more than the maximum amount of tries.
                        final int threadIndex=finalIndex;
                        final String threadItem=item;
                        try{
                            File tempTS=tempDownload(threadItem);
                            segmentMap.put(threadIndex, tempTS);
                            break;      //If the download and assignment to the navigable map successful, break the loop.
                        }
                        catch(IOException ignored){}
                        //For demonstration purposes, any exceptions are simply ignored but you would want to implement proper error handling.
                    }
                }
            });
        }
        downloadTPE.shutdown();
        try{    //Terminate the thread pool executor and if it takes too long and times out, force interrupt it.
            downloadTPE.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch(Exception e){
            Thread.currentThread().interrupt();
        }
        return segmentMap;
    }
}