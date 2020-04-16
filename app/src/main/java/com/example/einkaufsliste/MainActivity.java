package com.example.einkaufsliste;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView listView = findViewById(R.id.listView);

        final List<String> einkaufsliste = new ArrayList<String>();
        try {
            NutzerListe.update();
            final MyRequest req = new MyRequest("select * from einkaufsliste");
            for (int i=0; i<req.getEntries(); i++){
                String menge = req.getContentFromRow(i, "menge");
                String ware = req.getContentFromRow(i, "ware");

                String item = menge + "x " + ware;
                einkaufsliste.add(item);
            }

        } catch (InternetProblemException e) {
            Toast.makeText(getApplicationContext(), "aktualisierung fehlgeschlagen", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, einkaufsliste);
        listView.setAdapter(arrayAdapter);

        final TableLayout tl=findViewById(R.id.tableView);




        listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Toast.makeText(MainActivity.this, ""+i+""+i1+""+i2+""+i3+""+i4+""+i5+""+i6+""+i7, Toast.LENGTH_SHORT).show();
            }
        });



        final EditText editText2 = findViewById(R.id.editText2);
        final EditText editText3 = findViewById(R.id.editText3);
        final Button button = findViewById(R.id.button2);

        View.OnClickListener kaufen = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("abschicken?");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String produkt = editText2.getText().toString();
                        int anzahl;
                        try {
                            anzahl = Integer.parseInt(editText3.getText().toString());
                        }catch (NumberFormatException e){
                            anzahl = 1;
                        }
                        try {
                            new MyRequest("INSERT INTO einkaufsliste (ware, bestellt_nutzer_ID, bestellt_datum, menge) VALUES ('"+produkt+"', "+NutzerListe.getCurrentNutzer()+", NOW(), "+anzahl+")");
                            button.setText(":)");
                            editText2.setText("");
                            editText3.setText("");
                            updateTable(tl);
                        } catch (InternetProblemException e) {
                            Toast.makeText(getApplicationContext(), "keine Bestellung ohne Internet", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        };

        button.setOnClickListener(kaufen);

        final ImageView imageView5 = findViewById(R.id.imageView5);
        final View view = findViewById(R.id.view);
        final TextView layout_helper2 = findViewById(R.id.layout_helper2);

        final int upperTopMargin = 176;
        final int downerTopMargin = 1000;

        final MainActivity mainActivity = this;
        final Animation down = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout_helper2.getLayoutParams();
                params.topMargin = upperTopMargin + (int) ((downerTopMargin - upperTopMargin) * interpolatedTime);
                layout_helper2.setLayoutParams(params);
            }
        };
        down.setDuration(1000); // in ms

        final Animation up = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout_helper2.getLayoutParams();
                params.topMargin = downerTopMargin + (int) ((upperTopMargin - downerTopMargin) * interpolatedTime);
                layout_helper2.setLayoutParams(params);
            }
        };
        up.setDuration(1000); // in ms

        final View.OnClickListener extendImageView3;

        extendImageView3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (direction()){
                    layout_helper2.startAnimation(up);
                    ObjectAnimator.ofFloat(imageView5, "rotation", 45f, 0f).setDuration(1000).start();
                }else {
                    layout_helper2.startAnimation(down);
                    ObjectAnimator.ofFloat(imageView5, "rotation", 0f, 45f).setDuration(1000).start();
                }

            }
        };

        imageView5.setOnClickListener(extendImageView3);

    }

    public void initList(ArrayAdapter arrayAdapter) throws InternetProblemException{

    }


    public void updateTable(TableLayout tl) throws InternetProblemException{
        NutzerListe.update();

        tl.removeAllViews();
        final MyRequest req = new MyRequest("select * from einkaufsliste");

        String[] columnHeader = {"ware", "menge", "bestellt", "gekauft"};
        String[] columnAttr = {"ware", "menge", "bestellt_nutzer_id", "gekauft_nutzer_id"};

        TableRow head = new TableRow(this);
        head.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        for (String ch:columnHeader){
            TextView column = new TextView(this);
            column.setText(ch);
            head.addView(column);
        }

        tl.addView(head, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        for (int i=0; i<req.getEntries(); i++){
            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            for (final String ca:columnAttr){
                try {
                    String value = req.getContentFromRow(i, ca);
                    if (ca.contains("nutzer_id")) {
                        String datum = req.getContentFromRow(i, ca.split("nutzer_id")[0]+"datum");
                        value = "von " + NutzerListe.getNutzerFromId(Integer.parseInt(value)) + "\nam " + datum;
                    }
                    TextView column = new TextView(this);
                    column.setText(value);
                    row.addView(column);
                }catch (NumberFormatException e){
                    final Button button = new Button(this);
                    final int finalI = i;
                    View.OnClickListener kaufen = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            button.setText("gekauft?");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String id = req.getContentFromRow(finalI, "id");//falls es nicht klappt liegts an dem finalgedöns
                                    try {
                                        new MyRequest("UPDATE einkaufsliste SET gekauft_nutzer_id = '"+NutzerListe.getCurrentNutzer()+"', gekauft_datum = NOW() WHERE id = "+id);
                                    } catch (InternetProblemException e1) {
                                        Toast.makeText(getApplicationContext(), "Internet Probleme.. konnte nicht gespeichert werden", Toast.LENGTH_LONG).show();
                                    }
                                    button.setText(":)");
                                }
                            });
                        }
                    };
                    button.setText("kaufen");
                    button.setOnClickListener(kaufen);
                    row.addView(button);
                }
            }
            tl.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }


    static boolean b = true;
    static final public boolean direction(){
        b = !b;
        return b;
    }
}
