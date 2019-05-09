package com.example.kerrbenyuantongproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import bean.User;

public class RegisteActivity extends AppCompatActivity {
    Button confirm = null;
    EditText reg_account = null;
    EditText reg_password = null;
    List<User> userList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        init();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reg_account.getText() !=null&&reg_password != null){
                    //User user = new User(reg_account.getText().toString(),reg_password.getText().toString());
                    //userList.add(user);
                    Intent intent = new Intent();//user数据上传
                    intent.putExtra("accountReturn",reg_account.getText().toString());
                    intent.putExtra("passwordReturn",reg_password.getText().toString());
                    setResult(RESULT_OK,intent);
                    Toast.makeText(RegisteActivity.this,"注册成功"+reg_account.getText()+reg_password.getText(),Toast.LENGTH_LONG).show();
                    finish();//销毁活动
                    //-->弹出注册成功 会话框(Dialog)
                    //-->前往主活动

                }

            }
        });

    }

    public void init(){
        confirm = findViewById(R.id.confirm);
        reg_account = findViewById(R.id.reg_account);
        reg_password = findViewById(R.id.reg_password);
    }

}
