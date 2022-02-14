import Annotation.AfterSuite;
import Annotation.BeforeSuite;
import Annotation.Test;




@Test
public class TestFieldClass {


        @BeforeSuite
        public void beforeSuit() {
            int bs=0;
            for (int i = 0; i < 10; i++) {
                bs = bs +i;
            }
            System.out.println("BeforeSuite " + bs);
        }


        @Test(checkTestPriority = 50)
        public void FirstTest() {
            System.out.println("first ");
        }


        @Test(checkTestPriority = 70)
        public static void SecondTest() {
            System.out.println("Second ");
        }

        @Test(checkTestPriority = 99)
        public static void ThirdTest() {
            System.out.println("Third ");
        }

        @Test(checkTestPriority = 20)
        public static void FourTest() {
            System.out.println("Four ");
        }


        @AfterSuite
         private void afterSuit() {
        System.out.println("After method");
    }
}

