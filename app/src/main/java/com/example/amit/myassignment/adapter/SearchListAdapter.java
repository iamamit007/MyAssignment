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
import com.example.amit.myassignment.activity.ContriButorsDetailActivity;
import com.example.amit.myassignment.model.OwnerModel;
import com.example.amit.myassignment.model.SearchResultModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListAdapter  extends RecyclerView.Adapter<SearchListAdapter.DataObjectHolder> {
    private final Context context;
    List<SearchResultModel> mdataset;
    List<OwnerModel> ownerDataset;
    DatabaseHelper db;
    Bitmap im;
    public SearchListAdapter(Context context, List<SearchResultModel> mdataset,DatabaseHelper db) {
        this.context        = context;
        this.mdataset       = mdataset;
        this.db             = db;
    }

    public SearchListAdapter(Context context, List<SearchResultModel> mdataset,List<OwnerModel> dataset) {
        this.context = context;
        this.mdataset = mdataset;
        this.ownerDataset = dataset;
    }

    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_cell,parent,false);
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


        holder.repo_name.setText(mdataset.get(position).getName());
        holder.repository_full_name.setText(mdataset.get(position).getFull_name());
        holder.watcher_number.setText(mdataset.get(position).getWatchers_count());

            holder.score_count.setText(""+mdataset.get(position).getScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalDataService.getInstance().setOwnerName(ownerDataset.get(dataIndex).getLogin());
                GlobalDataService.getInstance().setReponame(mdataset.get(position).getName());
                Intent intent = new Intent(context, RepoDetailsActivity.class);
                intent.putExtra("name",mdataset.get(dataIndex).getName());
                context.startActivity(intent);
            }
        });

        String avatar_image = ownerDataset.get(position).getAvatar_url();
            Picasso.with(context).load(avatar_image).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.repo_image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.shimmer_view_container.stopShimmerAnimation();
                }

                @Override
                public void onError() {

                }
            });





        }



        catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public int getItemCount() {
        return 10;//restircted as per guidelines
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        private final ShimmerFrameLayout shimmer_view_container;
        TextView repo_name, watcher_number, score_count,repository_full_name;
        ImageView repo_image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            repo_name               = (TextView) itemView.findViewById(R.id.repository_name);
            repository_full_name    = (TextView) itemView.findViewById(R.id.repository_full_name);
            watcher_number          = (TextView) itemView.findViewById(R.id.watchers_count);
            score_count             = (TextView) itemView.findViewById(R.id.score);
            repo_image              = (ImageView) itemView.findViewById(R.id.repo_image);
            shimmer_view_container  = itemView.findViewById(R.id.shimmer_pic);
            shimmer_view_container.startShimmerAnimation();


        }
    }




}
