/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.client;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 *
 * @author user
 */
public class RemoveOverdateFile {

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private long fileReserveTime;

    public RemoveOverdateFile(long frt) throws IOException {
        this.fileReserveTime = frt;
    }

    private Date getDates(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateStr + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public void Delete() throws IOException {
        int delCount = 0;
        int resCount = 0;

        ArrayList<File> files = new ArrayList<File>();
        Collection filesCollection = FileUtils.listFiles(
                new File("/itsys-data"),
                new RegexFileFilter("^.*?\\.(read)$"),
                DirectoryFileFilter.DIRECTORY
        );
        files.addAll(filesCollection);
        Date date = new Date();
        System.out.println(files.size() + " files to be decide at " + dateFormat.format(date));
//        System.out.println("files to be decide at " + dateFormat.format(date));

        for (int i = 0; i < files.size(); i++) {

            Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher dateMatcher = DATE_PATTERN.matcher(files.get(i).getName());
            if (dateMatcher.find()) {

                long diff = date.getTime() - getDates(dateMatcher.group()).getTime();

                if (diff > fileReserveTime) {
                    System.out.println("Deleting file " + files.get(i).getName());
                    files.get(i).delete();
                    delCount++;
                } else {
//                System.out.println("file " + files.get(i).getName() + " is still in reserve time");
                    resCount++;
                }

            } else {
                System.out.println("fail to find date in file name");
            }
        }
        System.out.println("directory cleaned up, " + delCount + " files have been deleted and " + resCount + " files reserve for review");
        
    }

}
