package com.auto.archcomponentsanalyst.WorkManager;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.auto.archcomponentsanalyst.R;
import com.auto.archcomponentsanalyst.WorkManager.db.DatabaseHelper;
import com.auto.archcomponentsanalyst.WorkManager.db.RequestPojo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

/**
 * Created by haohuidong on 18-6-25.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context mContext;

    public RequestAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.workmanger_item_request_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder != null){
            RequestPojo request = mData.get(position);
            if (request != null){
                StringBuilder tip = new StringBuilder();
                if (request.type == 0){
                    tip.append(OneTimeWorkRequest.class.getSimpleName());
                }else {
                    tip.append(PeriodicWorkRequest.class.getSimpleName());
                }
                tip.append(" --> ");
                tip.append(Tools.formatTime(request.enqueueDate, "yyyy/MM/dd HH:mm:ss"));
                holder.tvType.setText(tip.toString());
                holder.tvId.setText(request.id);

                holder.llPanel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestPojo request = mData.get(position);
                        if (request == null)
                            return;
                        addObserver(request.id);
                    }
                });
            }
        }
    }

    private void addObserver(String id){
        WorkManager.getInstance().getStatusById(UUID.fromString(id)).observe((LifecycleOwner) mContext, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus workStatus) {
                switch (workStatus.getState()){
                    case ENQUEUED:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "ENQUEUED id:"+workStatus.getId().toString());
                    }break;
                    case RUNNING:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "RUNNING id:"+workStatus.getId().toString());
                    }break;
                    case SUCCEEDED:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "SUCCEEDED id:"+workStatus.getId().toString());
                        Data output = workStatus.getOutputData();
                        String requestStr = output.getString(WMConstants.DATA_OUTPUT_KEY_REQUEST, WMConstants.DATA_OUTPUT_DEFAULT_REQUEST);
                        String outputStr = output.getString(WMConstants.DATA_OUTPUT_KEY_CONTENT, WMConstants.DATA_OUTPUT_DEFAULT_VALUE);
                        deleteRequest(workStatus.getId().toString());
                        Log.e(WMConstants.TAG, requestStr + " : "+outputStr);
                    }break;
                    case FAILED:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "FAILED id:"+workStatus.getId().toString());
                    }break;
                    case BLOCKED:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "BLOCKED id:"+workStatus.getId().toString());
                    }break;
                    case CANCELLED:{
                        Log.e(WMConstants.TAG, "-------------------------------------------------------------");
                        Log.e(WMConstants.TAG, "CANCELLED id:"+workStatus.getId().toString());
                    }break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            mData = new ArrayList<>();
        int size = mData.size();
        return size;
    }

    private ArrayList<RequestPojo> mData;
    public void setDatas(ArrayList<RequestPojo> data){
        if (data == null)
            return;
        if (mData == null)
            mData = new ArrayList<>();
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, size);
        mData.addAll((Collection<? extends RequestPojo>) data.clone());
        size = mData.size();
        notifyItemRangeChanged(0, size);
    }

    private void deleteRequest(String id){
        DatabaseHelper.getInstance().delete(DatabaseHelper.TABLE_WM_REQUEST, DatabaseHelper.DELETE_WM_REQUEST_BY_ID, id);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvType, tvId;
        public LinearLayout llPanel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvId = itemView.findViewById(R.id.tv_id);
            llPanel = itemView.findViewById(R.id.ll_panel);
        }
    }
}
