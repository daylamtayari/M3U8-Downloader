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
    private static File tempDownload(String url) throws IOException {
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
}