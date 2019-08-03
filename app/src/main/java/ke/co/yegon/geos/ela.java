package ke.co.yegon.geos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ela extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader,_listSubHeader,_listContent;
    String editTextVal, editTextValC;
    // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ela(Context context, List<String> listDataHeader, List<String> listSubHeader,List<String> listContent,
               HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listSubHeader = listSubHeader;
        this._listContent = listContent;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater myInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = myInflater.inflate(R.layout.list_exp, null);

        }

        TextView txtListChild = convertView.findViewById(R.id.expandedListItem);
        txtListChild.setText(childText);

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {return this._listDataHeader.get(groupPosition); }

    public Object getSubGroup(int groupPosition) { return this._listSubHeader.get(groupPosition);}

    public Object getContent(int groupPosition) { return this._listContent.get(groupPosition);}

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        final String subheader = (String) getSubGroup(groupPosition);
        final String listCont = (String) getContent(groupPosition);

        if (convertView == null) {
            LayoutInflater myInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = myInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.listTitle);
//        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView sub = convertView.findViewById(R.id.preview);
        sub.setText(subheader);

        final ImageView pop = convertView.findViewById(R.id.popup_menu_button);
        pop.setFocusable(false);
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(parent.getContext(),pop);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_play) {

                            Intent i = new Intent(parent.getContext(),scr.class);
                            editTextVal = headerTitle;
                            editTextValC = listCont;
                            i.putExtra("Header",editTextVal);
                            i.putExtra("Content",editTextValC);
                            parent.getContext().startActivity(i);
//
//                            Dialog settingsDialog = new Dialog(parent.getContext());
//                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                            settingsDialog.setContentView(R.layout.list_group);
//                            settingsDialog.show();
                        }

                        if (id == R.id.action_share) {
                            String shareUrl = "https://yegon.pythonanywhere.com/blog?ie=UTF-8&source=android-browser&q=" + headerTitle;
                            if (shareUrl.isEmpty()) return false;

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, shareUrl);
                            parent.getContext().startActivity(Intent.createChooser(share, parent.getContext().getString(R.string.share)));
                        }


                        if (id == R.id.action_web) {
                            Intent i = new Intent(parent.getContext(),foot.class);
                            editTextVal = headerTitle;
                            i.putExtra("Value",editTextVal);
                            parent.getContext().startActivity(i);

                        }

                        return false;
                    }
                });
                popupMenu.show();
//     GOD Sent           Toast.makeText(parent.getContext(), "Menu coming",Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Intentionally put on comment, if you need on click deactivate it

}