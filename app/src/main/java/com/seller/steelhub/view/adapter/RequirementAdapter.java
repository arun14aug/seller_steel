package com.seller.steelhub.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.seller.steelhub.R;
import com.seller.steelhub.customUi.MyTextView;
import com.seller.steelhub.model.Requirements;
import com.seller.steelhub.utility.Utils;

import java.util.ArrayList;

public class RequirementAdapter extends BaseAdapter {
    private ArrayList<Requirements> list;
    // ListView histry_list;
    private Activity activity;

    public RequirementAdapter(final Activity context,
                              ArrayList<Requirements> list) {
        this.list = list;
        this.activity = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint({"InflateParams", "NewApi", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // HashMap<String, Object> map = (HashMap<String, Object>)
        View v = convertView;
        final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.row_orders_list, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {

            String[] buyer_type = list.get(position).getCustomer_type();
            StringBuilder type = new StringBuilder();
            if (buyer_type != null) {
                if (buyer_type.length > 0)
                    for (String aBuyer_type : buyer_type) type.append(aBuyer_type).append(", ");
            }
            String t = type.toString();
            if (!TextUtils.isEmpty(t))
                t = t.substring(0, t.length() - 2);
            viewHolder.txt_buyer_type.setText("Buyer Type : " + t);
            viewHolder.txt_city.setText(list.get(position).getCity());
            viewHolder.txt_state.setText(list.get(position).getState());
            viewHolder.txt_budget.setText(list.get(position).getBudget());

            viewHolder.txt_date.setText(list.get(position).getRequired_by_date());

            if (list.get(position).getIs_accepted().equalsIgnoreCase("1")) {
                viewHolder.color_view.setBackgroundColor(Utils.setColor(activity, R.color.green_color));
                viewHolder.txt_status.setBackgroundResource(R.drawable.green_bubble);
                viewHolder.txt_status.setText("Order Inprogress");
            } else if (list.get(position).getIs_seller_read().equalsIgnoreCase("0")) {
                viewHolder.color_view.setBackgroundColor(Utils.setColor(activity, R.color.red_color));
                viewHolder.txt_status.setBackgroundResource(R.drawable.red_bubble);
                viewHolder.txt_status.setText("New Message");
            } else if (list.get(position).getIs_seller_read_bargain().equalsIgnoreCase("0")
                    && list.get(position).getReq_for_bargain().equalsIgnoreCase("1")){
                viewHolder.color_view.setBackgroundColor(Utils.setColor(activity, R.color.orange_color));
                viewHolder.txt_status.setBackgroundResource(R.drawable.orange_purple_bubble);
                viewHolder.txt_status.setText("Order Posted");
            } else{
                viewHolder.color_view.setBackgroundColor(Utils.setColor(activity, R.color.k_blue_color));
                viewHolder.txt_status.setBackgroundResource(R.drawable.sky_bubble);
                viewHolder.txt_status.setText("Bargain Done");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }

    class CompleteListViewHolder {
        MyTextView txt_city, txt_state, txt_budget, txt_date, txt_buyer_type, txt_status;
        ImageView img_action;
        View color_view;

        CompleteListViewHolder(View convertview) {
            txt_city = convertview
                    .findViewById(R.id.txt_city);
            txt_state = convertview
                    .findViewById(R.id.txt_state);
            txt_budget = convertview
                    .findViewById(R.id.txt_budget);
            txt_date = convertview.findViewById(R.id.txt_date);
            img_action = convertview
                    .findViewById(R.id.img_action);
            color_view = convertview.findViewById(R.id.color_view);
            txt_buyer_type = convertview.findViewById(R.id.txt_buyer_type);
            txt_status = convertview.findViewById(R.id.txt_status);
        }
    }
}
