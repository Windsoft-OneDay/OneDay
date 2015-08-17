package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.activity.LoginActivity;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";
    private OnSettingHandler sender;

    private String id;

    private Spinner spinner;
    private Button logout;
    private Button signOut;
    private Button gide;

    public static SettingFragment newInstance(String id) {
        SettingFragment fragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Global.KEY_USER_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (OnSettingHandler) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString(Global.KEY_USER_ID);
    }

    public SettingFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        spinner = (Spinner) rootView.findViewById(R.id.fragment_setting_spinner);
        logout = (Button) rootView.findViewById(R.id.fragment_setting_logout);
        signOut = (Button) rootView.findViewById(R.id.fragment_setting_sign_out);
        gide = (Button) rootView.findViewById(R.id.fragment_setting_gide);

        setSpinner();
        setListener();
    }


    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[] {getActivity().getString(R.string.goodBy), getActivity().getString(R.string.timeBy)});
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sender.sortByGood();
                } else {
                    sender.sortByTime();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.pref = getActivity().getSharedPreferences(Global.PREF_KEY, getActivity().MODE_PRIVATE);
                Global.editor = Global.pref.edit();
                Global.editor.putString(Global.KEY_LOGIN_ID, null);
                Global.editor.putString(Global.KEY_LOGIN_PW, null);
                Global.editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
                builder.setTitle(R.string.sign_out_title);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), OneDayService.class);
                        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SIGN_OUT);
                        intent.putExtra(Global.KEY_USER_ID, id);
                        getActivity().startService(intent);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public interface OnSettingHandler {
        void sortByGood();
        void sortByTime();
    }
}
