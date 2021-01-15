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
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Scanner;

/**
 * This is the main class which
 * contains the main method of
 * the program.
 */
public class Main {
    /**
     * Main method of the program.
     * @param args
     */
    public static void main(String[] args){
        System.out.print(
                  "\nM3U8 Downloader:"
                + "\nThis is a proof of concept program for a M3U8 downloader using Java."
                + "\nAuthor: Daylam Tayari https://github.com/daylamtayari https://paypal.me/daylamtayari"
                + "\n"
        );
        Scanner sc=new Scanner(System.in);
        System.out.print("\nM3U8 URL: ");
        String url=sc.next();
        System.out.print("\nOutput FOLDER path: ");
        String folder=sc.next();
        //This assumes that the folder path entered ended with a '\'.
        //When implementing this into an actual program, add a '\' at the end if the user did not.
        System.out.print("\nOutput file name: ");
        String name=sc.next();
        String fp=folder+name+".ts";
        sc.close();
        FileHandler.createTempFolder();
        ArrayList<String> chunks=null;
        try {
            chunks=Downloader.getChunks(url);
        }
        catch(IOException e) {}
        NavigableMap<Integer, File> segmentMap=Downloader.TSDownload(chunks);
        FileHandler.mergeFile(segmentMap, fp);
        System.out.print(
                  "\nDownload complete!"
                + "\nFile downloaded at: "+fp
        );
    }
}