package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.*;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_SHININESS;
import static javax.media.opengl.GL.GL_SPECULAR;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;



/**
 * JOGLZad1.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Kata implements GLEventListener {
    private static float xrot = 0.0f, yrot = 0.0f;
    static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat?o otaczajšce
    static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat?o rozproszone
    static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
    static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja ?wiat?a

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Kata());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_UP)
                xrot -= 2.0f;
                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                xrot +=2.0f;
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                yrot += 2.0f;
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                yrot -=2.0f;
                
                if(e.getKeyChar() == 'q')
                    ambientLight=new float[] {ambientLight[0]-0.1f, ambientLight[0]-0.1f, ambientLight[0]-0.1f, 1};
                if(e.getKeyChar() == 'w')
                    ambientLight=new float[] {ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f, 1};
                if(e.getKeyChar() == 'a')
                    diffuseLight=new float[] {diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, 1};
                if(e.getKeyChar() == 's')
                    diffuseLight=new float[] {diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, 1};
                if(e.getKeyChar() == 'z')
                    specular=new float[] {specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f, 1};
                if(e.getKeyChar() == 'x')
                    specular=new float[] {specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f, 1};
                if(e.getKeyChar() == 'n')
                    lightPos[3]=0;
                if(e.getKeyChar() == 'm')
                    lightPos[3]=1;
            }
            public void keyReleased(KeyEvent e){}
            public void keyTyped(KeyEvent e){}
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glEnable(GL.GL_CULL_FACE);
        
        //warto?ci sk?adowe o?wietlenia i koordynaty ?ród?a ?wiat?a
        float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat?o otaczajšce
        float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat?o rozproszone
        float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
        float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja ?wiat?a
        //(czwarty parametr okre?la odleg?o?? ?ród?a:
        //0.0f-niesko?czona; 1.0f-okre?lona przez pozosta?e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametrów ?ród?a ?wiat?a nr. 0
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?ród?a ?wiat?a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolorów
        //kolory b?dš ustalane za pomocš glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiektów
        float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);
        
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    public float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3) {
        float[] norm = new float[3];
        float[] wektor0 = new float[3];
        float[] wektor1 = new float[3];

        for (int i = 0; i < 3; i++) {
            wektor0[i] = punkty[i + ind1] - punkty[i + ind2];
            wektor1[i] = punkty[i + ind2] - punkty[i + ind3];
        }

        norm[0] = wektor0[1] * wektor1[2] - wektor0[2] * wektor1[1];
        norm[1] = wektor0[2] * wektor1[0] - wektor0[0] * wektor1[2];
        norm[2] = wektor0[0] * wektor1[1] - wektor0[1] * wektor1[0];
        float d
                = (float) Math.sqrt((norm[0] * norm[0]) + (norm[1] * norm[1]) + (norm[2] * norm[2]));
        if (d == 0.0f) {
            d = 1.0f;
        }
        norm[0] /= d;
        norm[1] /= d;
        norm[2] /= d;

        return norm;
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
       
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó? osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó? osi Y
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
        
        rysowanieWalca(gl);
        gl.glFlush();
    }

     public void rysowanieWalca(GL gl){
        float kat, p, q;
        float x=0.0f;
        float z=0.0f;
        float s=1.0f;
        gl.glColor3f(0.01f,0.7f,0.3f);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
                p = x+s*(float)Math.sin(Math.PI*(31/32));
                q = z+s*(float)Math.cos(Math.PI*(31/32));
                float[] podstawa1={x,-1.0f,z,
                                0.0f,-1.0f,1.0f,
                                p,-1.0f,q};
                float[] norm1=WyznaczNormalna(podstawa1, 0, 3, 6);
                gl.glNormal3fv(norm1,0);
            gl.glVertex3f(x,-1.0f,z); //?rodek
            for(kat = (float) (2.0f*Math.PI); kat >0.0f;kat-=(Math.PI/32.0f))
            {
                p = x+s*(float)Math.sin(kat);
                q = z+s*(float)Math.cos(kat);
                
                gl.glVertex3f(p, -1.0f, q); //kolejne punkty
            }
            
        gl.glEnd();
        gl.glColor3f(0.01f,0.3f,0.5f);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
                p = x+s*(float)Math.sin(Math.PI*(1/32));
                q = z+s*(float)Math.cos(Math.PI*(1/32));
                float[] podstawa2={x,2.0f,z,
                                0.0f,2.0f,1.0f,
                                p,2.0f,q};
                float[] norm2=WyznaczNormalna(podstawa2, 0, 3, 6);
                gl.glNormal3fv(norm2,0);
            gl.glVertex3f(x,2.0f,z); //?rodek
            for(kat = 0.0f; kat < (2.0f*Math.PI);kat+=(Math.PI/32.0f))
            {
                p = x+s*(float)Math.sin(kat);
                q = z+s*(float)Math.cos(kat);
                gl.glVertex3f(p, 2.0f, q); //kolejne punkty
            }
        gl.glEnd();
        gl.glColor3f(0.6f,0.3f,0.2f);
        gl.glBegin(GL.GL_QUAD_STRIP);
            float[] kraw1={0.0f,2.0f,1.0f,
                           0.0f,-1.0f,1.0f,
                           0.0f,0.0f,0.0f};
            float[] norm3=WyznaczNormalna(kraw1, 0, 3, 6);
            gl.glNormal3fv(norm3,0);
            float prev[]={0.0f,2.0f,1.0f};
            for(kat = 0.0f; kat < (2.0f*Math.PI);kat+=(Math.PI/12.0f))
            {
                p = x+s*(float)Math.sin(kat);
                q = z+s*(float)Math.cos(kat);
                float[] kraw={p,2.0f,q,
                           p,-1.0f,q,
                           prev[0],prev[1],prev[2]};
                float[] norm=WyznaczNormalna(kraw, 0, 3, 6);
                gl.glNormal3fv(norm,0);
                prev[0]=p;
                prev[2]=q;
                gl.glVertex3f(p, 2.0f, q); //kolejne punkty
                gl.glVertex3f(p, -1.0f, q); //kolejne punkty
            }
        gl.glEnd();
    }
    
    
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

