package com.example.scheduletask.Tasks;

import com.example.scheduletask.FilesUtils.FileUtil;
//import com.github.ffpojo.decorator.InternalDateDecorator;
import com.example.scheduletask.Utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

//    Dùng 1 trong 4 phương thức tương ứng với 4 loại lập lịch bên dưới
    @Scheduled(fixedRate = 2000) //Sau khi deploy app, cứ mỗi 2000ms thực thi 1 lần phương thức dưới (Method không cần chạy xong vẫn chạy tiếp sau khoảng thời gian cấu hình)
    public void scheduleTaskWithFixedRate() {
        // Gọi phương thức cần thực hiện sau mỗi khoảng thời gian cấu hình trên
        logger.info("Send email to producers to inform quantity sold items");
    }

    @Scheduled(fixedDelay = 10000) // Sau khi method này chạy thành công thì đợi sau fixedDelay = 10000ms phương thức mới được thực hiện lại. (Method chạy xong mới chạy tiếp)
    public void scheduleTaskWithFixedDelay() {
        // Call send email method here
        System.out.println(readFile());
        // Pretend it takes 1000ms
//        try {
//            Thread.sleep(1000); //giả sử sau 1000ms phương thực mới thực hiện xong.
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        logger.info("Send email to producers to inform quantity sold items 1");
    }

//    Chạy lặp với khoảng thời gian fixedRate sau khi đi deploy initialDelay (Sử dụng kết hợp fixedRate và initialDelay)
    @Scheduled(fixedRate = 20000, initialDelay = 10000) // Sau 10s kể từ khi deploy started thành công, sẽ bắt đầu khởi chạy hàm, mỗi 2s sẽ khởi chạy tiếp
    public void scheduleTaskWithInitialDelay() {
        EmailUtils.sendEmail();
        logger.info("Send email to producers to inform quantity sold items 2");
    }

//    Hẹn giờ với cron (Sử dụng cron)" : ******* <=> Giây/Phút/Giờ/Ngày của tháng/Tháng/Ngày trong tuần/Năm
//            "0 0 * * * *" // Đầu giờ của tất cả các giờ của tất cả các ngày.
//
//            "*/10 * * * * *" // Mỗi 10 giây (số giây chia hết cho 10).
//
//            "0 0 8-10 * * *" // 8, 9 và 10 giờ các ngày
//
//            "0 0/30 8-10 * * *" // 8:00, 8:30, 9:00, 9:30 và 10 tất cả các ngày
//
//            "0 0 9-17 * * MON-FRI" // 9, .. 17 giờ các ngày thứ 2 tới thứ 6 (monday & friday)
//
//            "0 0 0 25 12 ?" // Tất cả các ngày giáng sinh, nửa đêm (0h0p0s).

//          Ký hiệu	    Ý nghĩa
//          *	        Khớp với bất kỳ
//          */X	        Tất cả các X (Chia hết cho X)
//          ?	        ("không chỉ định giá trị") - nó hữu ích khi bạn cần chỉ định gì đó trong một trong 2 trường (field), trong đó một trường cho phép còn trường kia thì không. Chẳng hạn, nếu tôi muốn ngày thứ 10 trong tháng, nhưng không quan tâm đó là ngày thứ mấy trong tuần, tôi cần đặt "10" vào trường day-of-month, và đặt "?" vào trường day-of-week.

    @Scheduled(cron = "*/10 * * * * ?", zone="Asia/Saigon") //zone: Thuộc tính zone chỉ định múi giờ. Mặc định giá trị của zone là rỗng, nghĩa là lấy theo múi giờ của Server
    public void scheduleTaskWithCronExpression() {
        logger.info("Send email to producers to inform quantity sold items 3");
        //xuat file
        exportFile();
    }

    private static String readFile(){
        return FileUtil.readFile(pathFile1);
    }

    static String pathFile1="";
    private static String exportFile(){

        try {
            SimpleDateFormat folderDecorator = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat fileNameDecorator = new SimpleDateFormat("yyyyMMddHHmmss");
            String exportPath = "F:\\Project_BE\\Source\\ScheduleTask";
            String pathSave = exportPath + "\\export\\" + folderDecorator.format(new Date());
            String fileNameSave = "File_" + fileNameDecorator.format(new Date()) + ".txt";
            File folder = new File(pathSave);
            if (!folder.exists()) {
                    FileUtil.forceFolderExist(pathSave);
            }

            if (!folder.exists()) {
                logger.info("Cannot create folder -> Next Toll");
                return "";
            }
            String content = " "+fileNameSave+" vừa được ghi ra tại thư mục: \n"+pathSave;
            System.out.println(" path: "+pathSave);
            System.out.println(" file: "+fileNameSave);
            String pathFile = pathSave+"\\"+fileNameSave;
            pathFile1 = pathFile;
//            long wroteRecords = fileWriter.write(pathSave + "\\" + fileNameSave, lst);
            boolean wroteRecords = FileUtil.writeFile(pathFile,content);
            logger.info("Export file succsessfull "+wroteRecords);

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Loi: "+e.toString());
        }
        return "";
    }
}
