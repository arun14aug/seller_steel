package com.seller.steelhub.model;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seller.steelhub.utility.Preferences;
import com.seller.steelhub.utility.STLog;
import com.seller.steelhub.utility.ServiceApi;
import com.seller.steelhub.utility.Utils;

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
                                         "requirement_id": 51,
                                         "user_id": 23,
                                         "quantity": [{
                                         "size": "10 mm",
                                         "quantity": "20"
                                         }, {
                                         "size": "12 mm",
                                         "quantity": "10"
                                         }],
                                         "grade_required": "500D",
                                         "physical": 1,
                                         "chemical": 0,
                                         "test_certificate_required": 1,
                                         "length": 0,
                                         "type": 1,
                                         "preffered_brands": ["Rathi", "Tata Steel", "JSW Steel"],
                                         "required_by_date": "28\/12\/2016",
                                         "budget": 700000,
                                         "state": "Haryana",
                                         "city": "rewari",
                                         "tax_type": "VAT",
                                         "is_seller_read": 1,
                                         "initial_amt": 200000,
                                         "is_buyer_read": 1,
                                         "req_for_bargain": 1,
                                         "is_seller_read_bargain": 1,
                                         "is_best_price": 0,
                                         "bargain_amt": 197900,
                                         "is_buyer_read_bargain": 1,
                                         "is_accepted": 0,
                                         "is_seller_deleted": 0,
                                         "is_buyer_deleted": 0,
                                         "initial_unit_price": [{
                                         "unit price": "5000",
                                         "size": "10 mm",
                                         "quantity": "20"
                                         }, {
                                         "unit price": "10000",
                                         "size": "12 mm",
                                         "quantity": "10"
                                         }],
                                         "bargain_unit_price": [{
                                         "size": "10 mm",
                                         "quantity": "20",
                                         "unit price": "5000",
                                         "new unit price": "4900"
                                         }, {
                                         "size": "12 mm",
                                         "quantity": "10",
                                         "unit price": "10000",
                                         "new unit price": "9990"
                                         }],
                                         "brands": ["Rathi", "Tata Steel"]
                                         }**/
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
                                        requirements.setTax_type(jsonArray.getJSONObject(i).getString("tax_type"));

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
                                        if (!jsonArray.getJSONObject(i).getString("initial_amt").equalsIgnoreCase("0"))
                                            if (jsonArray.getJSONObject(i).has("initial_unit_price") && !jsonArray.getJSONObject(i).isNull("initial_unit_price")) {
                                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("initial_unit_price");
                                                if (jsonArray1.length() > 0) {
                                                    ArrayList<InitialAmount> quantities = new ArrayList<>();
                                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                                        InitialAmount quantity = new InitialAmount();
                                                        quantity.setSize(jsonArray1.getJSONObject(j).getString("size"));
                                                        quantity.setQuantity(jsonArray1.getJSONObject(j).getString("quantity"));
                                                        if (jsonArray1.getJSONObject(j).has("unit price"))
                                                            quantity.setAmount(jsonArray1.getJSONObject(j).getString("unit price"));
                                                        quantities.add(quantity);
                                                    }
                                                    requirements.setInitialAmountArrayList(quantities);
                                                }
                                            }
                                        if (!jsonArray.getJSONObject(i).getString("bargain_amt").equalsIgnoreCase("0"))
                                            if (jsonArray.getJSONObject(i).has("bargain_unit_price")&& !jsonArray.getJSONObject(i).isNull("bargain_unit_price")) {
                                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("bargain_unit_price");
                                                if (jsonArray1.length() > 0) {
                                                    ArrayList<BargainAmount> quantities = new ArrayList<>();
                                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                                        BargainAmount quantity = new BargainAmount();
                                                        quantity.setSize(jsonArray1.getJSONObject(j).getString("size"));
                                                        quantity.setQuantity(jsonArray1.getJSONObject(j).getString("quantity"));
                                                        if (jsonArray1.getJSONObject(j).has("unit price"))
                                                            quantity.setAmount(jsonArray1.getJSONObject(j).getString("unit price"));
                                                        if (jsonArray1.getJSONObject(j).has("new unit price"))
                                                            quantity.setBargain_amount(jsonArray1.getJSONObject(j).getString("new unit price"));
                                                        quantities.add(quantity);
                                                    }
                                                    requirements.setBargainAmountArrayList(quantities);
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
                                        if (jsonArray.getJSONObject(i).has("customer_type")) {
                                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("customer_type");
                                            if (jsonArray1.length() > 0) {
                                                String[] type = new String[jsonArray1.length()];
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    type[j] = jsonArray1.getString(j);
                                                }
                                                requirements.setCustomer_type(type);
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


}
