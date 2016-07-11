package barreto.alessandro.curriculo.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import barreto.alessandro.curriculo.R;
import barreto.alessandro.curriculo.adapter.ViewPagerAdapter;
import barreto.alessandro.curriculo.view.fragment.FragHabilidades;
import barreto.alessandro.curriculo.view.fragment.FragProjetos;
import barreto.alessandro.curriculo.view.fragment.FragContato;
import barreto.alessandro.curriculo.view.fragment.FragSobre;

public class MainActivity extends AppCompatActivity{

    private ViewPagerAdapter mViewPagerAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Log.d("TAG","teste "+ FirebaseInstanceId.getInstance().getToken());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("nova_mensagem")){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.nova_mensagem),Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("nova_mensagem"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void setupViewPager(ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new FragSobre(), getResources().getString(R.string.sobre));
        mViewPagerAdapter.addFragment(new FragHabilidades(), getResources().getString(R.string.habilidades));
        mViewPagerAdapter.addFragment(new FragProjetos(), getResources().getString(R.string.projetos));
        mViewPagerAdapter.addFragment(new FragContato(), getResources().getString(R.string.contato));
        viewPager.setAdapter(mViewPagerAdapter);
    }
}
