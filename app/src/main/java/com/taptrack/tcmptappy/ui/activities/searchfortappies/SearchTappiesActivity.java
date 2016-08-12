/*
 * Copyright (c) 2016. Papyrus Electronics, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taptrack.tcmptappy.ui.activities.searchfortappies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.taptrack.tappyble.R;
import com.taptrack.tcmptappy.ui.base.BaseActivity;
import com.taptrack.tcmptappy.ui.modules.tappyblesearcher.vistas.list.TappyBleSearchListView;
import com.taptrack.tcmptappy.utils.MarshmallowCompatBlePermDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTappiesActivity extends BaseActivity {
    private static final String TAG_BOLLARD = "BOLLARD";

    private SearchTappiesActivityBollard bollard;

    private MarshmallowCompatBlePermDelegate blePermDelegate;

    @BindView(R.id.tappyList)
    TappyBleSearchListView view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tappies);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        blePermDelegate = new MarshmallowCompatBlePermDelegate(this);
        blePermDelegate.onCreate();

        FragmentManager fm = getSupportFragmentManager();
        bollard = (SearchTappiesActivityBollard) fm.findFragmentByTag(TAG_BOLLARD);
        if(bollard == null) {
            bollard = new SearchTappiesActivityBollard();
            fm.beginTransaction()
                    .add(bollard,TAG_BOLLARD)
                    .commit();
        }

        bollard.registerTappyListVista(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        blePermDelegate.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        blePermDelegate.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        blePermDelegate.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
