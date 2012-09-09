package com.apress.echo;


/**
 * Echo server.
 * 
 * @author Onur Cinar
 */
public class EchoServerActivity extends AbstractEchoActivity {
	/**
	 * Constructor.
	 */
	public EchoServerActivity() {
		super(R.layout.activity_echo_server);
	}

	protected void onStartButtonClicked() {
		Integer port = getPort();
		if (port != null) {
			ServerTask serverTask = new ServerTask(port);
			serverTask.execute();
		}
	}

	/**
	 * Starts the TCP server on the given port.
	 * 
	 * @param port
	 *            port number.
	 * @throws Exception
	 */
	private native void nativeStartTcpServer(int port) throws Exception;

	/**
	 * Starts the UDP server on the given port.
	 * 
	 * @param port
	 *            port number.
	 * @throws Exception
	 */
	private native void nativeStartUdpServer(int port) throws Exception;

	/**
	 * Starts the Local UNIX socket server binded to given name.
	 * 
	 * @param name
	 *            socket name.
	 * @throws Exception
	 */
	private native void nativeStartLocalServer(String name) throws Exception;

	/**
	 * Server task.
	 */
	private class ServerTask extends AbstractEchoTask {
		/**
		 * Constructor.
		 * 
		 * @param port
		 *            port number.
		 */
		public ServerTask(int port) {
			super(port);
		}

		protected Void doInBackground(Void... params) {
			publishProgress("Starting server.");

			try {
				// nativeStartTcpServer(port);
				// nativeStartUdpServer(port);
				nativeStartLocalServer(LOCAL_SOCKET_PREFIX + port);
			} catch (Exception e) {
				publishProgress(e.getMessage());
			}

			publishProgress("Server terminated.");

			return null;
		}
	}
}
