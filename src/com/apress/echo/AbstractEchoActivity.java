package com.apress.echo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Abstract echo activity object.
 * 
 * @author Onur Cinar
 */
public abstract class AbstractEchoActivity extends Activity implements
		OnClickListener {
	/** Local UNIX socket prefix. */
	protected static final String LOCAL_SOCKET_PREFIX = "com.apress.echo";
	
	/** Port number. */
	protected EditText portEdit;

	/** Server button. */
	protected Button startButton;

	/** Log scroll. */
	protected ScrollView logScroll;

	/** Log view. */
	protected TextView logView;

	/** Layout ID. */
	private final int layoutID;

	/**
	 * Constructor.
	 * 
	 * @param layoutID
	 *            layout ID.
	 */
	public AbstractEchoActivity(int layoutID) {
		this.layoutID = layoutID;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutID);

		portEdit = (EditText) findViewById(R.id.port_edit);
		startButton = (Button) findViewById(R.id.start_button);
		logScroll = (ScrollView) findViewById(R.id.log_scroll);
		logView = (TextView) findViewById(R.id.log_view);

		startButton.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view == startButton) {
			onStartButtonClicked();
		}
	}

	/**
	 * On start button clicked.
	 */
	protected abstract void onStartButtonClicked();

	/**
	 * Gets the port number as an integer.
	 * 
	 * @return port number or null.
	 */
	protected Integer getPort() {
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
	protected void logMessage(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				logView.append(message);
				logView.append("\n");
				logScroll.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	/**
	 * Abstract async echo task.
	 */
	protected abstract class AbstractEchoTask extends
			AsyncTask<Void, String, Void> {
		/** Port number. */
		protected final int port;

		/**
		 * Constructor.
		 * 
		 * @param port
		 *            port number.
		 */
		public AbstractEchoTask(int port) {
			this.port = port;
		}

		protected void onPreExecute() {
			startButton.setEnabled(false);
			logView.setText("");
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
		System.loadLibrary("Echo");
	}
}
