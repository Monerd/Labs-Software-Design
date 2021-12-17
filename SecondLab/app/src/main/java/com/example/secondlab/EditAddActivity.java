package com.example.secondlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditAddActivity extends AppCompatActivity{

    EditText workshopNumber;
    EditText productName;
    EditText count;

    ArrayList<Product> products = new ArrayList<>();
    int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);

        workshopNumber = (EditText) findViewById(R.id.workshopNumber);
        productName = (EditText) findViewById(R.id.productName);
        count = (EditText) findViewById(R.id.count);

        Button exitButton = (Button) findViewById(R.id.exit_edit_add_activity);
        Button acceptButton = (Button) findViewById(R.id.accept_edit_add_activity);

        acceptButton.setOnClickListener(view -> {
            if (dataChecking()) {
                if (productId != -1)
                    updateProductsData();
                else
                    addNewProduct();
                Intent intent = new Intent(EditAddActivity.this,
                        MainActivity.class);
                intent.putExtra("products", products);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(),
                        "Неверные данные",
                        Toast.LENGTH_SHORT).show();
        });
        exitButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditAddActivity.this,
                    MainActivity.class);
            intent.putExtra("products", products);
            startActivity(intent);
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            products = (ArrayList<Product>) bundle.get("products");
            if (bundle.get("product_id") != null) {
                productId = (int) bundle.get("product_id");
                setData();
            }
        }
    }

    protected void updateProductsData(){
        products.get(productId).setWorkshopNumber(
                Integer.parseInt(
                        workshopNumber.getText().toString()));
        products.get(productId).setProductName(
                productName.getText().toString());
        products.get(productId).setCount(
                Integer.parseInt(
                        count.getText().toString()));
    }

    protected void addNewProduct(){
        products.add(new Product(
                Integer.parseInt(count.getText().toString()),
                Integer.parseInt(workshopNumber.getText().toString()),
                productName.getText().toString()
        ));
    }

    protected boolean dataChecking(){
        return (!workshopNumber.getText().toString().equals("") &&
                !productName.getText().toString().equals("") &&
                !count.getText().toString().equals(""));
    }

    protected void setData(){
        workshopNumber.setText(String.valueOf(products
                .get(productId)
                .getWorkshopNumber()));
        productName.setText(String.valueOf(products
                .get(productId)
                .getProductName()));
        count.setText(String.valueOf(products
                .get(productId)
                .getCount()));
    }
}