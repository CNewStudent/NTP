package servlets;


import com.google.gson.Gson;
import data.MyStructure;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WorkWithImageServlet extends HttpServlet {

    private List buf = new ArrayList();
    private String eye, nose, mouth;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> options = new LinkedHashMap<>();
        String json;

        options.put("status", "ok");
        json = new Gson().toJson(options);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

        eye = request.getParameter("eye");
        nose = request.getParameter("nose");
        mouth = request.getParameter("mouth");


        chooseElements(findElementsOnImage());

        Mat image = Highgui.imread("webapp/image/test.jpg");
        Highgui.imwrite("webapp/image/result.jpg", image);

    }

    private ArrayList<MyStructure> findElementsOnImage() {
        ArrayList<MyStructure> res = new ArrayList<>();
        CascadeClassifier eyeDetector = new CascadeClassifier("haarcascade_eye_tree_eyeglasses.xml");
        CascadeClassifier mouthDetector = new CascadeClassifier("haarcascade_mcs_mouth.xml");
        CascadeClassifier noseDetector = new CascadeClassifier("haarcascade_mcs_nose.xml");

        Mat image = Highgui.imread("webapp/image/test.jpg");

        Point p = new Point();

        MatOfRect eyeDetections = new MatOfRect();
        MatOfRect mouthDetections = new MatOfRect();
        MatOfRect noseDetections = new MatOfRect();

        eyeDetector.detectMultiScale(image, eyeDetections);
        mouthDetector.detectMultiScale(image, mouthDetections);
        noseDetector.detectMultiScale(image, noseDetections);


        // Draw a bounding box around each face.
        for (Rect rect : eyeDetections.toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));

            res.add(new MyStructure("eye", rect.x, rect.y, rect.width, rect.height));
            buf.add(new Point(rect.x, rect.y + rect.height));
        }

        p = (Point) buf.get(buf.size() - 1);

        for (Rect rect : noseDetections.toArray()) {
            if (rect.y >= p.y) {
                Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 0, 255));
                res.add(new MyStructure("nose", rect.x, rect.y, rect.width, rect.height));
                buf.add(new Point(rect.x, rect.y + rect.height));
            }
        }

        p = (Point) buf.get(buf.size() - 1);

        for (Rect rect : mouthDetections.toArray()) {
            if (rect.y >= p.y) {
                Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(255, 0, 0));
                res.add(new MyStructure("mouth", rect.x, rect.y, rect.width, rect.height));
            }
        }

        return res;
    }

    private void chooseElements(ArrayList<MyStructure> arr) {
        Random random = new Random();
        for (MyStructure ms : arr) {
            if (ms.getName().equals("eye") && Integer.valueOf(eye) != 0) {
                draw(ms, "webapp/image/eye_" + String.valueOf(random.nextInt(2) + 1) + ".png");
            } else if (ms.getName().equals("nose") && Integer.valueOf(nose) != 0) {
                draw(ms, "webapp/image/nose_" + String.valueOf(random.nextInt(2) + 1) + ".png");
            } else if (ms.getName().equals("mouth") && Integer.valueOf(mouth) != 0) {
                draw(ms, "webapp/image/lips_" + String.valueOf(random.nextInt(2) + 1) + ".png");
            }
        }
    }

    private void draw(MyStructure ms, String path) {
        BufferedImage bigImage;
        BufferedImage smallImage;
        File input;
        //draw
        try {
            input = new File("webapp/image/test.jpg");
            bigImage = ImageIO.read(input);

            input = new File(path);
            smallImage = ImageIO.read(input);

            Graphics g = bigImage.getGraphics();
            g.drawImage(smallImage, ms.getX(), ms.getY(), null);
            g.dispose();

            ImageIO.write(bigImage, "JPEG", new File("webapp/image/test.jpg"));

        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }

    }
}
