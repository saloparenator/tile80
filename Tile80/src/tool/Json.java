/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public final class Json {
    private static final Logger LOG = Logger.getLogger(Json.class.getName());
    
    /**
     * load json file
     * @param uri
     * @return 
     */
    public static String loadFileJson(String uri)
    {
        StringBuilder fileString=new StringBuilder();
        try 
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(uri))));
            while(reader.ready())
            {
                fileString.append(reader.readLine());
            }
        } 
        catch (IOException ex) 
        {
            LOG.warning("error Loading "+uri);
        }
        return fileString.toString();
    }
}
