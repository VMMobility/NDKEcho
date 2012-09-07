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
 * Echo client.
 * 
 * @author Onur Cinar
 */
public class EchoClientActivity extends Activity implements
		View.OnClickListener {
	/** IP address. */
	private EditText ipEdit;

	/** Port number. */
	private EditText portEdit;

	/** Message edit. */
	private EditText messageEdit;

	/** Server button. */
	private Button startButton;

	/** Log scroll. */
	private ScrollView logScroll;

	/** Log view. */
	private TextView logView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_echo_client);

		ipEdit = (EditText) findViewById(R.id.ip_edit);
		portEdit = (EditText) findViewById(R.id.port_edit);
		messageEdit = (EditText) findViewById(R.id.message_edit);
		startButton = (Button) findViewById(R.id.start_button);
		logScroll = (ScrollView) findViewById(R.id.log_scroll);
		logView = (TextView) findViewById(R.id.log_view);

		startButton.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view == startButton) {
			String ip = ipEdit.getText().toString();
			Integer port = getPort();
			String message = messageEdit.getText().toString();

			if ((0 != ip.length()) && (port != null) && (0 != message.length())) {
				ClientTask clientTask = new ClientTask(ip, port, message);
				clientTask.execute();
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
	 * Starts the TCP client with the given server IP address and port number,
	 * and sends the given message.
	 * 
	 * @param ip
	 *            IP address.
	 * @param port
	 *            port number.
	 * @param message
	 *            message text.
	 * @throws Exception
	 */
	private native void nativeStartTcpClient(String ip, int port, String message)
			throws Exception;

	/**
	 * Client task.
	 */
	private class ClientTask extends AsyncTask<Void, String, Void> {
		/** IP address to connect. */
		private final String ip;

		/** Port number to connect. */
		private final int port;

		/** Message text to send. */
		private final String message;

		/**
		 * Constructor.
		 * 
		 * @param ip
		 *            IP address to connect.
		 * @param port
		 *            port number to connect.
		 */
		public ClientTask(String ip, int port, String message) {
			this.ip = ip;
			this.port = port;
			this.message = message;
		}

		protected void onPreExecute() {
			startButton.setEnabled(false);
			logView.setText("");
		}

		protected Void doInBackground(Void... params) {
			try {
				nativeStartTcpClient(ip, port, message);
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
		// System.loadLibrary("Echo");
	}
}
