package com.tagalong;

/**
 * Created by cpf5193 on 10/3/2015.
 */
  import java.util.ArrayList;
  import android.content.Context;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.BaseAdapter;
  import android.widget.CheckBox;
  import android.widget.TextView;
  import android.view.View.OnClickListener;

public class DropdownListAdapter extends BaseAdapter {

  private ArrayList<Friend> mListItems;
  private LayoutInflater mInflater;
  private TextView mSelectedItems;
  private static int selectedCount = 0;
  private static String firstSelected = "";
  private ViewHolder holder;
  private static String selected = "";	//shortened selected values representation

  public static String getSelected() {
    return selected;
  }

  public void setSelected(String selected) {
    DropdownListAdapter.selected = selected;
  }

  public DropdownListAdapter(Context context, ArrayList<Friend> items,
                             TextView selectedValues) {
    mListItems = new ArrayList<Friend>();
    if (items != null)
      mListItems.addAll(items);
    mInflater = LayoutInflater.from(context);
    mSelectedItems = selectedValues;
  }

  @Override
  public long getItemId(int index) {
    String idString = ((Friend) this.getItem(index)).getId();
   return Long.valueOf(idString).longValue();
  }

  @Override
  public int getCount() {
    return mListItems.size();
  }

  @Override
  public Object getItem(int index) {
    return mListItems.get(index);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.dropdown_item, null);
      holder = new ViewHolder();
      holder.tv = (TextView) convertView.findViewById(R.id.dropdownItemText);
      holder.chkbox = (CheckBox) convertView.findViewById(R.id.dropdownItemCheckbox);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.tv.setText(mListItems.get(position).getName());

    final int position1 = position;

    //whenever the checkbox is clicked the selected values textview is updated with new selected values
    holder.chkbox.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        setText(position1);
      }
    });

    if(isCheckedAtPosition(position))
      holder.chkbox.setChecked(true);
    else
      holder.chkbox.setChecked(false);
    return convertView;
  }

  private boolean isCheckedAtPosition(int index) {
    return this.mListItems.get(index).isSelected();
  }

  private void checkAtPosition(int index) {
    this.mListItems.get(index).setSelected(true);
  }

  private void uncheckAtPosition(int index) {
    this.mListItems.get(index).setSelected(false);
  }

  /*
   * Function which updates the selected values display and information(checkSelected[])
   * */
  private void setText(int index){
    if (!isCheckedAtPosition(index)) {
      this.checkAtPosition(index);
      selectedCount++;
    } else {
      this.uncheckAtPosition(index);
      selectedCount--;
    }

    if (selectedCount == 0) {
      mSelectedItems.setText(R.string.invite_input);
    } else if (selectedCount >= 1) {
      for (int i = 0; i < mListItems.size(); i++) {
        if (isCheckedAtPosition(i)) {
          firstSelected = mListItems.get(i).getName();
          break;
        }
      }
      if (selectedCount > 1) {
        mSelectedItems.setText(firstSelected + " & " + (selectedCount - 1) + " more");
        setSelected(firstSelected + " & " + (selectedCount - 1) + " more");
      } else {
        mSelectedItems.setText(firstSelected);
        setSelected(firstSelected);
      }
    }
  }

  private class ViewHolder {
    TextView tv;
    CheckBox chkbox;
  }
}