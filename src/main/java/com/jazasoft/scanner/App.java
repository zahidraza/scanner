package com.jazasoft.scanner;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    private long SLEEP_INTERVAL = 100L; //1 second

    private void readKeyPad() {
        System.out.println("Reading Dial Pad Input");

        PiezoKeyPad keyPad = new PiezoKeyPad();
        keyPad.addChangeListener((PropertyChangeEvent evt) -> {
                System.out.println(evt.getNewValue());
            }
        );

    }

    private void readQR(){

        while (true) {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String filename = "snap_" + dateTime + ".jpg";

            new RaspiStill().capturePicture(filename);

            File file = new File(filename);

            try {
                long start = System.currentTimeMillis();
                String output = decodeQRCode(file);
                long time = System.currentTimeMillis()-start;

                if (output != null) {
                    System.out.println("QR code read successfuly: [" + time + "ms]\n" + output);
                    sendData(output);
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

    public void sendData(String data){
        Boolean result = false;
        String urlString = "http://zahid.local:8011/api/qr?";
        String body = "data=" + data;

        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
            writer.writeBytes(body);
            writer.flush();
            writer.close();

            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();

            String response = buffer.toString();

            System.out.println("Response = " + response);


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        final App app = new App();
        
        Runnable taskQR = app::readQR;
        Runnable taskKeyPad = app::readKeyPad;

        //start task
        new Thread(taskQR).start();
        new Thread(taskKeyPad).start();

        while (true) {
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
