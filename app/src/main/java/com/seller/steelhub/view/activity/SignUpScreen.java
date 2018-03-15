package com.seller.steelhub.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.seller.steelhub.R;
import com.seller.steelhub.customUi.MyButton;
import com.seller.steelhub.customUi.MyEditText;
import com.seller.steelhub.customUi.MyTextView;
import com.seller.steelhub.model.AuthManager;
import com.seller.steelhub.model.Brands;
import com.seller.steelhub.model.ModelManager;
import com.seller.steelhub.model.States;
import com.seller.steelhub.utility.GPSTracker;
import com.seller.steelhub.utility.Preferences;
import com.seller.steelhub.utility.STLog;
import com.seller.steelhub.utility.Utils;
import com.seller.steelhub.view.adapter.CommonDialogAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 4/19/2016.
 */
public class SignUpScreen extends Activity implements View.OnClickListener {

    private String TAG = SignUpScreen.class.getSimpleName();
    private Activity activity = SignUpScreen.this;
    private MyEditText et_Email, et_Password, et_ConfirmPassword, et_Name, et_Company, et_Contact,
            et_Address, et_State, et_City, et_Zip, et_Tin, et_Pan, et_Required, et_brands;
    private AuthManager authManager;
    private double lat = 0.000, lng = 0.000;
    private GPSTracker gps;
    private ArrayList<States> statesArrayList;
    private ArrayList<Brands> brandsArrayList;
    private String stateId = "";
    private String[] brandSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        gps = new GPSTracker(activity);
        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(Utils.INITIAL_PERMS, Utils.INITIAL_REQUEST);
            }
        }

        authManager = ModelManager.getInstance().getAuthManager();
        String deviceId = Preferences.readString(getApplicationContext(), Preferences.DEVICE_ID, "");
        authManager.setDeviceToken(deviceId);

        if (!Utils.isEmptyString(Preferences.readString(getApplicationContext(), Preferences.USER_TOKEN, "")))
            ModelManager.getInstance().getAuthManager().setUserToken(Preferences.readString(getApplicationContext(), Preferences.USER_TOKEN, ""));


        MyButton submitBtn = findViewById(R.id.btn_submit);
        ImageView img_back = findViewById(R.id.back);


        if (lat == 0.000 || lng == 0.000)
            getLatLong();

        if (brandsArrayList == null) {
//            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getCommonDataManager().getBrands(activity, true);
        }
        if (statesArrayList == null) {
//            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getCommonDataManager().getStates(activity, true);
        }

        et_Email = findViewById(R.id.et_email);
        et_Password = findViewById(R.id.et_password);
        et_Name = findViewById(R.id.et_username);
        et_ConfirmPassword = findViewById(R.id.et_confirm_password);
        et_Company = findViewById(R.id.et_company_name);
        et_Contact = findViewById(R.id.et_contact);
        et_Address = findViewById(R.id.et_address);

        et_State = findViewById(R.id.et_state);
        et_City = findViewById(R.id.et_city);
        et_Zip = findViewById(R.id.et_zip);
        et_Tin = findViewById(R.id.et_tin);
        et_Pan = findViewById(R.id.et_pan);
        et_Required = findViewById(R.id.et_requirement_dropdown);
        et_brands = findViewById(R.id.et_brands);

        submitBtn.setTransformationMethod(null);

        submitBtn.setOnClickListener(this);
        img_back.setOnClickListener(this);
        et_State.setOnClickListener(this);
        et_brands.setOnClickListener(this);
    }

    private void getLatLong() {
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

    }

    public boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String perm) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (PackageManager.PERMISSION_GRANTED == activity.checkSelfPermission(perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Utils.LOCATION_REQUEST:
                if (canAccessLocation()) {
                    getLatLong();
                } else {
                    gps.showSettingsAlert();
                }
                break;
        }
    }


    private void showDropDownDialog() {
        final Dialog dropDownDialog = new Dialog(activity);
        dropDownDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dropDownDialog.setContentView(R.layout.dialog_dropdown_list);
        dropDownDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        MyTextView titleView = dropDownDialog
                .findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.please_select_an_option));
        final ListView listView = dropDownDialog
                .findViewById(R.id.list_view);

        ArrayList<String> list = new ArrayList<>();
        if (statesArrayList != null)
            if (statesArrayList.size() > 0)
                for (int i = 0; i < statesArrayList.size(); i++)
                    list.add(statesArrayList.get(i).getName());
            else {
                Utils.showMessage(activity, activity.getString(R.string.no_record_found));
                return;
            }
        else {
            Utils.showMessage(activity, activity.getString(R.string.no_record_found));
            return;
        }
        CommonDialogAdapter commonDialogAdapter = new CommonDialogAdapter(
                activity, list);
        listView.setAdapter(commonDialogAdapter);
        commonDialogAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                et_State.setText(statesArrayList.get(position).getName());
                stateId = statesArrayList.get(position).getCode();
                dropDownDialog.dismiss();
            }
        });

        dropDownDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (lat == 0.000 || lng == 0.000) {
                    getLatLong();
                }
                if (et_Name.getText().toString().trim().length() == 0) {
                    et_Name.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
                } else if (et_Password.getText().toString().trim().length() == 0) {
                    et_Password.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                } else if (et_ConfirmPassword.getText().toString().trim().length() == 0) {
                    et_ConfirmPassword.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_confirm_password), Toast.LENGTH_SHORT).show();
                } else if (!et_Password.getText().toString().equalsIgnoreCase(et_ConfirmPassword.getText().toString())) {
                    et_ConfirmPassword.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_correct_password), Toast.LENGTH_SHORT).show();
                } else if (et_Company.getText().toString().trim().length() == 0) {
                    et_Company.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_company), Toast.LENGTH_SHORT).show();
                } else if (et_Email.getText().toString().trim().length() == 0) {
                    et_Email.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else if (et_Contact.getText().toString().trim().length() == 0) {
                    et_Contact.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_contact), Toast.LENGTH_SHORT).show();
                } else if (et_Address.getText().toString().trim().length() == 0) {
                    et_Address.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
                } else if (et_State.getText().toString().trim().length() == 0) {
                    et_State.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
                } else if (et_Zip.getText().toString().trim().length() == 0) {
                    et_Zip.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_zip), Toast.LENGTH_SHORT).show();
                } else if (et_Tin.getText().toString().trim().length() == 0) {
                    et_Tin.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_tin), Toast.LENGTH_SHORT).show();
                } else if (et_Pan.getText().toString().trim().length() == 0) {
                    et_Pan.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_pan), Toast.LENGTH_SHORT).show();
                } else if (et_Required.getText().toString().trim().length() == 0) {
                    et_Required.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_expected_req), Toast.LENGTH_SHORT).show();
                } else {

                    Utils.showLoading(activity, getString(R.string.please_wait));
                    JSONObject post_data = new JSONObject();
                    try {
                        post_data.put("email", et_Email.getText().toString().trim());
                        post_data.put("password", et_Password.getText().toString().trim());
                        post_data.put("name", et_Name.getText().toString().trim());
                        post_data.put("contact", et_Contact.getText().toString().trim());
                        post_data.put("address", et_Address.getText().toString().trim());
                        post_data.put("state", stateId);
                        post_data.put("city", et_City.getText().toString().trim());
                        post_data.put("zip", et_Zip.getText().toString().trim());
                        post_data.put("tin", et_Tin.getText().toString().trim());

                        JSONArray arr = new JSONArray();
                        for (String aBrandSelected : brandSelected) arr.put(aBrandSelected);
                        post_data.put("brand", arr);

                        post_data.put("company_name", et_Company.getText().toString().trim());
                        post_data.put("role", "seller");
                        post_data.put("pan", et_Pan.getText().toString().trim());
                        post_data.put("latitude", lat);
                        post_data.put("longitude", lng);
                        post_data.put("exp_quantity", et_Required.getText().toString().trim());
                        post_data.put("device_type", "android");
                        post_data.put("device_token", authManager.getDeviceToken());


                        STLog.e(TAG, "Data" + post_data.toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    authManager.registerUser(SignUpScreen.this, post_data);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.et_brands:
                showMultiChoiceDropDown();
                break;
            case R.id.et_state:
                showDropDownDialog();
                break;
        }
    }

    private void showMultiChoiceDropDown() {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        // Convert the color array to list
//        final ArrayList<String> colorsList = new ArrayList<>();
        String[] colors;
        if (brandsArrayList != null)
            if (brandsArrayList.size() > 0) {
                colors = new String[brandsArrayList.size()];
                for (int i = 0; i < brandsArrayList.size(); i++)
                    colors[i] = brandsArrayList.get(i).getName();
//                    colorsList.add(brandsArrayList.get(i).getName());
            } else {
                Utils.showMessage(activity, activity.getString(R.string.no_record_found));
                return;
            }
        else {
            Utils.showMessage(activity, activity.getString(R.string.no_record_found));
            return;
        }
        final List<String> colorsList = Arrays.asList(colors);
        // Boolean array for initial selected items
        final boolean[] checkedColors = new boolean[colorsList.size()];


        builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                checkedColors[which] = isChecked;

                // Get the current focused item
                String currentItem = colorsList.get(which);

//                // Notify the current action
//                Toast.makeText(activity,
//                        currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(false);

        // Set a title for alert dialog
        builder.setTitle("Preferred Brands");

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click positive button
                StringBuilder val = new StringBuilder();
                int j = 0;
                for (int i = 0; i < checkedColors.length; i++) {
                    boolean checked = checkedColors[i];
                    if (checked) {
                        val.append(colorsList.get(i)).append(", ");
                        j++;
                    }
                }
                brandSelected = new String[j];
                int k = 0;
                for (int i = 0; i < checkedColors.length; i++)
                    if (checkedColors[i]) {
                        brandSelected[k] = colorsList.get(i);
                        k++;
                    }

                if (val.length() > 0)
                    val = new StringBuilder(val.substring(0, val.length() - 1));
                et_brands.setText(val.toString());
            }
        });

        // Set the neutral/cancel button click listener
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
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
        if (message.equalsIgnoreCase("GetBrandList True")) {
            Utils.dismissLoading();
            brandsArrayList = ModelManager.getInstance().getCommonDataManager().getBrands(activity, false);
            STLog.e(TAG, "GetBrandList True");
        } else if (message.contains("GetBrandList False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "GetBrandList False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetStateList True")) {
            Utils.dismissLoading();
            statesArrayList = ModelManager.getInstance().getCommonDataManager().getStates(activity, false);
            STLog.e(TAG, "GetStateList True");
        } else if (message.contains("GetStateList False")) {
            Utils.showMessage(activity, activity.getString(R.string.oops_something_went_wrong));
            STLog.e(TAG, "GetStateList False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Register True")) {
            Utils.dismissLoading();
            Preferences.writeString(activity, Preferences.EMAIL, et_Email.getText().toString());
            Preferences.writeString(activity, Preferences.PASSWORD, et_Password.getText().toString());
            STLog.e(TAG, "Register True");
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (message.contains("Register False")) {
            Utils.showMessage(activity, "Please check your credentials!");
            STLog.e(TAG, "Register False");
            Utils.dismissLoading();
        }

    }

}
