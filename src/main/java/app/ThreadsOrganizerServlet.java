package app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@WebServlet("/")
public class ThreadsOrganizerServlet extends HttpServlet {

  private final TemplateEngine templateEngine = new TemplateEngine();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    String template = templateEngine.render("organizer.html", Collections.emptyMap());
    writer.write(template);
  }
}
