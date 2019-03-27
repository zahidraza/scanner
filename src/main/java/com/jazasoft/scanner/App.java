package com.jazasoft.scanner;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    private long SLEEP_INTERVAL = 1000L; //1 second

    private void readSpeedDial() {
        while (true) {
            System.out.println("Reading Dial Pad Input");
            //Read Dial Pad Input

            // If data received from dail pad, process it

            // else
            // Sleep
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readQR(){

        while (true) {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String filename = "snap_" + dateTime + ".png";

            new RaspiStill().capturePicture(filename);

            File file = new File(filename);

            try {
                String output = decodeQRCode(file);
                if (output != null) {
                    System.out.println("QR code read successfuly: \n " + output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (file.exists()){
                    file.delete();
                }
            }

            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String decodeQRCode(File qrCodeimage) throws IOException {
        if (!qrCodeimage.exists()) {
            System.out.println("File not found. file = " + qrCodeimage.getAbsolutePath());
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }

    public static void main(String[] args) {

        final App app = new App();

//        File file = new File("MyQRCode2.png");
//        try {
//            System.out.println(app.decodeQRCode(file));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Runnable taskQR = app::readQR;
//        Runnable taskSpeedDail = app::readSpeedDial;

        //start task
        new Thread(taskQR).start();
//        new Thread(taskSpeedDail).start();


        while (true) {
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
