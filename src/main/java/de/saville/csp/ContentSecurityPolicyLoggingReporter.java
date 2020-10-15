package de.saville.csp;

import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Logs content security policy violations using Slf4J.
 * 
 *     <servlet>
 *         <servlet-name>ContentSecurityPolicyReporter</servlet-name>
 *         <servlet-class>de.saville.csp.ContentSecurityPolicyReporter</servlet-class>
 *     </servlet>
 *  
 *     <servlet-mapping>
 *         <servlet-name>ContentSecurityPolicyReporter</servlet-name>
 *         <url-pattern>/ContentSecurityPolicyReporter</url-pattern>
 *     </servlet-mapping>  
 */
public class ContentSecurityPolicyLoggingReporter extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ContentSecurityPolicyLoggingReporter.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.warn(toString(request.getReader()));
    }

    static String toString(Reader input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy((Reader) input, (Writer) sw);
        return sw.toString();
    }

    static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0L;

        int n;
        for (boolean var5 = false; -1 != (n = input.read(buffer)); count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

}
