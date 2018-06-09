package cst.hqu.edu.cn.myphone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by ASUS on 2018/5/24.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;

   // private Context context;

   /* private  static final int TYPE_RECEIVED=0;
    private static final int TYPE_SENT=1;*/

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View view){
            super(view);
            leftLayout =(LinearLayout)view.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            leftMsg=(TextView)view.findViewById(R.id.left_msg);
            rightMsg=(TextView)view.findViewById(R.id.right_msg);
        }
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
        //this.context=context;
    }

    //创建ViewHolder实例
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mag_item,parent,false);
        return new ViewHolder(view);
    }

    //对RecyclerView子项进行赋值
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ViewHolder viewHolder=(ViewHolder)holder;
        Msg msg=mMsgList.get(position);

        if (msg.getType()==Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }


    @Override
    public int getItemCount(){
        return mMsgList.size();
    }

   /* @Override
    public int getItemViewType(int position) {
        return mMsgList.get(position).getType()==0?TYPE_RECEIVED:TYPE_SENT;
    }*/

}
