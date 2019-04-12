package com.jazasoft.scanner;

public class RaspiStill {

    // Define the path to the raspistill executable.
    private final String _raspistillPath = "/opt/vc/bin/raspistill";
    // Define the amount of time that the camera will use to take a photo.
    private final int _picTimeout = 100;
    // Define the image quality.
    private final int _picQuality = 100;

    // Specify a default image width.
//    private int _picWidth = 1024;
    private int _picWidth = 720;
    // Specify a default image height.
//    private int _picHeight = 768;
    private int _picHeight = 480;
    // Specify a default image name.
    private String _picName = "example.jpg";
    // Specify a default image encoding.
    private String _picType = "jpg";

    // Default class constructor.
    public void RaspiStill()
    {
        // Do anything else here. For example, you could create another
        // constructor which accepts an alternate path to raspistill,
        // or defines global parameters like the image quality.
    }

    // Default method to take a photo using the private values for name/width/height.
    // Note: See the overloaded methods to override the private values.
    public void capturePicture()
    {
        try
        {
            // Determine the image type based on the file extension (or use the default).
            if (_picName.indexOf('.')!=-1) _picType = _picName.substring(_picName.lastIndexOf('.')+1);

            // Create a new string builder with the path to raspistill.
            StringBuilder sb = new StringBuilder(_raspistillPath);

            // Add parameters for no preview and burst mode.
            sb.append(" -n -bm");
//            sb.append(" -n");
            // Configure the camera timeout.
            sb.append(" -t " + _picTimeout);
            // Configure the picture width.
            sb.append(" -w " + _picWidth);
            // Configure the picture height.
            sb.append(" -h " + _picHeight);
            // Configure the picture quality.
            sb.append(" -q " + _picQuality);
            // Specify the image type.
            sb.append(" -e " + _picType);
            // Specify the name of the image.
            sb.append(" -o " + _picName);
//            System.out.println("Taking picture...");
            long timeStart = System.currentTimeMillis();
            // Invoke raspistill to take the photo.
            Process p = Runtime.getRuntime().exec(sb.toString());
            // Pause to allow the camera time to take the photo.
//            Thread.sleep(_picTimeout);

            p.waitFor();
//            System.out.println("picture taken... [" + (System.currentTimeMillis()-timeStart) + "ms]");
        }
        catch (Exception e)
        {
            // Exit the application with the exception's hash code.
            System.exit(e.hashCode());
        }
    }

    // Overloaded method to take a photo using specific values for the name/width/height.
    public void capturePicture(String name, int width, int height)
    {
        _picName = name;
        _picWidth = width;
        _picHeight = height;
        capturePicture();
    }

    // Overloaded method to take a photo using a specific value for the image name.
    public void capturePicture(String name)
    {
        capturePicture(name, _picWidth, _picHeight);
    }

    // Overloaded method to take a photo using specific values for width/height.
    public void capturePicture(int width, int height)
    {
        capturePicture(_picName, width, height);
    }
}
