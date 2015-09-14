package com.tagalong.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tagalong.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoingTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoingTabFragment extends Fragment {

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment GoingTabFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GoingTabFragment newInstance() {
    return new GoingTabFragment();
  }

  public GoingTabFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_going_tab, container, false);
  }
}
