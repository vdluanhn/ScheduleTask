package com.example.scheduletask.FilesUtils;

//import com.github.ffpojo.file.writer.FileSystemFlatFileWriter;
//import com.github.ffpojo.file.writer.FlatFileWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FileWriter<T> {
//    public long write(String filePath, List<T> data) throws IOException {
//        //Add file jar vào thư mục: lib/ ffpojo-1.1-SNAPSHOT.jar và khai báo trong file pom:
////        <dependency>
////            <groupId>com.github.ffpojo</groupId>
////            <artifactId>ffpojo</artifactId>
////            <version>1.1-SNAPSHOT</version>
////            <scope>system</scope>
////            <systemPath>${basedir}/lib/ffpojo-1.1-SNAPSHOT.jar</systemPath>
////        </dependency>
//        FlatFileWriter ffWriter = null;
//        try {
//            File file = new File(filePath);
//            ffWriter = new FileSystemFlatFileWriter(file, true);
//            ffWriter.writeRecordList(data);
//            return ffWriter.getRecordsWritten();
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            throw ex;
//        }finally {
//            ffWriter.close();
//        }
//    }

}
