package com.marolix.doitlater;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderClass> {
    Context context;
    ArrayList<Map<String,String>> arrayList;
    public CustomAdapter(Context context,ArrayList<Map<String,String>> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public CustomAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolderClass viewHolderClass,final int i) {
        viewHolderClass.name.setText(arrayList.get(i).get("name"));
        Log.e("name", (arrayList.get(i).get("name")));
        viewHolderClass.name.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent =new Intent(context,MainActivity.class);
        intent.putExtra("number",arrayList.get(i).get("number"));
        context.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);


        }
    }
}
