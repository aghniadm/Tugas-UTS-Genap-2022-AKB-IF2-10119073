package com.example.uts_akb_10119073_if2.main.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uts_akb_10119073_if2.R;
import com.example.uts_akb_10119073_if2.main.profile.ProfileActivity;
import com.example.uts_akb_10119073_if2.model.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity {
    String[] daftar, id_kategori;
    ListView listView;
    private Cursor cursor;
    DBHelper dbHelper;
    public static CategoryActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.CatatanHarian);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ProfileId:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CatatanHarian:
                        startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.About:
                        final Dialog dialog = new Dialog(CategoryActivity.this);

                        //memasang judul pada Custom Dialog
                        dialog.setTitle("About");

                        //memasang desain layout untuk Custom Dialog
                        dialog.setContentView(R.layout.custom_design_dialog);

                        //memasang listener/aksi saat tombol OK diklik
                        Button DialogButton = dialog.findViewById(R.id.ok);
                        DialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        return true;
                }

                return false;
            }

        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SaveCategoryActivity.class);
                startActivity(intent);
            }

        });

        ma = this;
        dbHelper = new DBHelper(this);
        RefreshList();
    }

    public void RefreshList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM kategori", null);
        id_kategori = new String[cursor.getCount()];
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            id_kategori[cc] = cursor.getString(0).toString();
            daftar[cc] = cursor.getString(1).toString();
        }

        listView = findViewById(R.id.rv_notes);

        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final int selection = Integer.parseInt(id_kategori[arg2]);
                Intent intent = new Intent(CategoryActivity.this, ListNoteActivity.class);
                intent.putExtra("kategori", selection);
                startActivity(intent);
            }});


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id2) {
                final int which_item = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                builder.setTitle("Delete Kategori");
                builder.setMessage("Apakah yakin menghapus kategori ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("delete from kategori where id_kategori ="+ id_kategori[position]);
                        RefreshList();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
                return true;
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.single_view_item, parent, false);

            TextView kategoriTitle = convertView.findViewById(R.id.kategori_title);
            //TextView kategoriDesc = convertView.findViewById(R.id.kategori_desc);
            kategoriTitle.setText(daftar[position]);

            return convertView;
        }
    }
}