package com.apress.echo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Echo server.
 * 
 * @author Onur Cinar
 */
public class EchoServerActivity extends Activity implements
		View.OnClickListener {
	/** Port number. */
	private EditText portEdit;

	/** Server button. */
	private Button startButton;

	/** Log scroll. */
	private ScrollView logScroll;

	/** Log view. */
	private TextView logView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_echo_server);

		portEdit = (EditText) findViewById(R.id.port_edit);
		startButton = (Button) findViewById(R.id.start_button);
		logScroll = (ScrollView) findViewById(R.id.log_scroll);
		logView = (TextView) findViewById(R.id.log_view);

		startButton.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view == startButton) {
			Integer port = getPort();
			if (port != null) {
				ServerTask serverTask = new ServerTask();
				serverTask.execute(port);
			}
		}
	}

	/**
	 * Gets the port number as an integer.
	 * 
	 * @return port number or null.
	 */
	private Integer getPort() {
		Integer port;

		try {
			port = Integer.valueOf(portEdit.getText().toString());
		} catch (NumberFormatException e) {
			port = null;
		}

		return port;
	}

	/**
	 * Logs the given message.
	 * 
	 * @param message
	 *            log message.
	 */
	private void logMessage(String message) {
		logView.append(message);
		logView.append("\n");
		logScroll.fullScroll(View.FOCUS_DOWN);
	}

	/**
	 * Starts the TCP server on the given port.
	 * 
	 * @param port
	 *            port number.
	 * @throws Exception
	 */
	private native void startTcpServer(int port) throws Exception;

	/**
	 * Server task.
	 */
	private class ServerTask extends AsyncTask<Integer, String, Void> {
		protected void onPreExecute() {
			startButton.setEnabled(false);
			logView.setText("");
		}

		protected Void doInBackground(Integer... ports) {
			int port = ports[0].intValue();

			try {
				startTcpServer(port);
			} catch (Exception e) {
				publishProgress(e.getMessage());
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			startButton.setEnabled(true);
		}

		protected void onProgressUpdate(String... messages) {
			for (String message : messages) {
				logMessage(message);
			}
		}
	}
	
	static {
//		System.loadLibrary("Echo");
	}
}
