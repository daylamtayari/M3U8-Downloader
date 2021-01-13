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

import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NavigableMap;

/**
 * This method handles all file methods which
 * handle files and also retrieves all of the
 * TS URLs from an M3U8 file.
 */
public class FileHandler {
    protected static Path TEMP_FOLDER_PATH;     //String value representing the absolute file path of the temporary folder.

    /**
     * This method creates a temporary folder
     * and assigns it to the TEMP_FOLDER_PATH variable.
     */
    protected static void createTempFolder(){
        String prefix="M3U8-Downloader-";       //Prefix of the temporary folder. It is best to set this to the name of your program.
        try {
            TEMP_FOLDER_PATH = Files.createTempDirectory(prefix).toAbsolutePath();
        }
        catch(IOException ignored){}
        File tempDir=new File(String.valueOf(TEMP_FOLDER_PATH));
        tempDir.deleteOnExit();     //Delete folder when exiting the program.
    }

    /**
     * This file merges all of the downloaded segment
     * files of the M3U8 playlist into a single file.
     * @param segmentMap    Navigable map containing the index and file objects of all the downloaded segment files.
     * @param fp            String value representing the file path of the output file.
     */
    protected static void mergeFile(NavigableMap<Integer, File> segmentMap, String fp){
        File output=new File(fp);
        segmentMap.forEach((key, segment) ->{
            try{
                fileMerger(segment, output);
            }
            catch(IOException ignored){}
        });
    }

    /**
     * This method merges two files together.
     * @param input     File object representing the file to be merged.
     * @param output    File object representing the file to be merged into.
     * @throws IOException
     */
    protected static void fileMerger(File input, File output) throws IOException{
        @Cleanup OutputStream os=new BufferedOutputStream(new FileOutputStream(output, true));
        @Cleanup InputStream is=new BufferedInputStream(new FileInputStream(input));
        IOUtils.copy(is, os);
        is.close();
        os.close();
    }
}