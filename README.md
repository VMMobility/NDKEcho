# NDKEcho

POSIX Socket API:Connection-Oriented Communication

A socket is a connection end-point that can be named and addressed in order to transmit data
between applications that are running either on the same machine or another machine on the
network. The POSIX Socket API, previously known as the Berkeley Socket API, is designed in a
highly generic fashion, enabling the applications to communicate over various protocol families
through the same set of API functions.
This chapter will give a brief overview of the POSIX Socket APIs for connection-oriented
communication with emphasis on the following key topics pertaining to Android platform:
 Overview of POSIX sockets
 Socket families
 Connection-oriented sockets
Prior to going into the details of the POSIX Socket APIs for connection-oriented communication, you
will create a simple example application called Echo. This example application will act as a testbed
enabling you to better understand the different aspects of socket programming as you work through
the material presented in this chapter and the next two chapters of the book.

Echo Socket Example Application
The example application will provide the following:
 A simple user interface for defining the parameters necessary to configure the
sockets.
 Service logic for a simple echo service repeating the received bytes back to the
sender.
 Boilerplate native code snippets to facilitate socket programming for Android in
native layer.
 A connection-oriented socket communication example.
 A connectionless socket communication example.
 A local socket communication example.
1.
