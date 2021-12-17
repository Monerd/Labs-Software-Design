package com.example.secondlab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> sortProducts;
    ListView productsList;
    int selectId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.add_product);
        Button selectButton = (Button) findViewById(R.id.choose_workshop);


        addButton.setOnClickListener(view -> addProduct());
        selectButton.setOnClickListener(view -> selectProducts());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            products = (ArrayList<Product>) bundle.get("products");
        else
            setInitialData();
        updateProducts(false);
        productsList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            showPopupMenu(view, i);
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void showPopupMenu(View v, int i) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    Intent intent = new Intent(this,
                            EditAddActivity.class);
                    intent.putExtra("products", products);
                    intent.putExtra("product_id", i);
                    startActivity(intent);
                    return true;
                case R.id.menu_del:
                    products.remove(i);
                    updateProducts(false);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void addProduct(){
        Intent intent = new Intent(this, EditAddActivity.class);
        intent.putExtra("products", products);
        startActivity(intent);
    }

    private void selectProducts(){
        sortProducts = new ArrayList<>();
        EditText editText = findViewById(R.id.choose_workshop_input);
        try {
            if (!editText.getText().toString().equals("")) {
                selectId = Integer.parseInt(editText.getText().toString());
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getWorkshopNumber() == selectId)
                        sortProducts.add(products.get(i));
                }
                if (!sortProducts.isEmpty()) {
                    Collections.sort(sortProducts, (o1, o2) ->
                            Integer.compare(o2.getCount(), o1.getCount()));
                    updateProducts(true);
                }
                else
                    Toast.makeText(getApplicationContext(),
                            "Неверные данные",
                            Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ignored){}
    }

    private void updateProducts(Boolean select){
        productsList = findViewById(R.id.productsList);
        ProductAdapter productAdapter;
        if (!select)
            productAdapter = new ProductAdapter(this,
                    R.layout.list_item, products);
        else
            productAdapter = new ProductAdapter(this,
                    R.layout.list_item, sortProducts);
        productsList.setAdapter(productAdapter);
    }

    private void setInitialData(){
        products.add(new Product (5, 1, "Диван"));
        products.add(new Product (7, 2, "Стол"));
        products.add(new Product (1, 2, "Стул"));
        products.add(new Product (3, 3, "Шкаф"));
        products.add(new Product (12, 4, "Дверь"));
    }
}