package fragment;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
  B类用于获得位置信息，并通过回调接口将位置信息传递给MapFragment类，在MapFragment类中调用接口实现相关功能
 */
public class TCP{
    Context context;
    private StringBuffer stringBuffer = null;
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    private Socket socket= null;
    private final String IP = "193.112.52.121";//未填
    private final int PORT =9997; //未填
    private StringBuffer recieve = null;
    private double lat;
    private double lon;

    public TCP(Context context){
        this.context = context;
    }
    public TCP(){}
    public void getlocation(final LocationListener locationListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(IP,PORT);
                   // socket = new Socket(IP,PORT);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                    String start = "p";
//                    String exit = "exit";
//                    String p = "123";
//                    dataOutputStream.writeChars("open");
//                    dataOutputStream.writeChars("exit");
                    Log.w( "run: ",""+"测试成功");

                    if(socket.isConnected()){
                        Log.w( "run: ","*****************connected*****************" );
                    }else {
                        Log.w( "run: ","*****************disconnected*****************" );
                    }
                    double lat = 39;
                    double lon = 115;
                    //InputStream is =socket.getInputStream();
                   // BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    while (true) {
                        locationListener.onLocationRecieved(lat,lon);
                            //byte[] bytes = new byte[80];
                         //   dataInputStream.read(bytes,0,80);
//                            double b = dataInputStream.readDouble();
//                            Log.w("run: ", ""+b)
                            //String info = null;
                        //    info=in.read();
//                       char c =  dataInputStream.readChar();
//                            Log.w("run: ",""+c);

                        //String s = "";
                        byte[] lobytes = new byte[10];
                        byte[] labytes = new byte[9];
                        dataInputStream.read(lobytes);
                        dataInputStream.read(labytes);
                        String lostr = new String(lobytes, StandardCharsets.UTF_8);
                        String lastr = new String(labytes, StandardCharsets.UTF_8);
                        //StandardCharsets.UTF_8
                      //  double db = getDouble(bytes);
                        Log.w("run: ", "收到str"+lostr);
                        Log.w("run: ","收到str"+lastr);
                     //   Log.w("run: ", "收到dou"+db);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("IO error");
                }

            }
        }).start();
    }

    public static double getDouble(byte[] b) {
        long m;
        m = b[0];
        m &= 0xff;
        m |= ((long) b[1] << 8);
        m &= 0xffff;
        m |= ((long) b[2] << 16);
        m &= 0xffffff;
        m |= ((long) b[3] << 24);
        m &= 0xffffffffl;
        m |= ((long) b[4] << 32);
        m &= 0xffffffffffl;
        m |= ((long) b[5] << 40);
        m &= 0xffffffffffffl;
        m |= ((long) b[6] << 48);
        m &= 0xffffffffffffffl;
        m |= ((long) b[7] << 56);
        return Double.longBitsToDouble(m);
    }

    public void sendMsg(final String s,final Socket sk){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //socket = new Socket(IP,PORT);
                    dataOutputStream = new DataOutputStream(sk.getOutputStream());
                    dataOutputStream.writeChars(s);
                    Log.w("run: ","发送成功" );
                }catch (Exception e){e.printStackTrace();}
            }
        }).start();

    }

}
