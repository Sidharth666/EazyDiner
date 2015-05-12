package com.easydiner.activities;

import com.classes.EasydinerClass;
import com.easydiner.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegistranAllActivity extends Activity implements 
ConnectionCallbacks, OnConnectionFailedListener {
	
	private RelativeLayout rlSignUp, rlSignIn;
	private ImageView ivFbLogin, ivTwitterLogin, ivGplusLogin;
	private SimpleFacebook simpleFB;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private String personName;
	private boolean mIntentInProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_all_layout);
		
		Tracker t = ((EasydinerClass) getApplication()).getTracker(EasydinerClass.TrackerName.APP_TRACKER);
		t.setScreenName("RegistranAllActivity");
	    t.send(new HitBuilders.ScreenViewBuilder().build());
	    
		initialization();
		onClick();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		simpleFB = SimpleFacebook.getInstance(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
		      mGoogleApiClient.disconnect();
		    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		simpleFB.onActivityResult(this, requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
		    mIntentInProgress = false;

		    if (!mGoogleApiClient.isConnecting()) {
		      mGoogleApiClient.connect();
		    }
		  }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initialization()
	{
		rlSignUp = (RelativeLayout)findViewById(R.id.rlSignUp);
		rlSignIn = (RelativeLayout)findViewById(R.id.rlSignIn);
		ivFbLogin = (ImageView)findViewById(R.id.ivFbLogin);
		//ivTwitterLogin = (ImageView)findViewById(R.id.ivTwitterLogin);
		ivGplusLogin = (ImageView)findViewById(R.id.ivGplusLogin);
		mGoogleApiClient = new GoogleApiClient.Builder(RegistranAllActivity.this)
		.addConnectionCallbacks(RegistranAllActivity.this)
		.addOnConnectionFailedListener(RegistranAllActivity.this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
	}
	
	
	private void onClick()
	{
		rlSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RegistranAllActivity.this, SignupActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		rlSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RegistranAllActivity.this, SigninActivity.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		ivFbLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simpleFB.login(onLoginListener);
			}
		});
		
		ivTwitterLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ivGplusLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGoogleApiClient.connect();
			}
		});
	}
	
	OnLoginListener onLoginListener = new OnLoginListener() {
		
		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onThinking() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onNotAcceptingPermissions(Type type) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLogin() {
			// TODO Auto-generated method stub
			simpleFB.getProfile(new OnProfileListener() {
				public void onFail(String reason) {
					
				};
				
				public void onException(Throwable throwable) {
					
				};
				
				public void onThinking() {
					
				};
				
				public void onComplete(com.sromku.simple.fb.entities.Profile response) {
					Toast.makeText(RegistranAllActivity.this, response.getName(),
							Toast.LENGTH_LONG).show();
				};
			});
		}
	};
	
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
		    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		    personName = currentPerson.getDisplayName();
		    String personGooglePlusProfile = currentPerson.getUrl();
		    Toast.makeText(this, personName, Toast.LENGTH_LONG).show();
		  }
	}

	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.e("suspended", "suspended");
	}
	
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (!mIntentInProgress && result.hasResolution()) {
		    try {
		    	
		      mIntentInProgress = true;
		      startIntentSenderForResult(result.getResolution().getIntentSender(),
		          RC_SIGN_IN, null, 0, 0, 0);
		    } catch (SendIntentException e) {
		      // The intent was canceled before it was sent.  Return to the default
		      // state and attempt to connect to get an updated ConnectionResult.
		      mIntentInProgress = false;
		      mGoogleApiClient.connect();
		    }
		  }
	}
	
}
