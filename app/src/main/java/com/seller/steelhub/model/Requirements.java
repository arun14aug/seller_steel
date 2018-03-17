package com.seller.steelhub.model;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seller.steelhub.utility.Preferences;
import com.seller.steelhub.utility.STLog;
import com.seller.steelhub.utility.ServiceApi;
import com.seller.steelhub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 9/14/2016.
 */
public class Requirements {

    private String requirement_id, user_id, grade_required, physical, chemical, test_certificate_required,
            length, type, required_by_date, budget, state, city, created_at, updated_at;
    private String is_seller_read, initial_amt, is_buyer_read, req_for_bargain, is_seller_read_bargain, is_best_price,
            bargain_amt, is_buyer_read_bargain, is_accepted, is_seller_deleted, is_buyer_deleted, tax_type;
    private ArrayList<Response> responseArrayList;

    private String[] preffered_brands;
    private String[] brands;
    private String[] customer_type;
    private ArrayList<Quantity> quantityArrayList;
    private ArrayList<InitialAmount> initialAmountArrayList;
    private ArrayList<BargainAmount> bargainAmountArrayList;

    public String[] getBrands() {
        return brands;
    }

    public void setBrands(String[] brands) {
        this.brands = brands;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public String[] getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String[] customer_type) {
        this.customer_type = customer_type;
    }

    public ArrayList<Response> getResponseArrayList() {
        return responseArrayList;
    }

    public void setResponseArrayList(ArrayList<Response> responseArrayList) {
        this.responseArrayList = responseArrayList;
    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGrade_required() {
        return grade_required;
    }

    public void setGrade_required(String grade_required) {
        this.grade_required = grade_required;
    }

    public String getPhysical() {
        return physical;
    }

    public void setPhysical(String physical) {
        this.physical = physical;
    }

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

    public String getTest_certificate_required() {
        return test_certificate_required;
    }

    public void setTest_certificate_required(String test_certificate_required) {
        this.test_certificate_required = test_certificate_required;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequired_by_date() {
        return required_by_date;
    }

    public void setRequired_by_date(String required_by_date) {
        this.required_by_date = required_by_date;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String[] getPreffered_brands() {
        return preffered_brands;
    }

    public void setPreffered_brands(String[] preffered_brands) {
        this.preffered_brands = preffered_brands;
    }

    public ArrayList<Quantity> getQuantityArrayList() {
        return quantityArrayList;
    }

    public void setQuantityArrayList(ArrayList<Quantity> quantityArrayList) {
        this.quantityArrayList = quantityArrayList;
    }

    public ArrayList<InitialAmount> getInitialAmountArrayList() {
        return initialAmountArrayList;
    }

    public void setInitialAmountArrayList(ArrayList<InitialAmount> initialAmountArrayList) {
        this.initialAmountArrayList = initialAmountArrayList;
    }

    public ArrayList<BargainAmount> getBargainAmountArrayList() {
        return bargainAmountArrayList;
    }

    public void setBargainAmountArrayList(ArrayList<BargainAmount> bargainAmountArrayList) {
        this.bargainAmountArrayList = bargainAmountArrayList;
    }

    public String getIs_seller_read() {
        return is_seller_read;
    }

    public void setIs_seller_read(String is_seller_read) {
        this.is_seller_read = is_seller_read;
    }

    public String getInitial_amt() {
        return initial_amt;
    }

    public void setInitial_amt(String initial_amt) {
        this.initial_amt = initial_amt;
    }

    public String getIs_buyer_read() {
        return is_buyer_read;
    }

    public void setIs_buyer_read(String is_buyer_read) {
        this.is_buyer_read = is_buyer_read;
    }

    public String getReq_for_bargain() {
        return req_for_bargain;
    }

    public void setReq_for_bargain(String req_for_bargain) {
        this.req_for_bargain = req_for_bargain;
    }

    public String getIs_seller_read_bargain() {
        return is_seller_read_bargain;
    }

    public void setIs_seller_read_bargain(String is_seller_read_bargain) {
        this.is_seller_read_bargain = is_seller_read_bargain;
    }

    public String getIs_best_price() {
        return is_best_price;
    }

    public void setIs_best_price(String is_best_price) {
        this.is_best_price = is_best_price;
    }

    public String getBargain_amt() {
        return bargain_amt;
    }

    public void setBargain_amt(String bargain_amt) {
        this.bargain_amt = bargain_amt;
    }

    public String getIs_buyer_read_bargain() {
        return is_buyer_read_bargain;
    }

    public void setIs_buyer_read_bargain(String is_buyer_read_bargain) {
        this.is_buyer_read_bargain = is_buyer_read_bargain;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }

    public String getIs_seller_deleted() {
        return is_seller_deleted;
    }

    public void setIs_seller_deleted(String is_seller_deleted) {
        this.is_seller_deleted = is_seller_deleted;
    }

    public String getIs_buyer_deleted() {
        return is_buyer_deleted;
    }

    public void setIs_buyer_deleted(String is_buyer_deleted) {
        this.is_buyer_deleted = is_buyer_deleted;
    }

    public void updateConversationStatus(final Activity activity, JSONObject jsonObject, final String token) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.UPDATE_CONVERSATION_STATUS, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        STLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("success");
                            if (state) {
                                EventBus.getDefault().postSticky(token + " True");
                            } else {
                                EventBus.getDefault().postSticky(token + " False@#@" + response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().postSticky(token + " False");
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                STLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky(token + "UpdateConversationStatus False");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer" + Preferences.readString(activity, Preferences.USER_TOKEN, ""));
                return params;
            }
        };
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void readBuyerPost(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.UPDATE_CONVERSATION_STATUS, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        STLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("success");
                            if (state) {
                                Requirements.this.setIs_seller_read("1");
                                EventBus.getDefault().postSticky("ReadPost True");
                            } else {
                                EventBus.getDefault().postSticky("ReadPost False@#@" + response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().postSticky("ReadPost False");
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                STLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("ReadPost False");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer" + Preferences.readString(activity, Preferences.USER_TOKEN, ""));
                return params;
            }
        };
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
