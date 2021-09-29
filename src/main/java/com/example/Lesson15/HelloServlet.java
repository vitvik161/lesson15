package com.example.Lesson15;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private volatile Integer count;

    public void init() {
        message = "We increase the counter if the parameter is present ";
        count = 1;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if(request.getParameter("name") != null && !request.getParameter("name").isEmpty())
            increaseCount();

        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<h1> Count = " + count + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }

    private static final AtomicReferenceFieldUpdater<HelloServlet,Integer> updater =
            AtomicReferenceFieldUpdater.newUpdater(
                    HelloServlet.class, Integer.class, "count" );

    public void increaseCount()
    {
        for (;;) {
            if(updater.compareAndSet(this, this.count, count+1)) return;
        }
    }
}