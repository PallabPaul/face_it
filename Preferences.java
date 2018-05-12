package org.faceit.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Preferences extends Activity implements OnItemSelectedListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0196R.layout.activity_preferences);
        Spinner faceShape = (Spinner) findViewById(C0196R.id.spinner1);
        Spinner hairTexture = (Spinner) findViewById(C0196R.id.spinner6);
        Spinner hairThickness = (Spinner) findViewById(C0196R.id.spinner4);
        Spinner facialHair = (Spinner) findViewById(C0196R.id.spinner5);
        Spinner acneLevel = (Spinner) findViewById(C0196R.id.spinner2);
        Spinner lifeStyle = (Spinner) findViewById(C0196R.id.spinner3);
        ArrayAdapter<String> myAdapter = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.faceshape));
        myAdapter.setDropDownViewResource(17367049);
        faceShape.setAdapter(myAdapter);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.hairtexture));
        myAdapter1.setDropDownViewResource(17367049);
        hairTexture.setAdapter(myAdapter1);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.hairthickness));
        myAdapter2.setDropDownViewResource(17367049);
        hairThickness.setAdapter(myAdapter2);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.facialhair));
        myAdapter3.setDropDownViewResource(17367049);
        facialHair.setAdapter(myAdapter3);
        ArrayAdapter<String> myAdapter4 = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.acnelevel));
        myAdapter4.setDropDownViewResource(17367049);
        acneLevel.setAdapter(myAdapter4);
        ArrayAdapter<String> myAdapter5 = new ArrayAdapter(this, 17367043, getResources().getStringArray(C0196R.array.lifestyle));
        myAdapter5.setDropDownViewResource(17367049);
        lifeStyle.setAdapter(myAdapter5);
        faceShape.setOnItemSelectedListener(this);
        hairTexture.setOnItemSelectedListener(this);
        hairThickness.setOnItemSelectedListener(this);
        facialHair.setOnItemSelectedListener(this);
        acneLevel.setOnItemSelectedListener(this);
        lifeStyle.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case C0196R.id.spinner1:
                Toast.makeText(this, "Face Shape Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            case C0196R.id.spinner6:
                Toast.makeText(this, "Hair Texture Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            case C0196R.id.spinner2:
                Toast.makeText(this, "Acne Level Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            case C0196R.id.spinner3:
                Toast.makeText(this, "Lifestyle Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            case C0196R.id.spinner4:
                Toast.makeText(this, "Hair Thickness Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            case C0196R.id.spinner5:
                Toast.makeText(this, "Facial Hair Selected: " + parent.getSelectedItem().toString(), 0).show();
                return;
            default:
                return;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void goHairstyles(View view) {
        Spinner hairTexture = (Spinner) findViewById(C0196R.id.spinner6);
        Spinner hairThickness = (Spinner) findViewById(C0196R.id.spinner4);
        Spinner facialHair = (Spinner) findViewById(C0196R.id.spinner5);
        Spinner acneLevel = (Spinner) findViewById(C0196R.id.spinner2);
        Spinner lifeStyle = (Spinner) findViewById(C0196R.id.spinner3);
        String faceshape = ((Spinner) findViewById(C0196R.id.spinner1)).getSelectedItem().toString();
        String hairtexture = hairTexture.getSelectedItem().toString();
        String hairthickness = hairThickness.getSelectedItem().toString();
        String facialhair = facialHair.getSelectedItem().toString();
        String acnelevel = acneLevel.getSelectedItem().toString();
        String lifestyle = lifeStyle.getSelectedItem().toString();
        Intent preferences = new Intent(this, Hairstyles.class);
        preferences.putExtra("Face_Shape", faceshape);
        preferences.putExtra("Hair_Texture", hairtexture);
        preferences.putExtra("Hair_Thickness", hairthickness);
        preferences.putExtra("Facial_Hair", facialhair);
        preferences.putExtra("Acne_Level", acnelevel);
        preferences.putExtra("Lifestyle", lifestyle);
        startActivity(preferences);
    }
}
