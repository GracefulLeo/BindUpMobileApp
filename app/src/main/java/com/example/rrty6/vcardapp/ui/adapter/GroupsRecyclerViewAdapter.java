package com.example.rrty6.vcardapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rrty6.vcardapp.GlideApp;
import com.example.rrty6.vcardapp.R;
import com.example.rrty6.vcardapp.data.storage.model.Group;
import com.example.rrty6.vcardapp.ui.interfaces.IMainActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.ViewHolder> {
    //constants
    private static final String TAG = "Groups Adapter";

    //vars
    private List<Group> mGroups;
    private Context mContext;
    private IMainActivity mInterface;


    public GroupsRecyclerViewAdapter(Context context, List<Group> groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_groups_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if (mGroups.get(position).getLogo() != null) {
            GlideApp
                    .with(holder.itemView.getContext())
                    .load(mGroups.get(position).getLogo().getLogoBitmap())
                    .into(holder.mGroupImage);
        }
        holder.mGroupsDescription.setText(mGroups.get(position).getDescription());
        holder.mGroupName.setText(mGroups.get(position).getName());
        holder.mGroupsPreviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.inflateViewGroupProfileGroups(mGroups.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mGroups != null) {
            return mGroups.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IMainActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mGroupsPreviewContainer;
        CircleImageView mGroupImage;
        TextView mGroupName;
        TextView mGroupsDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mGroupsPreviewContainer = itemView.findViewById(R.id.groups_preview_container);
            mGroupImage = itemView.findViewById(R.id.groups_image);
            mGroupName = itemView.findViewById(R.id.groups_name);
            mGroupsDescription = itemView.findViewById(R.id.groups_description);
        }
    }
}