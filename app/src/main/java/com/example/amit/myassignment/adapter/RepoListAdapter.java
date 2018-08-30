package com.example.amit.myassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.CircleTransform;
import com.example.amit.myassignment.Utils.DatabaseHelper;
import com.example.amit.myassignment.Utils.GlobalDataService;
import com.example.amit.myassignment.Utils.MyFont;
import com.example.amit.myassignment.activity.RepoDetailsActivity;
import com.example.amit.myassignment.model.OwnerModel;
import com.example.amit.myassignment.model.RepoModel;
import com.example.amit.myassignment.model.SearchResultModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.DataObjectHolder> {
    private final Context context;
    List<RepoModel> mdataset;
    List<OwnerModel> ownerDataset;
    DatabaseHelper db;
    Bitmap im;

    public RepoListAdapter(Context context, List<RepoModel> mdataset) {
        this.context = context;
        this.mdataset = mdataset;;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_cell,parent,false);
        DataObjectHolder dataObjectHolder=new DataObjectHolder(view);
        try {
            MyFont.applyCustomFont((ViewGroup) view, MyFont.TYPEFACE.FontThin(context));
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, final int position) {
       // holder.repo_image
        try{

            final int dataIndex = position;


        holder.repository_name_by_owner.setText(mdataset.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalDataService.getInstance().setReponame(mdataset.get(position).getName());
                Intent intent = new Intent(context, RepoDetailsActivity.class);
                context.startActivity(intent);
            }
        });



        }



        catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return mdataset.size();//restircted as per guidelines
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView repository_name_by_owner;

        public DataObjectHolder(View itemView) {
            super(itemView);
            repository_name_by_owner               = (TextView) itemView.findViewById(R.id.repository_name_by_owner);


        }
    }




}
