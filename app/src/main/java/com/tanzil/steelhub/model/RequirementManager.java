package com.tanzil.steelhub.model;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tanzil.steelhub.utility.Preferences;
import com.tanzil.steelhub.utility.STLog;
import com.tanzil.steelhub.utility.ServiceApi;
import com.tanzil.steelhub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 9/30/2016.
 */
public class RequirementManager {
    //    private String TAG = RequirementManager.class.getSimpleName();
    private ArrayList<Requirements> requirementsArrayList;

    public ArrayList<Requirements> getRequirements(Activity activity, boolean shouldRefresh) {
        if (shouldRefresh)
            getRequirementList(activity);
        return requirementsArrayList;
    }

    public void getRequirementList(final Activity activity) {
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("user_id", Preferences.readString(activity, Preferences.USER_ID, ""));
            jsonObject.put("user_id", "23");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        STLog.e("Post Data : ", "" + jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.POSTED_REQUIREMENTS, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        STLog.e("Success Response : ", "Response: " + response.toString());
                        try {
                            if (response.getBoolean("success")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                int count = jsonArray.length();
                                if (count > 0) {
                                    requirementsArrayList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        "response": [{
                                       /** {
                                            "requirement_id": 37,
                                                "user_id": 23,
                                                "quantity": [{
                                            "size": "10 mm",
                                                    "quantity": "222"
                                        }],
                                            "grade_required": "415",
                                                "physical": 1,
                                                "chemical": 1,
                                                "test_certificate_required": 1,
                                                "length": 0,
                                                "type": 0,
                                                "preffered_brands": ["Rathi"],
                                            "required_by_date": "18\/11\/2016",
                                                "budget": 222222,
                                                "state": "Haryana",
                                                "city": "dabwali",
                                                "tax_type": "GST",
                                                "is_seller_read": 1,
                                                "initial_amt": 22,
                                                "is_buyer_read": 0,
                                                "req_for_bargain": 0,
                                                "is_seller_read_bargain": 0,
                                                "is_best_price": 0,
                                                "bargain_amt": 0,
                                                "is_buyer_read_bargain": 0,
                                                "is_accepted": 0,
                                                "is_seller_deleted": 0,
                                                "is_buyer_deleted": 0
                                        },**/
                                        Requirements requirements = new Requirements();
                                        requirements.setRequirement_id(jsonArray.getJSONObject(i).getString("requirement_id"));
                                        requirements.setUser_id(jsonArray.getJSONObject(i).getString("user_id"));
                                        requirements.setGrade_required(jsonArray.getJSONObject(i).getString("grade_required"));
                                        requirements.setPhysical(jsonArray.getJSONObject(i).getString("physical"));
                                        requirements.setChemical(jsonArray.getJSONObject(i).getString("chemical"));
                                        requirements.setTest_certificate_required(jsonArray.getJSONObject(i).getString("test_certificate_required"));
                                        requirements.setLength(jsonArray.getJSONObject(i).getString("length"));
                                        requirements.setType(jsonArray.getJSONObject(i).getString("type"));
                                        requirements.setRequired_by_date(jsonArray.getJSONObject(i).getString("required_by_date"));
                                        requirements.setBudget(jsonArray.getJSONObject(i).getString("budget"));
                                        requirements.setState(jsonArray.getJSONObject(i).getString("state"));
                                        requirements.setCity(jsonArray.getJSONObject(i).getString("city"));

                                        requirements.setIs_seller_read(jsonArray.getJSONObject(i).getString("is_seller_read"));
                                        requirements.setInitial_amt(jsonArray.getJSONObject(i).getString("initial_amt"));
                                        requirements.setIs_buyer_read(jsonArray.getJSONObject(i).getString("is_buyer_read"));
                                        requirements.setReq_for_bargain(jsonArray.getJSONObject(i).getString("req_for_bargain"));
                                        requirements.setIs_seller_read_bargain(jsonArray.getJSONObject(i).getString("is_seller_read_bargain"));
                                        requirements.setIs_best_price(jsonArray.getJSONObject(i).getString("is_best_price"));
                                        requirements.setBargain_amt(jsonArray.getJSONObject(i).getString("bargain_amt"));
                                        requirements.setIs_buyer_read_bargain(jsonArray.getJSONObject(i).getString("is_buyer_read_bargain"));
                                        requirements.setIs_seller_deleted(jsonArray.getJSONObject(i).getString("is_seller_deleted"));
                                        requirements.setIs_accepted(jsonArray.getJSONObject(i).getString("is_accepted"));
                                        requirements.setIs_buyer_deleted(jsonArray.getJSONObject(i).getString("is_buyer_deleted"));

                                        if (jsonArray.getJSONObject(i).has("quantity")) {
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("quantity");
                                            if (jsonArray1.length() > 0) {
                                                ArrayList<Quantity> quantities = new ArrayList<>();
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    Quantity quantity = new Quantity();
                                                    quantity.setSize(jsonArray1.getJSONObject(j).getString("size"));
                                                    quantity.setQuantity(jsonArray1.getJSONObject(j).getString("quantity"));
                                                    quantities.add(quantity);
                                                }
                                                requirements.setQuantityArrayList(quantities);
                                            }
                                        }
                                        if (jsonArray.getJSONObject(i).has("preffered_brands")) {
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("preffered_brands");
                                            if (jsonArray1.length() > 0) {
                                                String[] brand = new String[jsonArray1.length()];
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    brand[j] = jsonArray1.getString(j);
                                                }
                                                requirements.setPreffered_brands(brand);
                                            }
                                        }
                                        requirementsArrayList.add(requirements);
                                    }
                                }
                                EventBus.getDefault().post("GetRequirements True");
                            } else
                                EventBus.getDefault().post("GetRequirements False");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetRequirements False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                STLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetRequirements False");
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

    public void addBuyerPost(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.BUYER_POST, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        STLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("success");
                            if (state) {
                                Preferences.writeBoolean(activity, Preferences.LOGIN, true);

                                EventBus.getDefault().postSticky("NewRequirementPosted True");
                            } else {
                                EventBus.getDefault().postSticky("NewRequirementPosted False@#@" + response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().postSticky("NewRequirementPosted False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                STLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("NewRequirementPosted False");
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
