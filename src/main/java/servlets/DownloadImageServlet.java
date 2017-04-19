package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DownloadImageServlet extends HttpServlet implements Serializable {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(1024 * 1024);

        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setSizeMax(1024 * 1024 * 10);

        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    processFormField(item);
                } else {
                    processUploadedFile(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

    }

    private void processUploadedFile(FileItem item) throws Exception {
        File uploadetFile = null;
        String path = "webapp/image/test.jpg";
        uploadetFile = new File(path);
        uploadetFile.createNewFile();
        item.write(uploadetFile);
    }

    private void processFormField(FileItem item) {
        System.out.println(item.getFieldName() + " = " + item.getString());
    }
}

