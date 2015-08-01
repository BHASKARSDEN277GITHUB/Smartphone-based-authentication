package finalyear.major.authenticationapp;

import java.io.File;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			
			if (profile.saved==1) {
				// display message
				//Toast.makeText(getActivity(), "file exists",
					//	Toast.LENGTH_LONG).show();
				return new profile();
				
			}else {
			// Games fragment activity
			// if not registered show form else show profile
			return new form();
			
			}
		case 1:
			// check if registered
			// if yes then launch authenticate
			 return new authenticate();
			// else launch not registered
			//return new notregistered();
		case 2:
			// Movies fragment activity
			// same as authenticate

			//return new notregistered();
			return new changepassword();
		

		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
