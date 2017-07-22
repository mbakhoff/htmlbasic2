package app;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateEngine {

  public String render(String templateName, Map<String, Object> context) throws IOException {
    String template = load(templateName);
    // TODO process the template
    return template;
  }

  private String load(String templateName) throws IOException {
    try (InputStream stream = TemplateEngine.class.getClassLoader().getResourceAsStream(templateName)) {
      if (stream == null)
        throw new FileNotFoundException(templateName);
      return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }
  }
}
