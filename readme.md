# Web app from scratch

In this tutorial we're going to build a simple forum page from scratch.
The functionality will be very limited:

* view forum thread names
* start a new thread
* view posts of the selected thread
* add a post to the selected thread

The web app must also meet these requirements:
* The data must be stored on disk in regular files.
  It is recommended to process the files using DataOutputStream/DataInputStream.
  You can use one file for the thread names and a separate file for each thread.
* All HTTP responses must contain appropriate HTTP headers.

## HTTP headers

Each request and response contains [HTTP headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers).
Headers are a set of key-value pairs that contain additional information for the request/response.
The headers are never rendered, but the server or browser may use them in other ways.
You can manually set response headers from a servlet by using `response.setHeader("header-name", "header-value");`.
The headers are visible in the browser developer tools' network tab.

| Header | Description |
| --- | --- |
| [Content-Length](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Length) | Indicates the size of the request/response body in bytes |
| [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type) | Indicates the media type of the resource (e.g. `text/html; charset=utf-8`, `text/plain`, `application/octet-stream`) |
| [Location](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Location) | indicates the URL to redirect a page to (the response body can be empty) |

*(there are many more headers, but we don't need them for now)*

Our forum page servlets should set the `Content-Type` and `Content-Length` headers.
The Content-Type must always include the charset specifier for text data.
For html pages you should almost always use `text/html; charset=utf-8`.
Note that the Content-Length must specify the content length in bytes, not the number of characters.

Try to set the content type to `text/plain` for a html page and see how the browser renders it.

## HTTP status

The request contains the Request-URI and the method name.
The response doesn't contain either of them.
Instead, the response has a [*status code* and a *reason phrase*](https://tools.ietf.org/html/rfc2616#section-6).

The status code is a 3-digit integer that shows the result of the request.
The reason phrase is a short textual description of the reason code.
The most common status codes (along with their reason phrases) are 200 (OK), 404 (Not Found), and 302 (Found).
There are more status codes with each having a different meaning.
Most status codes have a default reason phrase, but you can always override it manually.

| Code | Description |
| --- | --- |
| 1xx | Informational - Request received, continuing process |
| 2xx | Success - The action was successfully received, understood, and accepted |
| 3xx | Redirection - Further action must be taken in order to complete the request |
| 4xx | Client Error - The request contains bad syntax or cannot be fulfilled |
| 5xx | Server Error - The server failed to fulfill an apparently valid request |

Note that when sending the *Location* header, then the status code must be set to 3xx.
See the [MDN documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Location) for details.
You can set the status code in the servlet using `response.setStatus(302);`.
The HttpServletResponse contains a constant for most status codes, e.g. `HttpServletResponse.SC_FOUND` is a constant for 302.

## Exercise

Implement the forum.

1. `GET /` should show a html page that shows all the names of all forum threads.
2. `GET /` should include a form for starting a new forum thread.
   The data should be handled by `POST /`.
   The browser should be redirected to `/threads/threadName` after the `POST` (see headers).
3. `GET /threads/threadName` should show all the posts in the forum thread with the given name.
   Create a separate servlet for this.
   If the thread is not found, then send a status 404 response.
4. `GET /threads/threadName` should include a form for adding a new post to the given thread.
   The data should be handled by `POST /threads/threadName`.
   The response shouldn't be a redirect, but should instead immediately render the same view as `GET /threads/threadName`.
5. Each forum thread name showed on `GET /` should be a link to the corresponding `/threads/threadName` view.

Note that `threadName` is a variable. If you have threads "apples" and "oranges", then you could `GET /threads/apples` or `POST /threads/oranges`.

Additionally:
* Each response should have the Content-Type and Content-Length headers set.
* Each page should include a css stylesheet (can be shared between the pages) to make it look reasonable.

Hints:
* Send `GET` requests using `<a>` elements.
* Send `POST` requests using `<form>` elements.
* Data sent from `doGet` and `doPost` using `getWriter`/`getOutputStream` is rendered in the browser.
* `HttpServletRequest` objects contain the Request-URI, request headers and the request body sent by the browser.
  You cannot set headers manually when sending requests using `<a>` or `<form>`.
* `HttpServletResponse` contains the response code, response headers and the response body sent by the server.
  You can set all those values manually in the servlet.
* Each request ja response are completely independency and don't know anything about earlier request or responses.
* Writing HTML in a servlet is ugly - we'll see how to get rid of it in the next tutorial.
* You can debug the servlets from the IDE.
  Open the maven toolbar, find plugins, jetty, jetty-run; right click and select Debug.
  See [IntelliJ docs](https://www.jetbrains.com/help/idea/maven-projects-tool-window.html) if you can't find it.
* You don't need to create a new servlet for each thread.
  Recall from the previous tutorial the wildcard servlet mapping `@WebServlet("/users/*")`.
* Don't use `<br/>` for vertical spacing.
  Use padding and margin instead.
* Learn new CSS properties: `border`, `color`, `background-color`, `border-radius`, `box-shadow`.
