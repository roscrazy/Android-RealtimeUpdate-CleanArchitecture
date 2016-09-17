package com.mike.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class Util {

    public static int copyStream(InputStream input, OutputStream output) throws IOException{
        byte[] buffer = new byte[1024];
        int count = 0;
        int n = 0;

        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }

        output.flush();
        return count;
    }

}
