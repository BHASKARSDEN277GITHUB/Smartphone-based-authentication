package finalyear.major.authenticationapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class form extends Fragment {

	// variable declaration
	private String uidS = "";
	private String passS = "";
	private String cPassS = "";
	private String emailS = "";
	private String hPass = "";
	private String hUID = "";
	private String encHPass = "";
	private String encHUID = "";
	private String result = "hello";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.form, container, false);
		final EditText uid = (EditText) rootView.findViewById(R.id.editFormUID);
		final EditText email = (EditText) rootView
				.findViewById(R.id.editFormEmail);
		final EditText pass = (EditText) rootView
				.findViewById(R.id.editFormPassword);
		final EditText cPass = (EditText) rootView
				.findViewById(R.id.editFormCPassword);
		final TextView tuid=(TextView)rootView.findViewById(R.id.textFormUID);
		final TextView temail=(TextView)rootView.findViewById(R.id.textFormEmail);
		final TextView tpass=(TextView)rootView.findViewById(R.id.textFormPassword);
		final TextView tcpass=(TextView)rootView.findViewById(R.id.textFormCPassword);
		final TextView Details=(TextView)rootView.findViewById(R.id.formTextDetails);

		final Button cont = (Button) rootView.findViewById(R.id.buttonFormContinue);

		// continue button action here
		cont.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// get all details here
				uidS = uid.getText().toString();
				emailS = email.getText().toString();
				passS = pass.getText().toString();
				cPassS = cPass.getText().toString();

				// check validity of details and process
				if (uidS.length() > 0 && passS.length() >= 8
						&& passS.equals(cPassS) && emailS.matches(".+@.+")) {
					
					

					// calculating hash
					hashAlgo object = new hashAlgo();
					try {
						hPass = object.execute(passS).substring(0, 8);
						hUID = object.execute(uidS).substring(0, 8);
					} catch (Exception e) {
						e.printStackTrace();
					}

					// encrypt hPass and hUID
					try {
						encHPass = encryptor.encrypt(hPass);
						encHUID = encryptor.encrypt(hUID);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// send encrypted hash of password and hash of uid to the
					// server

					String url = "http://192.168.43.164:8080/authServerNew/register";
					
					new LongOperation().execute(url);
					uid.setVisibility(View.INVISIBLE);
					email.setVisibility(View.INVISIBLE);
					pass.setVisibility(View.INVISIBLE);
					cPass.setVisibility(View.INVISIBLE);
					
					
					
					tuid.setVisibility(View.INVISIBLE);
					temail.setVisibility(View.INVISIBLE);
					tpass.setVisibility(View.INVISIBLE);
					tcpass.setVisibility(View.INVISIBLE);
					
					cont.setVisibility(View.INVISIBLE);
					
					String Message ="hello "+uidS+"\nYou are successully Registered with auth Server\n";
					Details.setText(Message);
					
					profile.saved=1;
					// if entries to file exist redirect to new fragment (not
					// checking right now)
					File file = getActivity().getFileStreamPath("Token.txt");
					if (file.exists()) {
						// display message
						//Toast.makeText(getActivity(), "file exists",
							//	Toast.LENGTH_LONG).show();
						
					}
					
					
					
					

				} else {
					/*profile next=new profile();
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					//fragmentTransaction.replace(R.id.rootLayout,next);
					//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
					//fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();*/
					
					Toast.makeText(getActivity(), "Invalid Details",
							Toast.LENGTH_LONG).show();
					uid.setText("");
					email.setText("");
					pass.setText("");
					cPass.setText("");
					
				}

			}
		}); 
		
		
		return rootView;
	}
	
	
	

	

	// create private classes that extend asynchtask to do the background
	// processing ...

	private class LongOperation extends AsyncTask<String, Void, Void> {

		private ProgressDialog dialog = new ProgressDialog(getActivity());// creating
																			// a
																			// progress
																			// dialog
																			// ..
		private String error = null;
		private final HttpClient client = new DefaultHttpClient();

		protected void onPreExecute() {
			dialog.setMessage("Registering at authServer ..");
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... urls) {

			BufferedReader reader = null;

			// Creating HTTP client

			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost httpPost = new HttpPost(urls[0]);

			// Building post parameters, key and value pair
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("encHPass", encHPass));
			nameValuePair.add(new BasicNameValuePair("encHUID", encHUID));
			nameValuePair.add(new BasicNameValuePair("email", emailS));

			// Url Encoding the POST parameters
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				e.printStackTrace();
			}

			// Making HTTP Request
			try {
				HttpResponse response = httpClient.execute(httpPost);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) // SC_OK
																					// =200
				{
					// writing response to log
					reader = new BufferedReader(new InputStreamReader(response
							.getEntity().getContent()));
					String line = reader.readLine();
					String content = "";
					while (line != null) {

						content = content + line;
						line = reader.readLine();
					}
					if (content != null)
						result = content;
					else
						result = "null";
					reader.close();
				} else {
					String content = -1 + "";

				}

				reader.close();
			} catch (ClientProtocolException e) {
				// writing exception to log
				e.printStackTrace();

			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(Void unused) {

			dialog.dismiss();
			// set exist user flag ..
			//Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

			// extracting nonce and y from the result and saving to file reg.txt
			String nonce = result.split(" ")[1];
			String y = result.split(" ")[0];

			String token = "y " + y + "\n" + "nonce "+nonce; // to be stored

			BufferedWriter writer = null;
			BufferedReader reader = null;

			try {
				FileOutputStream outputstream = getActivity().openFileOutput(
						"Token.txt", Context.MODE_PRIVATE);
				writer = new BufferedWriter(
						new OutputStreamWriter(outputstream));
				writer.write(token);

				writer.close();
				// Toast.makeText(getApplicationContext(),nonce +
				// "has been written successfully to file ..",
				// Toast.LENGTH_LONG).show();

				// reading from Token.txt for check ..
				FileInputStream inputstream = getActivity().openFileInput(
						"Token.txt");
				reader = new BufferedReader(new InputStreamReader(inputstream));
				String line = "";
				String values = "";
				line = reader.readLine();
				while (line != null) {
					values = values + line + "\n";
					line = reader.readLine();
				}

				reader.close();
				
				
			//	Toast.makeText(getActivity(), values, Toast.LENGTH_SHORT)
				//		.show();

			} catch (Exception e) {
			}

		}
	}
}
