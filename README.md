AndroidRootApp

Written by Edward Wu (ewu7) for CS460

Installation:
	Build from source:
		Download the MaliciousRoot folder and open the project in Android Studio.
		Go to Run -> Run 'app'. You will be prompted to use an emulator or an Android device on USB debug mode.
	Build from apk:
		Download the app-debug.apk from the repository.
		Place the apk file in the root of the Android device.
		Run an apk installer to install the app.

Uses:
	This app is a proof of concept of Android malware.
	This app will extract the user's email and IMEI number and post it to an external server.
	The external server is currently set to point to http://impcalendar.web.engr.illinois.edu/maliciousServer.php".
	This uses the University of Illinois's cPanel service to host MySQL and the PHP script.
	The server can be redirected by changing the URL on line 37 of MainActivity.java.
	The server code is written in PHP and connects to a MySQL database.
	Source code for the server is in maliciousServer.php.
	The app communicates using JSON with the keys 'email' and 'imei'.

Report:
	The project's original proposal is documented in: ewu7_Project_Proposal.pdf
	The project's report is found in: ewu7_Project_Report.pdf