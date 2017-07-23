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
* The webapp must implement a custom template engine for rendering the pages.
* All HTTP responses must contain appropriate HTTP headers.

## Template engines

Web pages often contain dynamic content - some information that can change.
When writing the html for such pages, the parts that will change must be replaced with placeholders.
A html page with placeholders is called a template.
When the page is requested, then some code (called the template engine or template renderer) will process the template and replace the placeholders with actual values.

Different template engines exist for Java.
We are going to write our own template engine to better understand how they work.
The template engine should have a method `String render(String templateName, Map<String, Object> context)`.
The render method should load the requested template, process it and return the final result.
The template is a regular html file with two exceptions, as follows.

### Variable substitution

The template can include placeholders in the form `${variableName}` which will be replaced with the values taken from the `context` map.

Example:

The template file *example.html*:
```html
<p>Hello user ${user}</p>
<p>The time is ${time}</p>
```
The servlet code:
```java
Map<String, Object> context = new HashMap<>();
context.put("user", "mbakhoff");
context.put("time", Instant.now());
render("example.html", context);
```

The example should render something like
```html
<p>Hello user mbakhoff</p>
<p>The time is 2017-07-22T14:35:38.107Z</p>
```

### Repeating sections using `<block>`

The block element can be used for rendering a collection of items from the context map.
The element works like a for-each loop, repeating the contents of the block for each element of the collection.
The element must contain a single attribute *each* that is used to select the collection and assign a name to the loop variable: `<block each="item:collection"></block>`
The loop variable must be usable inside the block as if it was set in the context map.

Example:

The template file *example.html*:
```html
<ul>
<block each="prime:primes">
  <li>${prime}</li>
</block>
</ul>
```
The servlet code:
```java
Map<String, Object> context = new HashMap<>();
context.put("primes", Arrays.asList(3, 5, 7));
render("example.html", context);
```

The example should render exactly this:
```html
<ul>
  <li>3</li>
  <li>5</li>
  <li>7</li>
</ul>
```

A block element doesn't need to support nesting other block elements inside it.

Hint: you must cast the collection from `Object` to `List<Object>` when you take it from the context.

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
   Create a separate servlet and template for this.
4. `GET /threads/threadName` should include a form for adding a new post to the given thread.
   The data should be handled by `POST /threads/threadName`.
   The response shouldn't be a redirect, but should instead immediately render the same view as `GET /threads/threadName`.
5. Each forum thread name showed on `GET /` should be a link to the corresponding `/threads/threadName` view.

Additionally:
* Each response should have the Content-Type and Content-Length headers set.
* The pages should be simple enough that our template engine can handle them.
* Each page should include a css stylesheet (can be shared between the pages) to make it look reasonable.
