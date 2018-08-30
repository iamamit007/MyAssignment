package com.example.amit.myassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.CircleTransform;
import com.example.amit.myassignment.activity.ContriButorsDetailActivity;
import com.example.amit.myassignment.activity.RepoDetailsActivity;
import com.example.amit.myassignment.model.ContributorModel;
import com.example.amit.myassignment.model.OwnerModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContributorAdapter  extends BaseAdapter
{

    String [] result;
    Context context;
    ArrayList<ContributorModel> mdataset = new ArrayList<>();


    int [] imageId;
    private static LayoutInflater inflater=null;

    public ContributorAdapter(Context context,ArrayList<ContributorModel> mdataset) {
        this.context = context;
        this.mdataset= mdataset;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mdataset.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        final int dataIndex = i;


        rowView = inflater.inflate(R.layout.contributor_cell, null);
        holder.contributors_name =(TextView) rowView.findViewById(R.id.contributors_name);
        holder.contributors_img =(ImageView) rowView.findViewById(R.id.contributors_img);



        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                ContriButorsDetailActivity.setContributorModel(mdataset.get(dataIndex));
                Intent intent = new Intent(context, ContriButorsDetailActivity.class);
                context.startActivity(intent);
                // TODO Auto-generated method stub
            }
        });

        holder.contributors_name.setText(""+mdataset.get(dataIndex).getLogin());

        String avatar_image = mdataset.get(dataIndex).getAvatar_url();
        Picasso.with(context).load(avatar_image).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.contributors_img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });



        return rowView;
    }


    public class Holder
    {
        TextView contributors_name;
        ImageView contributors_img;
    }
}
