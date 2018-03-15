package com.seller.steelhub.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seller.steelhub.R;
import com.seller.steelhub.customUi.MyButton;
import com.seller.steelhub.customUi.MyEditText;
import com.seller.steelhub.customUi.MyTextView;
import com.seller.steelhub.model.BargainAmount;
import com.seller.steelhub.model.InitialAmount;
import com.seller.steelhub.model.ModelManager;
import com.seller.steelhub.model.Quantity;
import com.seller.steelhub.model.Requirements;
import com.seller.steelhub.model.Specifications;
import com.seller.steelhub.utility.Preferences;
import com.seller.steelhub.utility.STLog;
import com.seller.steelhub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 11/7/2016.
 */
public class RequirementDetailFragment extends Fragment implements View.OnClickListener {
    private String TAG = RequirementDetailFragment.class.getSimpleName();
    private Activity activity;
    private MyEditText /*et_quantity,*/ et_preferred_brands, et_grade_required, et_required_by_date, et_city, et_state,
            et_budget_amount, et_tax_type, et_amount, et_bargain_amount;
    //    private MyTextView txt_random, txt_standard, txt_bend, txt_straight/*, txt_diameter*/;
    private MyButton btn_submit/*, btn_show_more*/;
    private LinearLayout addMoreLayout, /*layout_seller_list, */
    /*layout_show_more,*/ layout_amount,
            layout_bargain_amount;
    private ImageView ic_physical, ic_chemical, /*ic_grade_required,*/
            ic_test_certificate, ic_bargain_allowed;
    private String brandId = "", steelId = "", gradeId = "", stateId = "", is_bargain = "",
            taxId = "", phy = "", che = "", gra = "", lngth = "", typ = "", test_cert = "", id = "", initial_amt = "";
    private int index = -1;
    //    private ArrayList<Requirements> requirementsArrayList;
//    private ArrayList<Response> responseArrayList;
    private ArrayList<Specifications> specificationsArrayList = new ArrayList<>();
    private MyEditText[] quantities, amounts, bargainAmounts;
    private MyTextView[] diameters;
    private float totalNewAmount = 0, initialAmount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.requirements));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.requirement_detail_screen, container, false);

        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
                index = bundle.getInt("index");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }


        et_budget_amount = rootView.findViewById(R.id.et_budget_amount);
//        et_quantity = rootView.findViewById(R.id.et_quantity);
//        txt_diameter =  rootView.findViewById(R.id.txt_diameter);
        et_preferred_brands = rootView.findViewById(R.id.et_preferred_brands);
        et_grade_required = rootView.findViewById(R.id.et_grade_required);
        et_city = rootView.findViewById(R.id.et_city);
        et_state = rootView.findViewById(R.id.et_state);
        et_tax_type = rootView.findViewById(R.id.et_tax_type);
        et_required_by_date = rootView.findViewById(R.id.et_required_by_date);
        et_amount = rootView.findViewById(R.id.et_amount);
        et_bargain_amount = rootView.findViewById(R.id.et_bargain_amount);

        MyTextView txt_random = rootView.findViewById(R.id.txt_random);
        MyTextView txt_standard = rootView.findViewById(R.id.txt_standard);
        MyTextView txt_bend = rootView.findViewById(R.id.txt_bend);
        MyTextView txt_straight = rootView.findViewById(R.id.txt_straight);

//        layout_show_more = (LinearLayout) rootView.findViewById(R.id.layout_show_more);
//        layout_seller_list = (LinearLayout) rootView.findViewById(R.id.layout_seller_list);
        layout_amount = rootView.findViewById(R.id.layout_amount);
        layout_bargain_amount = rootView.findViewById(R.id.layout_bargain_amount);
        addMoreLayout = rootView.findViewById(R.id.layout_add_more);
        LinearLayout layout_bargain_allowed = rootView.findViewById(R.id.layout_bargain_allowed);

        ic_physical = rootView.findViewById(R.id.ic_physical);
        ic_chemical = rootView.findViewById(R.id.ic_chemical);
//        ic_grade_required = rootView.findViewById(R.id.ic_grade_required);
        ic_test_certificate = rootView.findViewById(R.id.ic_test_certificate);
        ic_bargain_allowed = rootView.findViewById(R.id.ic_bargain_allowed);

//        btn_show_more = (MyButton) rootView.findViewById(R.id.btn_show_more);
//        btn_show_more.setTransformationMethod(null);
        btn_submit = rootView.findViewById(R.id.btn_submit);
        btn_submit.setTransformationMethod(null);

        btn_submit.setOnClickListener(this);
//        btn_show_more.setOnClickListener(this);
        layout_bargain_allowed.setOnClickListener(this);

        setData();
        return rootView;
    }

    private void setData() {
//        "requirement_id": 27,
//                "user_id": 1,
//                "customer_type": ["Wholesaler", "Retailer", "Company"],
//        "quantity": [{
//            "size": "8 mm",
//                    "quantity": "23"
//        }],
//        "grade_required": "415",
//                "physical": 0,
//                "chemical": 0,
//                "test_certificate_required": 0,
//                "length": 0,
//                "type": 0,
//                "preffered_brands": ["Kamdhenu"],
//        "required_by_date": "13\/03\/2018",
//                "budget": 222222,
//                "state": "Bihar",
//                "city": "nepal",
//                "tax_type": "GST",
//                "is_seller_read": 1,
//                "initial_amt": 21500,
//                "is_buyer_read": 0,
//                "req_for_bargain": 1,
//                "is_seller_read_bargain": 1,
//                "is_best_price": 0,
//                "bargain_amt": 23333,
//                "is_buyer_read_bargain": 0,
//                "is_accepted": 1,
//                "is_seller_deleted": 0,
//                "is_buyer_deleted": 0,
//                "initial_unit_price": [{
//            "size": "8 mm",
//                    "quantity": "23",
//                    "unit price": "21500"
//        }],
//        "bargain_unit_price": [{
//            "size": "8 mm",
//                    "quantity": "23",
//                    "unit price": "21500",
//                    "new unit price": "21000"
//        }],
//        "brands": ["tata", "birla", "ambuja"]

        Requirements requirementsArrayList = ModelManager.getInstance().getRequirementManager().getRequirements(activity, false).get(index);
//        for (int i = 0; i < requirementsArrayList.size(); i++) {
        if (id.equalsIgnoreCase(requirementsArrayList/*.get(i)*/.getRequirement_id())) {

            if (requirementsArrayList/*.get(i)*/.getChemical().equalsIgnoreCase("0"))
                ic_chemical.setImageResource(R.drawable.toggle_off);
            else
                ic_chemical.setImageResource(R.drawable.toggle_on);

            if (requirementsArrayList/*.get(i)*/.getPhysical().equalsIgnoreCase("0"))
                ic_physical.setImageResource(R.drawable.toggle_off);
            else
                ic_physical.setImageResource(R.drawable.toggle_on);

            if (requirementsArrayList/*.get(i)*/.getTest_certificate_required().equalsIgnoreCase("0"))
                ic_test_certificate.setImageResource(R.drawable.toggle_off);
            else
                ic_test_certificate.setImageResource(R.drawable.toggle_on);

            if (requirementsArrayList/*.get(i)*/.getReq_for_bargain().equalsIgnoreCase("0")) {
                is_bargain = "0";
                ic_bargain_allowed.setImageResource(R.drawable.toggle_off);
            } else {
                is_bargain = "1";
                ic_bargain_allowed.setImageResource(R.drawable.toggle_on);
            }

            initial_amt = requirementsArrayList.getInitial_amt();

            et_budget_amount.setText(requirementsArrayList/*.get(i)*/.getBudget());
            et_grade_required.setText(requirementsArrayList/*.get(i)*/.getGrade_required());
            et_required_by_date.setText(requirementsArrayList/*.get(i)*/.getRequired_by_date());
            et_city.setText(requirementsArrayList/*.get(i)*/.getCity());
            et_state.setText(requirementsArrayList/*.get(i)*/.getState());
            et_tax_type.setText(requirementsArrayList/*.get(i)*/.getTax_type());

            if (requirementsArrayList/*.get(i)*/.getInitial_amt().equalsIgnoreCase("1")) {
                layout_amount.setVisibility(View.VISIBLE);
                et_amount.setText(requirementsArrayList/*.get(i)*/.getInitial_amt());
                et_amount.setFocusable(false);
                layout_bargain_amount.setVisibility(View.VISIBLE);
                if (!Utils.isEmptyString(requirementsArrayList/*.get(i)*/.getBargain_amt())) {
                    et_bargain_amount.setText(requirementsArrayList/*.get(i)*/.getBargain_amt());
                    btn_submit.setVisibility(View.GONE);
                } else {
                    btn_submit.setVisibility(View.VISIBLE);
                }
            } else {
                et_amount.setFocusable(true);
                layout_bargain_amount.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
            }
            et_bargain_amount.setFocusable(false);

            String[] preferredBrands = requirementsArrayList/*.get(i)*/.getPreffered_brands();
            StringBuilder val = new StringBuilder();
            if (preferredBrands != null)
                if (preferredBrands.length > 0) {
                    for (String preferredBrand : preferredBrands)
                        val.append(preferredBrand).append(", ");
                }
            if (val.length() > 0)
                val = new StringBuilder(val.substring(0, val.length() - 2));
            et_preferred_brands.setText(val.toString());

            if (requirementsArrayList/*.get(i)*/.getIs_seller_read().equalsIgnoreCase("0")) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("requirement_id", requirementsArrayList/*.get(i)*/.getRequirement_id());
                    jsonObject.put("seller_id", Preferences.readString(activity, Preferences.USER_ID, ""));
                    jsonObject.put("buyer_id", requirementsArrayList/*.get(i)*/.getUser_id());
                    jsonObject.put("type", "sellerReadStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requirementsArrayList/*.get(i)*/.readBuyerPost(activity, jsonObject);
            }

            setList(requirementsArrayList/*, i*/);

//                break;
//            }
        }
    }

    private void setList(Requirements requirementsArrayList/*, int i*/) {
        ArrayList<Quantity> quantityArrayList = requirementsArrayList/*.get(i)*/.getQuantityArrayList();
        if (quantityArrayList != null)
            if (quantityArrayList.size() > 0) {

                quantities = new MyEditText[quantityArrayList.size()];
                amounts = new MyEditText[quantityArrayList.size()];
                bargainAmounts = new MyEditText[quantityArrayList.size()];
                diameters = new MyTextView[quantityArrayList.size()];

                for (int j = 0; j < quantityArrayList.size(); j++) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row_add_more, null);
                    MyEditText quantity = addView.findViewById(R.id.quantity);
                    MyTextView diameter = addView.findViewById(R.id.diameter);
                    final MyEditText amount = addView.findViewById(R.id.amount);
                    final MyEditText bargain_amount = addView.findViewById(R.id.bargain_amount);

                    quantities[j] = quantity;
                    amounts[j] = amount;
                    bargainAmounts[j] = bargain_amount;
                    diameters[j] = diameter;

                    quantity.setFocusable(false);
                    amount.setFocusable(true);
                    amount.setVisibility(View.VISIBLE);

                    quantity.setText(quantityArrayList.get(j).getQuantity());
                    diameter.setText(quantityArrayList.get(j).getSize());
                    ArrayList<BargainAmount> bargainAmountArrayList = requirementsArrayList/*.get(i)*/.getBargainAmountArrayList();
                    ArrayList<InitialAmount> initialAmountArrayList = requirementsArrayList/*.get(i)*/.getInitialAmountArrayList();
                    if (bargainAmountArrayList != null) {
                        if (bargainAmountArrayList.size() > 0) {
                            amount.setText(bargainAmountArrayList.get(j).getAmount());
                            bargain_amount.setText(bargainAmountArrayList.get(j).getBargain_amount());
                            amount.setFocusable(false);
                            bargain_amount.setFocusable(false);
                            amount.setVisibility(View.VISIBLE);
                            bargain_amount.setVisibility(View.VISIBLE);
                        } else if (initialAmountArrayList != null) {
                            if (initialAmountArrayList.size() > 0) {
                                amount.setFocusable(false);
                                amount.setText(initialAmountArrayList.get(j).getAmount());
                                bargain_amount.setText("");
                                if (is_bargain.equalsIgnoreCase("1")) {
                                    bargain_amount.setFocusable(true);
                                    bargain_amount.setVisibility(View.VISIBLE);
                                } else
                                    bargain_amount.setVisibility(View.GONE);
                            }
                        }
                    } else if (initialAmountArrayList != null)
                        if (initialAmountArrayList.size() > 0) {
                            amount.setFocusable(false);
                            amount.setText(initialAmountArrayList.get(j).getAmount());
                            bargain_amount.setText("");
                            if (is_bargain.equalsIgnoreCase("1")) {
                                bargain_amount.setFocusable(true);
                                bargain_amount.setVisibility(View.VISIBLE);
                            } else
                                bargain_amount.setVisibility(View.GONE);
                        }
                    addMoreLayout.addView(addView);

                    bargain_amount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            float enteredAmount = 0;
                            if (count > 0)
                                enteredAmount = Float.parseFloat(bargain_amount.getText().toString());
                            totalNewAmount += enteredAmount;
                            et_bargain_amount.setText("" + totalNewAmount);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    amount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            float enteredAmount = 0;
                            if (count > 0)
                                enteredAmount = Float.parseFloat(amount.getText().toString());
                            initialAmount += enteredAmount;
                            et_amount.setText("" + initialAmount);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        if (initial_amt.equalsIgnoreCase("0"))
            btn_submit.setVisibility(View.VISIBLE);
        else if (is_bargain.equalsIgnoreCase("1"))
            btn_submit.setVisibility(View.VISIBLE);

        STLog.e("Token : ", "" + Preferences.readString(activity, Preferences.USER_TOKEN, ""));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_bargain_allowed:
                if (is_bargain.equalsIgnoreCase("1")) {
                    ic_bargain_allowed.setImageResource(R.drawable.toggle_off);
                    is_bargain = "0";
                    et_bargain_amount.setFocusable(false);
                } else {
                    ic_bargain_allowed.setImageResource(R.drawable.toggle_on);
                    is_bargain = "1";
                    et_bargain_amount.setFocusable(true);
                }

//                setList();
                break;

            case R.id.btn_submit:
//            {
//
//                "specification" : [
//                {
//                    "size": "10mm",
//                        "quantity":"500",
//                        "unit price":"2000",
//                       “new unit price”:”1900”
//                },
//                {
//                    "size": "10mm",
//                        "quantity":"500",
//                        "unit price":"2500",
//                       “new unit price”:”1950”
//
//                }
//
//             ],
//                "buyer_id": "23",
//                    "requirement_id":"1",
//                    "type":"sellerAcceptOrNot",
//                    "bargain_amt":"49000",
//                    "is_best_price":"0"
//            }
                String token = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray();
                    if (diameters != null)
                        if (diameters.length > 0)
                            for (int i = 0; i < diameters.length; i++) {
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("size", diameters[i].getText().toString());
                                jsonObject1.put("quantity", quantities[i].getText().toString());
                                jsonObject1.put("unit price", amounts[i].getText().toString());
                                if (!TextUtils.isEmpty(bargainAmounts[i].getText().toString())) {
                                    jsonObject1.put("new unit price", bargainAmounts[i].getText().toString());
                                    token = "sellerAcceptOrNot";
                                } else
                                    token = "sellerQuotation";
                                jsonArray.put(i, jsonObject1);
                            }
                    if (jsonArray.length() > 0)
                        jsonObject.put("specification", jsonArray);

                    jsonObject.put("buyer_id", ModelManager.getInstance().getRequirementManager().getRequirements(activity, false).get(index).getUser_id());
                    jsonObject.put("requirement_id", id);
                    jsonObject.put("type", "sellerAcceptOrNot");
                    if (token.equalsIgnoreCase("sellerAcceptOrNot")) {
                        jsonObject.put("bargain_amt", et_bargain_amount.getText().toString());
                        jsonObject.put("is_best_price", "0");
                    } else
                        jsonObject.put("initial_amt", et_amount.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                STLog.e("JSON : ", "" + jsonObject.toString());
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getRequirementManager().
                        getRequirements(activity, false).get(index).
                        updateConversationStatus(activity, jsonObject, token);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("BuyerBargain True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
            STLog.e(TAG, "BuyerBargain True");
        } else if (message.contains("BuyerBargain False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "BuyerBargain False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("BuyerAcceptedOrNot True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
            STLog.e(TAG, "BuyerAcceptedOrNot True");
        } else if (message.contains("BuyerAcceptedOrNot False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "BuyerAcceptedOrNot False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("ReadPost True")) {
            Utils.dismissLoading();
            ModelManager.getInstance().getRequirementManager().getRequirements(activity, false).get(index).setIs_seller_read("1");
            STLog.e(TAG, "ReadPost True");
        } else if (message.contains("ReadPost False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "ReadPost False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("sellerAcceptOrNot True")) {
            Utils.dismissLoading();
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getRequirementManager().getRequirements(activity, true);
            STLog.e(TAG, "sellerAcceptOrNot True");
        } else if (message.contains("sellerAcceptOrNot False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "sellerAcceptOrNot False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("sellerQuotation True")) {
            Utils.dismissLoading();
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getRequirementManager().getRequirements(activity, true);
            STLog.e(TAG, "sellerQuotation True");
        } else if (message.contains("sellerQuotation False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "sellerQuotation False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetRequirements True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
            STLog.e(TAG, "GetRequirements True");
        } else if (message.contains("GetRequirements False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "GetRequirements False");
            Utils.dismissLoading();
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
        }

    }
}
