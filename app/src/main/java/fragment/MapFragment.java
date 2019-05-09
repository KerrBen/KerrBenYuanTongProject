package fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.kerrbenyuantongproject.MainActivity;
import com.example.kerrbenyuantongproject.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.SortedMap;

import Util.WJS2Baidu;


public class MapFragment extends Fragment implements View.OnClickListener {
    //*************地图模块******************
    private MapView mapView = null;
    private BaiduMap baiduMap = null;
    private boolean isFristLocate = true;
    private LocationClient locationClient;
    private final double BAIDUMAPDEFULTLATITUDE = 1;
    private final double BAIDUMAPDEFULTLONGTITUDE = 2;
    //*************TCPIP****************
    private StringBuffer stringBuffer = null;
    private String readInText = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
    private Socket socket= null;
    private final String IP = "193.112.52.121";//未填
    private final int SORT =9997; //未填
    private WJSlocationListener mylocationListener;
    //*******************接收信息**********************
    private TextView recieveMessage = null;
    private StringBuffer recieve = null;
    private Button button = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationClient = new LocationClient(getContext());
        initLocation();
        recieveMessage = getActivity().findViewById(R.id.recieveMessage);
        button = getActivity().findViewById(R.id.button);
        locationClient.start();
        mapView = getActivity().findViewById(R.id.bdmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(IP,SORT);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    String start = "p";
                    String exit = "exit";
                    String p = "123";
                    dataOutputStream.writeChars("p");
                    dataOutputStream.writeChars("open");
                    dataOutputStream.writeChars("exit");
                    while (true) {
                        readInText = dataInputStream.readUTF();
                        //将string转化为float
                        double lat = 1;
                        double lon = 2;


                        stringBuffer.append(readInText);
                        //不为默认坐标
                        if (mylocationListener!=null){
                            mylocationListener.onLocationChanged(lat,lon);
                        }

                        Log.w( "run: ",readInText );
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("IO error");
                }
                if(socket.isConnected()){
                    Log.w( "run: ","*****************connected*****************" );
                }else {
                    Log.w( "run: ","*****************disconnected*****************" );
                }






            }
        }).start();

    }


    public class LocationListener implements WJSlocationListener{
        @Override
        public void onLocationChanged(double lat, double lon) {
            if (lat != BAIDUMAPDEFULTLATITUDE && lon != BAIDUMAPDEFULTLONGTITUDE) {
                Log.w("onReceiveLocation: ", "**********************************调用");
                navigateTo(lat, lon);

            }
        }
    }

    private void navigateTo(double lat,double lon){
        if (isFristLocate){
            LatLng latLng = new LatLng(lat,lon);
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(mapStatusUpdate);
            mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(mapStatusUpdate);
            isFristLocate = false;
        }
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(lat);
        builder.longitude(lon);
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        locationClient.setLocOption(option);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bdmap,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);

    }



        /************TCPIP方法******************/
        public void initTCPIP() {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }//android4.0以上主线程中不支持socket
        }

        public interface WJSlocationListener{
            void onLocationChanged(double lat, double lon);
        }

        public void showMassage(){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                }
            });
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                try{
                    if (dataInputStream!= null){
                        recieve = new StringBuffer();
                        recieve.append("接收到");
                        recieve.append(dataInputStream.readUTF());
                        recieveMessage.append(recieve);
                    }
                }catch (Exception e){

                }
        }
    }
}

