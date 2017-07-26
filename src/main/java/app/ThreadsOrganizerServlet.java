package app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class ThreadsOrganizerServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.getWriter().write(page());
  }

  private String page() {
    return "" +
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "  <meta charset=\"UTF-8\">\n" +
        "  <title>Organizer</title>\n" +
        "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
        "</head>\n" +
        "<body>\n" +
        "\n" +
        "<h1>Forum from scratch</h1>\n" +
        "\n" +
        "<h2>Existing threads</h2>\n" +
        "<!-- show threads here -->\n" +
        "\n" +
        "<h2>Start a new thread</h2>\n" +
        "<!-- allow creating new threads here -->\n" +
        "\n" +
        "</body>\n" +
        "</html>\n";
  }
}
