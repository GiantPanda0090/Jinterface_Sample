import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static FileReader configLoader;
    private static BufferedReader bufferedReader;
    private static HashMap<String,String> settingLines;
    private static String serverName;

    public Config(String serverName){
        try {
            configLoader = new FileReader("src/main/config");
            bufferedReader = new BufferedReader(configLoader);
            String lines = null;
            this.serverName=serverName;
            while ((lines = bufferedReader.readLine()) != null){
                String [] nameSettingMatch=lines.split(": ");
                settingLines = new HashMap<String, String>();
                settingLines.put(nameSettingMatch[0],nameSettingMatch[1]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public int getPIDID(){
        String pidContent=settingLines.get("PID");//<0.103.0>
        String[] pidParser=pidContent.split("\\.");
        int id = Integer.parseInt(pidParser[1]);
        return id;
    }

    public String getServerName(){
        String hostname = getHostName();
        String ServerName= serverName+"@"+hostname;
        return ServerName;
    }

    private static String getHostName(){
        String hostname = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
            System.out.println(hostname);
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return hostname;
    }
}
