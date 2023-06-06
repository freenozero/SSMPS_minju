package com.example.ssmps_android.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ssmps_android.R;
import com.example.ssmps_android.data.SharedPreferenceUtil;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.network.RetrofitAPI;
import com.example.ssmps_android.network.RetrofitClient;
import com.example.ssmps_android.network.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ItemModifyDeleteActivity extends AppCompatActivity {
    EditText itemQuantity;
    TextView itemName, itemType, itemPrice;
    ImageView itemImg;
    Button modifyBtn, deleteBtn;
    Item nowItem;

    Retrofit retrofit;
    RetrofitAPI service;

    TokenInterceptor tokenInterceptor;
    SharedPreferenceUtil sharedPreferenceUtil;
    Gson gson;
    String token;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_delete_modify);

        initData();
        setItemData();
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyItem();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    public void initData(){
        itemName = findViewById(R.id.itemDeleteModify_item_name);
        itemQuantity = findViewById(R.id.itemDeleteModify_item_quantity);
        itemType = findViewById(R.id.itemDeleteModify_item_type);
        itemPrice = findViewById(R.id.itemDeleteModify_item_price);

        itemImg = findViewById(R.id.itemDeleteModify_item_img);
        modifyBtn = findViewById(R.id.itemDeleteModify_modify_btn);
        deleteBtn = findViewById(R.id.itemDeleteModify_delete_btn);

        sharedPreferenceUtil = new SharedPreferenceUtil(getApplicationContext());
        gson = new GsonBuilder().create();
        setToken();

        retrofit = RetrofitClient.getInstance(tokenInterceptor);
        service = retrofit.create(RetrofitAPI.class);
        Intent intent = getIntent();

        nowItem = (Item) intent.getSerializableExtra("item");
    }

    private void setToken(){
        token = sharedPreferenceUtil.getData("token", "err");
        tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setToken(token);
    }


    private void setItemData(){
        itemName.setText(nowItem.getName());
        itemQuantity.setText(Integer.toString(nowItem.getQuantity()));
        itemType.setText(nowItem.getType());
        itemPrice.setText(Integer.toString(nowItem.getPrice()));
    }

    private void deleteItem(){
        Call<Item> deleteItem = service.deleteItem(nowItem);
        deleteItem.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(!response.isSuccessful()){
                    Log.e("delete item error", response.errorBody().toString());
                    Toast.makeText(ItemModifyDeleteActivity.this, "물건 삭제 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ItemModifyDeleteActivity.this, "물건 삭제 완료!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("delete item fail", t.getMessage());
                Toast.makeText(ItemModifyDeleteActivity.this, "물건 삭제 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modifyItem(){
        String quantity = itemQuantity.getText().toString();
        nowItem.setQuantity(Integer.parseInt(quantity));
        Call<Item> modifyQuantity = service.modifyItemQuantity(nowItem);
        modifyQuantity.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(!response.isSuccessful()){
                    Log.e("modify item error", response.errorBody().toString());
                    Toast.makeText(ItemModifyDeleteActivity.this, "물건 수정 에러", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ItemModifyDeleteActivity.this, "재고 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("modify item fail", t.getMessage());
                Toast.makeText(ItemModifyDeleteActivity.this, "믈건 수정 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
