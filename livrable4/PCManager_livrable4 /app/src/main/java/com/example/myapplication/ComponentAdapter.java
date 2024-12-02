package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ComponentAdapter extends ArrayAdapter<Component> {

    private Context context;
    private List<Component> componentList;

    public ComponentAdapter(Context context, List<Component> componentList) {
        super(context, 0, componentList);
        this.context = context;
        this.componentList = componentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_component, parent, false);
        }

        // Get the component for this position
        Component component = componentList.get(position);

        // Populate the view with component details
        TextView nameTextView = convertView.findViewById(R.id.componentNameTextView);
        TextView quantityTextView = convertView.findViewById(R.id.componentQuantityTextView);

        nameTextView.setText(component.getName());
        quantityTextView.setText("Quantity: " + component.getQuantity());

        return convertView;
    }
}
