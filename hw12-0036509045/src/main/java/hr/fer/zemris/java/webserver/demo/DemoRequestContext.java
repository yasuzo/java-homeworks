package hr.fer.zemris.java.webserver.demo;

import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo program illustrating output of a {@link RequestContext} header.
 */
public class DemoRequestContext {
    public static void main(String[] args) throws IOException {
        demo1("src/main/resources/primjer1.txt", "ISO-8859-2");
        demo1("src/main/resources/primjer2.txt", "UTF-8");
        demo2("src/main/resources/primjer3.txt", "UTF-8");
    }

    private static void demo1(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(),
                new HashMap<>(),
                new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");
//        Only at this point will header be created and written...
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }

    private static void demo2(String filePath, String encoding) throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(filePath));
        RequestContext rc = new RequestContext(os, new HashMap<>(),
                new HashMap<>(),
                new ArrayList<>());
        rc.setEncoding(encoding);
        rc.setMimeType("text/plain");
        rc.setStatusCode(205);
        rc.setStatusText("Idemo dalje");
        rc.addRCCookie(new RequestContext.RCCookie("korisnik", "perica", 3600, "127.0.0.1",
                "/"));
        rc.addRCCookie(new RequestContext.RCCookie("zgrada", "B4", null, null, "/"));
//        Only at this point will header be created and written...
        rc.write("Čevapčići i Šiščevapčići.");
        os.close();
    }
}