p2w12-android-mouse
Team: CodenameJarvis
Project Name: Android Mouse By using accelerometer.

Instructions to use:
1.Provided is the source code for the android project(using Eclipse) and in the folder PCServer is the code for the PC(which is a C file to be compiled in ubuntu).

2. To use the application, make sure ur device has an accelerometer. Then install the file BTMouseFinal.apk on ur device.

3. In ubuntu , make sure u have installed the following packages:
          a. libbluetooth and libbluetooth-dev
          b. libxtst and libxtst-dev

4. Then in UBUNTU, go to the root directory in the terminal where the PC code is kept and compile using the following command:
      gcc -o btmouse-server btmouse-server.c -lbluetooth -L/usr/X11R6/lib -lX11 -lXtst

5. run using the command ./btmouse-server
  (we have also included the object file in the cide in case u dont want to compile)

6. Then Open the app in ur mobile and click on the connect Button. Click on scan or if ur PC is already paired then click on ur PC name.

7. (If u need to pair with the PC, ull have to run the server again and click connect again.)

8. Now, If the connection is successfull, ull get these messages.
      Accepted connection from xx.xx.xx.xx.xx.xx
  Connection Started :) 
  If you get these two messages , that means your phone is ready to use as a mouse :)

9. Now tilt your phone to move the cursor and click respectively to get right and left clicks.

Features:

1.Splash Screen in the beginning with the tam name.

2.Bluetooth enable intent.

3.Scan for device Activity.


P.S: 

1.The Scroll Buttons are not set as of now. Will work on that in the future.

2.The PC server code was compiled on ubuntu 10.04(Lucid). IT will not work in the higher versions because of change in the libs.


Credits:

1.HarshaVardhan Kode,Mentor(Big help in the Server specially).

2.Sushant Hiray,Manager(WnCC) to whom we were responsilbe.

3.WnCC Club, IIT Bombay.


4.IIT Bombay.

Team Members:

1.Hamza Najmi.

2.Divyansh Chug.

3.Mahak Gupta.

4.Shees Ali.

===================
