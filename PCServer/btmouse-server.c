/*...........................................................|
|BTMouse-server.c |
|Created by CodenameJarvis on 18th June 2013.|
|A very special thanks to our Mentor Harshavardhan Kode for his excellent help with the server.| 
|...........................................................*/

#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/rfcomm.h>
#include<X11/Xlib.h>
#include<X11/Xutil.h>
#include <X11/extensions/XTest.h>
#include<stdlib.h>
#include <time.h>
#include <string.h>



void movecursor(int x ,int y);
void mouseClick(int button);
void mouseChange(int button);
void act( char* buf);

Display *dpy;
Window root_window;

int sensitivity = 2 , xres = 1366 , yres = 768;

int cur_x = 0 ,cur_y = 0 ,temp_posx,temp_posy,mtemp_x,mtemp_y;
int l_clicked = 0 ;
int r_clicked = 0;

int main(int argc, char **argv)
{	
	fprintf(stderr, "Launch the app BTMouse in your phone and connect with the PC! \n");
    struct sockaddr_rc loc_addr = { 0 }, rem_addr = { 0 };
    char buf[1024] = { 0 };
    int s, client, bytes_read,flag=1;
    socklen_t opt = sizeof(rem_addr);

    // allocate socket
    s = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);

    // bind socket to port 1 of the first available 
    // local bluetooth adapter
    loc_addr.rc_family = AF_BLUETOOTH;
    loc_addr.rc_bdaddr = *BDADDR_ANY;
    loc_addr.rc_channel = (uint8_t) 1;
    bind(s, (struct sockaddr *)&loc_addr, sizeof(loc_addr));
	
    // put socket into listening mode
    listen(s, 1);

    // accept one connection
    client = accept(s, (struct sockaddr *)&rem_addr, &opt);

    ba2str( &rem_addr.rc_bdaddr, buf );
    fprintf(stderr, "accepted connection from %s\n", buf);
    memset(buf, 0, sizeof(buf));


	// initialise X components

	dpy = XOpenDisplay(0);
	root_window = XRootWindow(dpy, 0);
	XSelectInput(dpy, root_window, KeyReleaseMask);
    // read data from the client
	while(1){
    bytes_read = read(client, buf, sizeof(buf));
    if( bytes_read > 0 ) {
		//if(flag==1){
        printf("%s \n", buf);
		//flag=2;}
	switch(buf[0]){
		case 'l':
		mouseClick(1);
		break;
    	case 'r':
		mouseClick(3);
		break;
	case '(': 
		act(buf);
		break;
	
    	}}
	else { 
		}
	
	}
    // close connection
    close(client);
    close(s);
	
	//move(100,100);
	//refresh();


    return 0;
}

void act(char* buf){  // recursive function

int i,d1=0,d2=0,xpos,sensitivity =5;
int x=0,y=0;

	for(i=0;i<48;i++){if(buf[i]=='x')xpos=i;};
	for(i=0;i<xpos;i++){if(buf[i]=='.')d1=i;};
	for(i=xpos;i<48;i++){if(buf[i]=='.')d2=i;};	
	if(buf[d1-2]=='-'){x=buf[d1-1]-48; x=-1*x;}else{x=buf[d1-1]-48;};	
	if(buf[d2-2]=='-'){y=buf[d2-1]-48; y=-1*y;}else{y=buf[d2-1]-48;};
	printf("\n %u",x);
	printf("\n %u",y);	
	movecursor((-1*x)*sensitivity,y*sensitivity);
	//printf("xpos: %d ; d1: %d ; d2 : %d  ;x : %d ;y : %d ; isto_pos:%d ; cur_posx : %d , cur_posy : %d \n",xpos,d1,d2,x,y,pos_isto,cur_x,cur_y);
	//movecursor ( x, y);

	
	
}

	

void movecursor(int x ,int y)
{

int i;

// printf("\n moved \n");

XWarpPointer(dpy, None,None /*root_window*/, 0, 0, 0, 0, x, y);
XFlush(dpy); // Flushes the output buffer, therefore updates the cursor's position.
}



void mouseClick(int button)
{
XTestFakeButtonEvent(dpy, button, True, CurrentTime);
XTestFakeButtonEvent(dpy, button, False, CurrentTime);
XFlush(dpy);

}


