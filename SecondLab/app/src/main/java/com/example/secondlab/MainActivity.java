package com.example.secondlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    ListView productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialData();
        productsList = findViewById(R.id.productsList);
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.list_item, products);
        productsList.setAdapter(productAdapter);
        AdapterView.OnItemClickListener itemListener = (parent, v, position, id) -> {

            Product selectedProduct = (Product) parent.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedProduct.getProductName(),
                    Toast.LENGTH_SHORT).show();
        };
        productsList.setOnItemClickListener(itemListener);
    }
    private void setInitialData(){

        products.add(new Product (5, 1, "Станок"));
        products.add(new Product (5, 1, "Станок"));
        products.add(new Product (5, 1, "Станок"));
        products.add(new Product (5, 1, "Станок"));
        products.add(new Product (5, 1, "Станок"));
    }
}