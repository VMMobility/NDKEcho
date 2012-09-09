package com.apress.echo;

import java.io.File;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Echo local socket server and client.
 * 
 * @author Onur Cinar
 */
public class LocalEchoActivity extends AbstractEchoActivity {
	/** Message edit. */
	private EditText messageEdit;

	/**
	 * Constructor.
	 */
    public LocalEchoActivity() {
    	super(R.layout.activity_local_echo);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		messageEdit = (EditText) findViewById(R.id.message_edit);
	}

	protected void onStartButtonClicked() {
		String name = portEdit.getText().toString();
		String message = messageEdit.getText().toString();
		
		if ((name.length() > 0) && (message.length() > 0))
		{
			File file = new File(getFilesDir(), name);
			String fileName = file.getAbsolutePath(); 
			
			ServerTask serverTask = new ServerTask(fileName);
			serverTask.start();			

			ClientTask clientTask = new ClientTask(fileName, message);
			clientTask.start();
		}
	}
	
	/**
	 * Starts the Local UNIX socket server binded to given name.
	 * 
	 * @param name
	 *            socket name.
	 * @throws Exception
	 */
	private native void nativeStartLocalServer(String name) throws Exception;

	/**
	 * Starts the local UNIX socket client.
	 * 
	 * @param port
	 *            port number.
	 * @param message
	 *            message text.
	 * @throws Exception
	 */
	private void startLocalClient(String name, String message) throws Exception {
		// Construct a local socket
		//LocalServerSocket serverSocket = new LocalServerSocket(LOCAL_SOCKET_PREFIX);
		
		// Construct a local socket
		LocalSocket clientSocket = new LocalSocket();
		
		// Construct local socket address
		LocalSocketAddress address = new LocalSocketAddress(name, 
				LocalSocketAddress.Namespace.FILESYSTEM);
		
		// Connect to local socket
		logMessage("Connecting to " + address.getNamespace().toString() + " " + address.getName());
		clientSocket.connect(address);

		logMessage("Connected");
				
		// Close the local socket
		clientSocket.close();
	}

	/**
	 * Server task.
	 */
	private class ServerTask extends AbstractEchoTask {
		/** Socket name. */
		private final String name;
		
		/**
		 * Constructor.
		 * 
		 * @param name socket name.
		 */
		public ServerTask(String name) {
			this.name = name;
		}
		
		protected void onBackground() {
			logMessage("Starting server.");

			try {
				nativeStartLocalServer(name);
			} catch (Exception e) {
				logMessage(e.getMessage());
			}

			logMessage("Server terminated.");
		}
	}
	
	/**
	 * Client task.
	 */
	private class ClientTask extends Thread {
		/** Socket name. */
		private final String name;

		/** Message text to send. */
		private final String message;
		
		/**
		 * Constructor.
		 * 
		 * @parma name socket name.
		 * @param message message text to send.
		 */
		public ClientTask(String name, String message) {
			this.name = name;
			this.message = message;
		}
		
		public void run() {
			logMessage("Starting client.");

			try {
				startLocalClient(name, message);
			} catch (Exception e) {
				logMessage(e.getMessage());
			}

			logMessage("Client terminated.");
		}
	}
}
