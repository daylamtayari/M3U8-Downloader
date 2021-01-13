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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
}