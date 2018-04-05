package com.abdul.parkhapa;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity  implements DuoMenuView.OnMenuClickListener{
    private static final String TAG = "MainActivity" ;
    private static final int REQUEST_INVITE = 0;
    private  MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mViewHolder = new ViewHolder();
        handleToolbar();
        handleMenu();
        handleDrawer();
       goToFragment(new ExploreFragment(), false);

        View myView =  mViewHolder.mDuoMenuView.getHeaderView();
        TextView txtmyname =(TextView)myView.findViewById(R.id.duo_view_header_text_title);
        TextView txtSub = (TextView)myView.findViewById(R.id.duo_view_header_text_sub_title);

        txtmyname.setText(" Welcome ! ");
        txtSub.setText("Goulda  ");

//        BaseFragment.loadFragmentTransaction(this, R.id.container,
//                FragmentTag.GOOGLE_MAP.getAbbreviation(), FragmentAnimationType.NONE);

        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
    }


    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);
        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }



    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }
    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.add(R.id.container, fragment).commit();
    }


    @Override
    public void onFooterClicked() {
        FirebaseAuth.getInstance().signOut();
        startActivity( new Intent( getApplicationContext() , LoginActivity.class));

    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));
        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);
        // Navigate to the right fragment
        switch (position) {

            case 0 :
                goToFragment( new ExploreFragment(), false);
                break;
            case 1 :
                startActivity( new Intent( getApplicationContext(),
                        FeedbackActivity.class));
                break;
            case 2 :
                startActivity( new Intent( MainActivity.this,
                        AboutActivity.class));
                break;
            case 3 :
                onInviteClicked();
                break;
            default:
                goToFragment(  new ExploreFragment(), false);
                break;
        }
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void showMessage(String msg) {
      Toast.makeText( getApplicationContext() , msg ,
              Toast.LENGTH_LONG).show();
    }



    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed));
                // [END_EXCLUDE]
            }
        }
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
}
