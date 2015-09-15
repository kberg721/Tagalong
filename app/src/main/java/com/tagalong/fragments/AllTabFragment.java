package com.tagalong.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tagalong.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTabFragment extends Fragment {

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment AllTabFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static AllTabFragment newInstance() {
    return new AllTabFragment();
  }

  public AllTabFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_all_tab, container, false);
  }
}
