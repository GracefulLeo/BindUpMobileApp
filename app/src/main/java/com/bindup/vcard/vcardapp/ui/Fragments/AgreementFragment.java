package com.bindup.vcard.vcardapp.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bindup.vcard.vcardapp.R;
import com.bindup.vcard.vcardapp.ui.interfaces.IMainActivity;

public class AgreementFragment extends Fragment {

    //constants
    private static final String TAG = "AgreementFragment";

    //widgets
    private WebView mWebView;

    //vars
    private IMainActivity mInterface;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agreement_fragment, container, false);
        Log.d(TAG, "onCreateView: started.");

        mWebView = view.findViewById(R.id.webview_agreement);
        mWebView.loadUrl("http://telegra.ph/Privacy-Policy-of-Bind-Up-06-04");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mInterface = (IMainActivity) getActivity();
    }
}
