package com.example.optimizeprimeandroidapp.helper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.optimizeprimeandroidapp.R;
import com.example.optimizeprimeandroidapp.databinding.DialogProgressBinding;

import java.util.Objects;

public class ProgressDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        setCancelable(false);
        setRetainInstance(true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogProgressBinding dialogProgressBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_progress, container, false);
        Glide.with(getContext()).load(R.drawable.ic_loader).into(dialogProgressBinding.ivGif);
        return dialogProgressBinding.getRoot();
    }

    public void showProgress(FragmentManager fragmentManager) {
        try {
            if (!isAdded()) {
                String fragmentTag = "dialogLoader";
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(this, fragmentTag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException | NullPointerException exception) {
            setCancelable(true);
        }
    }

    public void hideProgress() {
        try {
            if (isAdded()) {
                super.dismissAllowingStateLoss();
            }
        } catch (NullPointerException | IllegalStateException exception) {
            setCancelable(true);
        }
    }
}