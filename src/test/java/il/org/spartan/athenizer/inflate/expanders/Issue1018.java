package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

@Ignore
@SuppressWarnings("static-method")
public class Issue1018 {
  @Test public void a() {
    expansionOf("if(x==0) x+=1; else if(x==1) x+=2; else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void b1() {
    expansionOf("if(x!=0) x+=1; else if(x==1) x+=2; else x+=10; ").gives("if(x!=0) x=x+1; else if(x==1) x+=2; else x+=10; ");
  }

  @Test public void b2() {
    expansionOf("if(x==0) x+=1; else if(x>=1) x+=2; else x+=10; ").gives("if(x==0) x=x+1; else if(x>=1) x+=2; else x+=10; ");
  }

  @Test public void c() {
    expansionOf("if(x==0){ x+=1;} else if(x==1) x+=2; else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void d() {
    expansionOf("if(x==0){ x+=1;} else if(x==1){ x+=2;} else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void e() {
    expansionOf("if(x==0){ x+=1;} else if(x==1){ x+=2;} else{ x+=10;} ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void f() {
    expansionOf("if(x==0){ x+=1;} else if(x==1){ x+=2;}").gives("switch(x){case 0: x+=1; break; case 1: x+=2; break;}");
  }
}
