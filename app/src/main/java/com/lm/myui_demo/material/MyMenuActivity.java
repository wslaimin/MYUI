package com.lm.myui_demo.material;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.lm.myui_demo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContent(new OverflowMenuFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (onOptionsItemSelected(itemId)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean onOptionsItemSelected(int itemId) {
        if (itemId == R.id.overflow_menu) {
            replaceContent(new OverflowMenuFragment());
            return true;
        } else if (itemId == R.id.context_menu) {
            replaceContent(new ContextMenuFragment());
            return true;
        } else if (itemId == R.id.popup_menu) {
            replaceContent(new PopupMenuFragment());
            return true;
        } else if (itemId == R.id.popup_list_menu) {
            replaceContent(new ListPopupMenuFragment());
            return true;
        } else if (itemId == R.id.exposed_dropdown_menu) {
            replaceContent(new ExposedDropdownMenuFragment());
            return true;
        }
        return false;
    }

    private void replaceContent(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(android.R.id.content, fragment).commit();
    }

    public static class OverflowMenuFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.frg_overflow_menu, container, false);
        }
    }

    public static class ContextMenuFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frg_context_menu, container, false);
            TextView textView = view.findViewById(R.id.text);
            registerForContextMenu(textView);
            return view;
        }

        @Override
        public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        }

        @Override
        public boolean onContextItemSelected(@NonNull MenuItem item) {
            return getActivity().onOptionsItemSelected(item);
        }
    }

    public static class PopupMenuFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frg_popup_menu, container, false);
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(v);
                }
            });
            initButton(button);
            return view;
        }

        protected void initButton(Button button) {
            button.setText("show popup menu");
        }

        protected void showMenu(View anchor) {
            PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return getActivity().onOptionsItemSelected(item);
                }
            });
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        }
    }

    public static class ListPopupMenuFragment extends PopupMenuFragment {
        @Override
        protected void initButton(Button button) {
            button.setText("show popup list menu");
        }

        @Override
        protected void showMenu(View anchor) {
            final ListPopupWindow listPopupWindow = new ListPopupWindow(getContext(), null, R.attr.listPopupWindowStyle);
            listPopupWindow.setAnchorView(anchor);
            List<Map<String, Object>> data = new ArrayList<>();
            String[] titles = new String[]{"overflow menu", "context menu", "popup menu", "popup list menu", "exposed dropdown menu"};
            int[] ids = new int[]{R.id.overflow_menu, R.id.context_menu, R.id.popup_menu, R.id.popup_list_menu, R.id.exposed_dropdown_menu};
            for (int i = 0; i < titles.length; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("icon", R.drawable.icon_menu);
                map.put("title", titles[i]);
                map.put("id", ids[i]);
                data.add(map);
            }

            listPopupWindow.setAdapter(new ListPopupMenuAdapter(getContext(), data, R.layout.item_list_menu, new String[]{"icon", "title"}, new int[]{R.id.icon, R.id.title}));
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((MyMenuActivity) getActivity()).onOptionsItemSelected((int) id);
                    listPopupWindow.dismiss();
                }
            });
            listPopupWindow.show();
        }

        static class ListPopupMenuAdapter extends SimpleAdapter {
            private List<? extends Map<String, ?>> listData;

            public ListPopupMenuAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
                super(context, data, resource, from, to);
                listData = data;
            }

            @Override
            public long getItemId(int position) {
                return (Integer) listData.get(position).get("id");
            }
        }
    }

    public static class ExposedDropdownMenuFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frg_exposed_dropdown_menu, container, false);
            AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.auto_complete_text);
            String[] items = new String[6];
            for (int i = 0; i < items.length; i++) {
                items[i] = "Option" + i;
            }
            autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_menu, items));
            return view;
        }
    }
}
