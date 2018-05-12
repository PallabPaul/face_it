package org.faceit.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Hairstyles extends Activity {
    public static int[] hairInt = new int[7];
    public static String[] hairString = new String[7];
    String acneLevel;
    String[] acnelevel = new String[0];
    CustomSwipeAdapter adapter;
    String[] business = new String[0];
    String[] coiled = new String[0];
    String[] diamond = new String[0];
    String[] excessive = new String[0];
    String faceShape;
    String[] faceshape = new String[0];
    String facialHair;
    String[] facialhair = new String[0];
    String hairTexture;
    String hairThickness;
    String[] hairtexture = new String[0];
    String[] hairthickness = new String[0];
    String lifeStyle;
    String[] lifestyle = new String[0];
    String[] moderate = new String[0];
    String[] no = new String[0];
    String[] none = new String[0];
    String[] oblong = new String[0];
    String[] oval = new String[0];
    String[] pnta = new String[0];
    String[] round = new String[0];
    String[] square = new String[0];
    String[] straight = new String[0];
    String[] student = new String[0];
    String[] thick = new String[0];
    String[] thin = new String[0];
    String[] triangular = new String[0];
    ViewPager viewPager;
    String[] wavy = new String[0];
    String[] yes = new String[0];

    public class CustomSwipeAdapter extends PagerAdapter {
        private Context ctx;
        public int[] image_resources = Hairstyles.hairInt;
        private LayoutInflater layoutinflater;

        public CustomSwipeAdapter(Context ctx) {
            this.ctx = ctx;
        }

        public int getCount() {
            return this.image_resources.length;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        public Object instantiateItem(ViewGroup container, int position) {
            this.layoutinflater = (LayoutInflater) this.ctx.getSystemService("layout_inflater");
            View item_view = this.layoutinflater.inflate(C0196R.layout.swipe_layout, container, false);
            TextView textView = (TextView) item_view.findViewById(C0196R.id.image_count);
            ((ImageView) item_view.findViewById(C0196R.id.image_view)).setImageResource(this.image_resources[position]);
            textView.setText(Hairstyles.hairString[position]);
            container.addView(item_view);
            return item_view;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        int[] hairImages;
        int i;
        String[] hairNames;
        super.onCreate(savedInstanceState);
        setContentView(C0196R.layout.activity_hairstyles);
        this.viewPager = (ViewPager) findViewById(C0196R.id.view_pager);
        this.adapter = new CustomSwipeAdapter(this);
        this.viewPager.setAdapter(this.adapter);
        ((TextView) findViewById(C0196R.id.tips)).setText("Tips:");
        TextView boxOne = (TextView) findViewById(C0196R.id.box1);
        TextView boxTwo = (TextView) findViewById(C0196R.id.box2);
        TextView boxThree = (TextView) findViewById(C0196R.id.box3);
        this.faceShape = getIntent().getExtras().getString("Face_Shape");
        this.hairTexture = getIntent().getExtras().getString("Hair_Texture");
        this.hairThickness = getIntent().getExtras().getString("Hair_Thickness");
        this.facialHair = getIntent().getExtras().getString("Facial_Hair");
        this.acneLevel = getIntent().getExtras().getString("Acne_Level");
        this.lifeStyle = getIntent().getExtras().getString("Lifestyle");
        if (this.faceShape.equals("Square")) {
            this.faceshape = this.square;
        }
        if (this.faceShape.equals("Round")) {
            this.faceshape = this.round;
        }
        if (this.faceShape.equals("Oval")) {
            this.faceshape = this.oval;
        }
        if (this.faceShape.equals("Oblong")) {
            this.faceshape = this.oblong;
        }
        if (this.faceShape.equals("Diamond")) {
            this.faceshape = this.diamond;
        }
        if (this.faceShape.equals("Triangular")) {
            this.faceshape = this.triangular;
        }
        if (this.hairTexture.equals("Straight")) {
            this.hairtexture = this.straight;
        }
        if (this.hairTexture.equals("Wavy")) {
            this.hairtexture = this.wavy;
        }
        if (this.hairTexture.equals("Coiled")) {
            this.hairtexture = this.coiled;
        }
        if (this.hairThickness.equals("Thick")) {
            this.hairthickness = this.thick;
        }
        if (this.hairThickness.equals("Thin")) {
            this.hairthickness = this.thin;
        }
        if (this.facialHair.equals("Yes")) {
            this.facialhair = this.yes;
        }
        if (this.facialHair.equals("No")) {
            this.facialhair = this.no;
        }
        if (this.acneLevel.equals("None")) {
            this.acnelevel = this.none;
        }
        if (this.acneLevel.equals("Moderate")) {
            this.acnelevel = this.moderate;
        }
        if (this.acneLevel.equals("Excessive")) {
            this.acnelevel = this.excessive;
        }
        if (this.acneLevel.equals("Prefer Not To Answer")) {
            this.acnelevel = this.pnta;
        }
        if (this.lifeStyle.equals("Business")) {
            this.lifestyle = this.business;
        }
        if (this.lifeStyle.equals("Student")) {
            this.lifestyle = this.student;
        }
        if (this.faceshape == this.square && this.hairtexture == this.straight) {
            boxOne.setText("1. Try having short hair on the back and sides with some length on top.");
        }
        if (this.faceshape == this.square && this.hairtexture == this.wavy) {
            boxOne.setText("1. Try having clean lines around the hairline and a few inches on top for styling.");
        }
        if (this.faceshape == this.square && this.hairtexture == this.coiled) {
            boxOne.setText("1. Try going for a tight fade on the back and sides.");
        }
        if (this.faceshape == this.round && this.hairtexture == this.straight) {
            boxOne.setText("1. Try having short hair on the back and sides with around 3 to 5 inches of length in the front for volume.");
        }
        if (this.faceshape == this.round && this.hairtexture == this.wavy) {
            boxOne.setText("1. Use the natural waviness of your hair to gain volume and try to keep the length of your hair under 6 inches.");
        }
        if (this.faceshape == this.round && this.hairtexture == this.coiled) {
            boxOne.setText("1. Go for a clean fade and a high top.");
        }
        if (this.faceshape == this.oval && this.hairtexture == this.straight) {
            boxOne.setText("1. Try leaving some length on top and sweeping your hair back or go for a shorter crop.");
        }
        if (this.faceshape == this.oval && this.hairtexture == this.wavy) {
            boxOne.setText("1. Let your hair naturally grow out and work with the texture without trimming the sides down too severely.");
        }
        if (this.faceshape == this.oval && this.hairtexture == this.coiled) {
            boxOne.setText("1. Try going for a skin fade with sharp details around the hairline and a short crop on top.");
        }
        if (this.faceshape == this.oblong && this.hairtexture == this.straight) {
            boxOne.setText("1. Try growing out your hair about an inch on top and sides and add texture.");
        }
        if (this.faceshape == this.oblong && this.hairtexture == this.wavy) {
            boxOne.setText("1. Try growing your hair out the same length all over and let it fall naturally.");
        }
        if (this.faceshape == this.oblong && this.hairtexture == this.coiled) {
            boxOne.setText("1. Try going for a short style about 3cm in length.");
        }
        if (this.faceshape == this.diamond && this.hairtexture == this.straight) {
            boxOne.setText("1. Try having longer hair on top with added texture for volume.");
        }
        if (this.faceshape == this.diamond && this.hairtexture == this.wavy) {
            boxOne.setText("1. Try having longer hair on top with added texture for volume. Grow out your sides and back about an inch to add softness to your hair.");
        }
        if (this.faceshape == this.diamond && this.hairtexture == this.coiled) {
            boxOne.setText("1. Try letting your hair grow out all around to get some height and width. You can try keeping it shorter on the sides and back if you like.");
        }
        if (this.faceshape == this.triangular && this.hairtexture == this.straight) {
            boxOne.setText("1. Try growing your hair out 3 inches all around and add a lot of texture to every side.");
        }
        if (this.faceshape == this.triangular && this.hairtexture == this.wavy) {
            boxOne.setText("1. Try letting your waves grow out to keep some softness around the back and sides. Add texture throughout your hair as well.");
        }
        if (this.faceshape == this.triangular && this.hairtexture == this.coiled) {
            boxOne.setText("1. Try going for clean short sides with a little length on top.");
        }
        if (this.hairthickness == this.thick && this.lifestyle == this.business) {
            boxTwo.setText("2. Go for a shorter hair length.");
        }
        if (!(this.hairthickness != this.thick || this.acnelevel == this.none || this.acnelevel == this.moderate || this.lifestyle == this.business)) {
            boxTwo.setText("2. Go for a longer hair length.");
        }
        if (!(this.hairthickness != this.thick || this.acnelevel == this.excessive || this.acnelevel == this.pnta || this.lifestyle == this.business)) {
            boxTwo.setText("2. Try both a long hair length and a short hair length and choose whichever length you like better.");
        }
        if (this.hairthickness == this.thin) {
            boxTwo.setText("2. Go for a longer hair length.");
        }
        if (this.facialhair == this.no && this.lifestyle != this.business) {
            boxThree.setText("3. Try growing out facial hair.");
        }
        if (this.facialhair == this.no && this.lifestyle == this.business) {
            boxThree.setText("3. Go for a clean shave after your haircut so the barber can properly blend in your sideburns.");
        }
        if (this.facialhair == this.yes) {
            boxThree.setText("3. Let us know if you would like facial hair recommendations.");
        }
        if (this.faceshape == this.square && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.square_business_side_part, C0196R.drawable.square_business_crew_cut, C0196R.drawable.square_business_buzz_cut, C0196R.drawable.square_business_close_cut, C0196R.drawable.square_business_pompadour, C0196R.drawable.square_business_undercut, C0196R.drawable.square_business_slicked_back_undercut};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Side Part", "Crew Cut", "Buzz Cut", "Close Cut", "Pompadour", "Undercut", "Slicked Back Undercut"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.square && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.square_student_brush_up, C0196R.drawable.square_student_buzz_cut, C0196R.drawable.square_student_close_cut, C0196R.drawable.square_student_ivy_league, C0196R.drawable.square_student_pompadour, C0196R.drawable.square_student_side_part, C0196R.drawable.square_student_side_swept_undercut};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Brush Up", "Buzz Cut", "Close Cut", "Ivy League", "Pompadour", "Side Part", "Side Swept Undercut"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.round && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.round_business_side_part, C0196R.drawable.round_business_comb_over, C0196R.drawable.round_business_pompadour, C0196R.drawable.round_business_undercut, C0196R.drawable.round_business_vertical_haircut};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Side Part", "Comb Over", "Pompadour", "Undercut", "Vertical Cut"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.round && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.round_student_asymmetrical_fringe, C0196R.drawable.round_student_comb_over, C0196R.drawable.round_student_side_and_up_spiky_haircut, C0196R.drawable.round_student_side_part, C0196R.drawable.round_student_side_swept_bangs, C0196R.drawable.round_student_undercut, C0196R.drawable.round_student_faux_hawk};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Asymmetrical Fringe", "Comb Over", "Spiky Side and Up", "Side Part", "Side Swept Bangs", "Undercut", "Faux Hawk"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.oval && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.oval_business_brushback, C0196R.drawable.oval_business_pompadour, C0196R.drawable.oval_business_undercut_with_combover};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Brushback", "Pompadour", "Undercut with Comb Over"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.oval && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.oval_student_brushback, C0196R.drawable.oval_student_long_and_wavy, C0196R.drawable.oval_student_pompadour, C0196R.drawable.oval_student_quiff, C0196R.drawable.oval_student_short_straight, C0196R.drawable.oval_student_long_and_wavy, C0196R.drawable.oval_student_undercut_with_combover};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Brushback", "Long and Wavy", "Pompadour", "Quiff", "Short and Straight", "Long and Wavy", "Undercut with Combover"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.oblong && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.oblong_business_brush_up, C0196R.drawable.oblong_business_buzz_cut, C0196R.drawable.oblong_business_side_part, C0196R.drawable.oblong_business_side_swept_crew_cut, C0196R.drawable.oblong_business_spiky};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Brush Up", "Buzz Cut", "Side Part", "Side Swept Crew Cut", "Spiky"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.oblong && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.oblong_student_brush_up, C0196R.drawable.oblong_student_buzz_cut, C0196R.drawable.oblong_student_side_part, C0196R.drawable.oblong_student_side_swept_crew_cut};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Brush Up", "Buzz Cut", "Side Part", "Side Swept Crew Cut"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.diamond && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.diamond_business_brushback_side_part, C0196R.drawable.diamond_business_slicked_back, C0196R.drawable.diamond_business_long_slicked_back, C0196R.drawable.diamond_business_wavy_side_part};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Side Part", "Slicked Back", "Long Slicked Back", "Wavy Side Part"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.diamond && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.diamond_student_faux_hawk, C0196R.drawable.diamond_student_high_fade_fringe, C0196R.drawable.diamond_student_shag, C0196R.drawable.diamond_student_short_wavy, C0196R.drawable.diamond_student_side_swept_undercut, C0196R.drawable.diamond_student_textured_crop};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Faux Hawk", "High Fade Fringe", "Shag", "Short and Wavy", "Side Swept Undercut", "Textured Crop"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.triangular && this.lifestyle == this.business) {
            hairImages = new int[]{C0196R.drawable.triangular_business_comb_over, C0196R.drawable.triangular_business_side_swept_crew_cut};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"Comb Over", "Side Swept Crew Cut"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
        if (this.faceshape == this.triangular && this.lifestyle == this.student) {
            hairImages = new int[]{C0196R.drawable.triangular_student_high_fade_with_fringe, C0196R.drawable.triangular_student_quiff, C0196R.drawable.triangular_student_side_swept_crew_cut, C0196R.drawable.triangular_student_undercut_with_combover};
            for (i = 0; i < hairImages.length; i++) {
                hairInt[i] = hairImages[i];
            }
            hairNames = new String[]{"High Fade with Fringe", "Quiff", "Side Swept Crew Cut", "Undercut with Comb Over"};
            for (i = 0; i < hairNames.length; i++) {
                hairString[i] = hairNames[i];
            }
        }
    }
}
