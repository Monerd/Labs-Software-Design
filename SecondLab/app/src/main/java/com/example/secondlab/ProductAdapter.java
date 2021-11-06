package com.example.secondlab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final LayoutInflater inflater;
    private final int layout;
    private final List<Product> products;

    public ProductAdapter(Context context, int resource, List<Product> products) {
        super(context, resource, products);
        this.products = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextView workshopNumberView = convertView.findViewById(R.id.workshopNumber);
        TextView productNameView = convertView.findViewById(R.id.productName);
        TextView countView = convertView.findViewById(R.id.count);

        Product product = products.get(position);

        workshopNumberView.setText("Цех " + String.valueOf(product.getWorkshopNumber()));
        productNameView.setText("Название: " + product.getProductName());
        countView.setText("Кол-во: " + String.valueOf(product.getCount()));

        return convertView;
    }

    private static class ViewHolder {
        final TextView workshopNumberView, productNameView, countView;
        ViewHolder(View view){
            workshopNumberView = view.findViewById(R.id.workshopNumber);
            productNameView = view.findViewById(R.id.productName);
            countView = view.findViewById(R.id.count);
        }
    }
}
