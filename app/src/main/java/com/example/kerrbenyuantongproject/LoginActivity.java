package com.example.kerrbenyuantongproject;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login = null;
    Button registe  = null;
    EditText account = null;
    EditText password = null;
    List<User> userList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();//界面初始化
        setClick();
        userList = new ArrayList<>(100);

    }

    public void setClick(){
        registe.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    public void init(){
        login = findViewById(R.id.login);
        registe = findViewById(R.id.registe);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    String reg_account = data.getStringExtra("accountReturn");
                    String reg_password = data.getStringExtra("passwordReturn");
                    System.out.println("*********************************************************");
                    Log.w( "onActivityResult: ",reg_account.toString()+reg_password.toString());
                    User user = new User(reg_account,reg_password);
                    userList.add(user);
                    Log.w("onActivityResult: ", userList.get(0).getName()+userList.get(0).getPassWord());
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registe:
                //跳往新的活动 开始注册
                Intent intent = new Intent(this,RegisteActivity.class);
                startActivityForResult(intent,1);


            case R.id.login:
                //比对数据库 有的话跳转下一个活动，没有的话弹出会话框 提示用户没有此用户
//                for(int i = 0 ;i<= userList.size()-1;i++){
//                    Log.w( "onClick: ", userList.get(0).getName()+userList.get(0).getPassWord()+account.getText()+password.getText());//
//                    if (userList.get(i).getName().toString().equals(account.getText().toString())){
//                        if (userList.get(i).getPassWord().equals(account.getText().toString())){
//                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//                            //活动跳转(activity)
                            Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent1);
//                        }
//                    }
//                    //Toast.makeText(this,"not equal",Toast.LENGTH_LONG).show();
//                }
                //没有此用户或密码错误(会话框)

        }
    }
}
