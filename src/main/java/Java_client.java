//https://stackoverflow.com/questions/22593537/sending-a-message-from-java-to-a-registered-erlang-gen-server

import com.ericsson.otp.erlang.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Java_client {
    static String server = "server";
    private static String dest;
    private static int id;
    private static OtpConnection conn;
    private static OtpErlangPid dest_pid;
    private static OtpSelf client;
    private static String serverName;
    public static void main(String[] args) {
        try {
            Java_client client = new Java_client("server");
            client.send("test message 001");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public Java_client(String serverName){
        this.serverName=serverName;
        Config configLoader = new Config(serverName);
        id=configLoader.getPIDID();
        dest=configLoader.getServerName();

        try {
            client = new OtpSelf("client", "java");
            OtpPeer peer = new OtpPeer(server);
            conn = client.connect(peer);
            System.out.println("Server connected!");
            dest_pid = new OtpErlangPid(dest, id, 0, 0);
            System.out.println(dest_pid.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(String message) throws Exception {
        conn.send(dest_pid,new OtpErlangTuple(new OtpErlangObject[] {
                new OtpErlangAtom("publish"),
                new OtpErlangTuple(new OtpErlangObject[] {
                        client.pid(),
                        new OtpErlangString(message)})
        }));
        System.out.println("Meessage Sent!");
        //OtpErlangObject reply = conn.receive();
        //System.out.println("Received " + reply);
        //conn.send("server",counter_name);

        /*
        conn.sendRPC("global", "send", new OtpErlangObject[] {
                new OtpErlangAtom(global),
                new OtpErlangTuple(new OtpErlangObject[] {
                        new OtpErlangAtom("publish"),
                        new OtpErlangTuple(new OtpErlangObject[] {
                                client.pid(),
                                new OtpErlangString(message)})
                    })});
        System.out.println("Meessage Sent!");
        */
        //conn.send("server",counter_name);


    }

    public static void call() throws Exception {
        OtpErlangObject aValue = new OtpErlangLong(10);
        OtpNode javaNode = new OtpNode("client", "java");
        OtpMbox jProcess = javaNode.createMbox();
        OtpErlangPid jPid = jProcess.self();
        jProcess.registerName("jProcess");
        OtpErlangObject counter_name = new OtpErlangList("my_counter");
        OtpErlangObject[] set_msg =
                {jPid, new OtpErlangAtom("set"), counter_name, aValue};
        //OtpErlangObject msg = new OtpErlangTuple(set_msg);
        jProcess.send("global","server",new OtpErlangTuple(set_msg));
        OtpErlangObject response = jProcess.receive();
        String remote_counter_name =
                ((OtpErlangString)((OtpErlangTuple) response).elementAt(1))
                        .stringValue();
        System.out.println(remote_counter_name);
    }


    public static String receive() throws Exception {
        OtpErlangObject reply = conn.receive();
        System.out.println("Received " + reply);
        return reply.toString();
    }
    }