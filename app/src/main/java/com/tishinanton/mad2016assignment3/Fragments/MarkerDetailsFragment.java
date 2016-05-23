package com.tishinanton.mad2016assignment3.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.tishinanton.mad2016assignment3.DAL.Place;
import com.tishinanton.mad2016assignment3.DAL.PlacesRepository;
import com.tishinanton.mad2016assignment3.Listeners.OnPlaceAddedListener;
import com.tishinanton.mad2016assignment3.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarkerDetailsFragment extends BottomSheetDialogFragment {

    private double Lat;
    private double Lng;

    private Button addPlaceButton;
    private EditText placeTitle;
    private Dialog thisDialog;

    private OnPlaceAddedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof  OnPlaceAddedListener) {
            listener = (OnPlaceAddedListener)activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lat = getArguments().getDouble("Lat");
        Lng = getArguments().getDouble("Lng");
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        thisDialog = dialog;
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_marker_details, null);
        dialog.setContentView(contentView);

        addPlaceButton = (Button)contentView.findViewById(R.id.addPlaceButton);
        placeTitle = (EditText)contentView.findViewById(R.id.placeTitle);

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlaceButtonClicked();
            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)((View)contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior)behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

    }

    private void addPlaceButtonClicked() {
        PlacesRepository repository = new PlacesRepository(getContext());
        Place place = repository.addPlace(placeTitle.getText().toString(), Lat, Lng);
        if (place != null && listener != null) {
            listener.onPlaceAdded(place);
        }
        thisDialog.dismiss();
    }
}
