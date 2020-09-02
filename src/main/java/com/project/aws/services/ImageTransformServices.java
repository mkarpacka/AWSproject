package com.project.aws.services;

import com.amazonaws.util.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ImageTransformServices {

    public MultipartFile saveGraphicAsImage(String fileName) throws IOException {

        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = ImageIO.read(new File(fileName));

        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();

        // fill all the image with white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        // create a circle with black
        g2d.setColor(Color.black);
        g2d.fillOval(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        // create a string with yellow
        g2d.setColor(Color.yellow);
        g2d.drawString("EDITED" + fileName,  bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);


        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as JPEG
        File file = new File("edited_" + fileName);
        ImageIO.write(bufferedImage, "jpg", file);

        //conversion to multipartfile
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(), "image/jpg", IOUtils.toByteArray(input));
    }

}
